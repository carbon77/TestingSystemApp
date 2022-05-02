package com.example.testsys.models.question;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class QuestionViewModelFactory implements ViewModelProvider.Factory {
    private String testId;

    public QuestionViewModelFactory(String testId) {
        this.testId = testId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new QuestionViewModel(testId);
    }
}
