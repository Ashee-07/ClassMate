package com.example.classmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.classmate.adapters.SeminarAdapter;
import com.example.classmate.databinding.ActivitySeminarsBinding;
import com.example.classmate.models.Seminar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SeminarsActivity extends AppCompatActivity {

    private ActivitySeminarsBinding binding;
    private SeminarAdapter adapter;
    private List<Seminar> seminarList;
    private DatabaseReference dbRef;
    private boolean isAdmin;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeminarsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Seminars");
        seminarList = new ArrayList<>();
        adapter = new SeminarAdapter(this, seminarList, isAdmin);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        if (isAdmin) {
            binding.addFab.setVisibility(View.VISIBLE);
            binding.addFab.setOnClickListener(v -> {
                startActivity(new Intent(this, AddSeminarActivity.class));
            });
        }

        binding.swipeRefresh.setOnRefreshListener(this::fetchSeminars);

        fetchSeminars();
    }

    private void fetchSeminars() {
        binding.swipeRefresh.setRefreshing(true);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                seminarList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Seminar seminar = postSnapshot.getValue(Seminar.class);
                    if (seminar != null) {
                        seminarList.add(seminar);
                    }
                }
                adapter.notifyDataSetChanged();
                binding.swipeRefresh.setRefreshing(false);
                binding.emptyState.setVisibility(seminarList.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }
}
