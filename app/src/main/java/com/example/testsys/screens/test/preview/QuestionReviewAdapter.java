package com.example.testsys.screens.test.preview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.QuestionReviewItemBinding;
import com.example.testsys.models.question.Answer;

import java.util.List;

public class QuestionReviewAdapter extends RecyclerView.Adapter<QuestionReviewAdapter.QuestionReviewViewHolder> {
    private List<Answer> answers;
    private Context context;

    public QuestionReviewAdapter(List<Answer> answers, Context context) {
        this.answers = answers;
        this.context = context;
    }

    @NonNull
    @Override
    public QuestionReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_review_item, parent, false);
        return new QuestionReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionReviewViewHolder holder, int position) {
        QuestionReviewItemBinding binding = holder.getBinding();
        binding.tvAnswerText.setText(answers.get(position).getText());

        if (!answers.get(position).getCorrect()) {
            binding.getRoot().setCardBackgroundColor(context.getResources().getColor(
                    R.color.fui_transparent,
                    context.getTheme()
            ));
        }
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class QuestionReviewViewHolder extends RecyclerView.ViewHolder {
        QuestionReviewItemBinding binding;

        public QuestionReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = QuestionReviewItemBinding.bind(itemView);
        }

        public QuestionReviewItemBinding getBinding() {
            return binding;
        }
    }
}
