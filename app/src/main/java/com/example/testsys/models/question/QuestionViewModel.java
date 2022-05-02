package com.example.testsys.models.question;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class QuestionViewModel extends ViewModel {
    private MutableLiveData<List<Question>> questions;
    private String testId;

    public LiveData<List<Question>> getQuestions() {
        if (questions == null) {
            questions = new MutableLiveData<>();
        }

        return questions;
    }

    public void createQuestions(String testId, List<Question> questions, Consumer<List<Question>> completeListener) {
        QuestionService.createQuestions(testId, questions, qs -> {
            completeListener.accept(qs);
            this.questions.setValue(qs);
        });
    }

    public void updateTestId(String testId) {
        this.testId = testId;

        if (testId == null) {
            questions.setValue(null);
        } else {
            QuestionService.getQuestions(testId, it -> {
                questions.setValue(it);
            });
        }
    }
}
