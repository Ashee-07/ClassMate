package com.example.classmate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classmate.databinding.ItemSeminarBinding;
import com.example.classmate.models.Seminar;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class SeminarAdapter extends RecyclerView.Adapter<SeminarAdapter.ViewHolder> {

    private final Context context;
    private final List<Seminar> seminars;
    private final boolean isAdmin;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public SeminarAdapter(Context context, List<Seminar> seminars, boolean isAdmin) {
        this.context = context;
        this.seminars = seminars;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSeminarBinding binding = ItemSeminarBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Seminar seminar = seminars.get(position);
        holder.binding.topicTV.setText(seminar.getTopic());
        holder.binding.speakerTV.setText("By: " + seminar.getSpeaker());
        holder.binding.dateTV.setText(seminar.getDate() + " " + seminar.getTime());
        holder.binding.venueTV.setText(seminar.getVenue());

        if (isAdmin) {
            holder.binding.deleteBtn.setVisibility(View.VISIBLE);
            holder.binding.deleteBtn.setOnClickListener(v -> showDeleteDialog(seminar.getId()));
        } else {
            holder.binding.deleteBtn.setVisibility(View.GONE);
        }
    }

    private void showDeleteDialog(String id) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Seminar")
                .setMessage("Are you sure you want to delete this seminar?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseDatabase.getInstance(DB_URL).getReference("Seminars").child(id).removeValue()
                            .addOnSuccessListener(aVoid -> Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(context, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return seminars.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemSeminarBinding binding;

        public ViewHolder(ItemSeminarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
