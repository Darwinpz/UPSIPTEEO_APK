package com.dpilaloa.upsipteeo.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.data.models.Alert;
import com.dpilaloa.upsipteeo.ui.holders.AlertHolder;

import java.util.ArrayList;
import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertHolder> {

    public List<Alert> alertList = new ArrayList<>();
    final Context context;

    public AlertAdapter(Context context) {
        this.context = context;
    }

    public void add (Alert alert ){
        alertList.add(alert);
        notifyItemInserted(alertList.size());
    }

    public void clear (){
        alertList.clear();
    }

    @NonNull
    @Override
    public AlertHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_alert,parent,false);
        return new AlertHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertHolder holder, int position) {

        holder.cardName.setText(alertList.get(position).name);
        holder.cardDateTime.setText(TextUtils.concat(alertList.get(position).date + " " +alertList.get(position).time));
        holder.cardSite.setText(alertList.get(position).site);
        holder.cardState.setText(alertList.get(position).state);
        holder.cardType.setText(alertList.get(position).type);
        holder.cardCanton.setText(alertList.get(position).canton);

        holder.cardViewAlert.setOnClickListener(view -> {

        });

    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }
}
