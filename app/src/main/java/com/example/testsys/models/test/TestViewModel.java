package com.example.testsys.models.test;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testsys.models.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TestViewModel extends ViewModel {
    private MutableLiveData<List<Test>> tests;
    private String uid;

    public TestViewModel(String uid) {
        super();
        this.uid = uid;
    }

    public LiveData<List<Test>> getTests() {
        if (tests == null) {
            tests = new MutableLiveData<>();

            TestService.loadTestsByUid(uid, it -> {
                tests.setValue(it);
            });
        }

        return tests;
    }

    public void createTest(Test test, Consumer<Test> completeListener) {
        TestService.createTest(test, completeListener);
    }

    public void updateTests(Consumer<List<Test>> completeListener) {
        TestService.loadTestsByUid(uid, it -> {
            completeListener.accept(it);
            tests.setValue(it);
        });
    }

    public void deleteTest(String testId, String uid, Runnable completeListener) {
        TestService.deleteTest(testId, uid, completeListener);
    }

    public void updateTest(String testId, Map<String, Object> updates, Runnable completeListener) {
        TestService.updateTest(testId, updates, test -> {
            completeListener.run();
        });
    }
}
