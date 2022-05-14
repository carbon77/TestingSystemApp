package com.example.testsys.models.testresult;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
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

    public void loadTestResultsByTest(String testId, Consumer<List<TestResult>> completeListener) {
        TestResultService.loadResultsByTest(testId, completeListener);
    }

    public void loadTestResultsByUser(String uid, Consumer<List<TestResult>> completeListener) {
        TestResultService.loadResultsByUser(uid, completeListener);
    }

    public void loadResults(String uid, String testId, Consumer<List<TestResult>> completeListener) {
        TestResultService.loadResults(uid, testId, completeListener);
    }
}
