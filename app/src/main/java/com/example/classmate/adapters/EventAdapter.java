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
import com.example.classmate.databinding.ItemEventBinding;
import com.example.classmate.models.Event;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private final Context context;
    private final List<Event> events;
    private final boolean isAdmin;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public EventAdapter(Context context, List<Event> events, boolean isAdmin) {
        this.context = context;
        this.events = events;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEventBinding binding = ItemEventBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.binding.titleTV.setText(event.getTitle());
        holder.binding.descriptionTV.setText(event.getDescription());
        holder.binding.dateTV.setText(event.getDate());
        holder.binding.venueTV.setText(event.getVenue());

        // Handle Admin Delete Option
        if (isAdmin) {
            holder.binding.deleteBtn.setVisibility(View.VISIBLE);
            holder.binding.deleteBtn.setOnClickListener(v -> showDeleteDialog(event.getId(), position));
        } else {
            holder.binding.deleteBtn.setVisibility(View.GONE);
        }

        if (event.getAttachmentUrl() != null && !event.getAttachmentUrl().isEmpty()) {
            holder.binding.downloadBtn.setVisibility(View.VISIBLE);
            holder.binding.downloadBtn.setOnClickListener(v -> openLink(event.getAttachmentUrl()));
        } else {
            holder.binding.downloadBtn.setVisibility(View.GONE);
        }
    }

    private void showDeleteDialog(String id, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseDatabase.getInstance(DB_URL).getReference("Events").child(id).removeValue()
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
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemEventBinding binding;

        public ViewHolder(ItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}