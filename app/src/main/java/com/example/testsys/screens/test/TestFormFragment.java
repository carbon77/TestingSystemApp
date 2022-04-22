package com.example.testsys.screens.test;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.TestFormFragmentBinding;
import com.example.testsys.models.test.Test;
import com.example.testsys.models.test.TestService;
import com.example.testsys.models.user.UserViewModel;
import com.example.testsys.utils.DateService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TestFormFragment extends Fragment {
    private NavController navController;
    private TestFormFragmentBinding binding;
    private String testId;
    private String userId;

    public TestFormFragment() {
        super(R.layout.test_form_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = TestFormFragmentBinding.bind(view);
        setHasOptionsMenu(true);

        NavHostFragment navHost = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.main_nav_host_fragment);
        navController = navHost.getNavController();

        testId = TestFormFragmentArgs.fromBundle(getArguments()).getTestId();
        userId = TestFormFragmentArgs.fromBundle(getArguments()).getUserId();

        if (testId == null) {
            binding.etTestVersion.setText("1");
            Calendar date = new GregorianCalendar();
            binding.etTestCreateDate.setText(DateService.fromCalendar(date));
            binding.etTestModificationDate.setText(DateService.fromCalendar(date));
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.test_form_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_test_item:
                saveTest();
                break;
        }

        return false;
    }

    private void saveTest() {
        if (binding.etTestText.getText().toString().equals("")) {
            binding.etTestText.setError("This field is required!");
            Toast.makeText(requireContext(), "Check the form", Toast.LENGTH_SHORT).show();
            return;
        }

        String text = binding.etTestText.getText().toString();
        TestService.createTest(userId, text);
        navController.navigateUp();
    }
}
