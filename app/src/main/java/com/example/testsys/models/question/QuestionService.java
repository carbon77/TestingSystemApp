package com.example.testsys.models.question;

import com.example.testsys.models.ModelService;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class QuestionService extends ModelService {
    public static void getQuestions(String testId, Consumer<List<Question>> completeListener) {
        dbRef.child("questions").child(testId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Question> questions = new ArrayList<>();

                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Question question = task.getResult().getValue(Question.class);
                    questions.add(question);
                }

                completeListener.accept(questions);
            }
        });
    }
}
