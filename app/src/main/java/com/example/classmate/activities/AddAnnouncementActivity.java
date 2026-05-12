package com.example.classmate.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classmate.databinding.ActivityAddAnnouncementBinding;
import com.example.classmate.models.Announcement;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAnnouncementActivity extends AppCompatActivity {

    private ActivityAddAnnouncementBinding binding;
    private DatabaseReference dbRef;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAnnouncementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Announcements");

        binding.saveBtn.setOnClickListener(v -> saveData());
    }

    private void saveData() {
        String title = binding.titleET.getText().toString().trim();
        String content = binding.contentET.getText().toString().trim();
        String driveLink = binding.driveLinkET.getText().toString().trim();
        boolean isUrgent = binding.urgentSwitch.isChecked();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Title and content are required", Toast.LENGTH_SHORT).show();
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

        Announcement announcement = new Announcement(id, title, content, System.currentTimeMillis(), isUrgent, driveLink);
        dbRef.child(id).setValue(announcement).addOnCompleteListener(task -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.saveBtn.setEnabled(true);
            if (task.isSuccessful()) {
                Toast.makeText(this, "Announcement posted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                Log.e("AddAnnouncement", "Database error: " + error);
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}