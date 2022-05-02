package com.example.testsys.screens.test.question;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.AnswerItemBinding;
import com.example.testsys.models.question.Answer;

import java.util.Map;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.AnswerViewHolder> {
    private Map<String, Answer> answers;
    private AnswerItemBinding binding;

    public AnswersAdapter(Map<String, Answer> answers) {
        this.answers = answers;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item, parent, false);
        return new AnswerViewHolder(view, new EditTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        int pos = holder.getLayoutPosition();
        holder.getListener().updatePosition(pos);
        Answer answer = answers.get(getAnswerKey(pos));

        binding = holder.getBinding();
        binding.etAnswerText.setText(answer.getText());
        binding.answerCheckbox.setChecked(answer.getCorrect());
        binding.answerCheckbox.setOnCheckedChangeListener((v, isChecked) -> {
            answer.setCorrect(isChecked);
        });
        binding.deleteAnswerBtn.setOnClickListener(v -> {
            answers.remove(getAnswerKey(pos));
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public void setAnswers(Map<String, Answer> answers) {
        this.answers = answers;
        notifyDataSetChanged();
    }

    static class AnswerViewHolder extends RecyclerView.ViewHolder {
        private AnswerItemBinding binding;
        private EditTextListener listener;

        public AnswerViewHolder(@NonNull View view, EditTextListener listener) {
            super(view);
            this.listener = listener;
            binding = AnswerItemBinding.bind(view);
            binding.etAnswerText.addTextChangedListener(listener);
        }

        public AnswerItemBinding getBinding() {
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
            answers.get(getAnswerKey(position)).setText(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private String getAnswerKey(int position) {
        return position + "_key";
    }
}
