package com.example.classmate.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classmate.databinding.ActivityAddLabBinding;
import com.example.classmate.models.Lab;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddLabActivity extends AppCompatActivity {

    private ActivityAddLabBinding binding;
    private DatabaseReference dbRef;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddLabBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Labs");

        binding.saveBtn.setOnClickListener(v -> saveData());
    }

    private void saveData() {
        String name = binding.labNameET.getText().toString().trim();
        String timings = binding.timingsET.getText().toString().trim();
        String experiments = binding.experimentsET.getText().toString().trim();
        String submission = binding.submissionET.getText().toString().trim();
        String link = binding.linkET.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(timings)) {
            Toast.makeText(this, "Lab name and timings are required", Toast.LENGTH_SHORT).show();
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

        Lab lab = new Lab(id, name, timings, experiments, submission, link, "Lab Manual/Link");
        dbRef.child(id).setValue(lab).addOnCompleteListener(task -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.saveBtn.setEnabled(true);
            if (task.isSuccessful()) {
                Toast.makeText(this, "Lab schedule added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                Log.e("AddLabActivity", "Database error: " + error);
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}