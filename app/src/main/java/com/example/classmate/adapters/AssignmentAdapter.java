package com.example.classmate.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classmate.databinding.ItemAssignmentBinding;
import com.example.classmate.models.Assignment;
import com.example.classmate.utils.LogUtils;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ViewHolder> {

    private final Context context;
    private final List<Assignment> assignments;
    private final boolean isAdmin;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private int lastPosition = -1;

    public AssignmentAdapter(Context context, List<Assignment> assignments, boolean isAdmin) {
        this.context = context;
        this.assignments = assignments;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAssignmentBinding binding = ItemAssignmentBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Assignment assignment = assignments.get(position);
        holder.binding.titleTV.setText(assignment.getTitle());
        holder.binding.subjectLabel.setText(assignment.getSubject());
        holder.binding.descriptionTV.setText(assignment.getDescription());
        holder.binding.dueDateTV.setText("Due: " + assignment.getDueDate());

        // Handle Admin Delete Option
        if (isAdmin) {
            holder.binding.deleteBtn.setVisibility(View.VISIBLE);
            holder.binding.deleteBtn.setOnClickListener(v -> showDeleteDialog(assignment, position));
        } else {
            holder.binding.deleteBtn.setVisibility(View.GONE);
        }

        if (assignment.getAttachmentUrl() != null && !assignment.getAttachmentUrl().isEmpty()) {
            holder.binding.fileNameTV.setText(assignment.getFileName() != null ? assignment.getFileName() : "Attachment Link");
            holder.binding.downloadBtn.setVisibility(View.VISIBLE);
            holder.binding.downloadBtn.setOnClickListener(v -> openLink(assignment.getAttachmentUrl()));
        } else {
            holder.binding.fileNameTV.setText("No attachment");
            holder.binding.downloadBtn.setVisibility(View.GONE);
        }

        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            viewToAnimate.setTranslationY(100f);
            viewToAnimate.setAlpha(0f);
            viewToAnimate.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setInterpolator(new DecelerateInterpolator(1.5f))
                    .setDuration(400)
                    .setStartDelay(position * 40L)
                    .start();
            lastPosition = position;
        }
    }

    private void showDeleteDialog(Assignment assignment, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Assignment")
                .setMessage("Are you sure you want to delete this assignment?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    String id = assignment.getId();
                    // 1. Move to Trash
                    FirebaseDatabase.getInstance(DB_URL).getReference("Trash").child(id).setValue(assignment)
                            .addOnSuccessListener(aVoid -> {
                                // 2. Remove from Assignments
                                FirebaseDatabase.getInstance(DB_URL).getReference("Assignments").child(id).removeValue()
                                        .addOnSuccessListener(aVoid2 -> {
                                            LogUtils.logAction("Deleted Assignment: " + assignment.getTitle());
                                            Toast.makeText(context, "Moved to Trash", Toast.LENGTH_SHORT).show();
                                        });
                            })
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
        return assignments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemAssignmentBinding binding;

        public ViewHolder(ItemAssignmentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
