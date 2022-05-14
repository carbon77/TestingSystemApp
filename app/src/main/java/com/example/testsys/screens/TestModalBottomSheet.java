package com.example.testsys.screens;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.testsys.R;
import com.example.testsys.databinding.TestBottomSheetBinding;
import com.example.testsys.models.question.QuestionViewModel;
import com.example.testsys.models.test.Test;
import com.example.testsys.models.test.TestViewModel;
import com.example.testsys.models.user.User;
import com.example.testsys.models.user.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TestModalBottomSheet extends BottomSheetDialogFragment {
    private Test test;
    private User user;
    private String testId;
    private TestBottomSheetBinding binding;
    private TestViewModel testViewModel;
    private UserViewModel userViewModel;
    private QuestionViewModel questionViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = TestBottomSheetBinding.bind(view);
        testId = TestModalBottomSheetArgs.fromBundle(getArguments()).getTestId();

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        testViewModel = new ViewModelProvider(requireActivity()).get(TestViewModel.class);
        questionViewModel = new ViewModelProvider(requireActivity()).get(QuestionViewModel.class);

        userViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
        });

        testViewModel.getTests().observe(getViewLifecycleOwner(), tests -> {
            for (Test test : tests) {
                if (test.getId().equals(testId)) {
                    this.test = test;
                    break;
                }
            }

            binding.testBottomSheetTitle.setText(test.getTitle());

            if (!test.getUserId().equals(user.getId())) {
                binding.editTestBtnTestSheet.setVisibility(View.GONE);
            } else {
                binding.editTestBtnTestSheet.setVisibility(View.VISIBLE);
            }
        });

        binding.deleteBtnTestSheet.setOnClickListener(v -> {
            testViewModel.deleteTest(test, user.getId(), () -> {
                testViewModel.updateTests(tests -> {});
                NavHostFragment.findNavController(this).navigateUp();
            });
        });

        binding.copyLinkBtn.setOnClickListener(v -> {
            String link = "http://elite.testing.com/" + testId;
            ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("test link", link);
            clipboard.setPrimaryClip(clip);
            getDialog().dismiss();
            Toast.makeText(requireContext(), "Copied", Toast.LENGTH_SHORT).show();
        });

        binding.editTestBtnTestSheet.setOnClickListener(v -> {
            questionViewModel.updateTestId(testId, () -> {
                Bundle args = new Bundle();
                args.putString("testId", testId);
                NavHostFragment navHost = (NavHostFragment) requireActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.main_nav_host_fragment);

                getDialog().dismiss();
                navHost.getNavController().navigate(R.id.test_form_fragment, args);
            });
        });

        binding.startTestBtnTestSheet.setOnClickListener(v -> {
            questionViewModel.updateTestId(testId, () -> {
                Bundle args = new Bundle();
                args.putString("testId", testId);
                NavHostFragment navHost = (NavHostFragment) requireActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.main_nav_host_fragment);

                getDialog().dismiss();
                navHost.getNavController().navigate(R.id.test_detail_fragment, args);
            });
        });

        binding.statsTestBtn.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("testId", testId);
            args.putString("subtitle", test.getTitle());
            NavHostFragment navHost = (NavHostFragment) requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentById(R.id.main_nav_host_fragment);

            getDialog().dismiss();
            navHost.getNavController().navigate(R.id.test_results_fragment, args);
        });
    }
}
