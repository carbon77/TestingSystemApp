package com.example.testsys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.View;

import com.example.testsys.databinding.ActivityMainBinding;
import com.example.testsys.screens.TabsFragment;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int[] NO_ACTION_BAR_DESTINATIONS = {
            R.id.sign_in_fragment,
            R.id.sign_up_fragment
    };

    private ActivityMainBinding binding;
    private NavController navController;

    private FragmentManager.FragmentLifecycleCallbacks fragmentListener = new FragmentManager.FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState);

            if (f instanceof TabsFragment || f instanceof NavHostFragment) return;
            onNavControllerActivated(NavHostFragment.findNavController(f));
        }
    };

    private NavController.OnDestinationChangedListener destinationListener = (navController, navDestination, bundle) -> {
        ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar == null) return;

        for (int id : NO_ACTION_BAR_DESTINATIONS) {
            if (id == navDestination.getId()) {
                supportActionBar.hide();
                break;
            } else {
                supportActionBar.show();
            }
        }

        supportActionBar.setTitle(navDestination.getLabel());
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentListener, true);

        navController = getRootNavController();
        navController.addOnDestinationChangedListener(destinationListener);
        onNavControllerActivated(navController);
    }

    @Override
    protected void onDestroy() {
        getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(fragmentListener);
        navController = null;
        super.onDestroy();
    }

    private NavController getRootNavController() {
        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav_host_fragment);
        return navHost.getNavController();
    }

    private void onNavControllerActivated(NavController navController) {
        if (this.navController == navController) return;
        this.navController.removeOnDestinationChangedListener(destinationListener);
        navController.addOnDestinationChangedListener(destinationListener);
        this.navController = navController;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (isStartDestination(navController.getCurrentDestination())) {
            super.onBackPressed();
        } else {
            navController.popBackStack();
        }
    }

    private boolean isStartDestination(NavDestination currentDestination) {
        if (currentDestination == null) return false;

        NavGraph graph = currentDestination.getParent();
        if (graph == null) return false;

        List<Integer> startDestinations = Arrays.asList(
                R.id.tabs_fragment,
                R.id.sign_in_fragment,
                graph.getStartDestinationId()
        );

        return startDestinations.contains(currentDestination.getId());
    }
}