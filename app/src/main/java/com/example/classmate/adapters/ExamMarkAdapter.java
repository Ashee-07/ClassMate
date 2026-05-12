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
import androidx.recyclerview.widget.RecyclerView;
import com.example.classmate.databinding.ItemExamMarkBinding;
import com.example.classmate.models.ExamMark;
import java.util.List;

public class ExamMarkAdapter extends RecyclerView.Adapter<ExamMarkAdapter.ViewHolder> {

    private final Context context;
    private final List<ExamMark> marks;
    private final boolean isAdmin;
    private final OnDeleteClickListener deleteClickListener;
    private int lastPosition = -1;

    public interface OnDeleteClickListener {
        void onDeleteClick(ExamMark mark);
    }

    public ExamMarkAdapter(Context context, List<ExamMark> marks, boolean isAdmin, OnDeleteClickListener deleteClickListener) {
        this.context = context;
        this.marks = marks;
        this.isAdmin = isAdmin;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExamMarkBinding binding = ItemExamMarkBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExamMark mark = marks.get(position);
        holder.binding.subjectTV.setText(mark.getSubject());
        holder.binding.examTypeTV.setText(mark.getExamType() != null ? mark.getExamType().toUpperCase() : "EXAM");

        if (isAdmin) {
            holder.binding.deleteBtn.setVisibility(View.VISIBLE);
            holder.binding.deleteBtn.setOnClickListener(v -> deleteClickListener.onDeleteClick(mark));
        } else {
            holder.binding.deleteBtn.setVisibility(View.GONE);
        }

        if (mark.getResultUrl() != null && !mark.getResultUrl().isEmpty()) {
            holder.binding.viewBtn.setVisibility(View.VISIBLE);
            holder.binding.viewBtn.setOnClickListener(v -> openLink(mark.getResultUrl()));
        } else {
            holder.binding.viewBtn.setVisibility(View.GONE);
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

    private void openLink(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Cannot open link", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return marks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemExamMarkBinding binding;
        public ViewHolder(ItemExamMarkBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
