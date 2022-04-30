package com.example.testsys.screens.test.question;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.QuestionFormFragmentBinding;
import com.example.testsys.models.question.Question;

import java.util.List;

public class QuestionFormAdapter extends RecyclerView.Adapter<QuestionFormAdapter.QuestionFormViewHolder> {
    private List<Question> questions;

    public QuestionFormAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public QuestionFormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_form_fragment, parent, false);
        return new QuestionFormViewHolder(view, new EditTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionFormViewHolder holder, int position) {
        int pos = holder.getLayoutPosition();
        holder.listener.updatePosition(pos);
        holder.binding.etQuestionText.setText(questions.get(pos).getText());
        holder.binding.tvQuestionPos.setText(String.valueOf(pos + 1));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class QuestionFormViewHolder extends RecyclerView.ViewHolder {
        private QuestionFormFragmentBinding binding;
        private EditTextListener listener;

        public QuestionFormViewHolder(View view, EditTextListener listener) {
            super(view);
            binding = QuestionFormFragmentBinding.bind(view);
            this.listener = listener;
            binding.etQuestionText.addTextChangedListener(listener);
        }
    }

    class EditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            questions.get(position).setText(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
