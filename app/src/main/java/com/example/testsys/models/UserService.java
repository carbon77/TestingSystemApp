package com.example.testsys.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/*
* UserService class has static methods to work with signed in user in Firebase
* */
public class UserService {
    public static FirebaseUser loadUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static void signIn(String email, String password, UserListener completeListener, Runnable canceledListener) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               completeListener.invoke(loadUser());
           } else {
               canceledListener.run();
           }
        });
    }

    public static void signUp(String email, String password, UserListener completeListener, Runnable canceledListener) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                completeListener.invoke(loadUser());
            } else {
                canceledListener.run();
            }
        });
    }

    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public static void setUsername(String username, UserListener completeListener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            return;
        }
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();
        user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                completeListener.invoke(user);
            }
        });
    }

    public interface UserListener {
        void invoke(FirebaseUser user);
    }
}
