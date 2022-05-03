package com.example.testsys.models.question;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class QuestionViewModel extends ViewModel {
    private MutableLiveData<List<Question>> questions = new MutableLiveData<>();

    public LiveData<List<Question>> getQuestions() {
        return questions;
    }

    public void createQuestions(String testId, List<Question> questions, Consumer<List<Question>> completeListener) {
        QuestionService.createQuestions(testId, questions, completeListener);
    }

    public void updateTestId(String testId) {
        if (testId == null) {
            questions.setValue(null);
        } else {
            QuestionService.getQuestions(testId, it -> {
                questions.setValue(it);
            });
        }
    }

    public void updateQuestions(String testId, Map<String, Object> updates, Consumer<List<Question>> completeListener) {
        QuestionService.updateQuestions(testId, updates, completeListener);
    }

    public void deleteQuestions(String testId, List<String> questionIds, Runnable completeListener) {
        QuestionService.deleteQuestions(testId, questionIds, completeListener);
    }
}
