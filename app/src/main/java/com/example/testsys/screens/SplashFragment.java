package com.example.testsys.screens;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.models.user.UserViewModel;

public class SplashFragment extends Fragment {

    private UserViewModel userViewModel;

    public SplashFragment() {
        super(R.layout.splash_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.loadUser();
        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            NavDirections action;
            if (user != null) {
                action = SplashFragmentDirections.actionSplashFragmentToNavFragment(null);
            } else {
                action = SplashFragmentDirections.actionSplashFragmentToSignInFragment();
            }
            NavHostFragment.findNavController(this).navigate(action);
        });
    }
}
