package com.example.classmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.classmate.adapters.MaterialAdapter;
import com.example.classmate.databinding.ActivityMaterialsBinding;
import com.example.classmate.models.StudyMaterial;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MaterialsActivity extends AppCompatActivity {

    private ActivityMaterialsBinding binding;
    private MaterialAdapter adapter;
    private List<StudyMaterial> materialList;
    private DatabaseReference dbRef;
    private boolean isAdmin = false;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMaterialsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("StudyMaterials");
        materialList = new ArrayList<>();
        adapter = new MaterialAdapter(this, materialList, isAdmin);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        if (isAdmin) {
            binding.addFab.setVisibility(View.VISIBLE);
            binding.addFab.setOnClickListener(v -> {
                startActivity(new Intent(this, AddMaterialActivity.class));
            });
        }

        binding.swipeRefresh.setOnRefreshListener(this::fetchMaterials);

        fetchMaterials();
    }

    private void fetchMaterials() {
        binding.swipeRefresh.setRefreshing(true);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                materialList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    StudyMaterial material = postSnapshot.getValue(StudyMaterial.class);
                    if (material != null) {
                        materialList.add(material);
                    }
                }
                adapter.notifyDataSetChanged();
                binding.swipeRefresh.setRefreshing(false);
                binding.emptyState.setVisibility(materialList.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }
}