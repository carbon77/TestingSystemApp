package com.example.testsys.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.testsys.R;
import com.example.testsys.databinding.TestBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TestModalBottomSheet extends BottomSheetDialogFragment {
    private String title;
    private TestBottomSheetBinding binding;

    public TestModalBottomSheet(String title) {
        super();
        this.title = title;
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
        binding.testBottomSheetTitle.setText(title);
    }
}
