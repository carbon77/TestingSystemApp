package com.example.testsys.screens.auth;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
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
import com.example.testsys.models.user.User;
import com.example.testsys.models.user.UserViewModel;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    private SignUpFragmentBinding binding;
    private UserViewModel userViewModel;
    private Map<String, Boolean> fieldsValidation = new HashMap<>();

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

        binding.etUsername.addTextChangedListener(new EditTextListener("username"));
        binding.etEmail.addTextChangedListener(new EditTextListener("email"));
        binding.etPassword.addTextChangedListener(new EditTextListener("password"));
        binding.etPasswordConfirmation.addTextChangedListener(new EditTextListener("passwordConfirm"));
    }

    public void signUp(View v) {
        String username = binding.etUsername.getText().toString();
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        userViewModel.signUp(email, password, username, this::completeSignedUp);
    }

    public void completeSignedUp(User user) {
        if (user == null) {
            Toast.makeText(requireContext(), getResources().getString(R.string.registration_failed), Toast.LENGTH_SHORT).show();
            binding.etPassword.setText("");
            return;
        }
        goToProfile();
    }

    public void goToProfile() {
        NavDirections action = SignUpFragmentDirections.actionSignUpFragmentToNavFragment();
        NavHostFragment.findNavController(this).navigate(action);
    }

    public class EditTextListener implements TextWatcher {
        private String field;

        public EditTextListener(String field) {
            this.field = field;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (field) {
                case "username":
                    String username = s.toString();
                    if (username.isEmpty()) {
                        binding.etUsernameLayout.setError(null);
                        fieldsValidation.put("username", false);
                    } else if (username.length() < 4) {
                        binding.etUsernameLayout.setError("Username length should be more than 4");
                        fieldsValidation.put("username", false);
                    } else {
                        binding.etUsernameLayout.setError(null);
                        fieldsValidation.put("username", true);
                    }
                    break;
                case "email":
                    String email = s.toString();
                    if (email.isEmpty()) {
                        binding.etEmailLayout.setError(null);
                        fieldsValidation.put("email", false);
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        binding.etEmailLayout.setError("Incorrect email");
                        fieldsValidation.put("email", false);
                    } else {
                        binding.etEmailLayout.setError(null);
                        fieldsValidation.put("email", true);
                    }
                    break;
                case "password": {
                    String password = s.toString();
                    String passwordConfirmation = binding.etPasswordConfirmation.getText().toString();
                    if (password.length() < 6) {
                        binding.etPasswordLayout.setError("Password length should be more 6");
                        fieldsValidation.put("password", false);
                    } else if (!password.equals(passwordConfirmation)) {
                        binding.etPasswordLayout.setError("Passwords don't match");
                        fieldsValidation.put("password", false);
                    } else {
                        binding.etPasswordLayout.setError(null);
                        fieldsValidation.put("passwordConfirm", true);
                        fieldsValidation.put("password", true);
                    }
                    break;
                }
                case "passwordConfirm": {
                    String password = binding.etPassword.getText().toString();
                    String passwordConfirmation = s.toString();
                    if (!passwordConfirmation.equals(password)) {
                        binding.etPasswordLayout.setError("Passwords don't match");
                        fieldsValidation.put("passwordConfirm", false);
                    } else {
                        binding.etPasswordLayout.setError(null);
                        fieldsValidation.put("passwordConfirm", true);
                        fieldsValidation.put("password", true);
                    }
                    break;
                }
            }

            binding.btnSignUp.setEnabled(!fieldsValidation.containsValue(false));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
