package com.example.testsys.models.test;

import com.example.testsys.models.ModelService;
import com.example.testsys.models.user.User;
import com.example.testsys.utils.DateService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

        DatabaseReference userTestRef = dbRef.child("userTests").child(user.getId());
        userTestRef.child(testId).setValue(true);

        return test;
    }

    static public void loadTestById(String id, TestListener completeListener) {
        dbRef.child("tests").child(id).get().addOnCompleteListener(task -> {
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
}
