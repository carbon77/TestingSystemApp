package com.example.testsys.models.question;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class QuestionViewModel extends ViewModel {
    private MutableLiveData<List<Question>> questions;
    private final String testId;

    public QuestionViewModel(String testId) {
        super();
        this.testId = testId;
    }

    public LiveData<List<Question>> getQuestions() {
        if (questions == null) {
            QuestionService.getQuestions(testId, it -> {
                questions.setValue(it);
            });
        }

        return questions;
    }

    public void createQuestion(Question question) {
        questions.getValue().add(QuestionService.createQuestion(question));
    }
}
