package com.example.classmate.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classmate.databinding.ActivityAddAssignmentBinding;
import com.example.classmate.models.Assignment;
import com.example.classmate.utils.LogUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAssignmentActivity extends AppCompatActivity {

    private ActivityAddAssignmentBinding binding;
    private DatabaseReference dbRef;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";
    
    private final String[] subjects = {"MAD", "Cloud Computing", "Honors DevOps", "CNS", "Deep Learning (DL)", "IDP-II", "QLAR"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAssignmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Assignments");

        setupSubjectSpinner();

        binding.saveBtn.setOnClickListener(v -> saveData());
    }

    private void setupSubjectSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjects);
        binding.subjectSpinner.setAdapter(adapter);
    }

    private void saveData() {
        String title = binding.titleET.getText().toString().trim();
        String subject = binding.subjectSpinner.getText().toString().trim();
        String description = binding.descriptionET.getText().toString().trim();
        String dueDate = binding.dueDateET.getText().toString().trim();
        String link = binding.linkET.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(subject) || TextUtils.isEmpty(description) || TextUtils.isEmpty(dueDate)) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
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

        Assignment assignment = new Assignment(id, title, subject, description, dueDate, link, "Manual/Reference Link");
        dbRef.child(id).setValue(assignment).addOnCompleteListener(task -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.saveBtn.setEnabled(true);
            if (task.isSuccessful()) {
                LogUtils.logAction("Added Assignment: " + title);
                Toast.makeText(this, "Assignment uploaded successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                Log.e("AddAssignment", "Database error: " + error);
                Toast.makeText(this, "Database error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}