package com.example.classmate.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classmate.databinding.ActivityAddDriveUpdateBinding;
import com.example.classmate.models.DriveUpdate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDriveUpdateActivity extends AppCompatActivity {

    private ActivityAddDriveUpdateBinding binding;
    private DatabaseReference databaseReference;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDriveUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance(DB_URL).getReference("DriveUpdates");

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.saveBtn.setOnClickListener(v -> saveData());
    }

    private void saveData() {
        String company = binding.companyNameET.getText().toString().trim();
        String position = binding.positionET.getText().toString().trim();
        String deadline = binding.deadlineET.getText().toString().trim();
        String description = binding.descriptionET.getText().toString().trim();
        String url = binding.infoUrlET.getText().toString().trim();

        if (company.isEmpty() || position.isEmpty() || deadline.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = databaseReference.push().getKey();
        if (id != null) {
            DriveUpdate update = new DriveUpdate(id, company, position, deadline, description, url);
            databaseReference.child(id).setValue(update)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Update posted successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to post update: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}