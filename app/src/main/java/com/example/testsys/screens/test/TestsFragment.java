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
import com.example.testsys.models.question.QuestionViewModel;
import com.example.testsys.models.test.Test;
import com.example.testsys.models.test.TestViewModel;
import com.example.testsys.models.test.TestViewModelFactory;
import com.example.testsys.models.user.UserViewModel;
import com.example.testsys.screens.TabsFragmentDirections;

import java.util.ArrayList;
import java.util.List;

public class TestsFragment extends Fragment {
    private TestsFragmentBinding binding;
    private String uid;
    private List<Test> tests;
    private TestsAdapter adapter;
    private UserViewModel userViewModel;
    private TestViewModel testViewModel;
    private NavController navController;

    public TestsFragment() {
        super(R.layout.tests_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uid = "";
        tests = new ArrayList<>();

        navController = NavHostFragment.findNavController(this);

        binding = TestsFragmentBinding.bind(view);
        setHasOptionsMenu(true);

        // Showing only progress circular
        binding.tvNoTests.setVisibility(View.GONE);
        binding.testsRecyclerView.setVisibility(View.GONE);

        binding.btnTestForm.setOnClickListener(this::btnTestFormClick);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            uid = user.getId();
            initRecylerView();
        });

        // Refreshing for tests
        binding.testsRefreshLayout.setOnRefreshListener(() -> {
            testViewModel.updateTests(tests -> {
                this.tests.clear();
                this.tests.addAll(tests);
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
        testViewModel = new ViewModelProvider(requireActivity(), new TestViewModelFactory(uid)).get(TestViewModel.class);
        adapter = new TestsAdapter((AppCompatActivity) requireActivity(), tests, uid, navController);
        binding.testsRecyclerView.setAdapter(adapter);
        binding.testsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        testViewModel.getTests().observe(getViewLifecycleOwner(), tests -> {
            this.tests.clear();
            this.tests.addAll(tests);
            adapter.notifyDataSetChanged();

            if (tests.size() == 0) {
                binding.tvNoTests.setVisibility(View.VISIBLE);
                binding.testsRecyclerView.setVisibility(View.GONE);
            } else {
                binding.testsRecyclerView.setVisibility(View.VISIBLE);
                binding.tvNoTests.setVisibility(View.GONE);
            }

            binding.progressCircular.setVisibility(View.GONE);
        });
    }

    private void btnTestFormClick(View view) {
        QuestionViewModel questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);
        questionViewModel.updateTestId(null);

        NavHostFragment navHost = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.main_nav_host_fragment);
        NavController navController = navHost.getNavController();
        NavDirections action = TabsFragmentDirections.actionTabsFragmentToTestFormFragment(null);
        navController.navigate(action);
    }
}
