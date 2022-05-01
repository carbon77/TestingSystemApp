package com.example.testsys.screens.test.question;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.QuestionFormFragmentBinding;
import com.example.testsys.models.question.Question;
import com.example.testsys.models.question.QuestionType;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
        ArrayAdapter<String> adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, items);

        return new QuestionFormViewHolder(view, new EditTextListener(), adapter);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionFormViewHolder holder, int position) {
        int pos = holder.getLayoutPosition();
        holder.setQuestion(questions.get(pos));
        holder.getListener().updatePosition(pos);
        holder.getBinding().etQuestionText.setText(questions.get(pos).getText());
        holder.getBinding().tvQuestionPos.setText(String.valueOf(pos + 1));
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
