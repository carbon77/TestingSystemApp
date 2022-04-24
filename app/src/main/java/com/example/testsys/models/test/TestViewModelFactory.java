package com.example.testsys.models.test;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TestViewModelFactory implements ViewModelProvider.Factory {
    private String uid;

    public TestViewModelFactory(String uid) {
        this.uid = uid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TestViewModel(uid);
    }
}
