package com.example.classmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.classmate.adapters.ExamMarkAdapter;
import com.example.classmate.databinding.ActivityExamMarksBinding;
import com.example.classmate.models.ExamMark;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ExamMarksActivity extends AppCompatActivity {

    private ActivityExamMarksBinding binding;
    private DatabaseReference databaseReference;
    private List<ExamMark> marksList;
    private ExamMarkAdapter adapter;
    private boolean isAdmin;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExamMarksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        databaseReference = FirebaseDatabase.getInstance(DB_URL).getReference("ExamMarks");

        setupUI();
        loadAllResults();
    }

    private void setupUI() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        marksList = new ArrayList<>();
        adapter = new ExamMarkAdapter(this, marksList, isAdmin, mark -> {
            databaseReference.child(mark.getId()).removeValue()
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show());
        });

        binding.marksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.marksRecyclerView.setAdapter(adapter);

        if (isAdmin) {
            binding.addFab.setVisibility(View.VISIBLE);
            binding.addFab.setOnClickListener(v -> {
                Intent intent = new Intent(this, AddExamMarkActivity.class);
                startActivity(intent);
            });
        }

        binding.swipeRefresh.setOnRefreshListener(this::loadAllResults);
    }

    private void loadAllResults() {
        binding.progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                marksList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ExamMark mark = ds.getValue(ExamMark.class);
                    if (mark != null) {
                        marksList.add(mark);
                    }
                }
                updateUI();
                binding.swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);
                Toast.makeText(ExamMarksActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
        binding.progressBar.setVisibility(View.GONE);
        binding.marksRecyclerView.setVisibility(marksList.isEmpty() ? View.GONE : View.VISIBLE);
        binding.emptyState.getRoot().setVisibility(marksList.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
