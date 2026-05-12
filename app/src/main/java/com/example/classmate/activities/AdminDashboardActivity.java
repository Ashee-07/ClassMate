package com.example.classmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.classmate.R;
import com.example.classmate.adapters.DashboardAdapter;
import com.example.classmate.databinding.ActivityAdminDashboardBinding;
import com.example.classmate.models.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private ActivityAdminDashboardBinding binding;
    private DashboardAdapter adapter;
    private DatabaseReference rootRef;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        rootRef = FirebaseDatabase.getInstance(DB_URL).getReference();

        setupRecyclerView();
        loadAnalytics();
    }

    private void setupRecyclerView() {
        List<DashboardItem> items = new ArrayList<>();
        items.add(new DashboardItem("Manage Assignments", R.drawable.ic_assignment, R.color.card_assignment));
        items.add(new DashboardItem("Manage Announcements", R.drawable.ic_announcement, R.color.card_announcement));
        items.add(new DashboardItem("Manage Exams", R.drawable.ic_exam, R.color.card_exam));
        items.add(new DashboardItem("Manage Labs", R.drawable.ic_lab, R.color.card_lab));
        items.add(new DashboardItem("Manage Events", R.drawable.ic_event, R.color.card_event));
        items.add(new DashboardItem("Manage Seminars", R.drawable.ic_seminar, R.color.card_seminar));
        items.add(new DashboardItem("Manage Materials", R.drawable.ic_material, R.color.card_material));
        items.add(new DashboardItem("Manage Calendar", R.drawable.ic_calendar, R.color.card_calendar));
        items.add(new DashboardItem("Manage Drive Updates", R.drawable.ic_material, R.color.card_drive));
        items.add(new DashboardItem("Manage Exam Marks", R.drawable.ic_exam, R.color.card_marks));

        adapter = new DashboardAdapter(this, items, item -> {
            Intent intent;
            switch (item.getTitle()) {
                case "Manage Assignments": intent = new Intent(this, AssignmentsActivity.class); break;
                case "Manage Announcements": intent = new Intent(this, AnnouncementsActivity.class); break;
                case "Manage Exams": intent = new Intent(this, ExamsActivity.class); break;
                case "Manage Labs": intent = new Intent(this, LabsActivity.class); break;
                case "Manage Events": intent = new Intent(this, EventsActivity.class); break;
                case "Manage Seminars": intent = new Intent(this, SeminarsActivity.class); break;
                case "Manage Materials": intent = new Intent(this, MaterialsActivity.class); break;
                case "Manage Calendar": intent = new Intent(this, CalendarActivity.class); break;
                case "Manage Drive Updates": intent = new Intent(this, DriveUpdatesActivity.class); break;
                case "Manage Exam Marks": intent = new Intent(this, ExamMarksActivity.class); break;
                default: return;
            }
            intent.putExtra("isAdmin", true);
            startActivity(intent);
        });

        binding.adminRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.adminRecyclerView.setAdapter(adapter);
    }

    private void loadAnalytics() {
        rootRef.child("Assignments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.totalAssignmentsTV.setText(String.valueOf(snapshot.getChildrenCount()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        rootRef.child("Announcements").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.activeNoticesTV.setText(String.valueOf(snapshot.getChildrenCount()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(this, AdminProfileActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, RoleSelectionActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
