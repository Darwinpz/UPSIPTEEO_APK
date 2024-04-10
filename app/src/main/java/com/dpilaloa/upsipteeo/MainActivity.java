package com.dpilaloa.upsipteeo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dpilaloa.upsipteeo.Controladores.Alert_dialog;
import com.dpilaloa.upsipteeo.Controladores.Progress_dialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
            //Login(txt_usuario.getText().toString(),txt_clave.getText().toString());
        });

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
