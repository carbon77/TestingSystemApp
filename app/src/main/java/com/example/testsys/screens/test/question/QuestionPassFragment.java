package com.example.testsys.screens.test.question;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testsys.R;
import com.example.testsys.databinding.QuestionPassFragmentBinding;
import com.example.testsys.models.ProgressTestViewModel;
import com.example.testsys.models.testresult.TestResult;
import com.example.testsys.models.testresult.TestResultViewModel;

import java.util.List;

public class QuestionPassFragment extends Fragment {
    private QuestionPassFragmentBinding binding;
    private List<TestResult.TestResultQuestion> questions;
    private int progress;

    private TestResultViewModel testResultViewModel;
    private ProgressTestViewModel progressTestViewModel;

    public QuestionPassFragment() {
        super(R.layout.question_pass_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = QuestionPassFragmentBinding.bind(view);
        testResultViewModel = new ViewModelProvider(requireActivity()).get(TestResultViewModel.class);
        progressTestViewModel = new ViewModelProvider(requireActivity()).get(ProgressTestViewModel.class);

        testResultViewModel.getTestResult().observe(getViewLifecycleOwner(), testResult -> {
            questions = testResult.getQuestionsArray();

            progressTestViewModel.getProgress().observe(getViewLifecycleOwner(), p -> {
                progress = p;

                binding.tvQuestionText.setText(questions.get(progress).getText());
            });
        });
    }
}
