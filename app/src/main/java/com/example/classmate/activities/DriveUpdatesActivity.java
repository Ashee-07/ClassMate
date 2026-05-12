package com.example.classmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.classmate.adapters.DriveUpdateAdapter;
import com.example.classmate.databinding.ActivityDriveUpdatesBinding;
import com.example.classmate.models.DriveUpdate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DriveUpdatesActivity extends AppCompatActivity {

    private ActivityDriveUpdatesBinding binding;
    private DatabaseReference databaseReference;
    private List<DriveUpdate> updates;
    private DriveUpdateAdapter adapter;
    private boolean isAdmin;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriveUpdatesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        // Standardized path to "DriveUpdates" and used DB_URL
        databaseReference = FirebaseDatabase.getInstance(DB_URL).getReference("DriveUpdates");

        setupUI();
        loadUpdates();
    }

    private void setupUI() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        updates = new ArrayList<>();
        adapter = new DriveUpdateAdapter(this, updates, isAdmin, update -> {
            databaseReference.child(update.getId()).removeValue()
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete", Toast.LENGTH_SHORT).show());
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        if (isAdmin) {
            binding.addFab.setVisibility(View.VISIBLE);
            binding.addFab.setOnClickListener(v -> {
                Intent intent = new Intent(this, AddDriveUpdateActivity.class);
                startActivity(intent);
            });
        }

        binding.swipeRefresh.setOnRefreshListener(this::loadUpdates);
    }

    private void loadUpdates() {
        binding.progressBar.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updates.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DriveUpdate update = ds.getValue(DriveUpdate.class);
                    if (update != null) {
                        updates.add(update);
                    }
                }
                adapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);
                binding.emptyState.getRoot().setVisibility(updates.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);
                Toast.makeText(DriveUpdatesActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
