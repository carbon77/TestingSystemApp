package com.example.testsys.screens.test.question;

import android.view.View;
import android.widget.ArrayAdapter;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.databinding.QuestionFormFragmentBinding;
import com.example.testsys.models.question.Question;
import com.example.testsys.models.question.QuestionType;

public class QuestionFormViewHolder extends RecyclerView.ViewHolder {
    private QuestionFormFragmentBinding binding;
    private QuestionFormAdapter.EditTextListener listener;
    private ArrayAdapter<String> questionTypeAdapter;
    private Question question;

    public QuestionFormViewHolder(View view, QuestionFormAdapter.EditTextListener listener, ArrayAdapter<String> questionTypeAdapter) {
        super(view);
        this.questionTypeAdapter = questionTypeAdapter;
        this.listener = listener;
        this.binding = QuestionFormFragmentBinding.bind(view);

        binding.etQuestionText.addTextChangedListener(listener);
        binding.tvQuestionType.setAdapter(questionTypeAdapter);
        binding.tvQuestionType.setOnItemClickListener((parent, view1, position, id) -> {
            QuestionType t = position == 0 ? QuestionType.RADIO : QuestionType.CHECKBOX;
            question.setType(t);
        });
    }

    public void setQuestion(Question question) {
        this.question = question;
        binding.tvQuestionType.setText(question.getTypeName(), false);
    }

    public QuestionFormAdapter.EditTextListener getListener() {
        return listener;
    }

    public QuestionFormFragmentBinding getBinding() {
        return binding;
    }
}
