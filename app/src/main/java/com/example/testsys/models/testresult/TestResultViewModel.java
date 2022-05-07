package com.example.testsys.models.testresult;

import androidx.lifecycle.ViewModel;

import java.util.function.Consumer;

public class TestResultViewModel extends ViewModel {
    public void createTestResult(TestResult testResult, Consumer<TestResult> completeListener) {
        TestResultService.createTestResult(testResult, completeListener);
    }
}
