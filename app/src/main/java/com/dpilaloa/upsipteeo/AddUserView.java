package com.dpilaloa.upsipteeo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dpilaloa.upsipteeo.Controllers.AlertDialogController;
import com.dpilaloa.upsipteeo.Objects.User;

public class AddUserView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        EditText editTextCed = findViewById(R.id.txt_cedula);
        EditText editTextName = findViewById(R.id.txt_nombre);
        EditText editTextEmail = findViewById(R.id.txt_correo);
        EditText editTextPhone = findViewById(R.id.txt_telefono);
        EditText editTextPassword = findViewById(R.id.txt_clave);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Button btnCreate = findViewById(R.id.btn_crear);

        Spinner spinner_rol = findViewById(R.id.spinner_rol);
        Spinner spinner_canton = findViewById(R.id.spinner_canton);

        AlertDialogController alertDialog = new AlertDialogController(this);

        toolbar.setOnClickListener(view -> finish());

        ArrayAdapter<CharSequence> adapterSpinnerRol = ArrayAdapter.createFromResource(this, R.array.rol, android.R.layout.simple_spinner_item);
        adapterSpinnerRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_rol.setAdapter(adapterSpinnerRol);

        ArrayAdapter<CharSequence> adapterSpinnerCanton = ArrayAdapter.createFromResource(this, R.array.canton, android.R.layout.simple_spinner_item);
        adapterSpinnerCanton.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_canton.setAdapter(adapterSpinnerCanton);

        editTextCed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String ced = editable.toString().trim();
                if (ced.length() != 10) {
                    editTextCed.setError("Ingresa 10 dígitos");
                } else if (!PrimaryView.userController.valCed(ced)) {
                    editTextCed.setError("Cédula Incorrecta");
                }

            }
        });

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if(!PrimaryView.userController.valUser(editable.toString().trim())){
                    editTextName.setError("Ingresa un nombre válido");
                }
            }
        });

        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if(!PrimaryView.userController.valEmail(editable.toString().trim())){
                    editTextEmail.setError("Ingresa un correo válido");
                }
            }
        });

        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String phone = editable.toString().trim();
                if (phone.length() != 10) {
                    editTextPhone.setError("Ingresa 10 dígitos");
                } else if (!PrimaryView.userController.valPhone(phone)) {
                    editTextPhone.setError("Ingresa un celular válido");
                }
            }
        });

        btnCreate.setOnClickListener(view -> {
            alertDialog.showProgressMessage("Creando...");
            if(!editTextCed.getText().toString().trim().isEmpty() && editTextCed.getError() == null &&
                    !editTextName.getText().toString().trim().isEmpty() && editTextName.getError() == null &&
                    !editTextEmail.getText().toString().trim().isEmpty() && editTextEmail.getError() == null &&
                    !editTextPhone.getText().toString().trim().isEmpty() && editTextPhone.getError() == null &&
                    !spinner_canton.getSelectedItem().toString().equals("Cantones") &&
                    !spinner_rol.getSelectedItem().toString().equals("Rol")) {

                User user = new User();
                user.ced = editTextCed.getText().toString();
                user.name = editTextName.getText().toString().toUpperCase();
                user.email = editTextEmail.getText().toString().toLowerCase();
                user.phone = editTextPhone.getText().toString();
                user.canton = spinner_canton.getSelectedItem().toString();
                user.rol = spinner_rol.getSelectedItem().toString();
                user.password = editTextPassword.getText().toString();

                PrimaryView.userController.createUser(user).addOnCompleteListener(task -> {
                    alertDialog.hideProgressMessage();
                    if(task.isSuccessful()){
                        alertDialog.createMessage("Correcto", "Usuario Creado Correctamente", builder -> {
                            builder.setCancelable(false);
                            builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                            builder.create().show();
                        });
                    }else{
                        alertDialog.createMessage("¡Advertencia!", "Error al crear el Usuario", builder -> {
                            builder.setCancelable(true);
                            builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                            builder.create().show();
                        });
                    }

                });

            }else{
                alertDialog.hideProgressMessage();
                alertDialog.createMessage("¡Advertencia!", "Completa todos los campos", builder -> {
                    builder.setCancelable(true);
                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                    builder.create().show();
                });
            }
        });

    }
}