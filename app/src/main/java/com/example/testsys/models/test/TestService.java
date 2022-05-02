package com.example.testsys.models.test;

import com.example.testsys.models.ModelService;
import com.example.testsys.models.user.User;
import com.example.testsys.utils.DateService;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TestService extends ModelService {
    static public void createTest(Test test, Consumer<Test> completeListener) {
        DatabaseReference testRef = dbRef.child("tests").push();
        testRef.setValue(test).continueWith(v -> {
            String testId = testRef.getKey();
            test.setId(testId);
            List<Task<Void>> tasks = new ArrayList<>();
            tasks.add(dbRef.child("userTest").child(test.getUserId()).child(testId).setValue(true));
            tasks.add(dbRef.child("testUser").child(testId).child(test.getUserId()).setValue(true));

            Tasks.whenAll(tasks).addOnSuccessListener(t -> {
                completeListener.accept(test);
            });

            return null;
        });
    }

    static public void loadTestsByUid(String uid, Consumer<List<Test>> completeListener) {
        List<Test> tests = new ArrayList<>();

        dbRef.child("userTest").child(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Task<Void>> tasks = new ArrayList<>();

                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String testId = snapshot.getKey();

                    tasks.add(dbRef.child("tests").child(testId).get().continueWith(t -> {
                        if (t.isSuccessful()) {
                            Test test = t.getResult().getValue(Test.class);
                            test.setId(testId);
                            tests.add(test);
                        }

                        return null;
                    }));
                }

                Tasks.whenAll(tasks).addOnCompleteListener(t -> {
                   if (t.isSuccessful()) {
                       completeListener.accept(tests);
                   }
                });
            }
        });
    }

    static public void loadTestById(String id, Consumer<Test> completeListener) {
        dbRef.child("tests").child(auth.getCurrentUser().getUid()).child(id).get().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               Test test = task.getResult().getValue(Test.class);
               test.setId(id);
               completeListener.accept(test);
           }
        });
    }

    static public void deleteTest(String testId, String uid, Runnable completeListener) {
        List<Task<Void>> tasks = new ArrayList<>();
        tasks.add(dbRef.child("tests").child(testId).removeValue());
        tasks.add(dbRef.child("userTest").child(uid).child(testId).removeValue());
        tasks.add(dbRef.child("testUser").child(testId).child(uid).removeValue());
        tasks.add(dbRef.child("questions").child(testId).removeValue());

        Tasks.whenAll(tasks).addOnSuccessListener(v -> {
            completeListener.run();
        });
    }

    static public void updateTest(String testId, Map<String, Object> updates, Consumer<Test> completeListener) {
        dbRef.child("tests")
                .child(testId)
                .updateChildren(updates)
                .addOnSuccessListener(v -> {
                    dbRef.child("tests").child(testId).get().addOnSuccessListener(dataSnapshot -> {
                        Test test = dataSnapshot.getValue(Test.class);
                        test.setId(dataSnapshot.getKey());
                        completeListener.accept(test);
                    });
                });
    }
}
