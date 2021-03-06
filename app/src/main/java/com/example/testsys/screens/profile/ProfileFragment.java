package com.example.testsys.screens.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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
import com.example.testsys.models.test.TestViewModel;
import com.example.testsys.models.user.UserViewModel;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.net.MalformedURLException;
import java.net.URL;

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
        TestViewModel testViewModel = new ViewModelProvider(requireActivity()).get(TestViewModel.class);

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

            Intent intent = requireActivity().getIntent();
            if (intent.getDataString() != null) {
                try {
                    URL path = new URL(intent.getDataString());
                    String testId = path.getPath().substring(1);
                    testViewModel.addTest(user.getId(), testId, () -> {});
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
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
                .setTitle(getString(R.string.sign_out))
                .setMessage(getString(R.string.sign_out_confirmation))
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    userViewModel.signOut();
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
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
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
        navController.navigate(R.id.auth_graph, null, navOptions);
    }
}
