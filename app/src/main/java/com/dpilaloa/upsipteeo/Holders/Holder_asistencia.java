package com.dpilaloa.upsipteeo.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dpilaloa.upsipteeo.R;


public class Holder_asistencia extends RecyclerView.ViewHolder{

    public TextView card_fecha, card_hora;
    public CardView cardView;
    public ImageView foto;

    public Holder_asistencia(@NonNull View itemView) {
        super(itemView);
        card_fecha = (TextView) itemView.findViewById(R.id.card_fecha);
        card_hora = (TextView) itemView.findViewById(R.id.card_hora);
        foto = (ImageView) itemView.findViewById(R.id.card_foto);
        cardView = (CardView) itemView.findViewById(R.id.cardview_asistencia);
    }

}
