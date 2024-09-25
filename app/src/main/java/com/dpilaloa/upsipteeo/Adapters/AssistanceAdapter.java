package com.dpilaloa.upsipteeo.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dpilaloa.upsipteeo.Holders.AssistanceHolder;
import com.dpilaloa.upsipteeo.Objects.Assistance;
import com.dpilaloa.upsipteeo.R;

import java.util.ArrayList;
import java.util.List;

public class AssistanceAdapter extends RecyclerView.Adapter<AssistanceHolder> {

    public List<Assistance> assistanceList = new ArrayList<>();
    final Context context;

    public AssistanceAdapter(Context context) {
        this.context = context;
    }

    public void add(Assistance assistance ){
        assistanceList.add(assistance);
        notifyItemInserted(assistanceList.size());
    }

    public void clear(){
        assistanceList.clear();
    }

    @NonNull
    @Override
    public AssistanceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_assistance,parent,false);
        return new AssistanceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssistanceHolder holder, int position) {

        holder.cardDate.setText(assistanceList.get(position).date);
        holder.cardTime.setText(assistanceList.get(position).time);

        if(!TextUtils.isEmpty(assistanceList.get(position).photo)) {
            Glide.with(context).load(assistanceList.get(position).photo).centerCrop().into(holder.cardPhoto);
        }else{
            Glide.with(context).load(R.drawable.perfil).fitCenter().into(holder.cardPhoto);
        }

    }

    @Override
    public int getItemCount() {
        return assistanceList.size();
    }



}
