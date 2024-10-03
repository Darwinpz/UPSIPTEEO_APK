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

    public static DatabaseReference databaseReference;
    SharedPreferences preferences;
    private AlertDialogController alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        alertDialog = new AlertDialogController(this);
        EditText editTextUser = findViewById(R.id.textViewCed);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button btnLogIn = findViewById(R.id.btn_ingresar);

        UserController userController = new UserController(databaseReference);

        btnLogIn.setOnClickListener(view -> {

            alertDialog.showProgressMessage(getString(R.string.msgLogIn));
            String ced = editTextUser.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (!ced.isEmpty() && !password.isEmpty()) {

                userController.logIn(ced, password, user -> {

                    if (user != null && preferences.getString("uid", "").isEmpty()) {
                        preferences.edit()
                                .putString("uid", user.uid)
                                .putString("rol", user.rol)
                                .apply();
                        alertDialog.showSuccess(getString(R.string.msgWelcome)+" "+ user.name);
                        startActivity(new Intent(getBaseContext(), PrimaryActivity.class));
                        finish();
                    } else if (preferences.getString("uid", "").isEmpty()) {
                        alertDialog.showError(getString(R.string.msgNotLogIn));
                    }

                },databaseError -> alertDialog.showError(getString(R.string.msgErrLogIn)));

            }else{
                alertDialog.showWarning(getString(R.string.msgEmptyFields));
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
