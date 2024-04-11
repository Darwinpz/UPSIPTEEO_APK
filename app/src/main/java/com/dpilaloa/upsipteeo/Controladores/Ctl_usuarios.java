package com.dpilaloa.upsipteeo.Controladores;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

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

    public void obtener_datos_perfil(String uid, final Interfaces.Firebase_calluser firebase_calldata){

        dbref.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    Ob_usuario user = new Ob_usuario();
                    user.uid = uid;

                    if(dataSnapshot.child("nombre").exists()){
                        user.nombre = Objects.requireNonNull(dataSnapshot.child("nombre").getValue()).toString();
                    }
                    if(dataSnapshot.child("cedula").exists()){
                        user.cedula = Objects.requireNonNull(dataSnapshot.child("cedula").getValue()).toString();
                    }
                    if(dataSnapshot.child("rol").exists()){
                        user.rol = Objects.requireNonNull(dataSnapshot.child("rol").getValue()).toString();
                    }
                    if(dataSnapshot.child("correo").exists()){
                        user.correo = Objects.requireNonNull(dataSnapshot.child("correo").getValue()).toString();
                    }
                    if(dataSnapshot.child("celular").exists()){
                        user.celular = Objects.requireNonNull(dataSnapshot.child("celular").getValue()).toString();
                    }
                    if(dataSnapshot.child("canton").exists()){
                        user.canton = Objects.requireNonNull(dataSnapshot.child("canton").getValue()).toString();
                    }
                    if(dataSnapshot.child("foto").exists()){
                        user.url_foto = Objects.requireNonNull(dataSnapshot.child("foto").getValue()).toString();
                    }

                    firebase_calldata.datos_usuario(user);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void cerrar_sesion(SharedPreferences preferences) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user","");
        editor.putString("rol","");
        editor.apply();

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
