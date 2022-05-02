package com.example.testsys.models.question;

import com.example.testsys.models.ModelService;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class QuestionService extends ModelService {
    public static void getQuestions(String testId, Consumer<List<Question>> completeListener) {
        dbRef.child("questions").child(testId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Question> questions = new ArrayList<>();

                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Question question = snapshot.getValue(Question.class);
                    questions.add(question);
                }

                completeListener.accept(questions);
            }
        });
    }

    public static Task<Void> createQuestion(Question question) {
        DatabaseReference questionRef = dbRef.child("questions").child(question.getTestId()).push();

        return questionRef.setValue(question).continueWith(task -> {
            if (task.isSuccessful()) {
                question.setId(questionRef.getKey());
            }

            return null;
        });
    }

    public static void createQuestions(String testId, List<Question> questions, Consumer<List<Question>> completeListener) {
        List<Task<Void>> tasks = new ArrayList<>();

        for (Question q : questions) {
            q.setTestId(testId);
            tasks.add(createQuestion(q));
        }

        Tasks.whenAll(tasks).addOnSuccessListener(s -> {
            completeListener.accept(questions);
        });
    }
}
