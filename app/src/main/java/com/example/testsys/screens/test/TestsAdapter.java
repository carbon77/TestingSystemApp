package com.example.testsys.screens.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.TestCardViewBinding;
import com.example.testsys.models.test.Test;

import java.util.List;

public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.TestViewHolder> {
    private List<Test> tests;
    private AppCompatActivity activity;
    private String uid;
    private NavController navController;

    public TestsAdapter(AppCompatActivity activity, List<Test> tests, String uid, NavController navController) {
        this.activity = activity;
        this.tests = tests;
        this.uid = uid;
        this.navController = navController;
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_card_view, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        TestCardViewBinding binding = holder.getBinding();
        Test test = tests.get(position);

        binding.testCardText.setText(test.getText());
        binding.testCardAuthor.setText("Author: " + test.getUserUsername());
        binding.testCreationDate.setText("Created: " + test.getCreationDate());
        binding.testCardQuestionCount.setText("Question count: " + test.getQuestionCount());
        binding.getRoot().setOnClickListener(v -> {
            NavDirections action = TestsFragmentDirections.actionTestsFragmentToTestModalBottomSheet(test.getId());
            navController.navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return tests.size();
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        private TestCardViewBinding binding;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TestCardViewBinding.bind(itemView);
        }

        public TestCardViewBinding getBinding() {
            return binding;
        }
    }
}
