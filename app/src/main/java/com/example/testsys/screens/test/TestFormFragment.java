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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            } else {
                this.questions = questions;
            }

            adapter = new QuestionFormAdapter(this.questions, requireActivity());
            binding.questionRecyclerView.setAdapter(adapter);
            binding.questionRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

            if (this.questions.size() == 0) {
                addQuestion();
            }
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
        questions.add(new Question("", QuestionType.RADIO));
        adapter.notifyDataSetChanged();
    }

    private void saveTest() {
        // Test text can't be empty
        if (binding.etTestText.getText().toString().equals("")) {
            binding.etTestTextLayout.setError("This field is required!");
            Toast.makeText(requireContext(), "Check the form", Toast.LENGTH_SHORT).show();
            return;
        }

        // Test must have questions
        if (questions.size() == 0) {
            Toast.makeText(requireContext(), "Questions are required!", Toast.LENGTH_SHORT).show();
            return;
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

        if (incorrectQuestion) {
            return;
        }

        test.setText(binding.etTestText.getText().toString());
        testViewModel.createTest(test, t -> {
            navController.navigateUp();
            questionViewModel.createQuestions(t.getId(), questions, qs -> {
                Map<String, Object> updates = new HashMap<>();
                updates.put("questionCount", qs.size());
                testViewModel.updateTest(t.getId(), updates, () -> {});
            });
        });

    }
}
