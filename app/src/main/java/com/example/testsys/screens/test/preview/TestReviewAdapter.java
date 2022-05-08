package com.example.testsys.screens.test.preview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.TestResultItemBinding;
import com.example.testsys.models.question.Answer;
import com.example.testsys.models.testresult.TestResult;

import java.util.ArrayList;
import java.util.List;

public class TestReviewAdapter extends RecyclerView.Adapter<TestReviewAdapter.TestReviewViewHolder> {
    private List<TestResult.TestResultQuestion> questions;
    private Context context;

    public TestReviewAdapter(List<TestResult.TestResultQuestion> questions, Context context) {
        this.questions = questions;
        this.context = context;
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
        TestResult.TestResultQuestion question = questions.get(position);
        List<Answer> answers = new ArrayList<>(question.getAnswers().values());
        QuestionReviewAdapter adapter = new QuestionReviewAdapter(answers, context);

        binding.tvQuestionText.setText((position + 1) + ". " + questions.get(position).getText());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
