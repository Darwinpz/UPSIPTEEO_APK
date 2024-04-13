package com.dpilaloa.upsipteeo.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dpilaloa.upsipteeo.Holders.Holder_asistencia;
import com.dpilaloa.upsipteeo.Objetos.Ob_asistencia;
import com.dpilaloa.upsipteeo.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_asistencia extends RecyclerView.Adapter<Holder_asistencia> {

    public List<Ob_asistencia> list_asistencia = new ArrayList<>();
    final Context context;

    public Adapter_asistencia(Context context) {
        this.context = context;
    }

    public void AddAsistencia (Ob_asistencia asistencia ){
        list_asistencia.add(asistencia);
        notifyItemInserted(list_asistencia.size());
    }

    public void ClearAsistencia (){
        list_asistencia.clear();
    }

    @NonNull
    @Override
    public Holder_asistencia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_asistencia,parent,false);
        return new Holder_asistencia(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_asistencia holder, int position) {

        holder.card_fecha.setText(list_asistencia.get(position).fecha);
        holder.card_hora.setText(list_asistencia.get(position).hora);

    }

    @Override
    public int getItemCount() {
        return list_asistencia.size();
    }



}
