package com.example.testsys.screens.test.question;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.QuestionFormFragmentBinding;

import java.util.HashMap;
import java.util.Map;

public class QuestionFormViewHolder extends RecyclerView.ViewHolder {
    private QuestionFormFragmentBinding binding;
    private ArrayAdapter<String> questionTypeAdapter;
    private Map<String, QuestionFormAdapter.EditTextListener> listeners;
    private AnswersAdapter answersAdapter;

    public QuestionFormViewHolder(
            View view,
            ArrayAdapter<String> questionTypeAdapter,
            Map<String, QuestionFormAdapter.EditTextListener> listeners,
            AnswersAdapter answersAdapter
    ) {
        super(view);
        this.questionTypeAdapter = questionTypeAdapter;
        this.binding = QuestionFormFragmentBinding.bind(view);
        this.answersAdapter = answersAdapter;
        this.listeners = listeners;

        binding.etQuestionText.addTextChangedListener(this.listeners.get("text"));
        binding.tvQuestionScore.addTextChangedListener(this.listeners.get("score"));
        binding.tvQuestionType.setAdapter(questionTypeAdapter);

        binding.answersRecyclerView.setAdapter(this.answersAdapter);
        binding.answersRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        binding.answersView.setVisibility(View.GONE);
        binding.answersViewBtn.setOnClickListener(v -> {
            if (binding.answersView.getVisibility() == View.GONE) {
                TransitionManager.beginDelayedTransition(binding.answersView, new AutoTransition());
                binding.answersView.setVisibility(View.VISIBLE);
                binding.answersViewIcon.setImageResource(R.drawable.ic_move_up);
            } else {
                TransitionManager.beginDelayedTransition(binding.answersView, new AutoTransition());
                binding.answersView.setVisibility(View.GONE);
                binding.answersViewIcon.setImageResource(R.drawable.ic_move_down);
            }
        });
    }

    public void updatePosition(int position) {
        for (QuestionFormAdapter.EditTextListener listener : listeners.values()) {
            listener.updatePosition(position);
        }
    }

    public QuestionFormFragmentBinding getBinding() {
        return binding;
    }

    public AnswersAdapter getAnswersAdapter() {
        return answersAdapter;
    }
}
