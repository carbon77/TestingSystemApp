package com.example.testsys.models.question;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
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
            questions = new MutableLiveData<>();
        }

        if (testId == null) {
            questions.setValue(new ArrayList<>());
        } else {
            QuestionService.getQuestions(testId, it -> {
                questions.setValue(it);
            });
        }

        return questions;
    }

    public void createQuestions(String testId, List<Question> questions, Runnable completeListener) {
        QuestionService.createQuestions(testId, questions, () -> {
            completeListener.run();
        });
    }
}
