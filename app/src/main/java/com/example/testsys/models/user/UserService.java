package com.example.testsys.models.user;

import android.net.Uri;

import com.example.testsys.models.ModelService;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public static void updateUser(String uid, Map<String, Object> updates, Runnable completeListener) {
        List<Task<Void>> tasks = new ArrayList<>();

        tasks.add(dbRef.child("users").child(uid).updateChildren(updates));

        if (updates.containsKey("email")) {
            tasks.add(auth.getCurrentUser().updateEmail((String) updates.get("email")));
        }

        Tasks.whenAll(tasks).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                completeListener.run();
            }
        });
    }

    public static void uploadAvatar(String uid, Uri file, Runnable completeListener) {
        storageRef.child("avatars").child(uid).putFile(file).addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               completeListener.run();
           }
        });
    }
}
