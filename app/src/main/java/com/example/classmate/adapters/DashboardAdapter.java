package com.example.classmate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classmate.R;
import com.example.classmate.databinding.ItemDashboardBinding;
import com.example.classmate.models.DashboardItem;
import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private final Context context;
    private final List<DashboardItem> items;
    private final OnItemClickListener listener;
    private int lastPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(DashboardItem item);
    }

    public DashboardAdapter(Context context, List<DashboardItem> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDashboardBinding binding = ItemDashboardBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DashboardItem item = items.get(position);
        holder.binding.itemTitle.setText(item.getTitle());
        holder.binding.itemIcon.setImageResource(item.getIconRes());
        
        int color = ContextCompat.getColor(context, item.getBgColor());
        holder.binding.iconContainer.setCardBackgroundColor(color);
        
        holder.itemView.setOnClickListener(v -> {
            // Scale animation on click
            v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction(() -> {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start();
                listener.onItemClick(item);
            }).start();
        });
        
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(android.view.View viewToAnimate, int position) {
        if (position > lastPosition) {
            viewToAnimate.setTranslationY(200f);
            viewToAnimate.setAlpha(0f);
            viewToAnimate.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setInterpolator(new android.view.animation.DecelerateInterpolator(2f))
                    .setDuration(500)
                    .setStartDelay(position * 50L)
                    .start();
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemDashboardBinding binding;

        public ViewHolder(ItemDashboardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
