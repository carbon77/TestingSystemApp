package com.example.testsys.screens.test;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.TestDetailFragmentBinding;
import com.example.testsys.models.test.Test;
import com.example.testsys.models.test.TestViewModel;
import com.example.testsys.models.test.TestViewModelFactory;
import com.example.testsys.models.user.UserViewModel;

public class TestDetailFragment extends Fragment {
    private TestDetailFragmentBinding binding;
    private String testId;
    private Test test;
    private UserViewModel userViewModel;
    private TestViewModel testViewModel;

    public TestDetailFragment() {
        super(R.layout.test_detail_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = TestDetailFragmentBinding.bind(view);
        testId = TestDetailFragmentArgs.fromBundle(getArguments()).getTestId();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            testViewModel = new ViewModelProvider(
                    requireActivity(),
                    new TestViewModelFactory(user.getId())
            ).get(TestViewModel.class);

            testViewModel.getTests().observe(getViewLifecycleOwner(), tests -> {
                for (Test t : tests) {
                    if (t.getId().equals(testId)) {
                        test = t;
                    }
                }
            });

            binding.tvTestTitle.setText(test.getTitle());
            binding.tvTestCreated.setText(test.getCreationDate());
            binding.tvTestQuestionCount.setText(String.valueOf(test.getQuestionCount()));
            binding.tvTestAuthor.setText(test.getUserUsername());

            if (test.getDescription().equals("")) {
                binding.tvTestDescription.setVisibility(View.GONE);
            } else {
                binding.tvTestDescription.setText(test.getDescription());
            }

            binding.btnTestCancel.setOnClickListener(v -> {
                NavHostFragment.findNavController(this).navigateUp();
            });
        });
    }
}
