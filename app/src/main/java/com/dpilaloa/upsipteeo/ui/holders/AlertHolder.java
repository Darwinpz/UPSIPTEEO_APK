package com.dpilaloa.upsipteeo.ui.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dpilaloa.upsipteeo.R;

public class AlertHolder extends RecyclerView.ViewHolder{

    public TextView cardType, cardCanton, cardSite, cardName, cardDateTime, cardState;
    public CardView cardViewAlert;

    public AlertHolder(@NonNull View itemView) {
        super(itemView);
        cardType = itemView.findViewById(R.id.cardType);
        cardCanton = itemView.findViewById(R.id.cardCanton);
        cardSite = itemView.findViewById(R.id.cardSite);
        cardName = itemView.findViewById(R.id.cardName);
        cardDateTime = itemView.findViewById(R.id.cardDateTime);
        cardState = itemView.findViewById(R.id.cardState);
        cardViewAlert = itemView.findViewById(R.id.cardViewAlert);
    }
}
