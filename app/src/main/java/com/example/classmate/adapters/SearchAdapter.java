package com.example.classmate.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classmate.R;
import com.example.classmate.activities.*;
import com.example.classmate.databinding.ItemSearchBinding;
import com.example.classmate.models.*;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private final Context context;
    private final List<Object> items;
    private final boolean isAdmin;

    public SearchAdapter(Context context, List<Object> items, boolean isAdmin) {
        this.context = context;
        this.items = items;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchBinding binding = ItemSearchBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object item = items.get(position);
        
        if (item instanceof Assignment) {
            Assignment assignment = (Assignment) item;
            setupUI(holder, "Assignment", assignment.getTitle(), "Subject: " + assignment.getSubject(), R.drawable.ic_assignment, R.color.card_assignment, AssignmentsActivity.class);
        } else if (item instanceof Lab) {
            Lab lab = (Lab) item;
            setupUI(holder, "Lab", lab.getTitle(), "Subject: " + lab.getSubject(), R.drawable.ic_lab, R.color.card_lab, LabsActivity.class);
        } else if (item instanceof Exam) {
            Exam exam = (Exam) item;
            setupUI(holder, "Exam", exam.getSubject(), "Date: " + exam.getDate(), R.drawable.ic_exam, R.color.card_exam, ExamsActivity.class);
        } else if (item instanceof Announcement) {
            Announcement announcement = (Announcement) item;
            setupUI(holder, "Announcement", announcement.getTitle(), announcement.getContent(), R.drawable.ic_announcement, R.color.card_announcement, AnnouncementsActivity.class);
        } else if (item instanceof StudyMaterial) {
            StudyMaterial material = (StudyMaterial) item;
            setupUI(holder, "Study Material", material.getTitle(), material.getSubject(), R.drawable.ic_material, R.color.card_material, MaterialsActivity.class);
        } else if (item instanceof Event) {
            Event event = (Event) item;
            setupUI(holder, "Event", event.getTitle(), event.getDate() + " at " + event.getVenue(), R.drawable.ic_event, R.color.card_event, EventsActivity.class);
        } else if (item instanceof Seminar) {
            Seminar seminar = (Seminar) item;
            setupUI(holder, "Seminar", seminar.getTopic(), seminar.getDate(), R.drawable.ic_seminar, R.color.card_seminar, SeminarsActivity.class);
        } else if (item instanceof DriveUpdate) {
            DriveUpdate update = (DriveUpdate) item;
            setupUI(holder, "Drive Update", update.getCompanyName(), update.getDescription(), R.drawable.ic_material, R.color.card_drive, DriveUpdatesActivity.class);
        } else if (item instanceof ExamMark) {
            ExamMark mark = (ExamMark) item;
            setupUI(holder, "Exam Mark", mark.getSubject(), "Type: " + mark.getExamType(), R.drawable.ic_exam, R.color.card_marks, ExamMarksActivity.class);
        }

        // Add reveal animation
        holder.itemView.setTranslationY(100f);
        holder.itemView.setAlpha(0f);
        holder.itemView.animate()
                .translationY(0f)
                .alpha(1f)
                .setDuration(400)
                .setStartDelay(position * 30L)
                .start();
    }

    private void setupUI(ViewHolder holder, String type, String title, String subtitle, int icon, int colorRes, Class<?> activityClass) {
        holder.binding.searchItemType.setText(type);
        holder.binding.searchItemTitle.setText(title);
        holder.binding.searchItemSubtitle.setText(subtitle);
        holder.binding.typeIcon.setImageResource(icon);
        
        int color = ContextCompat.getColor(context, colorRes);
        // Set icon container background to a light version of the type color
        holder.binding.typeIconContainer.setCardBackgroundColor(color);
        
        // Find a darker version or use primary for text and icon tint
        int primaryColor = ContextCompat.getColor(context, R.color.purple_primary);
        holder.binding.searchItemType.setTextColor(primaryColor);
        holder.binding.typeIcon.setImageTintList(ColorStateList.valueOf(primaryColor));
        
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, activityClass);
            intent.putExtra("isAdmin", isAdmin);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemSearchBinding binding;

        public ViewHolder(ItemSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
