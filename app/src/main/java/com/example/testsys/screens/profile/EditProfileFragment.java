package com.example.testsys.screens.profile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testsys.R;
import com.example.testsys.databinding.EditProfileFragmentBinding;

public class EditProfileFragment extends Fragment {
    EditProfileFragmentBinding binding;

    public EditProfileFragment() {
        super(R.layout.edit_profile_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = EditProfileFragmentBinding.bind(view);
    }
}
