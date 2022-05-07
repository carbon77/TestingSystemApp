package com.example.testsys.models.testresult;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.function.Consumer;

public class TestResultViewModel extends ViewModel {
    private MutableLiveData<TestResult> testResult = new MutableLiveData<>();

    public LiveData<TestResult> getTestResult() {
        return testResult;
    }

    public void updateTestResult(TestResult testResult) {
        this.testResult.setValue(testResult);
    }

    public void createTestResult(TestResult testResult, Consumer<TestResult> completeListener) {
        TestResultService.createTestResult(testResult, completeListener);
    }
}
