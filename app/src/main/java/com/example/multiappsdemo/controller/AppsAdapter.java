package com.example.multiappsdemo.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.multiappsdemo.model.AppInfo;
import com.example.multiappsdemo.R;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppViewHolder> {
    private List<AppInfo> apps;
    private View.OnClickListener listener;

    public AppsAdapter(List<AppInfo> apps, View.OnClickListener listener) {
        this.apps = apps;
        this.listener = listener;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_card, parent, false);
        return new AppViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        AppInfo app = apps.get(position);
        holder.tvTitle.setText(app.title);
        holder.tvDesc.setText(app.description);
        holder.itemView.setTag(position);

        // --- Animation au clic sur la box ---
        holder.itemView.setOnClickListener(v -> {
            v.setAlpha(0.5f);
            v.postDelayed(() -> v.setAlpha(1f), 120);
            listener.onClick(v);
        });

        // --- Icônes personnaliséees pour chaque app (exemple) ---
        int[] icones = {
                android.R.drawable.ic_lock_idle_alarm,        // à créer dans res/drawable
                R.drawable.ic_calc,
                android.R.drawable.sym_contact_card,
                android.R.drawable.stat_notify_chat,
                R.drawable.ic_timer,
                android.R.drawable.ic_menu_add,
                android.R.drawable.btn_star_big_on,
                android.R.drawable.ic_menu_upload_you_tube,
                R.drawable.ic_weather,
                R.drawable.ic_money
        };
        if (position < icones.length) {
            holder.ivAppIcon.setImageResource(icones[position]);
        } else {
            holder.ivAppIcon.setImageResource(R.drawable.ic_baseline_apps_24);
        }

        // --- CheckBox gère la sélection (option favori) ---
        holder.checkBox.setOnCheckedChangeListener(null); // évite les callbacks indésirables
        holder.checkBox.setChecked(app.isSelected);
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> app.isSelected = isChecked);
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        ImageView ivAppIcon;
        CheckBox checkBox;

        AppViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvAppTitle);
            tvDesc = itemView.findViewById(R.id.tvAppDesc);
            ivAppIcon = itemView.findViewById(R.id.ivAppIcon);
            checkBox = itemView.findViewById(R.id.checkBoxSelect);
        }
    }
}
