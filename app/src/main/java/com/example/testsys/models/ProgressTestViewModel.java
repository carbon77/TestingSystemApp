package com.example.testsys.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProgressTestViewModel extends ViewModel {
    private MutableLiveData<Integer> progress = new MutableLiveData<>();

    public LiveData<Integer> getProgress() {
        return progress;
    }

    public void updateProgress(int progress) {
        this.progress.setValue(progress);
    }
}
