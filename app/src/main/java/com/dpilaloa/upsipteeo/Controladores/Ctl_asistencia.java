package com.dpilaloa.upsipteeo.Controladores;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dpilaloa.upsipteeo.Adaptadores.Adapter_asistencia;
import com.dpilaloa.upsipteeo.Objetos.Ob_asistencia;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Ctl_asistencia {

    DatabaseReference databaseReference;

    public Ctl_asistencia(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public void VerAsistencia(Adapter_asistencia list_asistencia, String uid, final TextView textView, final ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        databaseReference.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list_asistencia.ClearAsistencia();
                int contador = 0;

                if (dataSnapshot.exists()) {

                    if(dataSnapshot.child("asistencia").exists()) {

                        for (DataSnapshot snapshot : dataSnapshot.child("asistencia").getChildren()) {

                            if(snapshot.child("fecha").exists() && snapshot.child("hora").exists()) {

                                Ob_asistencia asistencia = new Ob_asistencia();
                                asistencia.uid = snapshot.getKey();
                                asistencia.fecha = snapshot.child("fecha").getValue(String.class);
                                asistencia.hora = snapshot.child("hora").getValue(String.class);
                                asistencia.url_foto = dataSnapshot.child("foto").getValue(String.class);

                                list_asistencia.AddAsistencia(asistencia);
                                contador++;

                            }
                        }

                    }

                    txt_contador.setText(String.valueOf(contador));
                    txt_contador.append("\tAsistencias");
                    textView.setVisibility(list_asistencia.getItemCount() == 0 ? View.VISIBLE : View.GONE);

                } else {
                    textView.setVisibility(View.VISIBLE);
                }

                list_asistencia.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {throw error.toException();}

        });

    }

    public Task<Void> add_asistencia(String uid_user, Ob_asistencia asistencia) {

        Map<String, Object> datos = new HashMap<>();
        datos.put("fecha",asistencia.fecha);
        datos.put("hora",asistencia.hora);

        return databaseReference.child("usuarios").child(uid_user).child("asistencia").push().setValue(datos);

    }

    public Task<Void> eliminar_asistencia(String uid_usuario, String uid_asistencia){
        return databaseReference.child("usuarios").child(uid_usuario).child("asistencia").child(uid_asistencia).removeValue();
    }


}
