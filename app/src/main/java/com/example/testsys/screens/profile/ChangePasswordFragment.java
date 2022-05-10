package com.example.testsys.screens.profile;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.ChangePasswordFragmentBinding;
import com.example.testsys.models.user.User;
import com.example.testsys.models.user.UserViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordFragment extends Fragment {
    private ChangePasswordFragmentBinding binding;
    private UserViewModel userViewModel;
    private User user;
    private Map<String, Boolean> fieldsValidation = new HashMap<>();

    public ChangePasswordFragment() {
        super(R.layout.change_password_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = ChangePasswordFragmentBinding.bind(view);
        fieldsValidation.put("newPassword", false);
        fieldsValidation.put("oldPassword", false);
        fieldsValidation.put("passwordConfirm", false);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            this.user = user;

            binding.btnChangePassword.setOnClickListener(this::changePassword);
        });

        binding.etOldPassword.addTextChangedListener(new EditTextListener("oldPassword"));
        binding.etNewPassword.addTextChangedListener(new EditTextListener("newPassword"));
        binding.etPasswordConfirmation.addTextChangedListener(new EditTextListener("passwordConfirm"));
        binding.btnGoBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });
    }

    private void changePassword(View view) {
        userViewModel.reauthenticate(
                user.getEmail(),
                binding.etOldPassword.getText().toString(),
                () -> {
                    userViewModel.updatePassword(binding.etNewPassword.getText().toString(), () -> {
                        Snackbar.make(binding.getRoot(), "Password has changed", Snackbar.LENGTH_SHORT)
                                .setAnchorView(binding.actionsView)
                                .show();
                    });
                },
                () -> {
                    Snackbar.make(binding.getRoot(), "Incorrect password", Snackbar.LENGTH_SHORT)
                            .setAnchorView(binding.actionsView)
                            .show();
                }
        );
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
                case "oldPassword":
                    if (s.toString().equals("")) {
                        fieldsValidation.put("oldPassword", false);
                    } else {
                        fieldsValidation.put("oldPassword", true);
                    }
                    break;
                case "newPassword":
                    if (s.toString().equals("")) {
                        fieldsValidation.put("newPassword", false);
                        binding.etNewPasswordLayout.setError(null);
                    } else if (s.length() < 6) {
                        fieldsValidation.put("newPassword", false);
                        binding.etNewPasswordLayout.setError("Password length should be more than 5");
                    } else if (!s.toString().equals(binding.etPasswordConfirmation.getText().toString())) {
                        fieldsValidation.put("newPassword", false);
                        fieldsValidation.put("passwordConfirm", false);
                        binding.etNewPasswordLayout.setError("The passwords don't match");
                    } else {
                        fieldsValidation.put("newPassword", true);
                        fieldsValidation.put("passwordConfirm", true);
                        binding.etNewPasswordLayout.setError(null);
                    }
                    break;
                case "passwordConfirm":
                    if (s.toString().equals("")) {
                        fieldsValidation.put("passwordConfirm", false);
                    } else if (!s.toString().equals(binding.etNewPassword.getText().toString())) {
                        fieldsValidation.put("newPassword", false);
                        fieldsValidation.put("passwordConfirm", false);
                        binding.etNewPasswordLayout.setError("The passwords don't match");
                    } else {
                        fieldsValidation.put("newPassword", true);
                        fieldsValidation.put("passwordConfirm", true);
                        binding.etNewPasswordLayout.setError(null);
                    }
            }

            binding.btnChangePassword.setEnabled(!fieldsValidation.containsValue(false));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
