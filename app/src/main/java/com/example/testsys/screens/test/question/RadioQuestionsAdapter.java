package com.example.testsys.screens.test.question;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.RadioAnswerItemBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RadioQuestionsAdapter extends RecyclerView.Adapter<RadioQuestionsAdapter.RadioQuestionViewHolder> {
    private Map<String, Boolean> answers;
    private String selectedAnswer;
    private List<RadioButton> radios;

    public RadioQuestionsAdapter(Map<String, Boolean> answers) {
        this.answers = answers;
        this.selectedAnswer = (String) answers.keySet().toArray()[0];
        this.radios = new ArrayList<>();
    }

    @NonNull
    @Override
    public RadioQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.radio_answer_item, parent, false);
        radios.add((RadioButton) view.findViewById(R.id.radio));
        return new RadioQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RadioQuestionViewHolder holder, int position) {
        RadioAnswerItemBinding binding = holder.getBinding();
        String answerText = (String) answers.keySet().toArray()[position];

        binding.tvAnswerText.setText(answerText);
        answers.put(answerText, answers.get(answerText));
        binding.radio.setChecked(answers.get(answerText));

        binding.radio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                for (int i = 0; i < radios.size(); i++) {
                    if (i != holder.getLayoutPosition()) {
                        String text = (String) answers.keySet().toArray()[i];
                        answers.put(text, false);

                        radios.get(i).setChecked(false);
                    }
                }

                String text = (String) answers.keySet().toArray()[holder.getLayoutPosition()];
                answers.put(text, true);
            }
        });
        binding.getRoot().setOnClickListener(v -> {
            radios.get(holder.getLayoutPosition()).setChecked(true);
        });
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    class RadioQuestionViewHolder extends RecyclerView.ViewHolder{
        RadioAnswerItemBinding binding;

        public RadioQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RadioAnswerItemBinding.bind(itemView);
        }

        public RadioAnswerItemBinding getBinding() {
            return binding;
        }
    }
}
