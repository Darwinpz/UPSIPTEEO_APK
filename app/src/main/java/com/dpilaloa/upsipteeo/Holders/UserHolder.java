package com.dpilaloa.upsipteeo.Holders;

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

        cardCed = itemView.findViewById(R.id.card_cedula);
        cardName = itemView.findViewById(R.id.card_nombre);
        cardCanton = itemView.findViewById(R.id.card_canton);
        cardPhone = itemView.findViewById(R.id.card_celular);
        cardRol = itemView.findViewById(R.id.card_rol);
        cardPhoto = itemView.findViewById(R.id.card_foto);
        cardViewUser = itemView.findViewById(R.id.cardview_usuario);

    }
}
