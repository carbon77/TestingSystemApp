package com.example.testsys.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.SignInFragmentBinding;
import com.example.testsys.databinding.SignUpFragmentBinding;

public class SignUpFragment extends Fragment {

    private SignUpFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = SignUpFragmentBinding.bind(view);
        SignUpFragmentArgs args = SignUpFragmentArgs.fromBundle(requireArguments());
        String email = args.getEmail();

        binding.etEmail.setText(email);

        binding.btnGoBack.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
    }
}
