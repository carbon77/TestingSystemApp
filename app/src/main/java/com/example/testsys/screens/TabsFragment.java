package com.example.testsys.screens;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.testsys.R;
import com.example.testsys.databinding.TabsFragmentBinding;
import com.google.android.material.snackbar.Snackbar;

public class TabsFragment extends Fragment {
    private TabsFragmentBinding binding;
    private NavController navController;
    private NavHostFragment navHostFragment;

    public TabsFragment() {
        super(R.layout.tabs_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = TabsFragmentBinding.bind(view);
        navHostFragment = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.profile_nav_host_fragment);
        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        String snackbarMessage = TabsFragmentArgs.fromBundle(getArguments()).getSnackbackMessage();
        if (snackbarMessage != null) {
            Snackbar.make(binding.getRoot(), snackbarMessage, Snackbar.LENGTH_SHORT)
                    .setAnchorView(binding.bottomNavigation)
                    .show();
        }
    }
}
