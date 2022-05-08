package com.example.testsys.screens.test.preview;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.testsys.R;
import com.example.testsys.databinding.TestPreviewFragmentBinding;
import com.example.testsys.models.testresult.TestResult;
import com.example.testsys.models.testresult.TestResultViewModel;

import java.util.List;

public class TestPreviewFragment extends Fragment {
    TestPreviewFragmentBinding binding;
    TestResultViewModel testResultViewModel;
    TestResult testResult;
    List<TestResult.TestResultQuestion> questions;

    public TestPreviewFragment() {
        super(R.layout.test_preview_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = TestPreviewFragmentBinding.bind(view);
        testResultViewModel = new ViewModelProvider(requireActivity()).get(TestResultViewModel.class);
        testResultViewModel.getTestResult().observe(getViewLifecycleOwner(), testResult -> {
            this.testResult = testResult;
            this.questions = testResult.questionsToArray();

            TestPreviewAdapter adapter = new TestPreviewAdapter(questions, requireContext());
            binding.testResultRecyclerView.setAdapter(adapter);
            binding.testResultRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        });
    }
}
