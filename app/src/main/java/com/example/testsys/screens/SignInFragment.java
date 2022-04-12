package com.example.testsys.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.SignInFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {

    private SignInFragmentBinding binding;

    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_in_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = SignInFragmentBinding.bind(view);
        auth = FirebaseAuth.getInstance();

        binding.btnGoToSignUp.setOnClickListener(this::goToSignUp);
        binding.btnSignIn.setOnClickListener(this::signIn);
    }

    public void goToSignUp(View v) {
        String email = binding.etEmail.getText().toString();
        NavDirections action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment(email);
        NavHostFragment.findNavController(this).navigate(action);
    }

    public void signIn(View v) {
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                NavDirections action = SignInFragmentDirections.actionSignInFragmentToProfileFragment();
                NavHostFragment.findNavController(this).navigate(action);
            } else {
                Toast.makeText(requireActivity(), getResources().getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                binding.etPassword.setText("");
            }
        });
    }
}
