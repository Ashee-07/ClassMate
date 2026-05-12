package com.example.classmate.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classmate.databinding.ActivityActivityLogsBinding;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityLogsActivity extends AppCompatActivity {

    private ActivityActivityLogsBinding binding;
    private DatabaseReference logsRef;
    private List<String> logsList;
    private static final String DB_URL = "https://classmate-78a92-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityActivityLogsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

        logsRef = FirebaseDatabase.getInstance(DB_URL).getReference("ActivityLogs");
        logsList = new ArrayList<>();
        
        binding.logsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        loadLogs();
    }

    private void loadLogs() {
        logsRef.limitToLast(50).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                logsList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String log = ds.getValue(String.class);
                    if (log != null) {
                        logsList.add(log);
                    }
                }
                Collections.reverse(logsList);
                if (logsList.isEmpty()) {
                    binding.emptyState.getRoot().setVisibility(View.VISIBLE);
                    binding.logsRecyclerView.setVisibility(View.GONE);
                } else {
                    binding.emptyState.getRoot().setVisibility(View.GONE);
                    binding.logsRecyclerView.setVisibility(View.VISIBLE);
                    binding.logsRecyclerView.setAdapter(new RecyclerView.Adapter<LogViewHolder>() {
                        @NonNull
                        @Override
                        public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                            return new LogViewHolder(v);
                        }

                        @Override
                        public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
                            holder.textView.setText(logsList.get(position));
                        }

                        @Override
                        public int getItemCount() {
                            return logsList.size();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivityLogsActivity.this, "Failed to load logs", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}
