package com.dpilaloa.upsipteeo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dpilaloa.upsipteeo.data.controllers.AlertDialogController;
import com.dpilaloa.upsipteeo.ui.activities.PrimaryActivity;
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
    AlertDialogController alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("upsipteeo",MODE_PRIVATE);
        databaseReference = DB.getReference();
        alertDialog = new AlertDialogController(this);
        EditText editTextUser = findViewById(R.id.textViewCed);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button btnLogIn = findViewById(R.id.btn_ingresar);

        btnLogIn.setOnClickListener(view -> Login(editTextUser.getText().toString(),editTextPassword.getText().toString()));

    }

    public void Login(String username, String password){

        alertDialog.showProgressMessage("Iniciando sesión...");

        if (!username.isEmpty() && !password.isEmpty()) {
            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                    if (datasnapshot.exists()) {
                        boolean existe = false;
                        for (DataSnapshot snapshot : datasnapshot.getChildren()) {

                            if(snapshot.child("ced").exists() && snapshot.child("password").exists() && snapshot.child("rol").exists() ) {
                                if (Objects.requireNonNull(snapshot.child("ced").getValue()).toString().equals(username) &&
                                        Objects.requireNonNull(snapshot.child("password").getValue()).toString().equals(password)) {

                                    existe = true;

                                    if (preferences.getString("uid", "").isEmpty()) {

                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("uid", snapshot.getKey());
                                        editor.putString("rol", Objects.requireNonNull(snapshot.child("rol").getValue()).toString());
                                        editor.apply();
                                        alertDialog.hideProgressMessage();
                                        startActivity(new Intent(getBaseContext(), PrimaryActivity.class));
                                        finish();

                                    }
                                    break;
                                }
                            }
                        }

                        if(!existe) {
                            if (preferences.getString("uid", "").isEmpty()) {
                                alertDialog.hideProgressMessage();
                                alertDialog.showMessageDialog("¡Advertencia!", "Usuario y/o Clave Incorrecto", true, (dialogInterface, i) -> {});
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    alertDialog.hideProgressMessage();
                    alertDialog.showMessageDialog("¡Advertencia!", "Error al Iniciar Sesión", true, (dialogInterface, i) -> {});
                }
            });

        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        if (!preferences.getString("uid","").isEmpty()) {

            startActivity(new Intent(this, PrimaryActivity.class));
            finish();

        }

    }


}
