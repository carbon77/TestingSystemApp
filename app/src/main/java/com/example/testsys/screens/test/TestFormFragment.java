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
import com.example.testsys.models.question.Question;
import com.example.testsys.models.question.QuestionService;
import com.example.testsys.models.question.QuestionType;
import com.example.testsys.models.test.Test;
import com.example.testsys.models.test.TestViewModel;
import com.example.testsys.models.test.TestViewModelFactory;
import com.example.testsys.models.user.User;
import com.example.testsys.models.user.UserViewModel;
import com.example.testsys.screens.test.question.QuestionFormAdapter;
import com.example.testsys.utils.DateService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFormFragment extends Fragment {
    private NavController navController;
    private TestFormFragmentBinding binding;
    private UserViewModel userViewModel;
    private TestViewModel testViewModel;
    private String testId;
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

        NavHostFragment navHost = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.main_nav_host_fragment);
        navController = navHost.getNavController();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUser().observe(getViewLifecycleOwner(), currentUser -> {
            user = currentUser;
            testViewModel = new ViewModelProvider(requireActivity(), new TestViewModelFactory(user.getId())).get(TestViewModel.class);
        });

        testId = TestFormFragmentArgs.fromBundle(getArguments()).getTestId();
        if (testId == null) {
            binding.etTestVersion.setText("1");
            Calendar date = new GregorianCalendar();
            binding.etTestCreateDate.setText(DateService.fromCalendar(date));
            binding.etTestModificationDate.setText(DateService.fromCalendar(date));

            questions = new ArrayList<>();
        }

        adapter = new QuestionFormAdapter(questions, requireActivity());
        binding.questionRecyclerView.setAdapter(adapter);
        binding.questionRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        addQuestion();
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
            for (Question.Answer answer : q.getAnswers()) {
                // Answer text can't be empty
                if (answer.getText().equals("")) {
                    incorrectQuestion = true;
                    Toast.makeText(requireContext(), "Fill questions", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (q.getType() == QuestionType.RADIO && answer.isCorrect()) {
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

        String text = binding.etTestText.getText().toString();
        Test test = testViewModel.createTest(user, text);
        QuestionService.createQuestions(test.getId(), questions, () -> {
            Map<String, Object> updates = new HashMap<>();
            updates.put("questionCount", questions.size());
            testViewModel.updateTest(test.getId(), updates, () -> {});

            navController.navigateUp();
        });
    }
}
