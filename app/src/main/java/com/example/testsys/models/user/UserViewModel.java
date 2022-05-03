package com.example.testsys.models.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.function.Consumer;

public class UserViewModel extends ViewModel {
    private MutableLiveData<User> user = new MutableLiveData<>();

    public LiveData<User> getUser() {
        UserService.loadCurrentUser(currentUser -> {
            user.setValue(currentUser);
        });

        return user;
    }

    public void signOut() {
        UserService.signOut();
        user.setValue(null);
    }

    public void signIn(String email, String password, Consumer<User> completeListener) {
        UserService.signIn(email, password, currentUser -> {
            user.setValue(currentUser);
            completeListener.accept(currentUser);
        });
    }

    public void signUp(String email, String password, String username, Consumer<User> completeListener) {
        UserService.signUp(email, password, username, currentUser -> {
            user.setValue(currentUser);
            completeListener.accept(currentUser);
        });
    }
}
