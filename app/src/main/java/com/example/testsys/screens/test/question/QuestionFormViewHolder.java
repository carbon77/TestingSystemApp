package com.example.testsys.screens.test.question;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.QuestionFormFragmentBinding;
import com.example.testsys.models.question.Question;
import com.example.testsys.models.question.QuestionType;

public class QuestionFormViewHolder extends RecyclerView.ViewHolder {
    private QuestionFormFragmentBinding binding;
    private QuestionFormAdapter.EditTextListener listener;
    private ArrayAdapter<String> questionTypeAdapter;
    private AnswersAdapter answersAdapter;

    public QuestionFormViewHolder(
            View view,
            QuestionFormAdapter.EditTextListener listener,
            ArrayAdapter<String> questionTypeAdapter,
            AnswersAdapter answersAdapter
    ) {
        super(view);
        this.questionTypeAdapter = questionTypeAdapter;
        this.listener = listener;
        this.binding = QuestionFormFragmentBinding.bind(view);
        this.answersAdapter = answersAdapter;

        binding.etQuestionText.addTextChangedListener(listener);
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

    public QuestionFormAdapter.EditTextListener getListener() {
        return listener;
    }

    public QuestionFormFragmentBinding getBinding() {
        return binding;
    }

    public AnswersAdapter getAnswersAdapter() {
        return answersAdapter;
    }
}
