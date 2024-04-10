package com.dpilaloa.upsipteeo.Controladores;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dpilaloa.upsipteeo.Objetos.Ob_usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class Ctl_usuarios {

    DatabaseReference dbref;

    public Ctl_usuarios(DatabaseReference dbref) {
        this.dbref = dbref;
    }

    public void crear_usuarios(Ob_usuario obUsuario){
        dbref.child("usuarios").push().setValue(obUsuario);
    }

    public void eliminar_usuario(String uid){
        dbref.child("usuarios").child(uid).removeValue();
    }

    public void update_usuario(Ob_usuario usuario) {

        if(usuario.uid != null) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("correo", usuario.correo.toLowerCase());
            datos.put("celular", usuario.celular);
            datos.put("canton", usuario.canton);

            dbref.child("usuarios").child(usuario.uid).updateChildren(datos);
        }

    }


    public void VerUsuarios(Adapter_usuario list_usuario, String estado , String uid, final TextView textView, final ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        dbref.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list_usuario.ClearUsuario();
                    int contador = 0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        if(!Objects.equals(snapshot.getKey(), uid)) {

                            Ob_usuario usuario = new Ob_usuario();
                            usuario.uid = snapshot.getKey();

                            if (snapshot.child("cedula").exists()) {
                                usuario.cedula = Objects.requireNonNull(snapshot.child("cedula").getValue()).toString();
                            }
                            if (snapshot.child("estado").exists()) {
                                usuario.estado = Objects.requireNonNull(snapshot.child("estado").getValue()).toString();
                            }
                            if (snapshot.child("nombre").exists()) {
                                usuario.nombre = Objects.requireNonNull(snapshot.child("nombre").getValue()).toString();
                            }
                            if (snapshot.child("url_foto").exists()) {
                                usuario.url_foto = Objects.requireNonNull(snapshot.child("url_foto").getValue()).toString();
                            }
                            if (snapshot.child("email").exists()) {
                                usuario.email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                            }
                            if (snapshot.child("telefono").exists()) {
                                usuario.telefono = Objects.requireNonNull(snapshot.child("telefono").getValue()).toString();
                            }
                            if (snapshot.child("rol").exists()) {
                                usuario.rol = Objects.requireNonNull(snapshot.child("rol").getValue()).toString();
                            }

                            if (estado.isEmpty() || usuario.estado.equalsIgnoreCase(estado)) {
                                list_usuario.AddUsuario(usuario);
                                contador++;
                            }

                        }

                    }

                    txt_contador.setText(contador + " Usuarios");
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(list_usuario.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                    list_usuario.notifyDataSetChanged();

                } else {
                    list_usuario.ClearUsuario();
                    list_usuario.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }


    public boolean Validar_Cedula(String cedula){

        int suma = 0;

        for (int i = 0; i < 9; i++)
        {
            int coeficiente = ((i % 2) == 0) ? 2 : 1;
            int calculo = Integer.parseInt(String.valueOf(cedula.charAt(i))) * coeficiente;
            suma += (calculo >= 10) ? calculo - 9 : calculo;
        }

        int residuo = suma % 10;
        int valor = (residuo == 0) ? 0 : (10 - residuo);

        return Integer.parseInt(String.valueOf(cedula.charAt(9))) == valor;

    }

    public boolean validar_correo(String correo){

        Pattern patron = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.([a-zA-Z]{2,4})+$");

        return patron.matcher(correo).matches();

    }

    public boolean validar_celular(String celular){

        Pattern patron = Pattern.compile("^(0|593)?9[0-9]\\d{7}$");

        return patron.matcher(celular).matches();

    }
    
}
