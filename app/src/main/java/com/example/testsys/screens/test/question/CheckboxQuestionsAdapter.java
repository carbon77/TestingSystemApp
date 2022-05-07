package com.example.testsys.screens.test.question;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.CheckboxAnswerItemBinding;

import java.util.Map;

public class CheckboxQuestionsAdapter extends RecyclerView.Adapter<CheckboxQuestionsAdapter.CheckboxQuestionViewHolder> {
    private Map<String, Boolean> answers;

    public CheckboxQuestionsAdapter(Map<String, Boolean> answers) {
        this.answers = answers;
    }

    @NonNull
    @Override
    public CheckboxQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_answer_item, parent, false);
        return new CheckboxQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckboxQuestionViewHolder holder, int position) {
        int pos = holder.getLayoutPosition();
        String answerText = (String) answers.keySet().toArray()[pos];
        CheckboxAnswerItemBinding binding = holder.getBinding();

        binding.tvAnswerText.setText(answerText);
        binding.checkbox.setChecked(answers.get(answerText));

        binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String text = (String) answers.keySet().toArray()[holder.getLayoutPosition()];
            answers.put(text, isChecked);
        });
        binding.getRoot().setOnClickListener(v -> {
            String text = (String) answers.keySet().toArray()[holder.getLayoutPosition()];
            binding.checkbox.setChecked(!answers.get(text));
        });
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    static class CheckboxQuestionViewHolder extends RecyclerView.ViewHolder {
        private CheckboxAnswerItemBinding binding;

        public CheckboxQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CheckboxAnswerItemBinding.bind(itemView);
        }

        public CheckboxAnswerItemBinding getBinding() {
            return binding;
        }
    }
}
