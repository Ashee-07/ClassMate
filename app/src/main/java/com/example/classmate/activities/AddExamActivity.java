package com.example.classmate.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classmate.databinding.ActivityAddExamBinding;
import com.example.classmate.models.Exam;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddExamActivity extends AppCompatActivity {

    private ActivityAddExamBinding binding;
    private DatabaseReference dbRef;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExamBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Exams");

        binding.saveBtn.setOnClickListener(v -> saveData());
    }

    private void saveData() {
        String subject = binding.subjectET.getText().toString().trim();
        String date = binding.dateET.getText().toString().trim();
        String time = binding.timeET.getText().toString().trim();
        String link = binding.linkET.getText().toString().trim();

        if (TextUtils.isEmpty(subject) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
            Toast.makeText(this, "Subject, Date and Time are required", Toast.LENGTH_SHORT).show();
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

        Exam exam = new Exam(id, subject, date, time, link, "Exam Link");
        dbRef.child(id).setValue(exam).addOnCompleteListener(task -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.saveBtn.setEnabled(true);
            if (task.isSuccessful()) {
                Toast.makeText(this, "Exam scheduled successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                Log.e("AddExamActivity", "Database error: " + error);
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}