package com.example.testsys.models.testresult;

import com.example.testsys.models.ModelService;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
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

    static public void loadResultsByTest(String testId, Consumer<List<TestResult>> completeListener) {
        dbRef.child("testTestResult").child(testId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful())
                return;

            List<TestResult> testResults = new ArrayList<>();
            List<Task<Void>> tasks = new ArrayList<>();

            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                tasks.add(dbRef.child("testResults").child(snapshot.getKey()).get().continueWith(trSnapshot -> {
                    if (!trSnapshot.isSuccessful())
                        return null;

                    TestResult tr = trSnapshot.getResult().getValue(TestResult.class);
                    tr.setId(snapshot.getKey());
                    testResults.add(tr);
                    return null;
                }));
            }

            Tasks.whenAll(tasks).addOnCompleteListener(t -> {
               completeListener.accept(testResults);
            });
        });
    }

    static public void loadResultsByUser(String uid, Consumer<List<TestResult>> completeListener) {
        dbRef.child("userTestResult").child(uid).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful())
                return;

            List<TestResult> testResults = new ArrayList<>();
            List<Task<Void>> tasks = new ArrayList<>();

            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                tasks.add(dbRef.child("testResults").child(snapshot.getKey()).get().continueWith(trSnapshot -> {
                    if (!trSnapshot.isSuccessful())
                        return null;

                    TestResult tr = trSnapshot.getResult().getValue(TestResult.class);
                    tr.setId(snapshot.getKey());
                    testResults.add(tr);
                    return null;
                }));
            }

            Tasks.whenAll(tasks).addOnCompleteListener(t -> {
                completeListener.accept(testResults);
            });
        });
    }

    static public void loadResults(String uid, String testId, Consumer<List<TestResult>> completeListener) {
        dbRef.child("userTestResult").child(uid).get().addOnCompleteListener(task -> {
            List<Task<Void>> tasks = new ArrayList<>();
            List<TestResult> trs = new ArrayList<>();
            if (task.isSuccessful()) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String trId = snapshot.getKey();

                    tasks.add(dbRef.child("testResults").child(trId).get().continueWith(t -> {
                        if (t.isSuccessful()) {
                            TestResult tr = t.getResult().getValue(TestResult.class);
                            if (tr.getTestId().equals(testId)) {
                                trs.add(tr);
                            }
                        }
                        return null;
                    }));
                }

                Tasks.whenAll(tasks).addOnSuccessListener(v -> {
                    completeListener.accept(trs);
                });
            }
        });
    }
}
