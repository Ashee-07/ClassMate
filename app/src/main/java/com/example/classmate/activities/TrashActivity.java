package com.example.classmate.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.classmate.databinding.ActivityActivityLogsBinding; // Reusing same layout structure
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class TrashActivity extends AppCompatActivity {

    private ActivityActivityLogsBinding binding;
    private DatabaseReference trashRef;
    private List<String> trashList;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActivityLogsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Trash Bin");
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        trashRef = FirebaseDatabase.getInstance(DB_URL).getReference("Trash");
        trashList = new ArrayList<>();
        
        binding.logsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        loadTrash();
    }

    private void loadTrash() {
        trashRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trashList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String item = ds.child("title").getValue(String.class);
                    if (item != null) {
                        trashList.add(item);
                    }
                }
                
                if (trashList.isEmpty()) {
                    binding.emptyState.getRoot().setVisibility(View.VISIBLE);
                    binding.logsRecyclerView.setVisibility(View.GONE);
                } else {
                    binding.emptyState.getRoot().setVisibility(View.GONE);
                    binding.logsRecyclerView.setVisibility(View.VISIBLE);
                    binding.logsRecyclerView.setAdapter(new androidx.recyclerview.widget.RecyclerView.Adapter() {
                        @NonNull
                        @Override
                        public androidx.recyclerview.widget.RecyclerView.ViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
                            View v = android.view.LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                            return new androidx.recyclerview.widget.RecyclerView.ViewHolder(v) {};
                        }
                        @Override
                        public void onBindViewHolder(@NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder holder, int position) {
                            ((android.widget.TextView) holder.itemView).setText(trashList.get(position));
                        }
                        @Override
                        public int getItemCount() {
                            return trashList.size();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
