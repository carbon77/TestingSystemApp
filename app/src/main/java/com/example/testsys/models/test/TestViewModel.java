package com.example.testsys.models.test;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testsys.models.user.User;

import java.net.PortUnreachableException;
import java.util.List;

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

            TestService.loadTests(uid, it -> {
                tests.setValue(it);
            });
        }

        return tests;
    }

    public Test createTest(User user, String text) {
        Test test = TestService.createTest(user, text);
        tests.getValue().add(test);
        return test;
    }
}
