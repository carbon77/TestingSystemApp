package com.example.testsys.models.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends ViewModel {
    private MutableLiveData<FirebaseUser> user;

    public LiveData<FirebaseUser> getUser() {
        if (user == null) {
            user = new MutableLiveData<FirebaseUser>();
        }
        user.setValue(UserService.loadUser());
        return user;
    }

    public void signOut() {
        UserService.signOut();
    }

    public void signIn(String email, String password, UserService.UserListener completeListener, Runnable cancelListener) {
        UserService.signIn(email, password, completeListener, cancelListener);
    }

    public void signUp(String email, String password, UserService.UserListener completeListener, Runnable cancelListener) {
        UserService.signUp(email, password, completeListener, cancelListener);
    }

    public void setUsername(String username, UserService.UserListener completeListener) {
        UserService.setUsername(username, completeListener);
    }
}
