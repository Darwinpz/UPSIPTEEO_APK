package com.dpilaloa.upsipteeo.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dpilaloa.upsipteeo.R;

public class AssistanceHolder extends RecyclerView.ViewHolder{

    public TextView cardDate, cardTime;
    public CardView cardViewAssistance;
    public ImageView cardPhoto;

    public AssistanceHolder(@NonNull View itemView) {
        super(itemView);
        cardDate = itemView.findViewById(R.id.card_fecha);
        cardTime = itemView.findViewById(R.id.card_hora);
        cardPhoto = itemView.findViewById(R.id.card_foto);
        cardViewAssistance = itemView.findViewById(R.id.cardview_asistencia);
    }

}
