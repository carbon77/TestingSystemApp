package com.example.testsys.screens.test;

import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.testsys.R;
import com.example.testsys.databinding.TestFormFragmentBinding;
import com.example.testsys.models.question.Answer;
import com.example.testsys.models.question.Question;
import com.example.testsys.models.question.QuestionType;
import com.example.testsys.models.question.QuestionViewModel;
import com.example.testsys.models.test.Test;
import com.example.testsys.models.test.TestViewModel;
import com.example.testsys.models.user.User;
import com.example.testsys.models.user.UserViewModel;
import com.example.testsys.screens.test.question.QuestionFormAdapter;
import com.example.testsys.utils.DateService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestFormFragment extends Fragment {
    private NavController navController;
    private TestFormFragmentBinding binding;
    private UserViewModel userViewModel;
    private TestViewModel testViewModel;
    private QuestionViewModel questionViewModel;
    private String testId;
    private Test test;
    private User user;
    private List<Question> questions;
    private List<Question> newQuestions;
    private QuestionFormAdapter adapter;

    public TestFormFragment() {
        super(R.layout.test_form_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = TestFormFragmentBinding.bind(view);
        setHasOptionsMenu(true);

        NavHostFragment navHost = (NavHostFragment) requireActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.main_nav_host_fragment);
        navController = navHost.getNavController();

        testId = TestFormFragmentArgs.fromBundle(getArguments()).getTestId();

        binding.testForm.setVisibility(View.GONE);
        binding.questionsView.setVisibility(View.GONE);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        testViewModel = new ViewModelProvider(requireActivity()).get(TestViewModel.class);
        questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);
        
        userViewModel.getUser().observe(getViewLifecycleOwner(), currentUser -> {
            user = currentUser;
        });

        testViewModel.getTests().observe(getViewLifecycleOwner(), this::initTest);

        questionViewModel.getQuestions().observe(getViewLifecycleOwner(), questions -> {
            if (questions == null) {
                this.questions = new ArrayList<>();
                Question question = new Question("", QuestionType.RADIO);
                question.setOrder(0);
                this.questions.add(question);
            } else {
                questions.sort(Comparator.comparingInt(Question::getOrder));
                this.questions = questions;
            }

            this.newQuestions = new ArrayList<>();
            this.questions.forEach(question -> {
                newQuestions.add(new Question(question));
            });
            adapter = new QuestionFormAdapter(this.newQuestions, requireActivity());
            binding.questionRecyclerView.setAdapter(adapter);
            binding.questionRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

            binding.testForm.setVisibility(View.VISIBLE);
            binding.questionsView.setVisibility(View.VISIBLE);
            binding.progressCircular.setVisibility(View.GONE);
        });

        binding.testInfoForm.setVisibility(View.GONE);
        binding.testFormInfoBtn.setOnClickListener(v -> {
            if (binding.testInfoForm.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(binding.testInfoForm, new AutoTransition());
                binding.testInfoForm.setVisibility(View.VISIBLE);
                binding.testInfoFormIcon.setImageResource(R.drawable.ic_move_up);
            } else {
                TransitionManager.beginDelayedTransition(binding.testInfoForm, new AutoTransition());
                binding.testInfoForm.setVisibility(View.GONE);
                binding.testInfoFormIcon.setImageResource(R.drawable.ic_move_down);
            }
        });
    }

    private void initTest(List<Test> tests) {
        if (testId == null) {
            test = new Test(user);
        } else {
            for (Test t : tests) {
                if (t.getId().equals(testId)) {
                    test = t;
                    break;
                }
            }
        }

        binding.etTestTitle.setText(test.getTitle());
        binding.etTestDescription.setText(test.getDescription());
        binding.etTestCreateDate.setText(test.getCreationDate());
        binding.etTestModificationDate.setText(test.getModificationDate());
        binding.etTestVersion.setText(String.valueOf(test.getVersion()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.test_form_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_test_item:
                saveTest();
                break;
            case R.id.add_question_item:
                addQuestion();
                break;
        }

        return false;
    }

    private void addQuestion() {
        Question question = new Question("", QuestionType.RADIO);
        question.setOrder(newQuestions.get(newQuestions.size() - 1).getOrder() + 1);
        newQuestions.add(question);
        adapter.notifyDataSetChanged();
    }

    private void saveTest() {
        if (validateForm(newQuestions)) return;

        binding.testForm.setVisibility(View.GONE);
        binding.questionsView.setVisibility(View.GONE);
        binding.progressCircular.setVisibility(View.VISIBLE);

        if (testId != null) {
            updateQuestions();
            return;
        }

        test.setTitle(binding.etTestTitle.getText().toString());
        test.setDescription(binding.etTestDescription.getText().toString());
        testViewModel.createTest(test, t -> {
            questionViewModel.createQuestions(t.getId(), newQuestions, qs -> {
                Map<String, Object> updates = new HashMap<>();
                updates.put("questionCount", qs.size());
                updates.put("totalScores", calculateTotalScores());
                testViewModel.updateTest(t.getId(), updates, () -> {
                    testViewModel.updateTests(tests -> {});
                    navController.navigateUp();
                });
            });
        });

    }

    private int calculateTotalScores() {
        int totalScores = 0;

        for (Question question : newQuestions) {
            totalScores += question.getScore();
        }

        return totalScores;
    }

    private void updateQuestions() {
        Map<String, Object> testUpdates = new HashMap<>();
        Map<String, Object> questionUpdates = new HashMap<>();
        boolean isTestModified = false;
        boolean isQuestionsModified = false;

        String testTitle = binding.etTestTitle.getText().toString();
        String testDescription = binding.etTestDescription.getText().toString();
        List<Question> questionsToCreate = new ArrayList<>();
        List<String> questionsToDeleteIds = new ArrayList<>();
        List<String> oldQuestionsIds = questions.stream().map(q -> q.getId()).collect(Collectors.toList());
        List<String> newQuestionsIds = newQuestions.stream().map(q -> q.getId() == null ? "" : q.getId()).collect(Collectors.toList());

        if (!test.getTitle().equals(testTitle)) {
            testUpdates.put("title", testTitle);
            isTestModified = true;
        }

        if (test.getQuestionCount() != newQuestions.size()) {
            testUpdates.put("questionCount", newQuestions.size());
            isTestModified = true;
        }

        if (!test.getDescription().equals(testDescription)) {
            testUpdates.put("description", testDescription);
            isTestModified = true;
        }

        for (Question question : newQuestions) {
            // New created question
            if (question.getId() == null) {
                questionsToCreate.add(question);
                isQuestionsModified = true;
                continue;
            }

            Question oldQuestion = questions.stream()
                    .filter(oldQ -> question.getId().equals(oldQ.getId()))
                    .findFirst()
                    .orElse(null);

            // If text changed
            if (!question.getText().equals(oldQuestion.getText())) {
                questionUpdates.put(question.getId() + "/text", question.getText());
                isQuestionsModified = true;
            }

            // If question type changed
            if (question.getType() != oldQuestion.getType()) {
                questionUpdates.put(question.getId() + "/type", question.getType());
                isQuestionsModified = true;
            }

            // If order changed
            if (question.getOrder() != oldQuestion.getOrder()) {
                questionUpdates.put(question.getId() + "/order", question.getOrder());
                isQuestionsModified = true;
            }

            // If score changed
            if (question.getScore() != oldQuestion.getScore()) {
                questionUpdates.put(question.getId() + "/score", question.getScore());
                isQuestionsModified = true;
            }

            for (Map.Entry<String, Answer> entry : question.getAnswers().entrySet()) {
                // Created new answer
                if (!oldQuestion.getAnswers().containsKey(entry.getKey())) {
                    questionUpdates.put(question.getId() + "/answers/" + entry.getKey() + "/correct", entry.getValue().getCorrect());
                    questionUpdates.put(question.getId() + "/answers/" + entry.getKey() + "/text", entry.getValue().getText());
                    isQuestionsModified = true;
                    continue;
                }

                Answer oldAnswer = oldQuestion.getAnswers().get(entry.getKey());

                // If answer text changed
                if (!oldAnswer.getText().equals(entry.getValue().getText())) {
                    questionUpdates.put(question.getId() + "/answers/" + entry.getKey() + "/text", entry.getValue().getText());
                    isQuestionsModified = true;
                }

                // If answer correct changed
                if (oldAnswer.getCorrect() != entry.getValue().getCorrect()) {
                    questionUpdates.put(question.getId() + "/answers/" + entry.getKey() + "/correct", entry.getValue().getCorrect());
                    isQuestionsModified = true;
                }
            }

            // Deleted answers
            for (Map.Entry<String, Answer> entry : oldQuestion.getAnswers().entrySet()) {
                if (!question.getAnswers().containsKey(entry.getKey())) {
                    questionUpdates.put(question.getId() + "/answers/" + entry.getKey(), null);
                    isQuestionsModified = true;
                }
            }
        }

        // Deleted questions
        for (String oldId : oldQuestionsIds) {
            if (!newQuestionsIds.contains(oldId)) {
                questionsToDeleteIds.add(oldId);
                isQuestionsModified = true;
            }
        }

        if (!isTestModified && !isQuestionsModified) {
            navController.navigateUp();
            return;
        }

        if (isQuestionsModified) {
            testUpdates.put("version", test.getVersion() + 1);
            testUpdates.put("totalScores", calculateTotalScores());
        }

        testUpdates.put("modificationDate", DateService.fromCalendar(new GregorianCalendar()));
        questionViewModel.createQuestions(testId, questionsToCreate, qs -> {
            questionViewModel.deleteQuestions(testId, questionsToDeleteIds, () -> {
                questionViewModel.updateQuestions(testId, questionUpdates, newQs -> {
                    testViewModel.updateTest(testId, testUpdates, () -> {
                        testViewModel.updateTests(tests -> {});
                        navController.navigateUp();
                    });
                });
            });
        });
    }

    private boolean validateForm(List<Question> questions) {
        // Test text can't be empty
        if (binding.etTestTitle.getText().toString().equals("")) {
            binding.etTestTitleLayout.setError("This field is required!");
            Toast.makeText(requireContext(), "Check the form", Toast.LENGTH_SHORT).show();
            return true;
        }

        // Test must have questions
        if (questions.size() == 0) {
            Toast.makeText(requireContext(), "Questions are required!", Toast.LENGTH_SHORT).show();
            return true;
        }

        boolean incorrectQuestion = false;
        for (Question q : questions) {
            // Question text can't be empty
            if (q.getText().equals("")) {
                incorrectQuestion = true;
                Toast.makeText(requireContext(), "Fill questions", Toast.LENGTH_SHORT).show();
                break;
            }

            // Question must have answers
            if (q.getAnswers().size() == 0) {
                incorrectQuestion = true;
                Toast.makeText(requireContext(), "Fill answers", Toast.LENGTH_SHORT).show();
                break;
            }

            int countAnswersRadio = 0;
            for (Map.Entry<String, Answer> entry : q.getAnswers().entrySet()) {
                Answer answer = entry.getValue();

                // Answer text can't be empty
                if (answer.getText().equals("")) {
                    incorrectQuestion = true;
                    Toast.makeText(requireContext(), "Fill questions", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (q.getType() == QuestionType.RADIO && answer.getCorrect()) {
                    countAnswersRadio++;
                }
            }

            // When question type is RADIO, it has to have only 1 correct answer
            if (q.getType() == QuestionType.RADIO && countAnswersRadio != 1) {
                incorrectQuestion = true;
                Toast.makeText(requireContext(), "Radio question has to have only 1 correct answer", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        return incorrectQuestion;
    }
}
