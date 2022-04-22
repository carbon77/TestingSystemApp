package com.example.testsys.screens.test;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.testsys.R;
import com.example.testsys.databinding.TestsFragmentBinding;
import com.example.testsys.models.user.UserViewModel;
import com.example.testsys.screens.TabsFragmentDirections;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TestsFragment extends Fragment {
    private TestsFragmentBinding binding;
    private String userId;
    private TestRecyclerAdapter adapter;

    public TestsFragment() {
        super(R.layout.tests_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = TestsFragmentBinding.bind(view);

        binding.btnTestForm.setOnClickListener(this::btnTestFormClick);

        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            userId = user.getId();

            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
            Query query = dbRef.child("userTests").child(userId);
            FirebaseRecyclerOptions<String> options = new FirebaseRecyclerOptions.Builder<String>()
                    .setQuery(query, DataSnapshot::getKey)
                    .setLifecycleOwner(getViewLifecycleOwner())
                    .build();

            adapter = new TestRecyclerAdapter(options);
            binding.testsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.testsRecyclerView.setAdapter(adapter);
        });
    }

    private void btnTestFormClick(View view) {
        NavHostFragment navHost = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.main_nav_host_fragment);
        NavController navController = navHost.getNavController();
        NavDirections action = TabsFragmentDirections.actionTabsFragmentToTestFormFragment(null, userId);
        navController.navigate(action);
    }
}
