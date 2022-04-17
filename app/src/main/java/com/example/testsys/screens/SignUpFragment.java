package com.example.testsys.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.SignUpFragmentBinding;
import com.example.testsys.models.UserViewModel;

public class SignUpFragment extends Fragment {

    private SignUpFragmentBinding binding;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = SignUpFragmentBinding.bind(view);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        SignUpFragmentArgs args = SignUpFragmentArgs.fromBundle(requireArguments());
        String email = args.getEmail();

        binding.etEmail.setText(email);
        binding.btnGoBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
        binding.btnSignUp.setOnClickListener(this::signUp);
    }

    public void signUp(View v) {
        String username = binding.etUsername.getText().toString();
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        userViewModel.signUp(
                email,
                password,
                user -> {
                    userViewModel.setUsername(username, u -> {
                        goToProfile();
                    });
                },
                () -> {
                    Toast.makeText(requireContext(), getResources().getString(R.string.registration_failed), Toast.LENGTH_SHORT).show();
                    binding.etPassword.setText("");
                }
        );
    }

    private void goToProfile() {
        NavDirections action = SignUpFragmentDirections.actionSignUpFragmentToProfileFragment();
        NavHostFragment.findNavController(this).navigate(action);
    }
}
