package com.example.classmate.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classmate.databinding.ActivityAddCalendarEventBinding;
import com.example.classmate.models.CalendarEvent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;
import java.util.Locale;

public class AddCalendarEventActivity extends AppCompatActivity {

    private ActivityAddCalendarEventBinding binding;
    private DatabaseReference dbRef;
    private Calendar calendar = Calendar.getInstance();
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCalendarEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Calendar");

        binding.dateET.setOnClickListener(v -> showDatePicker());
        binding.saveBtn.setOnClickListener(v -> saveEvent());
    }

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String date = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
            binding.dateET.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveEvent() {
        String title = binding.titleET.getText().toString().trim();
        String description = binding.descriptionET.getText().toString().trim();
        String date = binding.dateET.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Title and Date are required", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.saveBtn.setEnabled(false);

        String id = dbRef.push().getKey();
        if (id != null) {
            CalendarEvent event = new CalendarEvent(id, title, description, date);
            dbRef.child(id).setValue(event).addOnCompleteListener(task -> {
                binding.progressBar.setVisibility(View.GONE);
                binding.saveBtn.setEnabled(true);
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Event added to calendar", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}