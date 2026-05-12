package com.example.classmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.classmate.activities.*;
import com.example.classmate.adapters.DashboardAdapter;
import com.example.classmate.adapters.SearchAdapter;
import com.example.classmate.databinding.ActivityMainBinding;
import com.example.classmate.models.*;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DashboardAdapter adapter;
    private SearchAdapter searchAdapter;
    private List<Object> searchResults;
    private DatabaseReference rootRef;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        rootRef = FirebaseDatabase.getInstance(DB_URL).getReference();

        setupRecyclerView();
        setupSearch();
        
        binding.swipeRefresh.setOnRefreshListener(() -> {
            binding.swipeRefresh.setRefreshing(false);
        });
    }

    private void setupRecyclerView() {
        List<DashboardItem> items = new ArrayList<>();
        items.add(new DashboardItem(getString(R.string.module_assignments), R.drawable.ic_assignment, R.color.card_assignment));
        items.add(new DashboardItem(getString(R.string.module_announcements), R.drawable.ic_announcement, R.color.card_announcement));
        items.add(new DashboardItem(getString(R.string.module_exams), R.drawable.ic_exam, R.color.card_exam));
        items.add(new DashboardItem(getString(R.string.module_labs), R.drawable.ic_lab, R.color.card_lab));
        items.add(new DashboardItem(getString(R.string.module_events), R.drawable.ic_event, R.color.card_event));
        items.add(new DashboardItem(getString(R.string.module_seminars), R.drawable.ic_seminar, R.color.card_seminar));
        items.add(new DashboardItem(getString(R.string.module_materials), R.drawable.ic_material, R.color.card_material));
        items.add(new DashboardItem(getString(R.string.module_calendar), R.drawable.ic_calendar, R.color.card_calendar));
        items.add(new DashboardItem(getString(R.string.module_drive_updates), R.drawable.ic_material, R.color.card_drive));
        items.add(new DashboardItem(getString(R.string.module_exam_marks), R.drawable.ic_exam, R.color.card_marks));

        adapter = new DashboardAdapter(this, items, item -> {
            Intent intent;
            String title = item.getTitle();
            if (title.equals(getString(R.string.module_assignments))) {
                intent = new Intent(this, AssignmentsActivity.class);
            } else if (title.equals(getString(R.string.module_announcements))) {
                intent = new Intent(this, AnnouncementsActivity.class);
            } else if (title.equals(getString(R.string.module_exams))) {
                intent = new Intent(this, ExamsActivity.class);
            } else if (title.equals(getString(R.string.module_labs))) {
                intent = new Intent(this, LabsActivity.class);
            } else if (title.equals(getString(R.string.module_events))) {
                intent = new Intent(this, EventsActivity.class);
            } else if (title.equals(getString(R.string.module_seminars))) {
                intent = new Intent(this, SeminarsActivity.class);
            } else if (title.equals(getString(R.string.module_materials))) {
                intent = new Intent(this, MaterialsActivity.class);
            } else if (title.equals(getString(R.string.module_calendar))) {
                intent = new Intent(this, CalendarActivity.class);
            } else if (title.equals(getString(R.string.module_drive_updates))) {
                intent = new Intent(this, DriveUpdatesActivity.class);
            } else if (title.equals(getString(R.string.module_exam_marks))) {
                intent = new Intent(this, ExamMarksActivity.class);
            } else {
                return;
            }
            startActivity(intent);
        });

        binding.dashboardRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.dashboardRecyclerView.setAdapter(adapter);
    }

    private void setupSearch() {
        searchResults = new ArrayList<>();
        searchAdapter = new SearchAdapter(this, searchResults, false);
        
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    binding.dashboardRecyclerView.setAdapter(adapter);
                    binding.dashboardRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                } else {
                    binding.dashboardRecyclerView.setAdapter(searchAdapter);
                    binding.dashboardRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    performSearch(newText);
                }
                return true;
            }
        });
    }

    private void performSearch(String query) {
        String lowerQuery = query.toLowerCase();
        searchResults.clear();
        
        String[] nodes = {"Assignments", "Labs", "Exams", "Announcements", "Events", "Seminars", "StudyMaterials", "DriveUpdates", "ExamMarks", "Calendar"};
        
        for (String node : nodes) {
            rootRef.child(node).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Object item = null;
                        
                        String content = data.toString().toLowerCase();
                        if (content.contains(lowerQuery)) {
                            item = mapToModel(node, data);
                            if (item != null) searchResults.add(item);
                        }
                    }
                    searchAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }

    private Object mapToModel(String node, DataSnapshot data) {
        try {
            switch (node) {
                case "Assignments": return data.getValue(Assignment.class);
                case "Labs": return data.getValue(Lab.class);
                case "Exams": return data.getValue(Exam.class);
                case "Announcements": return data.getValue(Announcement.class);
                case "Events": return data.getValue(Event.class);
                case "Seminars": return data.getValue(Seminar.class);
                case "StudyMaterials": return data.getValue(StudyMaterial.class);
                case "DriveUpdates": return data.getValue(DriveUpdate.class);
                case "ExamMarks": return data.getValue(ExamMark.class);
                case "Calendar": return data.getValue(CalendarEvent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
