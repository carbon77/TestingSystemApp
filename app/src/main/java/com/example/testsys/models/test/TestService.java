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

public class TestService extends ModelService {
    static public Test createTest(User user, String text) {
        String creationDate = DateService.fromCalendar(new GregorianCalendar());
        String modificationDate = DateService.fromCalendar(new GregorianCalendar());

        Test test = new Test(null, user.getId(), text);
        test.setCreationDate(creationDate);
        test.setModificationDate(modificationDate);
        test.setVersion(1);
        test.setUserUsername(user.getUsername());

        DatabaseReference testRef = dbRef.child("tests").push();
        testRef.setValue(test);

        String testId = testRef.getKey();
        test.setId(testId);

        dbRef.child("userTest").child(user.getId()).child(testId).setValue(true);
        dbRef.child("testUser").child(testId).child(user.getId()).setValue(true);

        return test;
    }

    static public void loadTestsByUid(String uid, TestsListener completeListener) {
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
                       completeListener.invoke(tests);
                   }
                });
            }
        });
    }

    static public void loadTestById(String id, TestListener completeListener) {
        dbRef.child("tests").child(auth.getCurrentUser().getUid()).child(id).get().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               Test test = task.getResult().getValue(Test.class);
               test.setId(id);
               completeListener.invoke(test);
           }
        });
    }

    public interface TestListener {
        void invoke(Test test);
    }

    public interface TestsListener {
        void invoke(List<Test> tests);
    }
}
