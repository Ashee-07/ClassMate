package com.example.classmate.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classmate.databinding.ActivityAddExamMarkBinding;
import com.example.classmate.models.ExamMark;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddExamMarkActivity extends AppCompatActivity {

    private ActivityAddExamMarkBinding binding;
    private DatabaseReference dbRef;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExamMarkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("ExamMarks");

        binding.saveBtn.setOnClickListener(v -> saveMarks());
    }

    private void saveMarks() {
        String subject = binding.subjectET.getText().toString().trim();
        String type = binding.examTypeET.getText().toString().trim();
        String url = binding.urlET.getText().toString().trim();

        if (TextUtils.isEmpty(subject) || TextUtils.isEmpty(type) || TextUtils.isEmpty(url)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.saveBtn.setEnabled(false);
        String id = dbRef.push().getKey();
        if (id != null) {
            ExamMark examMark = new ExamMark(id, subject, type, url);
            dbRef.child(id).setValue(examMark).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Result posted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    binding.saveBtn.setEnabled(true);
                    Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
