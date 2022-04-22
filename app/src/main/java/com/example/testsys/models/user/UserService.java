package com.example.testsys.models.user;

import android.util.Log;

import com.example.testsys.models.ModelService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
* UserService class has static methods to work with signed in user in Firebase
* */
public class UserService extends ModelService {
    public static void loadCurrentUser(UserListener completeListener) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            completeListener.invoke(null);
            return;
        }

        String id = firebaseUser.getUid();
        dbRef.child("users").child(id).get().addOnCompleteListener(task -> {
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
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               loadCurrentUser(completeListener);
           } else {
               completeListener.invoke(null);
           }
        });
    }

    public static void signUp(String email, String password, String username, UserListener completeListener) {
        dbRef.child("usernames").child(username).get().addOnCompleteListener(t -> {
            if (!t.isSuccessful() || t.getResult().getValue() != null) {
                completeListener.invoke(null);
                return;
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String id = firebaseUser.getUid();
                    User user = new User(null, email, username, username);
                    createUserModel(id, user, completeListener);
                } else {
                    completeListener.invoke(null);
                }
            });
        });
    }

    private static void createUserModel(String id, User user, UserListener completeListener) {
        dbRef.child("users").child(id).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.setId(id);
                dbRef.child("usernames").child(user.getUsername()).setValue(id).addOnCompleteListener(t -> {
                   if (t.isSuccessful()) {
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
        auth.signOut();
    }

    public interface UserListener {
        void invoke(User user);
    }
}
