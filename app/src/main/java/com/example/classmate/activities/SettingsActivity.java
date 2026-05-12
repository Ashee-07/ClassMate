package com.example.classmate.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classmate.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "ClassMatePrefs";
    private static final String NOTIF_KEY = "notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        // Load saved preferences
        boolean isNotifEnabled = sharedPreferences.getBoolean(NOTIF_KEY, true);
        binding.notifSwitch.setChecked(isNotifEnabled);

        // Handle Notifications Toggle
        binding.notifSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(NOTIF_KEY, isChecked).apply();
            Toast.makeText(this, isChecked ? "Notifications Enabled" : "Notifications Disabled", Toast.LENGTH_SHORT).show();
        });

        // Placeholder actions for new features
        binding.themeColorBtn.setOnClickListener(v -> Toast.makeText(this, "Theme customization coming soon!", Toast.LENGTH_SHORT).show());
        binding.fontSizeBtn.setOnClickListener(v -> Toast.makeText(this, "Font size control coming soon!", Toast.LENGTH_SHORT).show());
        binding.languageBtn.setOnClickListener(v -> Toast.makeText(this, "Language selection coming soon!", Toast.LENGTH_SHORT).show());
        binding.backupBtn.setOnClickListener(v -> Toast.makeText(this, "Cloud backup initiated...", Toast.LENGTH_SHORT).show());
        binding.restoreBtn.setOnClickListener(v -> Toast.makeText(this, "Cloud restore initiated...", Toast.LENGTH_SHORT).show());
    }
}
