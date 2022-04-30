package com.example.testsys.models.test;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testsys.models.user.User;

import java.util.List;
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

    public void createTest(User user, String text) {
        Test test = TestService.createTest(user, text);
        tests.getValue().add(test);
    }

    public void updateTests(Consumer<List<Test>> completeListener) {
        TestService.loadTestsByUid(uid, it -> {
            completeListener.accept(it);
            tests.setValue(it);
        });
    }
}
