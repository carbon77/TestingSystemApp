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
import com.example.testsys.databinding.QuestionFormFragmentBinding;
import com.example.testsys.models.question.Question;
import com.example.testsys.models.question.QuestionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        AnswersAdapter answersAdapter = new AnswersAdapter(new HashMap<>());
        Map<String, EditTextListener> listeners = new HashMap<>();
        listeners.put("text", new EditTextListener("text"));
        listeners.put("score", new EditTextListener("score"));

        return new QuestionFormViewHolder(view, questionTypeAdapter, listeners, answersAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionFormViewHolder holder, int position) {
        int pos = holder.getLayoutPosition();
        Question question = questions.get(pos);
        holder.updatePosition(pos);
        holder.getAnswersAdapter().setAnswers(question.getAnswers());

        binding = holder.getBinding();
        binding.etQuestionText.setText(question.getText());
        binding.tvQuestionPos.setText(String.valueOf(pos + 1));
        binding.tvQuestionScore.setText(String.valueOf(question.getScore()));

        binding.tvQuestionType.setText(question.typeToString(), false);
        binding.tvQuestionType.setOnItemClickListener((parent, view1, p, id) -> {
            QuestionType t = p == 0 ? QuestionType.RADIO : QuestionType.CHECKBOX;
            question.setType(t);
        });

        binding.addAnswerBtn.setOnClickListener(v -> {
            question.addAnswer("", false);
            holder.getAnswersAdapter().notifyDataSetChanged();
        });

        initQuestionActionButtons(pos);
    }

    private void initQuestionActionButtons(int pos) {
        if (pos == 0 && questions.size() == 1) {
            binding.moveDownQuestionBtn.setVisibility(View.GONE);
            binding.moveUpQuestionBtn.setVisibility(View.GONE);
        } else if (pos == 0) {
            binding.moveUpQuestionBtn.setVisibility(View.GONE);
            binding.moveDownQuestionBtn.setVisibility(View.VISIBLE);
        }
        else if (pos == questions.size() - 1) {
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
            moveQuestions(pos, pos - 1);
            notifyDataSetChanged();
        });

        binding.moveDownQuestionBtn.setOnClickListener(v -> {
            moveQuestions(pos, pos + 1);
            notifyDataSetChanged();
        });
    }

    private void moveQuestions(int i1, int i2) {
        Question temp = questions.get(i1);
        int tempOrder = questions.get(i1).getOrder();

        questions.get(i1).setOrder(questions.get(i2).getOrder());
        questions.set(i1, questions.get(i2));

        questions.get(i2).setOrder(tempOrder);
        questions.set(i2, temp);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class EditTextListener implements TextWatcher {
        private int position;
        private String field;

        public EditTextListener(String field) {
            super();
            this.field = field;
        }

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (field.equals("text")) {
                questions.get(position).setText(s.toString());
            } else if (field.equals("score")) {
                if (s.toString().equals(""))
                    return;
                questions.get(position).setScore(Integer.parseInt(s.toString()));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
