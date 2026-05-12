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
import com.example.classmate.databinding.ItemAnnouncementBinding;
import com.example.classmate.models.Announcement;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {

    private final Context context;
    private final List<Announcement> announcements;
    private final boolean isAdmin;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public AnnouncementAdapter(Context context, List<Announcement> announcements, boolean isAdmin) {
        this.context = context;
        this.announcements = announcements;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAnnouncementBinding binding = ItemAnnouncementBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        holder.binding.titleTV.setText(announcement.getTitle());
        holder.binding.contentTV.setText(announcement.getContent());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        holder.binding.dateTV.setText(sdf.format(new Date(announcement.getTimestamp())));

        holder.binding.urgentTag.setVisibility(announcement.isUrgent() ? View.VISIBLE : View.GONE);

        if (isAdmin) {
            holder.binding.deleteBtn.setVisibility(View.VISIBLE);
            holder.binding.deleteBtn.setOnClickListener(v -> showDeleteDialog(announcement.getId()));
        } else {
            holder.binding.deleteBtn.setVisibility(View.GONE);
        }

        if (announcement.getDriveLink() != null && !announcement.getDriveLink().isEmpty()) {
            holder.binding.downloadBtn.setVisibility(View.VISIBLE);
            holder.binding.downloadBtn.setText("VIEW LINK");
            holder.binding.downloadBtn.setOnClickListener(v -> openLink(announcement.getDriveLink()));
        } else {
            holder.binding.downloadBtn.setVisibility(View.GONE);
        }
    }

    private void showDeleteDialog(String id) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Announcement")
                .setMessage("Are you sure you want to delete this notice?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseDatabase.getInstance(DB_URL).getReference("Announcements").child(id).removeValue()
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
        return announcements.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemAnnouncementBinding binding;

        public ViewHolder(ItemAnnouncementBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}