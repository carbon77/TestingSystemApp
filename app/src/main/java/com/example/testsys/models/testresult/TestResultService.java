package com.example.testsys.models.testresult;

import com.example.testsys.models.ModelService;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TestResultService extends ModelService {
    static public void createTestResult(TestResult testResult, Consumer<TestResult> completeListener) {
        DatabaseReference testResultRef = dbRef.child("testResults").push();
        testResultRef.setValue(testResult).continueWith(v -> {
            testResult.setId(testResultRef.getKey());

            List<Task<Void>> tasks = new ArrayList<>();
            tasks.add(dbRef.child("userTestResult").child(testResult.getUserId()).child(testResult.getId()).setValue(true));
            tasks.add(dbRef.child("testTestResult").child(testResult.getTestId()).child(testResult.getId()).setValue(true));

            Tasks.whenAll(tasks).addOnSuccessListener(task -> {
               completeListener.accept(testResult);
            });

            return null;
        });

    }
}
