package com.dpilaloa.upsipteeo.ui.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.data.controllers.AlertDialogController;
import com.dpilaloa.upsipteeo.data.models.User;
import com.dpilaloa.upsipteeo.ui.adapters.ArraySpinnerAdapter;
import com.dpilaloa.upsipteeo.utils.ValEditTextWatcher;

public class AddUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        EditText editTextCed = findViewById(R.id.textViewCed);
        EditText editTextName = findViewById(R.id.textViewName);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPhone = findViewById(R.id.editTextPhone);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Button btnCreate = findViewById(R.id.buttonSaveUser);

        Spinner spinner_rol = findViewById(R.id.spinnerRol);
        Spinner spinner_canton = findViewById(R.id.spinnerCanton);

        AlertDialogController alertDialog = new AlertDialogController(this);

        toolbar.setOnClickListener(view -> finish());

        ArrayAdapter<CharSequence> adapterSpinnerRol = new ArraySpinnerAdapter(this,android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.rol), PrimaryActivity.rol,1);
        adapterSpinnerRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_rol.setAdapter(adapterSpinnerRol);

        ArrayAdapter<CharSequence> adapterSpinnerCanton = ArrayAdapter.createFromResource(this, R.array.canton, android.R.layout.simple_spinner_item);
        adapterSpinnerCanton.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_canton.setAdapter(adapterSpinnerCanton);

        editTextCed.addTextChangedListener(new ValEditTextWatcher(editTextCed,
                input -> PrimaryActivity.userController.valCed(input),"Ingresa una cédula válida"));

        editTextName.addTextChangedListener(new ValEditTextWatcher(editTextName,
                input -> PrimaryActivity.userController.valUser(input),"Ingresa un nombre válido"));

        editTextEmail.addTextChangedListener(new ValEditTextWatcher(editTextEmail,
                input -> PrimaryActivity.userController.valEmail(input),"Ingresa un correo válido"));

        editTextPhone.addTextChangedListener(new ValEditTextWatcher(editTextPhone,
                input -> PrimaryActivity.userController.valPhone(input),"Ingresa un teléfono válido"));

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

                PrimaryActivity.userController.createUser(user).addOnCompleteListener(task -> {
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