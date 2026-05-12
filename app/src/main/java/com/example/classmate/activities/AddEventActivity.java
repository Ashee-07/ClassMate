package com.example.classmate.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classmate.databinding.ActivityAddEventBinding;
import com.example.classmate.models.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEventActivity extends AppCompatActivity {

    private ActivityAddEventBinding binding;
    private DatabaseReference dbRef;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Events");

        // UI already exists from previous version, just ensure we use standard save
        binding.saveBtn.setOnClickListener(v -> saveData());
    }

    private void saveData() {
        String title = binding.titleET.getText().toString().trim();
        String description = binding.descriptionET.getText().toString().trim();
        String date = binding.dateET.getText().toString().trim();
        String venue = binding.venueET.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date) || TextUtils.isEmpty(venue)) {
            Toast.makeText(this, "Title, Date and Venue are required", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.saveBtn.setEnabled(false);

        String id = dbRef.push().getKey();
        if (id == null) {
            binding.progressBar.setVisibility(View.GONE);
            binding.saveBtn.setEnabled(true);
            Toast.makeText(this, "Failed to generate ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Event event = new Event(id, title, description, date, venue);
        dbRef.child(id).setValue(event).addOnCompleteListener(task -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.saveBtn.setEnabled(true);
            if (task.isSuccessful()) {
                Toast.makeText(this, "Event saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                Log.e("AddEventActivity", "Database error: " + error);
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}