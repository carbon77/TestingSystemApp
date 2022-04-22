package com.example.testsys.screens.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testsys.R;
import com.example.testsys.databinding.TestCardViewBinding;
import com.example.testsys.models.test.TestService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class TestRecyclerAdapter extends FirebaseRecyclerAdapter<String, TestRecyclerAdapter.TestViewHolder> {

    public TestRecyclerAdapter(@NonNull FirebaseRecyclerOptions<String> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TestViewHolder holder, int position, @NonNull String testId) {
        TestCardViewBinding binding = holder.getBinding();
        holder.getBinding().testFormCardBody.setVisibility(View.GONE);
        holder.getBinding().progressCircular.setVisibility(View.VISIBLE);

        TestService.loadTestById(testId, test -> {
            binding.testCardText.setText(test.getText());
            binding.testCardQuestionCount.setText("Questions count: " + test.getQuestionCount());
            binding.textCardAuthor.setText("Author: " + test.getUserUsername());
            binding.testFormCreationDate.setText("Created: " + test.getCreationDate());
            binding.testFormCardBody.setVisibility(View.VISIBLE);
            binding.progressCircular.setVisibility(View.GONE);
        });
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.test_card_view, parent, false);
        return new TestViewHolder(view);
    }

    public static class TestViewHolder extends RecyclerView.ViewHolder {
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
