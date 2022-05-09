package com.example.testsys.screens.profile;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.EditProfileFragmentBinding;
import com.example.testsys.models.user.User;
import com.example.testsys.models.user.UserViewModel;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment {
    private EditProfileFragmentBinding binding;
    private UserViewModel userViewModel;
    private User user;
    private Map<String, Boolean> fieldsChanged = new HashMap<>();

    private ActivityResultLauncher<String> getPictureLauncher = registerForActivityResult(
        new ActivityResultContracts.GetContent(),
        this::uploadAvatar
    );

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

            binding.etDisplayName.addTextChangedListener(new EditTextListener("displayName"));
            binding.etEmail.addTextChangedListener(new EditTextListener("email"));
            binding.avatarView.setImageURI(user.getAvatarUrl());
        });

        binding.btnGoBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigateUp();
        });
        binding.btnSave.setOnClickListener(this::onSaveClick);
        binding.btnChangeAvatar.setOnClickListener(v -> {
            getPictureLauncher.launch("image/*");
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(requireContext());
    }

    private void onSaveClick(View view) {
        String displayName = binding.etDisplayName.getText().toString();
        String email = binding.etEmail.getText().toString();

        Map<String, Object> userUpdates = new HashMap<>();
        boolean isModified = false;

        if (!displayName.equals(user.getDisplayName())) {
            userUpdates.put("displayName", displayName);
            isModified = true;
        }

        if (!email.equals(user.getEmail())) {
            userUpdates.put("email", email);
            isModified = true;
        }

        if (isModified) {
            userViewModel.updateUser(user.getId(), userUpdates, () -> {
                Snackbar.make(binding.getRoot(), "Saved", Snackbar.LENGTH_SHORT)
                        .setAnchorView(binding.buttonsLayout)
                        .show();
                userViewModel.loadUser();
            });
        }
    }

    private void uploadAvatar(Uri file) {
        userViewModel.uploadAvatar(user.getId(), file, () -> {
            Snackbar.make(binding.getRoot(), "Uploaded", Snackbar.LENGTH_SHORT)
                    .setAnchorView(binding.buttonsLayout)
                    .show();
            userViewModel.loadUser();
        });
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
            if (field.equals("displayName")) {
                if (binding.etDisplayName.getText().toString().isEmpty()) {
                    binding.etDisplayNameLayout.setError("This field can't be empty!");
                    fieldsChanged.put(field, false);
                } else if (binding.etDisplayName.getText().toString().length() < 4) {
                    binding.etDisplayNameLayout.setError("Length of display name should be more than 4");
                    fieldsChanged.put(field, false);
                } else {
                    fieldsChanged.put(field, !s.toString().equals(user.getDisplayName()));
                    binding.etDisplayNameLayout.setError(null);
                }

            } else if (field.equals("email")) {
                if (binding.etEmail.getText().toString().isEmpty()) {
                    binding.etEmailLayout.setError("This field can't be empty!");
                    fieldsChanged.put(field, false);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    binding.etEmailLayout.setError("Incorrect email address!");
                    fieldsChanged.put(field, false);
                } else {
                    fieldsChanged.put(field, !s.toString().equals(user.getEmail()));
                    binding.etEmailLayout.setError(null);
                }
            }

            binding.btnSave.setEnabled(fieldsChanged.containsValue(true));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
