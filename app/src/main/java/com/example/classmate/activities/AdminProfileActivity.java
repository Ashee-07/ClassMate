package com.example.classmate.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.classmate.R;
import com.example.classmate.databinding.ActivityAdminProfileBinding;
import com.example.classmate.databinding.DialogEditProfileBinding;
import com.example.classmate.models.Admin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class AdminProfileActivity extends AppCompatActivity {

    private ActivityAdminProfileBinding binding;
    private DatabaseReference adminRef;
    private FirebaseAuth mAuth;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private Admin currentAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            adminRef = FirebaseDatabase.getInstance(DB_URL).getReference("Admins").child(user.getUid());
            loadAdminData();
        }

        binding.logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            finishAffinity();
            startActivity(new android.content.Intent(this, RoleSelectionActivity.class));
        });

        binding.editProfileBtn.setOnClickListener(v -> {
            if (currentAdmin != null) {
                showEditProfileDialog();
            } else {
                Toast.makeText(this, "Profile data not loaded yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAdminData() {
        adminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentAdmin = snapshot.getValue(Admin.class);
                    if (currentAdmin != null) {
                        binding.adminNameTV.setText(currentAdmin.getName());
                        binding.adminEmailTV.setText(currentAdmin.getEmail());
                        binding.deptTV.setText(currentAdmin.getDepartment());
                        binding.roleTV.setText(currentAdmin.getRole());
                    }
                } else {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        binding.adminNameTV.setText(user.getDisplayName() != null ? user.getDisplayName() : "Admin");
                        binding.adminEmailTV.setText(user.getEmail());
                        currentAdmin = new Admin(user.getUid(), 
                            user.getDisplayName() != null ? user.getDisplayName() : "Admin", 
                            user.getEmail(), "Computer Science", "Admin");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditProfileDialog() {
        DialogEditProfileBinding dialogBinding = DialogEditProfileBinding.inflate(LayoutInflater.from(this));
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogBinding.getRoot())
                .create();

        dialogBinding.editNameET.setText(currentAdmin.getName());
        dialogBinding.editEmailET.setText(currentAdmin.getEmail());
        dialogBinding.editDeptET.setText(currentAdmin.getDepartment());
        dialogBinding.editRoleET.setText(currentAdmin.getRole());

        dialogBinding.cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialogBinding.saveBtn.setOnClickListener(v -> {
            String newName = dialogBinding.editNameET.getText().toString().trim();
            String newEmail = dialogBinding.editEmailET.getText().toString().trim();
            String newPassword = dialogBinding.editPasswordET.getText().toString().trim();
            String newDept = dialogBinding.editDeptET.getText().toString().trim();
            String newRole = dialogBinding.editRoleET.getText().toString().trim();

            if (newName.isEmpty()) {
                dialogBinding.editNameET.setError("Name is required");
                return;
            }
            if (newEmail.isEmpty()) {
                dialogBinding.editEmailET.setError("Email is required");
                return;
            }

            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) return;

            // Start updating
            binding.editProfileBtn.setEnabled(false);
            
            // 1. Update Email in Auth (if changed)
            if (!newEmail.equals(user.getEmail())) {
                user.updateEmail(newEmail).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        updatePasswordAndDatabase(dialog, newName, newEmail, newPassword, newDept, newRole);
                    } else {
                        binding.editProfileBtn.setEnabled(true);
                        Toast.makeText(this, "Email update failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                updatePasswordAndDatabase(dialog, newName, newEmail, newPassword, newDept, newRole);
            }
        });

        dialog.show();
    }

    private void updatePasswordAndDatabase(AlertDialog dialog, String name, String email, String password, String dept, String role) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        // 2. Update Password in Auth (if provided)
        if (!password.isEmpty()) {
            user.updatePassword(password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    saveToDatabase(dialog, name, email, dept, role);
                } else {
                    binding.editProfileBtn.setEnabled(true);
                    Toast.makeText(this, "Password update failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            saveToDatabase(dialog, name, email, dept, role);
        }
    }

    private void saveToDatabase(AlertDialog dialog, String name, String email, String dept, String role) {
        currentAdmin.setName(name);
        currentAdmin.setEmail(email);
        currentAdmin.setDepartment(dept);
        currentAdmin.setRole(role);

        adminRef.setValue(currentAdmin).addOnCompleteListener(task -> {
            binding.editProfileBtn.setEnabled(true);
            if (task.isSuccessful()) {
                Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Database Update Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
