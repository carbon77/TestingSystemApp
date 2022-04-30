package com.example.testsys.models.user;

import com.example.testsys.models.ModelService;
import com.google.firebase.auth.FirebaseUser;

import java.util.function.Consumer;

/*
* UserService class has static methods to work with signed in user in Firebase
* */
public class UserService extends ModelService {
    public static void loadCurrentUser(Consumer<User> completeListener) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            completeListener.accept(null);
            return;
        }

        String id = firebaseUser.getUid();
        dbRef.child("users").child(id).get().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               User user = task.getResult().getValue(User.class);
               user.setId(id);
               completeListener.accept(user);
           } else {
               completeListener.accept(null);
           }
        });
    }

    public static void signIn(String email, String password, Consumer<User> completeListener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               loadCurrentUser(completeListener);
           } else {
               completeListener.accept(null);
           }
        });
    }

    public static void signUp(String email, String password, String username, Consumer<User> completeListener) {
        dbRef.child("usernames").child(username).get().addOnCompleteListener(t -> {
            if (!t.isSuccessful() || t.getResult().getValue() != null) {
                completeListener.accept(null);
                return;
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String id = firebaseUser.getUid();
                    User user = new User(null, email, username, username);
                    createUserModel(id, user, completeListener);
                } else {
                    completeListener.accept(null);
                }
            });
        });
    }

    private static void createUserModel(String id, User user, Consumer<User> completeListener) {
        dbRef.child("users").child(id).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.setId(id);
                dbRef.child("usernames").child(user.getUsername()).setValue(id).addOnCompleteListener(t -> {
                   if (t.isSuccessful()) {
                       completeListener.accept(user);
                   } else {
                       completeListener.accept(null);
                   }
                });
            } else {
                completeListener.accept(null);
            }
        });
    }

    public static void signOut() {
        auth.signOut();
    }
}
