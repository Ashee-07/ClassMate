package com.example.classmate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classmate.databinding.ActivityAdminLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {

    private ActivityAdminLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.loginButton.setOnClickListener(v -> loginAdmin());
        binding.backBtn.setOnClickListener(v -> onBackPressed());
        
        binding.forgotPasswordTV.setOnClickListener(v -> {
            String email = binding.emailEditText.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter your email to reset password", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void loginAdmin() {
        String email = binding.emailEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.passwordEditText.setError("Password is required");
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loginButton.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.loginButton.setEnabled(true);
            if (task.isSuccessful()) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
