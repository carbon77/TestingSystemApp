package com.example.testsys.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.testsys.R;
import com.example.testsys.databinding.TestBottomSheetBinding;
import com.example.testsys.models.test.Test;
import com.example.testsys.models.test.TestViewModel;
import com.example.testsys.models.test.TestViewModelFactory;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.function.Consumer;

public class TestModalBottomSheet extends BottomSheetDialogFragment {
    private Test test;
    private TestBottomSheetBinding binding;
    private TestViewModel testViewModel;
    private String uid;

    public TestModalBottomSheet(Test test, String uid) {
        super();
        this.test = test;
        this.uid = uid;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = TestBottomSheetBinding.bind(view);
        binding.testBottomSheetTitle.setText(test.getText());

        testViewModel = new ViewModelProvider(requireActivity(), new TestViewModelFactory(uid)).get(TestViewModel.class);
        binding.deleteBtnTestSheet.setOnClickListener(v -> {
            testViewModel.deleteTest(test.getId(), uid, () -> {
                getDialog().hide();
            });
        });
    }
}
