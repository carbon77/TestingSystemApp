package com.example.testsys.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.ProfileFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileFragmentBinding binding;
    private FirebaseUser user;
    private FirebaseAuth auth;

    public ProfileFragment() {
        super(R.layout.profile_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = ProfileFragmentBinding.bind(view);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        binding.tvEmail.setText(String.format("%s: %s", getResources().getString(R.string.email), user.getEmail()));
        binding.tvUsername.setText(String.format("%s: %s", getResources().getString(R.string.username), user.getDisplayName()));
        binding.btnSingOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        auth.signOut();
        NavDirections action = ProfileFragmentDirections.actionProfileFragmentToSplashFragment();
        NavHostFragment.findNavController(this).navigate(action);
    }
}
