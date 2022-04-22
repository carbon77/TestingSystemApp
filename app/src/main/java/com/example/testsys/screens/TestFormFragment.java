package com.example.testsys.screens;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testsys.R;
import com.example.testsys.databinding.TestFormFragmentBinding;

public class TestFormFragment extends Fragment {
    private TestFormFragmentBinding binding;

    public TestFormFragment() {
        super(R.layout.test_form_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = TestFormFragmentBinding.bind(view);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.test_form_toolbar_menu, menu);
    }
}
