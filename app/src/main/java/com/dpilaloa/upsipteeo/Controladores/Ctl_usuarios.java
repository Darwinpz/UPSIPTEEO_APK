package com.dpilaloa.upsipteeo.Controladores;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dpilaloa.upsipteeo.Adaptadores.Adapter_usuario;
import com.dpilaloa.upsipteeo.Interfaces.UserInterface;
import com.dpilaloa.upsipteeo.Objetos.Ob_usuario;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Ctl_usuarios {

    DatabaseReference dbref;

    public Ctl_usuarios(DatabaseReference dbref) {
        this.dbref = dbref;
    }

    public Task<Void> crear_usuarios(Ob_usuario obUsuario){
        return dbref.child("usuarios").push().setValue(obUsuario);
    }

    public Task<Void> eliminar_usuario(String uid){
        return dbref.child("usuarios").child(uid).removeValue();
    }

    public Task<Void> update_usuario(Ob_usuario usuario) {

        Map<String, Object> datos = new HashMap<>();
        datos.put("cedula", usuario.cedula);
        datos.put("nombre", usuario.nombre.toUpperCase());
        datos.put("correo", usuario.correo.toLowerCase());
        datos.put("celular", usuario.celular);
        datos.put("canton", usuario.canton);
        datos.put("rol", usuario.rol);
        datos.put("clave",usuario.clave);

        return dbref.child("usuarios").child(usuario.uid).updateChildren(datos);

    }

    public Task<Void> update_foto(String uid, String url_foto) {

        return dbref.child("usuarios").child(uid).updateChildren(Collections.singletonMap("foto", url_foto));

    }


    public void obtener_datos_perfil(String uid, UserInterface firebase_calldata){

        dbref.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    Ob_usuario user = new Ob_usuario();
                    user.uid = uid;
                    user.cedula = dataSnapshot.child("cedula").getValue(String.class);
                    user.nombre = dataSnapshot.child("nombre").getValue(String.class);
                    user.canton = dataSnapshot.child("canton").getValue(String.class);
                    user.celular = dataSnapshot.child("celular").getValue(String.class);
                    user.rol = dataSnapshot.child("rol").getValue(String.class);
                    user.clave = dataSnapshot.child("clave").getValue(String.class);
                    user.correo = dataSnapshot.child("correo").getValue(String.class);
                    user.url_foto = dataSnapshot.child("foto").getValue(String.class);

                    firebase_calldata.getUser(user);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {throw databaseError.toException();}

        });

    }


    public void VerUsuarios(Adapter_usuario list_usuarios, String uid, String rol, String filtro, TextView textView, ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        dbref.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list_usuarios.ClearUsuario();

                    int contador = 0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Ob_usuario usuario = new Ob_usuario();
                        usuario.uid = snapshot.getKey();
                        usuario.cedula = snapshot.child("cedula").getValue(String.class);
                        usuario.nombre = snapshot.child("nombre").getValue(String.class);
                        usuario.canton = snapshot.child("canton").getValue(String.class);
                        usuario.celular = snapshot.child("celular").getValue(String.class);
                        usuario.rol = snapshot.child("rol").getValue(String.class);
                        usuario.url_foto = snapshot.child("foto").getValue(String.class);

                        if (usuario.cedula!=null && usuario.cedula.trim().contains(filtro.trim().toLowerCase()) ||
                            usuario.nombre!=null && usuario.nombre.toLowerCase().trim().contains(filtro.trim().toLowerCase()) ||
                            usuario.canton!=null && usuario.canton.toLowerCase().trim().contains(filtro.trim().toLowerCase())) {

                            if(usuario.rol!=null && (rol.equals("Rol") || usuario.rol.equals(rol))) {
                                assert usuario.uid != null;
                                if (!usuario.uid.equals(uid)) {
                                    list_usuarios.AddUsuario(usuario);
                                    contador++;
                                }
                            }

                        }

                    }

                    txt_contador.setText(String.valueOf(contador));
                    txt_contador.append("\tUsuarios");
                    textView.setVisibility(list_usuarios.getItemCount() == 0 ? View.VISIBLE : View.GONE);

                } else {
                    list_usuarios.ClearUsuario();
                    textView.setVisibility(View.VISIBLE);
                }

                list_usuarios.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {throw error.toException();}

        });

    }


    public void cerrar_sesion(SharedPreferences preferences) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("uid","");
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

    public boolean validar_usuario(String usuario){
        Pattern patron = Pattern.compile("^[ a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+$");
        return patron.matcher(usuario).matches();
    }

    public boolean validar_celular(String celular){
        Pattern patron = Pattern.compile("^(0|593)?9[0-9]\\d{7}$");
        return patron.matcher(celular).matches();
    }
    
}
