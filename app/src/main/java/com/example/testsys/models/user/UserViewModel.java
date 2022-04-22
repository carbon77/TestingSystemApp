package com.example.testsys.models.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private MutableLiveData<User> user;

    public LiveData<User> getUser() {
        if (user == null) {
            user = new MutableLiveData<User>();
        }

        UserService.loadCurrentUser(currentUser -> {
            user.setValue(currentUser);
        });
        return user;
    }

    public void signOut() {
        UserService.signOut();
        user.setValue(null);
    }

    public void signIn(String email, String password, UserService.UserListener completeListener) {
        UserService.signIn(email, password, currentUser -> {
            user.setValue(currentUser);
            completeListener.invoke(currentUser);
        });
    }

    public void signUp(String email, String password, String username, UserService.UserListener completeListener) {
        UserService.signUp(email, password, username, currentUser -> {
            user.setValue(currentUser);
            completeListener.invoke(currentUser);
        });
    }
}
