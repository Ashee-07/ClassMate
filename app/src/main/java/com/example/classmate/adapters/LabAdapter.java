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
import com.example.classmate.databinding.ItemLabBinding;
import com.example.classmate.models.Lab;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class LabAdapter extends RecyclerView.Adapter<LabAdapter.ViewHolder> {

    private final Context context;
    private final List<Lab> labs;
    private final boolean isAdmin;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public LabAdapter(Context context, List<Lab> labs, boolean isAdmin) {
        this.context = context;
        this.labs = labs;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLabBinding binding = ItemLabBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lab lab = labs.get(position);
        holder.binding.titleTV.setText(lab.getTitle());
        holder.binding.subjectLabel.setText(lab.getSubject());
        holder.binding.descriptionTV.setText(lab.getDescription());
        holder.binding.dueDateTV.setText("Submission: " + lab.getDueDate());

        // Handle Admin Delete Option
        if (isAdmin) {
            holder.binding.deleteBtn.setVisibility(View.VISIBLE);
            holder.binding.deleteBtn.setOnClickListener(v -> showDeleteDialog(lab.getId(), position));
        } else {
            holder.binding.deleteBtn.setVisibility(View.GONE);
        }

        if (lab.getAttachmentUrl() != null && !lab.getAttachmentUrl().isEmpty()) {
            holder.binding.fileNameTV.setText(lab.getFileName() != null ? lab.getFileName() : "Lab Manual Link");
            holder.binding.downloadBtn.setVisibility(View.VISIBLE);
            holder.binding.downloadBtn.setOnClickListener(v -> openLink(lab.getAttachmentUrl()));
        } else {
            holder.binding.fileNameTV.setText("No manual attached");
            holder.binding.downloadBtn.setVisibility(View.GONE);
        }
    }

    private void showDeleteDialog(String id, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Lab Record")
                .setMessage("Are you sure you want to delete this lab record?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseDatabase.getInstance(DB_URL).getReference("Labs").child(id).removeValue()
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
            Toast.makeText(context, "No app found to open this link", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return labs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemLabBinding binding;

        public ViewHolder(ItemLabBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}