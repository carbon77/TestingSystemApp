package com.example.testsys.screens.test.preview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.QuestionPreviewItemBinding;
import com.example.testsys.models.question.Answer;

import java.util.List;

public class QuestionPreviewAdapter extends RecyclerView.Adapter<QuestionPreviewAdapter.QuestionPreviewViewHolder> {
    private List<Answer> answers;
    private Context context;

    public QuestionPreviewAdapter(List<Answer> answers, Context context) {
        this.answers = answers;
        this.context = context;
    }

    @NonNull
    @Override
    public QuestionPreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_preview_item, parent, false);
        return new QuestionPreviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionPreviewViewHolder holder, int position) {
        QuestionPreviewItemBinding binding = holder.getBinding();
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

    public class QuestionPreviewViewHolder extends RecyclerView.ViewHolder {
        QuestionPreviewItemBinding binding;

        public QuestionPreviewViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = QuestionPreviewItemBinding.bind(itemView);
        }

        public QuestionPreviewItemBinding getBinding() {
            return binding;
        }
    }
}
