package com.example.classmate.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classmate.databinding.ActivityAddSeminarBinding;
import com.example.classmate.models.Seminar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddSeminarActivity extends AppCompatActivity {

    private ActivityAddSeminarBinding binding;
    private DatabaseReference dbRef;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSeminarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("Seminars");

        binding.saveBtn.setOnClickListener(v -> saveSeminar());
    }

    private void saveSeminar() {
        String topic = binding.topicET.getText().toString().trim();
        String speaker = binding.speakerET.getText().toString().trim();
        String venue = binding.venueET.getText().toString().trim();
        String date = binding.dateET.getText().toString().trim();
        String time = binding.timeET.getText().toString().trim();

        if (TextUtils.isEmpty(topic) || TextUtils.isEmpty(speaker) || TextUtils.isEmpty(venue)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.saveBtn.setEnabled(false);

        String id = dbRef.push().getKey();
        if (id != null) {
            Seminar seminar = new Seminar(id, topic, speaker, venue, date, time);
            dbRef.child(id).setValue(seminar).addOnCompleteListener(task -> {
                binding.progressBar.setVisibility(View.GONE);
                binding.saveBtn.setEnabled(true);
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Seminar added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}