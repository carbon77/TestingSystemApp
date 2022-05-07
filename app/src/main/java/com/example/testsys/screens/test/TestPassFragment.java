package com.example.testsys.screens.test;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testsys.R;
import com.example.testsys.databinding.TestPassFragmentBinding;
import com.example.testsys.models.ProgressTestViewModel;
import com.example.testsys.models.question.Question;
import com.example.testsys.models.question.QuestionViewModel;
import com.example.testsys.models.test.Test;
import com.example.testsys.models.test.TestViewModel;
import com.example.testsys.models.test.TestViewModelFactory;
import com.example.testsys.models.testresult.TestResult;
import com.example.testsys.models.testresult.TestResultViewModel;
import com.example.testsys.models.user.User;
import com.example.testsys.models.user.UserViewModel;

import java.util.List;

public class TestPassFragment extends Fragment {
    private TestPassFragmentBinding binding;
    private String testId;
    private Test test;
    private User user;
    private List<Question> questions;
    private TestResult result;
    private List<TestResult.TestResultQuestion> testResultQuestions;

    private UserViewModel userViewModel;
    private TestViewModel testViewModel;
    private QuestionViewModel questionViewModel;
    private TestResultViewModel testResultViewModel;
    private ProgressTestViewModel progressTestViewModel;

    public TestPassFragment() {
        super(R.layout.test_pass_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = TestPassFragmentBinding.bind(view);
        testId = TestPassFragmentArgs.fromBundle(getArguments()).getTestId();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);
        testResultViewModel = new ViewModelProvider(requireActivity()).get(TestResultViewModel.class);
        progressTestViewModel = new ViewModelProvider(requireActivity()).get(ProgressTestViewModel.class);

        userViewModel.getUser().observe(getViewLifecycleOwner(), u -> {
            user = u;
            testViewModel = new ViewModelProvider(
                    requireActivity(),
                    new TestViewModelFactory(user.getId())
            ).get(TestViewModel.class);

            testViewModel.getTests().observe(getViewLifecycleOwner(), tests -> {
                for (Test t : tests) {
                    if (t.getId().equals(testId)) {
                        test = t;
                        break;
                    }
                }

                questionViewModel.updateTestId(test.getId());
            });
        });

        questionViewModel.getQuestions().observe(getViewLifecycleOwner(), qs -> {
            questions = qs;
            result = new TestResult(test, questions);
            testResultViewModel.updateTestResult(result);
            progressTestViewModel.updateProgress(0);
        });
    }
}
