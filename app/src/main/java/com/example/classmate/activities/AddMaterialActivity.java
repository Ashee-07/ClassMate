package com.example.classmate.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classmate.databinding.ActivityAddMaterialBinding;
import com.example.classmate.models.StudyMaterial;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMaterialActivity extends AppCompatActivity {

    private ActivityAddMaterialBinding binding;
    private DatabaseReference dbRef;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMaterialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        
        dbRef = FirebaseDatabase.getInstance(DB_URL).getReference("StudyMaterials");

        binding.saveBtn.setOnClickListener(v -> saveMaterial());
    }

    private void saveMaterial() {
        String title = binding.titleET.getText().toString().trim();
        String subject = binding.subjectET.getText().toString().trim();
        String type = binding.typeET.getText().toString().trim();
        String url = binding.urlET.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(subject) || TextUtils.isEmpty(url)) {
            Toast.makeText(this, "Title, Subject and URL are required", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.saveBtn.setEnabled(false);

        saveToDatabase(title, subject, type, url);
    }

    private void saveToDatabase(String title, String subject, String type, String url) {
        String id = dbRef.push().getKey();
        if (id != null) {
            StudyMaterial material = new StudyMaterial(id, title, subject, type, url, "View Link");
            dbRef.child(id).setValue(material).addOnCompleteListener(task -> {
                binding.progressBar.setVisibility(View.GONE);
                binding.saveBtn.setEnabled(true);
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Material saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
