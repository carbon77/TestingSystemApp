package com.example.testsys.models.test;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.testsys.models.user.User;

import java.util.ArrayList;
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

    public Test createTest(User user, String text) {
        Test test = TestService.createTest(user, text);
        List<Test> copy = new ArrayList<>(tests.getValue());
        copy.add(test);
        tests.setValue(copy);
        return test;
    }

    public void updateTests(Consumer<List<Test>> completeListener) {
        TestService.loadTestsByUid(uid, it -> {
            completeListener.accept(it);
            tests.setValue(it);
        });
    }

    public void deleteTest(String testId, String uid, Runnable completeListener) {
        List<Test> copy = new ArrayList<>(tests.getValue());

        TestService.deleteTest(testId, uid, () -> {
            for (int i = 0; i < copy.size(); i++) {
                if (copy.get(i).getId().equals(testId)) {
                    copy.remove(i);
                    break;
                }
            }

            tests.setValue(copy);
            completeListener.run();
        });

    }
}
