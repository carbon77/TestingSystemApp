package com.example.testsys.screens.test;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.TestPassFragmentBinding;
import com.example.testsys.models.ProgressTestViewModel;
import com.example.testsys.models.question.Question;
import com.example.testsys.models.question.QuestionViewModel;
import com.example.testsys.models.test.Test;
import com.example.testsys.models.test.TestViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class TestPassFragment extends Fragment {
    private TestPassFragmentBinding binding;
    private String testId;
    private Test test;
    private List<Question> questions;
    private int progress;

    private TestViewModel testViewModel;
    private QuestionViewModel questionViewModel;
    private ProgressTestViewModel progressTestViewModel;

    public TestPassFragment() {
        super(R.layout.test_pass_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = TestPassFragmentBinding.bind(view);
        testId = TestPassFragmentArgs.fromBundle(getArguments()).getTestId();

        questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);
        progressTestViewModel = new ViewModelProvider(requireActivity()).get(ProgressTestViewModel.class);
        testViewModel = new ViewModelProvider(requireActivity()).get(TestViewModel.class);

        testViewModel.getTests().observe(getViewLifecycleOwner(), tests -> {
            for (Test t : tests) {
                if (t.getId().equals(testId)) {
                    test = t;
                    break;
                }
            }
        });

        questionViewModel.getQuestions().observe(getViewLifecycleOwner(), qs -> {
            questions = qs;
            progressTestViewModel.updateProgress(0);
            initTabs();
        });

        progressTestViewModel.getProgress().observe(getViewLifecycleOwner(), p -> {
            progress = p;
            navigateToQuestion();

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

        binding.btnTestPassFinish.setOnClickListener(v -> {
            NavDirections action = TestPassFragmentDirections
                    .actionTestPassFragmentToTestPreviewFragment(test.getTitle(), testId);
            NavHostFragment.findNavController(this).navigate(action);
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

    private void navigateToQuestion() {
        Bundle args = new Bundle();
        args.putInt("progress", progress);
        args.putString("subtitle", test.getTitle());
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.question_pass_fragment, true)
                .setEnterAnim(R.anim.nav_default_enter_anim)
                .setExitAnim(R.anim.nav_default_exit_anim)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.question_pass_view);
        navHostFragment.getNavController().navigate(
                R.id.question_pass_fragment,
                args,
                navOptions
        );
    }

    private void initTabs() {
        for (int i = 0; i < questions.size(); i++) {
            TabLayout.Tab tab = binding.testPassTabs.newTab();
            tab.setText(String.valueOf(i + 1));
            binding.testPassTabs.addTab(tab);
        }
    }
}
