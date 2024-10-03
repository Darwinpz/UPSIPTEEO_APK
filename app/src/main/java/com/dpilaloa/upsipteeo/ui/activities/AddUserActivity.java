package com.dpilaloa.upsipteeo.ui.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
                    !editTextPassword.getText().toString().trim().isEmpty() &&
                    !spinner_canton.getSelectedItem().toString().equals("Cantones") &&
                    !spinner_rol.getSelectedItem().toString().equals("Rol")) {

                User user = new User();
                user.ced = editTextCed.getText().toString().trim();
                user.name = editTextName.getText().toString().trim().toUpperCase();
                user.email = editTextEmail.getText().toString().trim().toLowerCase();
                user.phone = editTextPhone.getText().toString().trim();
                user.canton = spinner_canton.getSelectedItem().toString().trim();
                user.rol = spinner_rol.getSelectedItem().toString().trim();
                user.password = editTextPassword.getText().toString().trim();

                PrimaryActivity.userController.existCed(user.ced, val -> {
                    if(!val){
                        PrimaryActivity.userController.createUser(user).addOnCompleteListener(task -> {
                            alertDialog.hideProgressMessage();
                            if(task.isSuccessful()){
                                alertDialog.showMessageDialog("Correcto", "Usuario Creado Correctamente", false, (dialogInterface, i) -> finish());
                            }else{
                                alertDialog.showMessageDialog("¡Advertencia!", "Error al crear el Usuario", true, (dialogInterface, i) -> {});
                            }
                        });
                    }else{
                        alertDialog.hideProgressMessage();
                        alertDialog.showMessageDialog("¡Advertencia!", "Cédula existente", true, (dialogInterface, i) -> {});
                    }
                },databaseError -> {
                    alertDialog.hideProgressMessage();
                    Toast.makeText(this, "Error al buscar la cédula", Toast.LENGTH_LONG).show();
                });

            }else{
                alertDialog.hideProgressMessage();
                alertDialog.showMessageDialog("¡Advertencia!", "Completa todos los campos", true, (dialogInterface, i) -> {});
            }
        });

    }

}