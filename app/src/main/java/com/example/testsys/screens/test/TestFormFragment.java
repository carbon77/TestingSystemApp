package com.example.testsys.screens.test;

import android.os.Bundle;
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
import com.example.testsys.models.test.TestViewModelFactory;
import com.example.testsys.models.user.User;
import com.example.testsys.models.user.UserViewModel;
import com.example.testsys.screens.test.question.QuestionFormAdapter;
import com.example.testsys.utils.DateService;

import java.util.ArrayList;
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

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUser().observe(getViewLifecycleOwner(), currentUser -> {
            user = currentUser;
            testViewModel = new ViewModelProvider(
                    requireActivity(),
                    new TestViewModelFactory(user.getId())).get(TestViewModel.class);
            testViewModel.getTests().observe(getViewLifecycleOwner(), this::initTest);
        });
    }

    private void initTest(List<Test> tests) {
        if (testId == null) {
            test = new Test(user);
        } else {
            for (Test t : tests) {
                if (t.getId() == testId) {
                    test = t;
                    break;
                }
            }
        }

        binding.etTestText.setText(test.getText());
        binding.etTestCreateDate.setText(test.getCreationDate());
        binding.etTestModificationDate.setText(test.getModificationDate());
        binding.etTestVersion.setText(String.valueOf(test.getVersion()));

        questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);
        questionViewModel.getQuestions().observe(getViewLifecycleOwner(), questions -> {
            if (questions == null) {
                this.questions = new ArrayList<>();
                this.questions.add(new Question("", QuestionType.RADIO));
            } else {
                this.questions = questions;
            }

            this.newQuestions = new ArrayList<>();
            this.questions.forEach(question -> {
                newQuestions.add(new Question(question));
            });
            adapter = new QuestionFormAdapter(this.newQuestions, requireActivity());
            binding.questionRecyclerView.setAdapter(adapter);
            binding.questionRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        });
        questionViewModel.updateTestId(testId);
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
        newQuestions.add(new Question("", QuestionType.RADIO));
        adapter.notifyDataSetChanged();
    }

    private void saveTest() {
        if (validateForm(newQuestions)) return;

        if (testId != null) {
            updateQuestions();
            return;
        }

        test.setText(binding.etTestText.getText().toString());
        testViewModel.createTest(test, t -> {
            navController.navigateUp();
            questionViewModel.createQuestions(t.getId(), newQuestions   , qs -> {
                Map<String, Object> updates = new HashMap<>();
                updates.put("questionCount", qs.size());
                testViewModel.updateTest(t.getId(), updates, () -> {});
            });
        });

    }

    private void updateQuestions() {
        Map<String, Object> testUpdates = new HashMap<>();
        Map<String, Object> questionUpdates = new HashMap<>();
        boolean isModified = false;

        String testText = binding.etTestText.getText().toString();
        List<Question> questionsToCreate = new ArrayList<>();
        List<String> questionsToDeleteIds = new ArrayList<>();
        List<String> oldQuestionsIds = questions.stream().map(q -> q.getId()).collect(Collectors.toList());
        List<String> newQuestionsIds = newQuestions.stream().map(q -> q.getId() == null ? "" : q.getId()).collect(Collectors.toList());

        if (!test.getText().equals(testText)) {
            testUpdates.put("text", testText);
            isModified = true;
        }

        if (test.getQuestionCount() != newQuestions.size()) {
            testUpdates.put("questionCount", newQuestions.size());
            isModified = true;
        }

        for (Question question : newQuestions) {
            // New created question
            if (question.getId() == null) {
                questionsToCreate.add(question);
                isModified = true;
                continue;
            }

            Question oldQuestion = questions.stream()
                    .filter(oldQ -> question.getId().equals(oldQ.getId()))
                    .findFirst()
                    .orElse(null);

            // If text changed
            if (!question.getText().equals(oldQuestion.getText())) {
                questionUpdates.put(question.getId() + "/text", question.getText());
                isModified = true;
            }

            // If question type changed
            if (question.getType() != oldQuestion.getType()) {
                questionUpdates.put(question.getId() + "/type", question.getType());
                isModified = true;
            }

            for (Map.Entry<String, Answer> entry : question.getAnswers().entrySet()) {
                // Created new answer
                if (!oldQuestion.getAnswers().containsKey(entry.getKey())) {
                    questionUpdates.put(question.getId() + "/answers/" + entry.getKey() + "/correct", entry.getValue().getCorrect());
                    questionUpdates.put(question.getId() + "/answers/" + entry.getKey() + "/text", entry.getValue().getText());
                    isModified = true;
                    continue;
                }

                Answer oldAnswer = oldQuestion.getAnswers().get(entry.getKey());

                // If answer text changed
                if (!oldAnswer.getText().equals(entry.getValue().getText())) {
                    questionUpdates.put(question.getId() + "/answers/" + entry.getKey() + "/text", entry.getValue().getText());
                    isModified = true;
                }

                // If answer correct changed
                if (oldAnswer.getCorrect() != entry.getValue().getCorrect()) {
                    questionUpdates.put(question.getId() + "/answers/" + entry.getKey() + "/correct", entry.getValue().getCorrect());
                    isModified = true;
                }
            }

            // Deleted answers
            for (Map.Entry<String, Answer> entry : oldQuestion.getAnswers().entrySet()) {
                if (!question.getAnswers().containsKey(entry.getKey())) {
                    questionUpdates.put(question.getId() + "/answers/" + entry.getKey(), null);
                    isModified = true;
                }
            }
        }

        // Deleted questions
        for (String oldId : oldQuestionsIds) {
            if (!newQuestionsIds.contains(oldId)) {
                questionsToDeleteIds.add(oldId);
            }
        }

        if (!isModified) {
            navController.navigateUp();
            return;
        }

        testUpdates.put("modificationDate", DateService.fromCalendar(new GregorianCalendar()));
        questionViewModel.createQuestions(testId, questionsToCreate, qs -> {
            questionViewModel.deleteQuestions(testId, questionsToDeleteIds, () -> {
                questionViewModel.updateQuestions(testId, questionUpdates, newQs -> {
                    testViewModel.updateTest(testId, testUpdates, () -> {
                        navController.navigateUp();
                    });
                });
            });
        });
    }

    private boolean validateForm(List<Question> questions) {
        // Test text can't be empty
        if (binding.etTestText.getText().toString().equals("")) {
            binding.etTestTextLayout.setError("This field is required!");
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
