package com.example.classmate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classmate.databinding.ItemCalendarEventBinding;
import com.example.classmate.models.CalendarEvent;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.ViewHolder> {

    private final Context context;
    private final List<CalendarEvent> events;
    private final boolean isAdmin;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public CalendarEventAdapter(Context context, List<CalendarEvent> events, boolean isAdmin) {
        this.context = context;
        this.events = events;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCalendarEventBinding binding = ItemCalendarEventBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CalendarEvent event = events.get(position);
        holder.binding.titleTV.setText(event.getTitle());
        holder.binding.descriptionTV.setText(event.getDescription());
        holder.binding.dateTV.setText(event.getDate());

        if (isAdmin) {
            holder.binding.deleteBtn.setVisibility(View.VISIBLE);
            holder.binding.deleteBtn.setOnClickListener(v -> showDeleteDialog(event.getId()));
        } else {
            holder.binding.deleteBtn.setVisibility(View.GONE);
        }
    }

    private void showDeleteDialog(String id) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseDatabase.getInstance(DB_URL).getReference("Calendar").child(id).removeValue()
                            .addOnSuccessListener(aVoid -> Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(context, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemCalendarEventBinding binding;

        public ViewHolder(ItemCalendarEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
