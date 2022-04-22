package com.example.testsys.screens;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.TestsFragmentBinding;
import com.example.testsys.models.user.UserViewModel;

public class TestsFragment extends Fragment {
    private TestsFragmentBinding binding;
    private String userId;

    public TestsFragment() {
        super(R.layout.tests_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = TestsFragmentBinding.bind(view);

        binding.btnTestForm.setOnClickListener(this::btnTestFormClick);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            userId = user.getUid();
        });
    }

    private void btnTestFormClick(View view) {
        NavHostFragment navHost = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.main_nav_host_fragment);
        NavController navController = navHost.getNavController();
        NavDirections action = TabsFragmentDirections.actionTabsFragmentToTestFormFragment(null, userId);
        navController.navigate(action);
    }
}
