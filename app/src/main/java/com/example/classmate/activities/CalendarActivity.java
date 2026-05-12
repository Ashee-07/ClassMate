package com.example.classmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.classmate.adapters.CalendarEventAdapter;
import com.example.classmate.databinding.ActivityCalendarBinding;
import com.example.classmate.models.CalendarEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private ActivityCalendarBinding binding;
    private DatabaseReference dbRef;
    private List<CalendarEvent> allEvents = new ArrayList<>();
    private List<CalendarEvent> todayEvents = new ArrayList<>();
    private List<CalendarEvent> upcomingEvents = new ArrayList<>();
    private CalendarEventAdapter todayAdapter, upcomingAdapter;
    private boolean isAdmin;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Calendar");

        setupUI();
        fetchEvents();
    }

    private void setupUI() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Setup Today's Events RV
        todayAdapter = new CalendarEventAdapter(this, todayEvents, isAdmin);
        binding.todayEventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.todayEventsRecyclerView.setAdapter(todayAdapter);

        // Setup Upcoming Events RV
        upcomingAdapter = new CalendarEventAdapter(this, upcomingEvents, isAdmin);
        binding.upcomingEventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.upcomingEventsRecyclerView.setAdapter(upcomingAdapter);

        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
            filterByDate(selectedDate);
        });

        if (isAdmin) {
            binding.addFab.setVisibility(View.VISIBLE);
            binding.addFab.setOnClickListener(v -> {
                startActivity(new Intent(this, AddCalendarEventActivity.class));
            });
        }
    }

    private void fetchEvents() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allEvents.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CalendarEvent event = ds.getValue(CalendarEvent.class);
                    if (event != null) {
                        allEvents.add(event);
                    }
                }
                Collections.sort(allEvents, (e1, e2) -> e1.getDate().compareTo(e2.getDate()));
                updateLists();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CalendarActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLists() {
        todayEvents.clear();
        upcomingEvents.clear();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        for (CalendarEvent event : allEvents) {
            if (event.getDate() != null) {
                if (event.getDate().equals(today)) {
                    todayEvents.add(event);
                } else if (event.getDate().compareTo(today) > 0) {
                    upcomingEvents.add(event);
                }
            }
        }

        todayAdapter.notifyDataSetChanged();
        upcomingAdapter.notifyDataSetChanged();

        binding.noTodayEventsTV.setVisibility(todayEvents.isEmpty() ? View.VISIBLE : View.GONE);
        binding.noUpcomingEventsTV.setVisibility(upcomingEvents.isEmpty() ? View.VISIBLE : View.GONE);
        
        binding.todayHeaderTV.setText("Today's Events (" + today + ")");
    }

    private void filterByDate(String date) {
        todayEvents.clear();
        for (CalendarEvent event : allEvents) {
            if (event.getDate() != null && event.getDate().equals(date)) {
                todayEvents.add(event);
            }
        }
        todayAdapter.notifyDataSetChanged();
        binding.todayHeaderTV.setText("Events for " + date);
        binding.noTodayEventsTV.setVisibility(todayEvents.isEmpty() ? View.VISIBLE : View.GONE);
        
        if (todayEvents.isEmpty()) {
            binding.noTodayEventsTV.setText("No events on this day. Tap to show today's.");
            binding.noTodayEventsTV.setOnClickListener(v -> {
                updateLists();
                binding.noTodayEventsTV.setOnClickListener(null);
            });
        }
    }
}
