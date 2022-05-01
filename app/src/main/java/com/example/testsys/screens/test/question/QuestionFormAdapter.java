package com.example.testsys.screens.test.question;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.models.question.Question;

import java.util.List;

public class QuestionFormAdapter extends RecyclerView.Adapter<QuestionFormViewHolder> {
    private Activity activity;
    private List<Question> questions;

    public QuestionFormAdapter(List<Question> questions, Activity activity) {
        this.questions = questions;
        this.activity = activity;
    }

    @NonNull
    @Override
    public QuestionFormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        String[] items = new String[] { "Radio", "Checkbox" };

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_form_fragment, parent, false);
        ArrayAdapter<String> questionTypeAdapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, items);

        return new QuestionFormViewHolder(view, new EditTextListener(), questionTypeAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionFormViewHolder holder, int position) {
        int pos = holder.getLayoutPosition();
        holder.setQuestion(questions.get(pos));
        holder.getListener().updatePosition(pos);
        holder.getBinding().etQuestionText.setText(questions.get(pos).getText());
        holder.getBinding().tvQuestionPos.setText(String.valueOf(pos + 1));

        initQuestionActionButtons(holder, pos);
    }

    private void initQuestionActionButtons(@NonNull QuestionFormViewHolder holder, int pos) {
        if (pos == 0) {
            holder.getBinding().deleteQuestionBtn.setVisibility(View.GONE);
            holder.getBinding().moveUpQuestionBtn.setVisibility(View.GONE);
            holder.getBinding().moveDownQuestionBtn.setVisibility(View.GONE);
        } else if (pos == questions.size() - 1) {
            holder.getBinding().deleteQuestionBtn.setVisibility(View.VISIBLE);
            holder.getBinding().moveUpQuestionBtn.setVisibility(View.VISIBLE);
            holder.getBinding().moveDownQuestionBtn.setVisibility(View.GONE);
        } else {
            holder.getBinding().deleteQuestionBtn.setVisibility(View.VISIBLE);
            holder.getBinding().moveUpQuestionBtn.setVisibility(View.VISIBLE);
            holder.getBinding().moveDownQuestionBtn.setVisibility(View.VISIBLE);
        }

        holder.getBinding().deleteQuestionBtn.setOnClickListener(v -> {
            questions.remove(pos);
            notifyDataSetChanged();
        });

        holder.getBinding().moveUpQuestionBtn.setOnClickListener(v -> {
            Question temp = questions.get(pos);
            questions.set(pos, questions.get(pos - 1));
            questions.set(pos - 1, temp);
            notifyDataSetChanged();
        });

        holder.getBinding().moveDownQuestionBtn.setOnClickListener(v -> {
            Question temp = questions.get(pos);
            questions.set(pos, questions.get(pos + 1));
            questions.set(pos + 1, temp);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
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
