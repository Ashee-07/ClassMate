package com.example.classmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.classmate.adapters.LabAdapter;
import com.example.classmate.databinding.ActivityLabsBinding;
import com.example.classmate.models.Lab;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class LabsActivity extends AppCompatActivity {

    private ActivityLabsBinding binding;
    private LabAdapter adapter;
    private List<Lab> labList;
    private DatabaseReference dbRef;
    private boolean isAdmin = false;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLabsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Labs");
        labList = new ArrayList<>();
        adapter = new LabAdapter(this, labList, isAdmin);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        if (isAdmin) {
            binding.addFab.setVisibility(View.VISIBLE);
            binding.addFab.setOnClickListener(v -> {
                startActivity(new Intent(this, AddLabActivity.class));
            });
        }

        binding.swipeRefresh.setOnRefreshListener(this::fetchLabs);

        fetchLabs();
    }

    private void fetchLabs() {
        binding.swipeRefresh.setRefreshing(true);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                labList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Lab lab = postSnapshot.getValue(Lab.class);
                    if (lab != null) {
                        labList.add(lab);
                    }
                }
                adapter.notifyDataSetChanged();
                binding.swipeRefresh.setRefreshing(false);
                binding.emptyState.setVisibility(labList.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.swipeRefresh.setRefreshing(false);
                Log.e("LabsActivity", "Database Error: " + error.getMessage());
                Toast.makeText(LabsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}