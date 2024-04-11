package com.dpilaloa.upsipteeo.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dpilaloa.upsipteeo.R;

public class Holder_usuario extends RecyclerView.ViewHolder{

    public TextView card_cedula,card_nombre, card_canton, card_telefono, card_rol;
    public CardView cardView;
    public ImageView foto;
    public Holder_usuario(@NonNull View itemView) {
        super(itemView);

        card_cedula = (TextView) itemView.findViewById(R.id.card_cedula);
        card_nombre = (TextView) itemView.findViewById(R.id.card_nombre);
        card_canton = (TextView) itemView.findViewById(R.id.card_canton);
        card_telefono = (TextView) itemView.findViewById(R.id.card_celular);
        card_rol = (TextView) itemView.findViewById(R.id.card_rol);
        foto = (ImageView) itemView.findViewById(R.id.card_foto);
        cardView = (CardView) itemView.findViewById(R.id.cardview_usuario);

    }
}
