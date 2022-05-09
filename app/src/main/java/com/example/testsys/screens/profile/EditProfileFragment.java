package com.example.testsys.screens.profile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.EditProfileFragmentBinding;
import com.example.testsys.models.user.User;
import com.example.testsys.models.user.UserViewModel;

public class EditProfileFragment extends Fragment {
    private EditProfileFragmentBinding binding;
    private UserViewModel userViewModel;
    private User user;

    public EditProfileFragment() {
        super(R.layout.edit_profile_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = EditProfileFragmentBinding.bind(view);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            this.user = user;

            binding.etDisplayName.setText(user.getDisplayName());
            binding.etEmail.setText(user.getEmail());
        });

        binding.btnCancel.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });
    }
}
