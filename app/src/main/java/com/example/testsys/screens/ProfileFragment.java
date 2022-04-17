package com.example.testsys.screens;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.ProfileFragmentBinding;
import com.example.testsys.models.UserViewModel;

public class ProfileFragment extends Fragment {

    private ProfileFragmentBinding binding;
    private UserViewModel userViewModel;

    public ProfileFragment() {
        super(R.layout.profile_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = ProfileFragmentBinding.bind(view);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            binding.tvEmail.setText(String.format("%s: %s", getResources().getString(R.string.email), user.getEmail()));
            binding.tvUsername.setText(String.format("%s: %s", getResources().getString(R.string.username), user.getDisplayName()));
            binding.btnSingOut.setOnClickListener(this::signOut);
        });
    }

    public void signOut(View v) {
        userViewModel.signOut();
        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity()
                .getSupportFragmentManager()
                .findFragmentById(R.id.main_nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.nav_fragment, true).build();
        navController.navigate(R.id.sign_in_fragment, null, navOptions);
    }
}
