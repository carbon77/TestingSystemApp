package com.example.testsys.screens.test.result;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.TestResultItemBinding;
import com.example.testsys.models.question.Question;
import com.example.testsys.models.testresult.TestResult;

import java.util.ArrayList;
import java.util.List;

public class QuestionResultAdapter extends RecyclerView.Adapter<QuestionResultAdapter.QuestionResultViewHolder> {
    private Context context;
    private List<TestResult.TestResultQuestion> resultQuestions;
    private List<Question> questions;

    public QuestionResultAdapter(Context context, List<TestResult.TestResultQuestion> resultQuestions, List<Question> questions) {
        this.context = context;
        this.resultQuestions = resultQuestions;
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_result_item, parent, false);
        return new QuestionResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionResultViewHolder holder, int position) {
        TestResultItemBinding binding = holder.getBinding();
        TestResult.TestResultQuestion resultQuestion = resultQuestions.get(position);
        Question question = questions.get(position);
        AnswerResultAdapter adapter = new AnswerResultAdapter(
                context,
                new ArrayList<>(resultQuestion.getAnswers().values()),
                new ArrayList<>(question.getAnswers().values()),
                resultQuestion.getType()
        );

        binding.tvQuestionText.setText((position + 1) + ". " + resultQuestion.getText());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount() {
        return resultQuestions.size();
    }

    public class QuestionResultViewHolder extends RecyclerView.ViewHolder {
        TestResultItemBinding binding;

        public QuestionResultViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TestResultItemBinding.bind(itemView);
        }

        public TestResultItemBinding getBinding() {
            return binding;
        }
    }
}
