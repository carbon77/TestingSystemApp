package com.example.testsys.models.test;

import com.example.testsys.utils.DateService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TestService {
    static public Test createTest(String userId, String text) {
        String creationDate = DateService.fromCalendar(new GregorianCalendar());
        String modificationDate = DateService.fromCalendar(new GregorianCalendar());

        Test test = new Test(null, userId, text, 1, creationDate, modificationDate);
        DatabaseReference testRef = FirebaseDatabase.getInstance().getReference("tests").push();
        testRef.setValue(test);

        String testId = testRef.getKey();
        test.setId(testId);

        DatabaseReference userTestRef = FirebaseDatabase.getInstance().getReference("userTests").child(userId);
        userTestRef.child(testId).setValue(true);

        return test;
    }

    static public void loadTestById(String id, TestListener successListener) {
        FirebaseDatabase.getInstance().getReference().child("tests").child(id).get().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               Test test = task.getResult().getValue(Test.class);
               test.setId(id);
               successListener.invoke(test);
           }
        });
    }

    public interface TestListener {
        void invoke(Test test);
    }
}
