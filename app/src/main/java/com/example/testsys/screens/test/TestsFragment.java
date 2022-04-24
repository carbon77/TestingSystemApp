package com.example.testsys.screens.test;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.testsys.R;
import com.example.testsys.databinding.TestsFragmentBinding;
import com.example.testsys.models.test.TestViewModel;
import com.example.testsys.models.test.TestViewModelFactory;
import com.example.testsys.models.user.UserViewModel;
import com.example.testsys.screens.TabsFragmentDirections;

public class TestsFragment extends Fragment {
    private TestsFragmentBinding binding;
    private String userId;
    private TestsAdapter adapter;
    private UserViewModel userViewModel;
    private TestViewModel testViewModel;

    public TestsFragment() {
        super(R.layout.tests_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = TestsFragmentBinding.bind(view);
        setHasOptionsMenu(true);

        binding.tvNoTests.setVisibility(View.GONE);
        binding.testsRecyclerView.setVisibility(View.GONE);

        binding.btnTestForm.setOnClickListener(this::btnTestFormClick);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            userId = user.getId();
            initRecylerView();
        });

        binding.testsRefreshLayout.setOnRefreshListener(() -> {
            testViewModel.updateTests(tests -> {
                adapter.notifyDataSetChanged();
                binding.testsRefreshLayout.setRefreshing(false);
            });
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.tests_refresh_menu, menu);
    }

    private void initRecylerView() {
        testViewModel = new ViewModelProvider(requireActivity(), new TestViewModelFactory(userId)).get(TestViewModel.class);
        testViewModel.getTests().observe(getViewLifecycleOwner(), tests -> {
            adapter = new TestsAdapter((AppCompatActivity) requireActivity(), tests);
            binding.testsRecyclerView.setAdapter(adapter);
            binding.testsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            if (tests.size() == 0) {
                binding.tvNoTests.setVisibility(View.VISIBLE);
            } else {
                binding.testsRecyclerView.setVisibility(View.VISIBLE);
            }

            binding.progressCircular.setVisibility(View.GONE);
        });
    }

    private void btnTestFormClick(View view) {
        NavHostFragment navHost = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.main_nav_host_fragment);
        NavController navController = navHost.getNavController();
        NavDirections action = TabsFragmentDirections.actionTabsFragmentToTestFormFragment(null);
        navController.navigate(action);
    }
}
