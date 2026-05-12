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
import com.example.classmate.databinding.ItemExamBinding;
import com.example.classmate.models.Exam;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {

    private final Context context;
    private final List<Exam> exams;
    private final boolean isAdmin;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public ExamAdapter(Context context, List<Exam> exams, boolean isAdmin) {
        this.context = context;
        this.exams = exams;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExamBinding binding = ItemExamBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exam exam = exams.get(position);
        holder.binding.subjectTV.setText(exam.getSubject());
        holder.binding.dateTV.setText(exam.getDate());
        holder.binding.timeTV.setText(exam.getTime());

        if (isAdmin) {
            holder.binding.deleteBtn.setVisibility(View.VISIBLE);
            holder.binding.deleteBtn.setOnClickListener(v -> showDeleteDialog(exam.getId()));
        } else {
            holder.binding.deleteBtn.setVisibility(View.GONE);
        }

        if (exam.getAttachmentUrl() != null && !exam.getAttachmentUrl().isEmpty()) {
            holder.binding.downloadBtn.setVisibility(View.VISIBLE);
            holder.binding.downloadBtn.setOnClickListener(v -> openLink(exam.getAttachmentUrl()));
        } else {
            holder.binding.downloadBtn.setVisibility(View.GONE);
        }
    }

    private void showDeleteDialog(String id) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Exam")
                .setMessage("Are you sure you want to delete this exam schedule?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseDatabase.getInstance(DB_URL).getReference("Exams").child(id).removeValue()
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
        return exams.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemExamBinding binding;

        public ViewHolder(ItemExamBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}