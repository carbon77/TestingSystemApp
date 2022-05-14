package com.example.testsys.screens.test.preview;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.testsys.R;
import com.example.testsys.databinding.TestPreviewFragmentBinding;
import com.example.testsys.models.question.Answer;
import com.example.testsys.models.question.Question;
import com.example.testsys.models.question.QuestionType;
import com.example.testsys.models.question.QuestionViewModel;
import com.example.testsys.models.test.Test;
import com.example.testsys.models.test.TestViewModel;
import com.example.testsys.models.testresult.TestResult;
import com.example.testsys.models.testresult.TestResultViewModel;

import java.util.List;
import java.util.Map;

public class TestPreviewFragment extends Fragment {
    TestPreviewFragmentBinding binding;
    TestResultViewModel testResultViewModel;
    TestViewModel testViewModel;
    TestResult result;
    Test test;
    List<TestResult.TestResultQuestion> resultQuestions;
    List<Question> questions;

    public TestPreviewFragment() {
        super(R.layout.test_preview_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String testId = TestPreviewFragmentArgs.fromBundle(getArguments()).getTestId();

        binding = TestPreviewFragmentBinding.bind(view);
        testResultViewModel = new ViewModelProvider(requireActivity()).get(TestResultViewModel.class);
        testViewModel = new ViewModelProvider(requireActivity()).get(TestViewModel.class);
        QuestionViewModel questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);

        testResultViewModel.getTestResult().observe(getViewLifecycleOwner(), testResult -> {
            this.result = testResult;
            this.resultQuestions = testResult.questionsToArray();

            TestPreviewAdapter adapter = new TestPreviewAdapter(resultQuestions, requireContext());
            binding.testResultRecyclerView.setAdapter(adapter);
            binding.testResultRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        });

        questionViewModel.getQuestions().observe(getViewLifecycleOwner(), qs -> {
            questions = qs;
        });

        testViewModel.getTests().observe(getViewLifecycleOwner(), tests -> {
            for (Test t : tests) {
                if (t.getId().equals(testId)) {
                    test = t;
                    break;
                }
            }
        });

        binding.btnCancel.setOnClickListener(v -> {
            testResultViewModel.updateTestResult(result);
            String testTitle = TestPreviewFragmentArgs.fromBundle(getArguments()).getSubtitle();
            NavDirections action = TestPreviewFragmentDirections
                    .actionTestPreviewFragmentToTestPassFragment(testId, testTitle);
            NavHostFragment.findNavController(this).navigate(action);
        });

        binding.btnFinish.setOnClickListener(v -> {
            calculateScores();

            if (test.getMinScores() <= result.getTotalScores()) {
                result.setSuccessful(true);
            }

            testResultViewModel.createTestResult(result, it -> {
                NavDirections action = TestPreviewFragmentDirections
                        .actionTestPreviewFragmentToTestResultFragment(test.getTitle());
                NavHostFragment.findNavController(this).navigate(action);
            });
        });
    }

    private void calculateScores() {
        float scores = 0;

        List<TestResult.TestResultQuestion> resultQuestions = result.questionsToArray();

        for (int i = 0; i < resultQuestions.size(); i++) {
            float questionScores = 0;
            TestResult.TestResultQuestion resQ = resultQuestions.get(i);
            Question question = questions.get(i);

            if (resQ.getType() == QuestionType.RADIO) {
                for (Map.Entry<String, Answer> entry : resQ.getAnswers().entrySet()) {
                    Answer questionAnswer = question.getAnswers().get(entry.getKey());
                    if (entry.getValue().getCorrect() && questionAnswer.getCorrect()) {
                        questionScores += question.getScore();
                    }
                }
            } else if (resQ.getType() == QuestionType.CHECKBOX) {
                int countCorrectAnswers = 0;
                int countCorrectOptions = 0;

                for (Map.Entry<String, Answer> entry : resQ.getAnswers().entrySet()) {
                    Answer questionAnswer = question.getAnswers().get(entry.getKey());
                    if (questionAnswer.getCorrect()) {
                        countCorrectOptions++;
                    }

                    if (entry.getValue().getCorrect() && questionAnswer.getCorrect()) {
                        countCorrectAnswers++;
                    } else if (entry.getValue().getCorrect() && !questionAnswer.getCorrect()) {
                        countCorrectAnswers--;
                    }
                }

                questionScores += (question.getScore() * countCorrectAnswers) / (float) countCorrectOptions;
            }

            resQ.setScore(questionScores);
            scores += questionScores;
        }

        result.setTotalScores(scores);
    }
}
