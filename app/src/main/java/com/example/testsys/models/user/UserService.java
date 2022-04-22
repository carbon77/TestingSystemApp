package com.example.testsys.models.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/*
* UserService class has static methods to work with signed in user in Firebase
* */
public class UserService {
    public static void loadCurrentUser(UserListener completeListener) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            completeListener.invoke(null);
            return;
        }

        String id = firebaseUser.getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(id).get().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               User user = task.getResult().getValue(User.class);
               user.setId(id);
               completeListener.invoke(user);
           } else {
               completeListener.invoke(null);
           }
        });
    }

    public static void signIn(String email, String password, UserListener completeListener) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               loadCurrentUser(completeListener);
           } else {
               completeListener.invoke(null);
           }
        });
    }

    public static void signUp(String email, String password, String username, UserListener completeListener) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String id = firebaseUser.getUid();
                User user = new User(null, email, username, username);
                FirebaseDatabase.getInstance().getReference().child("users").child(id).setValue(user).addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        user.setId(id);
                        completeListener.invoke(user);
                    } else {
                        completeListener.invoke(null);
                    }
                });
            } else {
                completeListener.invoke(null);
            }
        });
    }

    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public interface UserListener {
        void invoke(User user);
    }
}
