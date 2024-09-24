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
import java.util.Objects;

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

                if (dataSnapshot.exists()) {

                    list_asistencia.ClearAsistencia();
                    int contador = 0;

                    if(dataSnapshot.child("asistencia").exists()) {

                        for (DataSnapshot snapshot : dataSnapshot.child("asistencia").getChildren()) {

                            if(snapshot.child("fecha").exists() && snapshot.child("hora").exists()) {

                                Ob_asistencia asistencia = new Ob_asistencia();
                                asistencia.uid = snapshot.getKey();

                                if (snapshot.child("fecha").exists()) {
                                    asistencia.fecha = Objects.requireNonNull(snapshot.child("fecha").getValue()).toString();
                                }
                                if (snapshot.child("hora").exists()) {
                                    asistencia.hora = Objects.requireNonNull(snapshot.child("hora").getValue()).toString();
                                }
                                if (dataSnapshot.child("foto").exists()) {
                                    asistencia.url_foto = Objects.requireNonNull(dataSnapshot.child("foto").getValue()).toString();
                                }

                                list_asistencia.AddAsistencia(asistencia);
                                contador++;

                            }
                        }

                    }

                    txt_contador.setText(contador + " Asistencias");
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(list_asistencia.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                    list_asistencia.notifyDataSetChanged();

                } else {
                    list_asistencia.ClearAsistencia();
                    list_asistencia.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);

                }

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

    public void eliminar_asistencia(String uid_usuario, String uid_asistencia){

        if(uid_usuario != null && !uid_usuario.isEmpty() && uid_asistencia != null && !uid_asistencia.isEmpty()) {
            databaseReference.child("usuarios").child(uid_usuario).child("asistencia").child(uid_asistencia).removeValue();
        }
    }


}
