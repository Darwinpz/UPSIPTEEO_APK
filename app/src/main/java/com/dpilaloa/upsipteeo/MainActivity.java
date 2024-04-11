package com.dpilaloa.upsipteeo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dpilaloa.upsipteeo.Controladores.Alert_dialog;
import com.dpilaloa.upsipteeo.Controladores.Progress_dialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static FirebaseDatabase DB = FirebaseDatabase.getInstance();
    public static DatabaseReference databaseReference;
    SharedPreferences preferences;
    Progress_dialog dialog;
    Alert_dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("upsipteeo",MODE_PRIVATE);
        databaseReference = DB.getReference();
        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);
        EditText txt_usuario = findViewById(R.id.txt_cedula);
        EditText txt_clave = findViewById(R.id.txt_clave);
        Button btn_ingreso = findViewById(R.id.btn_ingresar);

        btn_ingreso.setOnClickListener(view -> {
            Login(txt_usuario.getText().toString(),txt_clave.getText().toString());
        });

    }

    public void Login(String usuario, String clave){

        dialog.mostrar_mensaje("Iniciando sesión...");

        if (!usuario.isEmpty() && !clave.isEmpty()) {
            databaseReference.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                    if (datasnapshot.exists()) {
                        boolean existe = false;
                        for (DataSnapshot snapshot : datasnapshot.getChildren()) {

                            if(snapshot.child("cedula").exists() && snapshot.child("clave").exists() && snapshot.child("rol").exists() ) {
                                if (Objects.requireNonNull(snapshot.child("cedula").getValue()).toString().equals(usuario) &&
                                        Objects.requireNonNull(snapshot.child("clave").getValue()).toString().equals(clave)) {

                                    existe = true;

                                    if (preferences.getString("uid", "").isEmpty()) {

                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("uid", snapshot.getKey());
                                        editor.putString("rol", Objects.requireNonNull(snapshot.child("rol").getValue()).toString());
                                        editor.apply();
                                        dialog.ocultar_mensaje();
                                        startActivity(new Intent(getBaseContext(), Principal.class));
                                        finish();

                                    }
                                    break;
                                }
                            }
                        }

                        if(!existe) {
                            if (preferences.getString("uid", "").isEmpty()) {
                                dialog.ocultar_mensaje();
                                alertDialog.crear_mensaje("Advertencia", "Usuario y/o Clave Incorrecto", builder -> {
                                    builder.setCancelable(true);
                                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {
                                    });
                                    builder.create().show();
                                });
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("Advertencia", "Error al Iniciar Sesión",builder -> {
                        builder.setCancelable(true);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }
            });

        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        if (!preferences.getString("uid","").isEmpty()) {

            startActivity(new Intent(this, Principal.class));
            finish();

        }

    }


}
