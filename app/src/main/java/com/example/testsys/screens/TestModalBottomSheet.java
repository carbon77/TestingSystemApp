package com.example.testsys.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.TestBottomSheetBinding;
import com.example.testsys.models.question.QuestionViewModel;
import com.example.testsys.models.test.Test;
import com.example.testsys.models.test.TestViewModel;
import com.example.testsys.models.user.User;
import com.example.testsys.models.user.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TestModalBottomSheet extends BottomSheetDialogFragment {
    private Test test;
    private User user;
    private String testId;
    private TestBottomSheetBinding binding;
    private TestViewModel testViewModel;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = TestBottomSheetBinding.bind(view);
        testId = TestModalBottomSheetArgs.fromBundle(getArguments()).getTestId();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        testViewModel = new ViewModelProvider(requireActivity()).get(TestViewModel.class);

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
        });

        testViewModel.getTests().observe(getViewLifecycleOwner(), tests -> {
            for (Test test : tests) {
                if (test.getId().equals(testId)) {
                    this.test = test;
                    break;
                }
            }

            binding.testBottomSheetTitle.setText(test.getTitle());
        });

        binding.deleteBtnTestSheet.setOnClickListener(v -> {
            testViewModel.deleteTest(test.getId(), user.getId(), () -> {
                testViewModel.updateTests(tests -> {});
                NavHostFragment.findNavController(this).navigateUp();
            });
        });

        binding.editTestBtnTestSheet.setOnClickListener(v -> {
            QuestionViewModel questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);
            questionViewModel.updateTestId(testId);

            Bundle args = new Bundle();
            args.putString("testId", testId);
            NavHostFragment navHost = (NavHostFragment) requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.main_nav_host_fragment);

            getDialog().dismiss();
            navHost.getNavController().navigate(R.id.test_form_fragment, args);
        });

        binding.startTestBtnTestSheet.setOnClickListener(v -> {
            QuestionViewModel questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);
            questionViewModel.updateTestId(testId);

            Bundle args = new Bundle();
            args.putString("testId", testId);
            NavHostFragment navHost = (NavHostFragment) requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.main_nav_host_fragment);

            getDialog().dismiss();
            navHost.getNavController().navigate(R.id.testDetailFragment, args);
        });
    }
}
