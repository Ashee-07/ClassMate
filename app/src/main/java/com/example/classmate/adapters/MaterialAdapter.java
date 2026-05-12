package com.example.classmate.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classmate.databinding.ItemMaterialBinding;
import com.example.classmate.models.StudyMaterial;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    private final Context context;
    private final List<StudyMaterial> materials;
    private final boolean isAdmin;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public MaterialAdapter(Context context, List<StudyMaterial> materials, boolean isAdmin) {
        this.context = context;
        this.materials = materials;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMaterialBinding binding = ItemMaterialBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudyMaterial material = materials.get(position);
        holder.binding.titleTV.setText(material.getTitle());
        holder.binding.subjectTV.setText(material.getSubject());
        holder.binding.typeTV.setText(material.getType());

        if (isAdmin) {
            holder.binding.deleteBtn.setVisibility(View.VISIBLE);
            holder.binding.deleteBtn.setOnClickListener(v -> deleteMaterial(material.getId()));
        } else {
            holder.binding.deleteBtn.setVisibility(View.GONE);
        }

        if (material.getFileUrl() != null && !material.getFileUrl().isEmpty()) {
            holder.binding.downloadBtn.setVisibility(View.VISIBLE);
            holder.binding.downloadBtn.setOnClickListener(v -> openLink(material.getFileUrl()));
        } else {
            holder.binding.downloadBtn.setVisibility(View.GONE);
        }
    }

    private void deleteMaterial(String id) {
        FirebaseDatabase.getInstance(DB_URL).getReference("StudyMaterials").child(id).removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
        return materials.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemMaterialBinding binding;

        public ViewHolder(ItemMaterialBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}