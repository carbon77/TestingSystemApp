package com.example.testsys.screens.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.TestResultCardBinding;
import com.example.testsys.models.test.Test;
import com.example.testsys.models.testresult.TestResult;
import com.example.testsys.models.testresult.TestResultViewModel;

import java.util.List;

public class TestResultsAdapter extends RecyclerView.Adapter<TestResultsAdapter.TestResultViewHolder> {
    private Test test;
    private List<TestResult> testResults;
    private int totalScores;
    private AppCompatActivity activity;
    private TestResultViewModel testResultViewModel;

    public TestResultsAdapter(Test test, List<TestResult> testResults, int totalScores, AppCompatActivity activity) {
        this.test = test;
        this.testResults = testResults;
        this.totalScores = totalScores;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TestResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        testResultViewModel = new ViewModelProvider(activity).get(TestResultViewModel.class);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_result_card, parent, false);
        return new TestResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestResultViewHolder holder, int position) {
        TestResultCardBinding binding = holder.getBinding();
        TestResult testResult = testResults.get(position);

        String status = testResult.isSuccessful() ? "Passed" : "Not passed";
        String scores = String.format("%.2f/%d", testResult.getTotalScores(), totalScores);

        binding.tvTestResultStatus.setText(status);
        binding.tvTestResultDate.setText(testResult.getPassingDate());
        binding.tvTestResultScores.setText(scores);
        binding.tvNumber.setText(String.valueOf(position + 1));
        binding.getRoot().setOnClickListener(v -> {
            testResultViewModel.updateTestResult(testResult);
            NavDirections action = TestResultsFragmentDirections
                    .actionTestResultsFragmentToTestResultFragment(test.getTitle());
            NavHostFragment navHost = (NavHostFragment) activity
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.main_nav_host_fragment);
            navHost.getNavController().navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return testResults.size();
    }

    public class TestResultViewHolder extends RecyclerView.ViewHolder {
        private TestResultCardBinding binding;

        public TestResultViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TestResultCardBinding.bind(itemView);
        }

        public TestResultCardBinding getBinding() {
            return binding;
        }
    }
}
