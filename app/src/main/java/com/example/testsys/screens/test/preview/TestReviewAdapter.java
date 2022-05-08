package com.example.testsys.screens.test.preview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.TestResultItemBinding;
import com.example.testsys.models.testresult.TestResult;

import java.util.List;

public class TestReviewAdapter extends RecyclerView.Adapter<TestReviewAdapter.TestReviewViewHolder> {
    private List<TestResult.TestResultQuestion> questions;

    public TestReviewAdapter(List<TestResult.TestResultQuestion> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public TestReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_result_item, parent, false);
        return new TestReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestReviewViewHolder holder, int position) {
        TestResultItemBinding binding = holder.getBinding();
        binding.tvQuestionText.setText((position + 1) + ". " + questions.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class TestReviewViewHolder extends RecyclerView.ViewHolder {
        TestResultItemBinding binding;

        public TestReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TestResultItemBinding.bind(itemView);
        }

        public TestResultItemBinding getBinding() {
            return binding;
        }
    }
}
