package com.example.testsys.fragments;

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
import com.example.testsys.models.test.Test;
import com.example.testsys.models.test.TestViewModel;
import com.example.testsys.models.test.TestViewModelFactory;
import com.example.testsys.models.user.User;
import com.example.testsys.models.user.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.function.Consumer;

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
        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            this.user = user;

            testViewModel = new ViewModelProvider(requireActivity(), new TestViewModelFactory(user.getId())).get(TestViewModel.class);
            testViewModel.getTests().observe(getViewLifecycleOwner(), tests -> {
                for (Test test : tests) {
                    if (test.getId().equals(testId)) {
                        this.test = test;
                    }
                }

                binding.testBottomSheetTitle.setText(test.getText());
            });
        });



        binding.deleteBtnTestSheet.setOnClickListener(v -> {
            testViewModel.deleteTest(test.getId(), user.getId(), () -> {
                NavHostFragment.findNavController(this).navigateUp();
            });
        });
    }
}
