package com.dpilaloa.upsipteeo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.dpilaloa.upsipteeo.data.controllers.AlertDialogController;
import com.dpilaloa.upsipteeo.data.controllers.UserController;
import com.dpilaloa.upsipteeo.ui.activities.PrimaryActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static FirebaseDatabase DB = FirebaseDatabase.getInstance();
    public static DatabaseReference databaseReference;
    SharedPreferences preferences;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("upsipteeo",MODE_PRIVATE);
        databaseReference = DB.getReference();
        AlertDialogController alertDialog = new AlertDialogController(this);
        EditText editTextUser = findViewById(R.id.textViewCed);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button btnLogIn = findViewById(R.id.btn_ingresar);

        userController = new UserController(databaseReference);

        btnLogIn.setOnClickListener(view -> {

            alertDialog.showProgressMessage("Iniciando sesión...");
            String ced = editTextUser.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (!ced.isEmpty() && !password.isEmpty()) {

                userController.logIn(ced, password, user -> {

                    if (user != null) {

                        if (preferences.getString("uid", "").isEmpty()) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("uid", user.uid);
                            editor.putString("rol", user.rol);
                            editor.apply();
                            alertDialog.hideProgressMessage();
                            startActivity(new Intent(getBaseContext(), PrimaryActivity.class));
                            finish();
                        }

                    } else {
                        if (preferences.getString("uid", "").isEmpty()) {
                            alertDialog.hideProgressMessage();
                            alertDialog.showMessageDialog("¡Advertencia!", "Usuario y/o Clave Incorrecto", true, (dialogInterface, i) -> {});
                        }
                    }

                },databaseError -> {
                    alertDialog.hideProgressMessage();
                    alertDialog.showMessageDialog("¡Advertencia!", "Ocurrió un error al Iniciar Sesión", true, (dialogInterface, i) -> {});
                });

            }else{
                alertDialog.hideProgressMessage();
                alertDialog.showMessageDialog("¡Advertencia!", "Completa todos los campos", true, (dialogInterface, i) -> {});
            }

        });

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
