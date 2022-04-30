package com.example.testsys.models.question;

import com.example.testsys.models.ModelService;
import com.example.testsys.models.test.Test;
import com.google.android.gms.tasks.Task;
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
                    Question question = task.getResult().getValue(Question.class);
                    questions.add(question);
                }

                completeListener.accept(questions);
            }
        });
    }

    public static Question createQuestion(Question question) {
        DatabaseReference questionRef = dbRef.child("questions").child(question.getTestId()).push();
        questionRef.setValue(question);
        question.setId(questionRef.getKey());
        questionRef.child("answers").setValue(question.getAnswers());
        return question;
    }

    public static void createQuestions(String testId, List<Question> questions, Runnable completeListener) {
        for (Question q : questions) {
            q.setTestId(testId);
            createQuestion(q);
        }

        dbRef.child("tests").child(testId).get().continueWith(task -> {
           if (task.isSuccessful()) {
               int questionCount = task.getResult().getValue(Test.class).getQuestionCount();

               dbRef.child("tests").child(testId).child("questionCount").setValue(questionCount + questions.size()).addOnCompleteListener(t -> {
                   if (t.isSuccessful()) {
                       completeListener.run();
                   }
               });
           }

           return null;
        });
    }
}
