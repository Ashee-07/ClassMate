package com.example.classmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.classmate.adapters.AnnouncementAdapter;
import com.example.classmate.databinding.ActivityAnnouncementsBinding;
import com.example.classmate.models.Announcement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementsActivity extends AppCompatActivity {

    private ActivityAnnouncementsBinding binding;
    private AnnouncementAdapter adapter;
    private List<Announcement> announcementList;
    private DatabaseReference dbRef;
    private boolean isAdmin = false;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnnouncementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Announcements");
        announcementList = new ArrayList<>();
        adapter = new AnnouncementAdapter(this, announcementList, isAdmin);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        if (isAdmin) {
            binding.addFab.setVisibility(View.VISIBLE);
            binding.addFab.setOnClickListener(v -> {
                startActivity(new Intent(this, AddAnnouncementActivity.class));
            });
        }

        binding.swipeRefresh.setOnRefreshListener(this::fetchAnnouncements);

        fetchAnnouncements();
    }

    private void fetchAnnouncements() {
        binding.swipeRefresh.setRefreshing(true);
        dbRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                announcementList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Announcement announcement = postSnapshot.getValue(Announcement.class);
                    if (announcement != null) {
                        announcementList.add(0, announcement); // Latest first
                    }
                }
                adapter.notifyDataSetChanged();
                binding.swipeRefresh.setRefreshing(false);
                binding.emptyState.setVisibility(announcementList.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.swipeRefresh.setRefreshing(false);
                Log.e("AnnouncementsActivity", "Database Error: " + error.getMessage());
                Toast.makeText(AnnouncementsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}