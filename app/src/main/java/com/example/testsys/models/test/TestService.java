package com.example.testsys.models.test;

import com.example.testsys.models.ModelService;
import com.example.testsys.models.user.User;
import com.example.testsys.utils.DateService;
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

        DatabaseReference testRef = dbRef.child("tests").child(user.getId()).push();
        testRef.setValue(test);

        String testId = testRef.getKey();
        test.setId(testId);

        return test;
    }

    static public void loadTests(String uid, TestsListener completeListener) {
        List<Test> tests = new ArrayList<>();

        dbRef.child("tests").child(uid).get().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                   Test test = dataSnapshot.getValue(Test.class);
                   test.setId(dataSnapshot.getKey());
                   tests.add(test);
               }

               completeListener.invoke(tests);
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
