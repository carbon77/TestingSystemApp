package com.example.testsys.screens.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.TestModalBottomSheet;
import com.example.testsys.databinding.TestCardViewBinding;
import com.example.testsys.models.test.Test;

import java.util.List;

public class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.TestViewHolder> {
    private List<Test> tests;
    private AppCompatActivity activity;

    public TestsAdapter(AppCompatActivity activity, List<Test> tests) {
        this.activity = activity;
        this.tests = tests;
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
            TestModalBottomSheet modal = new TestModalBottomSheet(test.getText());
            modal.show(activity.getSupportFragmentManager(), "TestCardModal");
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
