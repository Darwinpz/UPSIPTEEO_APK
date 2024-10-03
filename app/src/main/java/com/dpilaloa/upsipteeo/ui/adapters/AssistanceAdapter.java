package com.dpilaloa.upsipteeo.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dpilaloa.upsipteeo.ui.activities.DetAssistanceActivity;
import com.dpilaloa.upsipteeo.ui.activities.PrimaryActivity;
import com.dpilaloa.upsipteeo.ui.holders.AssistanceHolder;
import com.dpilaloa.upsipteeo.data.models.Assistance;
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
            Glide.with(context).load(assistanceList.get(position).photo).fitCenter().into(holder.cardPhoto);
        }else{
            holder.cardPhoto.setImageResource(R.drawable.ic_person);
        }

        holder.cardViewAssistance.setOnLongClickListener(view -> {
            String UID_USER = DetAssistanceActivity.UID_USER;
            String ROL = PrimaryActivity.rol;
            if(!TextUtils.isEmpty(UID_USER) && ROL.equals(context.getString(R.string.admin_one))) {
                DetAssistanceActivity.alertDialog.showConfirmDialog(context.getString(R.string.msgConfirmDelAssistance),context.getString(R.string.msgNotReversible),
                        context.getString(R.string.msgAccept), context.getString(R.string.msgCancel), (dialogInterface, i) ->
                                DetAssistanceActivity.assistanceController.deleteAssistance(UID_USER, assistanceList.get(position).uid).addOnCompleteListener(task -> {
                                    if (!task.isSuccessful()) {DetAssistanceActivity.alertDialog.showError(context.getString(R.string.msgNotDelAssistance));}
                                })
                , (dialogInterface, i) -> {});
            }
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return assistanceList.size();
    }



}
