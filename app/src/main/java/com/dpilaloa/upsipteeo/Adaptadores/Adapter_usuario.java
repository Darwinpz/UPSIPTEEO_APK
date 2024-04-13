package com.dpilaloa.upsipteeo.Adaptadores;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dpilaloa.upsipteeo.Det_usuario;
import com.dpilaloa.upsipteeo.Holders.Holder_usuario;
import com.dpilaloa.upsipteeo.Objetos.Ob_usuario;
import com.dpilaloa.upsipteeo.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_usuario extends RecyclerView.Adapter<Holder_usuario> {

    public List<Ob_usuario> list_usuario = new ArrayList<>();
    final Context context;

    public Adapter_usuario(Context context) {
        this.context = context;
    }

    public void AddUsuario (Ob_usuario usuario ){
        list_usuario.add(usuario);
        notifyItemInserted(list_usuario.size());
    }

    public void ClearUsuario (){
        list_usuario.clear();
    }

    @NonNull
    @Override
    public Holder_usuario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_usuario,parent,false);
        return new Holder_usuario(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_usuario holder, int position) {

        String ced = " C.I: " + list_usuario.get(position).cedula;
        holder.card_cedula.setText(ced);
        holder.card_nombre.setText(list_usuario.get(position).nombre);
        holder.card_canton.setText(list_usuario.get(position).canton);
        holder.card_telefono.setText(list_usuario.get(position).celular);
        holder.card_rol.setText(list_usuario.get(position).rol);
        Glide.with(context).load(list_usuario.get(position).url_foto).centerCrop().into(holder.foto);

        holder.cardView.setOnClickListener(view -> {

            Intent i = new Intent(context, Det_usuario.class).putExtra("uid",list_usuario.get(position).uid);
            context.startActivity(i);

        });

        holder.cardView.setOnLongClickListener(view -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            String text = list_usuario.get(position).celular;
            ClipData clip = ClipData.newPlainText("celular",  text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context,"Celular copiado al Portapapeles",Toast.LENGTH_SHORT).show();
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return list_usuario.size();
    }
}
