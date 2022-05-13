package com.example.testsys.models.test;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TestViewModel extends ViewModel {
    private MutableLiveData<List<Test>> tests;
    private String uid;

    public LiveData<List<Test>> getTests() {
        if (tests == null) {
            tests = new MutableLiveData<>();
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

    public void deleteTest(Test test, String uid, Runnable completeListener) {
        TestService.deleteTest(test, uid, completeListener);
    }

    public void updateTest(String testId, Map<String, Object> updates, Runnable completeListener) {
        TestService.updateTest(testId, updates, test -> {
            completeListener.run();
        });
    }

    public void loadTests(String uid) {
        this.uid = uid;
        TestService.loadTestsByUid(uid, tests -> {
            this.tests.setValue(tests);
        });
    }

    public void addTest(String uid, String testId, Runnable completeListener) {
        TestService.addTest(uid, testId, completeListener);
    }
}
