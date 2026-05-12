package com.example.classmate.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classmate.MainActivity;
import com.example.classmate.databinding.ActivityRoleSelectionBinding;

public class RoleSelectionActivity extends AppCompatActivity {

    private ActivityRoleSelectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoleSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.studentCard.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectionActivity.this, MainActivity.class);
            startActivity(intent);
        });

        binding.adminCard.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectionActivity.this, AdminLoginActivity.class);
            startActivity(intent);
        });
    }
}