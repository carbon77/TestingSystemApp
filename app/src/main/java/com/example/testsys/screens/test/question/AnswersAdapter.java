package com.example.testsys.screens.test.question;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.AnswerItemCheckboxBinding;
import com.example.testsys.models.question.Question;
import com.example.testsys.models.question.QuestionType;

import java.util.List;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.AnswerViewHolder> {
    private List<Question.Answer> answers;
    private QuestionType type;
    private AnswerItemCheckboxBinding binding;

    public AnswersAdapter(List<Question.Answer> answers, QuestionType type) {
        this.answers = answers;
        this.type = type;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item_checkbox, parent, false);
        return new AnswerViewHolder(view, new EditTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        int pos = holder.getLayoutPosition();
        holder.getListener().updatePosition(pos);

        binding = holder.getBinding();
        binding.etAnswerText.setText(answers.get(pos).getText());
        binding.answerCheckbox.setChecked(answers.get(pos).isCorrect());
        binding.answerCheckbox.setOnClickListener(v -> {
            answers.get(pos).setCorrect(binding.answerCheckbox.isChecked());
        });
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public void setAnswers(List<Question.Answer> answers) {
        this.answers = answers;
        notifyDataSetChanged();
    }

    public void setType(QuestionType type) {
        this.type = type;
        notifyDataSetChanged();
    }

    static class AnswerViewHolder extends RecyclerView.ViewHolder {
        private AnswerItemCheckboxBinding binding;
        private EditTextListener listener;

        public AnswerViewHolder(@NonNull View view, EditTextListener listener) {
            super(view);
            this.listener = listener;
            binding = AnswerItemCheckboxBinding.bind(view);
            binding.etAnswerText.addTextChangedListener(listener);
        }

        public AnswerItemCheckboxBinding getBinding() {
            return binding;
        }

        public EditTextListener getListener() {
            return listener;
        }
    }

    public class EditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            answers.get(position).setText(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
