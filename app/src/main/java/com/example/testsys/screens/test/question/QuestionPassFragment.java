package com.example.testsys.screens.test.question;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.testsys.R;
import com.example.testsys.databinding.QuestionPassFragmentBinding;
import com.example.testsys.models.question.Question;
import com.example.testsys.models.question.QuestionType;
import com.example.testsys.models.question.QuestionViewModel;
import com.example.testsys.models.test.Test;
import com.example.testsys.models.test.TestViewModel;
import com.example.testsys.models.testresult.TestResult;
import com.example.testsys.models.testresult.TestResultViewModel;

import java.util.List;

public class QuestionPassFragment extends Fragment {
    private QuestionPassFragmentBinding binding;
    private List<TestResult.TestResultQuestion> resultQuestions;
    private TestResult.TestResultQuestion question;
    private int progress;

    private TestResultViewModel testResultViewModel;

    public QuestionPassFragment() {
        super(R.layout.question_pass_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progress = QuestionPassFragmentArgs.fromBundle(getArguments()).getProgress();
        binding = QuestionPassFragmentBinding.bind(view);

        testResultViewModel = new ViewModelProvider(requireActivity()).get(TestResultViewModel.class);
        testResultViewModel.getTestResult().observe(getViewLifecycleOwner(), testResult -> {
            resultQuestions = testResult.questionsToArray();
            question = resultQuestions.get(progress);

            binding.tvQuestionText.setText(question.getText());
            if (question.getType() == QuestionType.CHECKBOX) {
                CheckboxQuestionsAdapter adapter = new CheckboxQuestionsAdapter(question.getAnswers());
                binding.checkboxQuestionRecyclerView.setAdapter(adapter);
                binding.checkboxQuestionRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
                binding.checkboxQuestionRecyclerView.setVisibility(View.VISIBLE);
                binding.radioQuestionRecyclerView.setVisibility(View.GONE);
            } else if (question.getType() == QuestionType.RADIO) {
                RadioQuestionsAdapter adapter = new RadioQuestionsAdapter(question.getAnswers());
                binding.radioQuestionRecyclerView.setAdapter(adapter);
                binding.radioQuestionRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
                binding.radioQuestionRecyclerView.setVisibility(View.VISIBLE);
                binding.checkboxQuestionRecyclerView.setVisibility(View.GONE);
            }
        });
    }
}
