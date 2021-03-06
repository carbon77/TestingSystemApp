package com.example.testsys.screens.auth;

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
import com.example.testsys.databinding.SignInFragmentBinding;
import com.example.testsys.models.user.User;
import com.example.testsys.models.user.UserViewModel;

public class SignInFragment extends Fragment {

    private SignInFragmentBinding binding;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_in_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = SignInFragmentBinding.bind(view);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

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
        userViewModel.signIn(email, password, this::completeSignedIn);
    }

    private void completeSignedIn(User user) {
        if (user == null) {
            Toast.makeText(requireActivity(), getResources().getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
            binding.etPassword.setText("");
            return;
        }
        NavDirections action = SignInFragmentDirections.actionSignInFragmentToNavFragment("You have signed in");
        NavHostFragment.findNavController(this).navigate(action);
    }
}
