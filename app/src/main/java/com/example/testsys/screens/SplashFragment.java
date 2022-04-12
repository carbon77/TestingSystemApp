package com.example.testsys.screens;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;

    public SplashFragment() {
        super(R.layout.splash_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            NavDirections action = SplashFragmentDirections.actionSplashFragmentToProfileFragment();
            NavHostFragment.findNavController(this).navigate(action);
        } else {
            NavDirections action = SplashFragmentDirections.actionSplashFragmentToSignInFragment();
            NavHostFragment.findNavController(this).navigate(action);
        }
    }
}
