package com.example.testsys.screens.test;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.testsys.R;
import com.example.testsys.databinding.TestResultsFragmentBinding;
import com.example.testsys.models.test.Test;
import com.example.testsys.models.test.TestViewModel;
import com.example.testsys.models.testresult.TestResult;
import com.example.testsys.models.testresult.TestResultViewModel;
import com.example.testsys.models.user.User;
import com.example.testsys.models.user.UserViewModel;

import java.util.List;

public class TestResultsFragment extends Fragment {
    private TestResultsFragmentBinding binding;
    private User user;
    private List<TestResult> testResults;
    private Test test;
    private String testId;

    private UserViewModel userViewModel;
    private TestViewModel testViewModel;
    private TestResultViewModel testResultViewModel;

    public TestResultsFragment() {
        super(R.layout.test_results_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = TestResultsFragmentBinding.bind(view);

        testId = TestResultsFragmentArgs.fromBundle(getArguments()).getTestId();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        testViewModel = new ViewModelProvider(requireActivity()).get(TestViewModel.class);
        testResultViewModel = new ViewModelProvider(requireActivity()).get(TestResultViewModel.class);

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            this.user = user;

            testViewModel.getTests().observe(getViewLifecycleOwner(), tests -> {
                for (Test t : tests) {
                    if (t.getId().equals(testId)) {
                        this.test = t;
                    }
                }

                testResultViewModel.loadResults(user.getId(), testId, testResults -> {
                    this.testResults = testResults;
                    TestResultsAdapter adapter = new TestResultsAdapter(
                            test,
                            testResults,
                            test.getTotalScores(),
                            (AppCompatActivity) requireActivity()
                    );
                    binding.recyclerView.setAdapter(adapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                });
            });
        });
    }
}
