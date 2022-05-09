package com.example.testsys.screens.profile;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.ProfileFragmentBinding;
import com.example.testsys.models.user.UserViewModel;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ProfileFragment extends Fragment {

    private ProfileFragmentBinding binding;
    private UserViewModel userViewModel;

    public ProfileFragment() {
        super(R.layout.profile_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        binding = ProfileFragmentBinding.bind(view);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user == null) {
                goToSignIn();
                return;
            }

            binding.tvDisplayName.setText(user.getDisplayName());
            binding.tvUsername.setText("@" + user.getUsername());
            if (user.getAvatarUrl().equals("")) {
                binding.avatarImageView.setActualImageResource(R.drawable.avatar_placeholder);
            } else {
                binding.avatarImageView.setImageURI(user.getAvatarUrl());
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(requireContext());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_profile_item:
                goToEditProfile();
                break;
            case R.id.sign_out_item:
                singOut();
                break;
        }

        return false;
    }

    private void singOut() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Sign out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    requireActivity().getViewModelStore().clear();
                    NavHostFragment navHost = (NavHostFragment) requireActivity()
                            .getSupportFragmentManager()
                            .findFragmentById(R.id.main_nav_host_fragment);
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.profile_fragment, true)
                            .build();
                    navHost.getNavController().navigate(
                            R.id.sign_in_fragment,
                            null,
                            navOptions
                    );
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private void goToEditProfile() {
        NavDirections action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment();
        NavHostFragment.findNavController(this).navigate(action);
    }

    private void goToSignIn() {
        NavHostFragment navHost =
                (NavHostFragment) requireActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.main_nav_host_fragment);
        NavController navController = navHost.getNavController();
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.nav_fragment, true).build();
        navController.navigate(R.id.sign_in_fragment, null, navOptions);
    }
}
