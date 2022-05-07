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
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class TestPassFragment extends Fragment {
    private TestPassFragmentBinding binding;
    private String testId;
    private Test test;
    private User user;
    private List<Question> questions;
    private TestResult result;
    private List<TestResult.TestResultQuestion> testResultQuestions;
    private int progress;
    private boolean isTabsInit = false;

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
            });
        });

        questionViewModel.getQuestions().observe(getViewLifecycleOwner(), qs -> {
            questions = qs;
            result = new TestResult(test, questions);
            testResultViewModel.updateTestResult(result);
            progressTestViewModel.updateProgress(0);

            if (!isTabsInit) {
                initTabs();
                isTabsInit = true;
            }
        });

        progressTestViewModel.getProgress().observe(getViewLifecycleOwner(), p -> {
            progress = p;

            if (questions == null)
                return;

            if (progress == questions.size() - 1) {
                binding.btnTestPassNext.setVisibility(View.GONE);
                binding.btnTestPassFinish.setVisibility(View.VISIBLE);
            } else {
                binding.btnTestPassNext.setVisibility(View.VISIBLE);
                binding.btnTestPassFinish.setVisibility(View.GONE);
            }

            if (progress == 0) {
                binding.btnTestPassPrevious.setVisibility(View.GONE);
            } else {
                binding.btnTestPassPrevious.setVisibility(View.VISIBLE);
            }
        });

        binding.btnTestPassNext.setOnClickListener(v -> {
            progressTestViewModel.updateProgress(progress + 1);
            binding.testPassTabs.getTabAt(progress).select();
        });

        binding.btnTestPassPrevious.setOnClickListener(v -> {
            progressTestViewModel.updateProgress(progress - 1);
            binding.testPassTabs.getTabAt(progress).select();
        });

        binding.testPassTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                progressTestViewModel.updateProgress(Integer.parseInt(tab.getText().toString()) - 1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initTabs() {
        if (questions == null)
            return;

        for (int i = 0; i < questions.size(); i++) {
            TabLayout.Tab tab = binding.testPassTabs.newTab();
            tab.setText(String.valueOf(i + 1));
            binding.testPassTabs.addTab(tab);
        }
    }
}
