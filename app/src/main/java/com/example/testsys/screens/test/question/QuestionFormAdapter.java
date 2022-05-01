package com.example.testsys.screens.test.question;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.QuestionFormFragmentBinding;
import com.example.testsys.models.question.Question;
import com.example.testsys.utils.ViewAnimationUtils;

import java.util.List;

public class QuestionFormAdapter extends RecyclerView.Adapter<QuestionFormViewHolder> {
    private Activity activity;
    private List<Question> questions;
    private QuestionFormFragmentBinding binding;

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
        binding = holder.getBinding();
        holder.setQuestion(questions.get(pos));
        holder.getListener().updatePosition(pos);
        binding.etQuestionText.setText(questions.get(pos).getText());
        binding.tvQuestionPos.setText(String.valueOf(pos + 1));

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

        initQuestionActionButtons(holder, pos);
    }

    private void initQuestionActionButtons(@NonNull QuestionFormViewHolder holder, int pos) {
        if (pos == 0) {
            binding.moveUpQuestionBtn.setVisibility(View.GONE);
            binding.moveDownQuestionBtn.setVisibility(View.GONE);
        } else if (pos == questions.size() - 1) {
            binding.moveUpQuestionBtn.setVisibility(View.VISIBLE);
            binding.moveDownQuestionBtn.setVisibility(View.GONE);
        } else {
            binding.moveUpQuestionBtn.setVisibility(View.VISIBLE);
            binding.moveDownQuestionBtn.setVisibility(View.VISIBLE);
        }

        binding.deleteQuestionBtn.setOnClickListener(v -> {
            questions.remove(pos);
            notifyDataSetChanged();
        });

        binding.moveUpQuestionBtn.setOnClickListener(v -> {
            Question temp = questions.get(pos);
            questions.set(pos, questions.get(pos - 1));
            questions.set(pos - 1, temp);
            notifyDataSetChanged();
        });

        binding.moveDownQuestionBtn.setOnClickListener(v -> {
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
