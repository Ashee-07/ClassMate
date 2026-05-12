package com.example.classmate.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classmate.databinding.ItemDriveUpdateBinding;
import com.example.classmate.models.DriveUpdate;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class DriveUpdateAdapter extends RecyclerView.Adapter<DriveUpdateAdapter.ViewHolder> {

    private final Context context;
    private final List<DriveUpdate> updates;
    private final boolean isAdmin;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public DriveUpdateAdapter(Context context, List<DriveUpdate> updates, boolean isAdmin, OnDeleteClickListener ignored) {
        this.context = context;
        this.updates = updates;
        this.isAdmin = isAdmin;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(DriveUpdate update);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDriveUpdateBinding binding = ItemDriveUpdateBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DriveUpdate update = updates.get(position);
        holder.binding.companyNameTV.setText(update.getCompanyName());
        holder.binding.positionTV.setText(update.getPosition());
        holder.binding.deadlineTV.setText("Deadline: " + update.getDeadline());
        holder.binding.descriptionTV.setText(update.getDescription());

        holder.binding.infoBtn.setOnClickListener(v -> {
            if (update.getInfoUrl() != null && !update.getInfoUrl().isEmpty()) {
                openLink(update.getInfoUrl());
            } else {
                Toast.makeText(context, "No details link available", Toast.LENGTH_SHORT).show();
            }
        });

        if (isAdmin) {
            holder.binding.deleteBtn.setVisibility(View.VISIBLE);
            holder.binding.deleteBtn.setOnClickListener(v -> showDeleteDialog(update.getId()));
        } else {
            holder.binding.deleteBtn.setVisibility(View.GONE);
        }
    }

    private void showDeleteDialog(String id) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Update")
                .setMessage("Are you sure you want to delete this placement update?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseDatabase.getInstance(DB_URL).getReference("DriveUpdates").child(id).removeValue()
                            .addOnSuccessListener(aVoid -> Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(context, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openLink(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Cannot open link", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return updates.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemDriveUpdateBinding binding;
        public ViewHolder(ItemDriveUpdateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
