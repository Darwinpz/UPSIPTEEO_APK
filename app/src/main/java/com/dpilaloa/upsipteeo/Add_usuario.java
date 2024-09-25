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

public class Add_usuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_usuario);

        EditText txt_cedula = findViewById(R.id.txt_cedula);
        EditText txt_nombre = findViewById(R.id.txt_nombre);
        EditText txt_correo = findViewById(R.id.txt_correo);
        EditText txt_telefono = findViewById(R.id.txt_telefono);
        EditText txt_clave = findViewById(R.id.txt_clave);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Button btn_crear = findViewById(R.id.btn_crear);

        Spinner spinner_rol = findViewById(R.id.spinner_rol);
        Spinner spinner_canton = findViewById(R.id.spinner_canton);

        AlertDialogController alertDialog = new AlertDialogController(this);

        toolbar.setOnClickListener(view -> finish());

        ArrayAdapter<CharSequence> adapterspinner_rol = ArrayAdapter.createFromResource(this, R.array.rol, android.R.layout.simple_spinner_item);
        adapterspinner_rol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_rol.setAdapter(adapterspinner_rol);

        ArrayAdapter<CharSequence> adapterspinner_canton = ArrayAdapter.createFromResource(this, R.array.cantones, android.R.layout.simple_spinner_item);
        adapterspinner_canton.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_canton.setAdapter(adapterspinner_canton);

        txt_cedula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String cedula = editable.toString().trim();
                if (cedula.length() != 10) {
                    txt_cedula.setError("Ingresa 10 dígitos");
                } else if (!Principal.ctlUsuarios.valCed(cedula)) {
                    txt_cedula.setError("Cédula Incorrecta");
                }

            }
        });

        txt_nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if(!Principal.ctlUsuarios.valUser(editable.toString().trim())){
                    txt_nombre.setError("Ingresa un nombre válido");
                }
            }
        });

        txt_correo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                if(!Principal.ctlUsuarios.valEmail(editable.toString().trim())){
                    txt_correo.setError("Ingresa un correo válido");
                }
            }
        });

        txt_telefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String telefono = editable.toString().trim();
                if (telefono.length() != 10) {
                    txt_telefono.setError("Ingresa 10 dígitos");
                } else if (!Principal.ctlUsuarios.valPhone(telefono)) {
                    txt_telefono.setError("Ingresa un celular válido");
                }
            }
        });

        btn_crear.setOnClickListener(view -> {
            alertDialog.showProgressMessage("Creando...");
            if(!txt_cedula.getText().toString().trim().isEmpty() && txt_cedula.getError() == null &&
                    !txt_nombre.getText().toString().trim().isEmpty() && txt_nombre.getError() == null &&
                    !txt_correo.getText().toString().trim().isEmpty() && txt_correo.getError() == null &&
                    !txt_telefono.getText().toString().trim().isEmpty() && txt_telefono.getError() == null &&
                    !spinner_canton.getSelectedItem().toString().equals("Cantones") &&
                    !spinner_rol.getSelectedItem().toString().equals("Rol")) {

                User user = new User();
                user.ced = txt_cedula.getText().toString();
                user.name = txt_nombre.getText().toString().toUpperCase();
                user.email = txt_correo.getText().toString().toLowerCase();
                user.phone = txt_telefono.getText().toString();
                user.canton = spinner_canton.getSelectedItem().toString();
                user.rol = spinner_rol.getSelectedItem().toString();
                user.password = txt_clave.getText().toString();

                Principal.ctlUsuarios.createUser(user).addOnCompleteListener(task -> {
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