package com.dpilaloa.upsipteeo.ui.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dpilaloa.upsipteeo.R;

public class UserHolder extends RecyclerView.ViewHolder{

    public TextView cardCed, cardName, cardCanton, cardPhone, cardRol;
    public CardView cardViewUser;
    public ImageView cardPhoto;
    public UserHolder(@NonNull View itemView) {
        super(itemView);

        cardCed = itemView.findViewById(R.id.cardCed);
        cardName = itemView.findViewById(R.id.cardName);
        cardCanton = itemView.findViewById(R.id.cardCanton);
        cardPhone = itemView.findViewById(R.id.cardPhone);
        cardRol = itemView.findViewById(R.id.cardRol);
        cardPhoto = itemView.findViewById(R.id.cardPhoto);
        cardViewUser = itemView.findViewById(R.id.cardViewUser);

    }
}
