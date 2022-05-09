package com.example.testsys.screens.test.result;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.QuestionPreviewItemBinding;
import com.example.testsys.models.question.Answer;
import com.example.testsys.models.question.QuestionType;

import java.util.List;

public class AnswerResultAdapter extends RecyclerView.Adapter<AnswerResultAdapter.AnswerResultViewHolder> {
    private List<Answer> resultAnswers;
    private List<Answer> answers;
    private Context context;
    private QuestionType type;

    public AnswerResultAdapter(Context context, List<Answer> resultAnswers, List<Answer> answers, QuestionType type) {
        this.context = context;
        this.resultAnswers = resultAnswers;
        this.answers = answers;
        this.type = type;
    }

    @NonNull
    @Override
    public AnswerResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_preview_item, parent, false);
        return new AnswerResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerResultViewHolder holder, int position) {
        QuestionPreviewItemBinding binding = holder.getBinding();
        Answer resultAnswer = resultAnswers.get(position);
        Answer answer = answers.get(position);

        TypedValue rightAnswerColor = new TypedValue();
        TypedValue wrongAnswerColor = new TypedValue();
        TypedValue extraAnswerColor = new TypedValue();

        context.getTheme().resolveAttribute(R.attr.rightAnswerCardColor, rightAnswerColor, true);
        context.getTheme().resolveAttribute(R.attr.wrongAnswerCardColor, wrongAnswerColor, true);
        context.getTheme().resolveAttribute(R.attr.extraAnswerCardColor, extraAnswerColor, true);

        binding.tvAnswerText.setText(resultAnswer.getText());

        if (!resultAnswer.getCorrect() && !answer.getCorrect()) {
            binding.getRoot().setCardBackgroundColor(context.getColor(R.color.fui_transparent));
            return;
        }

        if (type == QuestionType.RADIO) {
            if (answer.getCorrect()) {
                binding.getRoot().setCardBackgroundColor(rightAnswerColor.data);
            } else if (resultAnswer.getCorrect() && !answer.getCorrect()) {
                binding.getRoot().setCardBackgroundColor(wrongAnswerColor.data);
            }
        } else if (type == QuestionType.CHECKBOX) {
            if (resultAnswer.getCorrect() && answer.getCorrect()) {
                binding.getRoot().setCardBackgroundColor(rightAnswerColor.data);
            } else if (!resultAnswer.getCorrect() && answer.getCorrect()) {
                binding.getRoot().setCardBackgroundColor(extraAnswerColor.data);
            } else if (resultAnswer.getCorrect() && !answer.getCorrect()) {
                binding.getRoot().setCardBackgroundColor(wrongAnswerColor.data);
            }
        }
    }

    @Override
    public int getItemCount() {
        return resultAnswers.size();
    }

    public class AnswerResultViewHolder extends RecyclerView.ViewHolder {
        QuestionPreviewItemBinding binding;

        public AnswerResultViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = QuestionPreviewItemBinding.bind(itemView);
        }

        private QuestionPreviewItemBinding getBinding() {
            return binding;
        }
    }
}
