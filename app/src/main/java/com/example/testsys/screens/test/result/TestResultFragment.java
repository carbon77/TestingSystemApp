package com.example.testsys.screens.test.result;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.TokenWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.testsys.R;
import com.example.testsys.databinding.TestResultFragmentBinding;
import com.example.testsys.models.question.Question;
import com.example.testsys.models.question.QuestionViewModel;
import com.example.testsys.models.testresult.TestResultViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestResultFragment extends Fragment {
    private TestResultFragmentBinding binding;
    private TestResultViewModel testResultViewModel;
    private QuestionViewModel questionViewModel;

    public TestResultFragment() {
        super(R.layout.test_result_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = TestResultFragmentBinding.bind(view);

        testResultViewModel = new ViewModelProvider(requireActivity()).get(TestResultViewModel.class);
        questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);


        questionViewModel.getQuestions().observe(getViewLifecycleOwner(), questions -> {
            questions.sort(Comparator.comparingInt(Question::getOrder));

            testResultViewModel.getTestResult().observe(getViewLifecycleOwner(), tr -> {
                QuestionResultAdapter adapter = new QuestionResultAdapter(
                        requireContext(),
                        tr.questionsToArray(),
                        questions
                );
                binding.recyclerView.setAdapter(adapter);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

                int totalScores = questions.stream().map(Question::getScore).reduce(0, Integer::sum);
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(tr.isSuccessful() ? "The test is passed" : "The test is not passed")
                        .setMessage(String.format("Scores: %.2f/%d", tr.getTotalScores(), totalScores))
                        .setPositiveButton("Close", (dialog, which) -> {
                            goToTabs();
                        })
                        .setNegativeButton("View the result", ((dialog, which) -> {
                            dialog.dismiss();
                        }))
                        .show();
            });
        });

        binding.btnClose.setOnClickListener(v -> {
            goToTabs();
        });
    }

    private void goToTabs() {
        NavDirections action = TestResultFragmentDirections.actionTestResultFragmentToTabsFragment();
        NavHostFragment.findNavController(this).navigate(action);
    }
}
