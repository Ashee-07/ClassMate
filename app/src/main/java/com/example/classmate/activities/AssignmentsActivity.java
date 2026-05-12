package com.example.classmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.classmate.adapters.AssignmentAdapter;
import com.example.classmate.databinding.ActivityAssignmentsBinding;
import com.example.classmate.models.Assignment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AssignmentsActivity extends AppCompatActivity {

    private ActivityAssignmentsBinding binding;
    private AssignmentAdapter adapter;
    private List<Assignment> assignmentList;
    private DatabaseReference dbRef;
    private boolean isAdmin = false;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Assignments");
        assignmentList = new ArrayList<>();
        adapter = new AssignmentAdapter(this, assignmentList, isAdmin);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        if (isAdmin) {
            binding.addFab.setVisibility(View.VISIBLE);
            binding.addFab.setOnClickListener(v -> {
                startActivity(new Intent(this, AddAssignmentActivity.class));
            });
        }

        binding.swipeRefresh.setOnRefreshListener(this::fetchAssignments);

        fetchAssignments();
    }

    private void fetchAssignments() {
        binding.swipeRefresh.setRefreshing(true);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assignmentList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Assignment assignment = postSnapshot.getValue(Assignment.class);
                    if (assignment != null) {
                        assignmentList.add(assignment);
                    }
                }
                adapter.notifyDataSetChanged();
                binding.swipeRefresh.setRefreshing(false);
                binding.emptyState.setVisibility(assignmentList.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.swipeRefresh.setRefreshing(false);
                Log.e("AssignmentsActivity", "Database Error: " + error.getMessage());
                Toast.makeText(AssignmentsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}