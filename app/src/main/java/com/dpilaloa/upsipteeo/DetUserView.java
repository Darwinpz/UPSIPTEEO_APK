package com.dpilaloa.upsipteeo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.dpilaloa.upsipteeo.Controllers.AlertDialogController;
import com.dpilaloa.upsipteeo.Objects.User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class DetUserView extends AppCompatActivity {

    String UID_USER = "", USERNAME = "", URL_PHOTO = "" , ROL_USER = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_user);

        EditText editTextCed = findViewById(R.id.textViewCed);
        EditText editTextName = findViewById(R.id.textViewName);
        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPhone = findViewById(R.id.editTextPhone);
        TextView textViewPassword = findViewById(R.id.labelPassword);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        TextInputLayout textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        android.widget.ImageView img_perfil = findViewById(R.id.imageViewProfile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Button btnUpdate = findViewById(R.id.buttonUpdate);
        Button btnDelete = findViewById(R.id.btn_eliminar);

        UID_USER = Objects.requireNonNull(getIntent().getExtras()).getString("uid","");
        ROL_USER = Objects.requireNonNull(getIntent().getExtras()).getString("rol","");

        Spinner spinner_rol = findViewById(R.id.spinnerRol);
        Spinner spinner_canton = findViewById(R.id.spinnerCanton);
        ImageButton imageButton = findViewById(R.id.btn_ver_asistencia);

        AlertDialogController alertDialog = new AlertDialogController(this);

        toolbar.setOnClickListener(view -> finish());

        ArrayAdapter<CharSequence> adapterSpinnerRol = ArrayAdapter.createFromResource(this, R.array.rol, android.R.layout.simple_spinner_item);
        adapterSpinnerRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_rol.setAdapter(adapterSpinnerRol);

        ArrayAdapter<CharSequence> adapterSpinnerCanton = ArrayAdapter.createFromResource(this, R.array.canton, android.R.layout.simple_spinner_item);
        adapterSpinnerCanton.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_canton.setAdapter(adapterSpinnerCanton);

        if(!UID_USER.isEmpty()){

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

            PrimaryView.userController.getProfile(UID_USER, user -> {

                if(user!=null){

                    editTextCed.setText(user.ced);
                    editTextName.setText(user.name);
                    editTextEmail.setText(user.email);
                    editTextPhone.setText(user.phone);
                    editTextPassword.setText(user.password);

                    USERNAME = user.name;

                    int spinnerPosition_rol = adapterSpinnerRol.getPosition(user.rol);
                    spinner_rol.setSelection(spinnerPosition_rol);

                    int spinnerPosition_canton = adapterSpinnerCanton.getPosition(user.canton);
                    spinner_canton.setSelection(spinnerPosition_canton);

                    if (!TextUtils.isEmpty(user.photo)) {
                        Glide.with(getApplicationContext()).load(user.photo).centerCrop().into(img_perfil);
                        URL_PHOTO = user.photo;

                    }

                }

            });

            boolean isAdminOneDiffUid = PrimaryView.rol.equals(getString(R.string.admin_one)) && !getString(R.string.admin_uid).equals(UID_USER);

            btnDelete.setVisibility( isAdminOneDiffUid? View.VISIBLE : View.GONE);
            btnUpdate.setVisibility(
                    (isAdminOneDiffUid) ||
                            (PrimaryView.rol.equals(getString(R.string.admin_two)) &&
                                    !getString(R.string.admin_uid).equals(UID_USER) &&
                                    !ROL_USER.equals(getString(R.string.admin_one)))
                            ? View.VISIBLE
                            : View.GONE
            );

            textViewPassword.setVisibility(isAdminOneDiffUid? View.VISIBLE : View.GONE);
            textInputLayoutPassword.setVisibility(isAdminOneDiffUid? View.VISIBLE : View.GONE);

            img_perfil.setOnClickListener(view1 -> {

                if(!TextUtils.isEmpty(URL_PHOTO)) {
                    startActivity(new Intent(this, ImageView.class).putExtra("url", URL_PHOTO));
                }else{
                    Toast.makeText(this,"Sin foto de perfil", Toast.LENGTH_SHORT).show();
                }

            });

            btnUpdate.setOnClickListener(view1 -> {
                alertDialog.showProgressMessage("Actualizando...");
                if(!editTextCed.getText().toString().trim().isEmpty() && editTextCed.getError() == null &&
                        !editTextName.getText().toString().trim().isEmpty() && editTextName.getError() == null &&
                        !editTextEmail.getText().toString().trim().isEmpty() && editTextEmail.getError() == null &&
                        !editTextPhone.getText().toString().trim().isEmpty() && editTextPhone.getError() == null &&
                        !spinner_canton.getSelectedItem().toString().equals("Cantones") &&
                        !spinner_rol.getSelectedItem().toString().equals("Rol")) {
                    User user = new User();
                    user.uid = UID_USER;
                    user.ced = editTextCed.getText().toString();
                    user.name = editTextName.getText().toString().toUpperCase();
                    user.email = editTextEmail.getText().toString().toLowerCase();
                    user.phone = editTextPhone.getText().toString();
                    user.canton = spinner_canton.getSelectedItem().toString();
                    user.rol = spinner_rol.getSelectedItem().toString();
                    user.password = editTextPassword.getText().toString();

                    if(!TextUtils.isEmpty(UID_USER)) {
                        PrimaryView.userController.updateUser(user).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                alertDialog.hideProgressMessage();
                                alertDialog.createMessage("Correcto", "Usuario Actualizado Correctamente", builder -> {
                                    builder.setCancelable(false);
                                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {
                                    });
                                    builder.create().show();
                                });
                            } else {
                                alertDialog.hideProgressMessage();
                                alertDialog.createMessage("¡Advertencia!", "Ocurrió un error al Actualizar el Usuario", builder -> {
                                    builder.setCancelable(true);
                                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {
                                    });
                                    builder.create().show();
                                });
                            }
                        });
                    }else{
                        alertDialog.hideProgressMessage();
                        Toast.makeText(this, "Ocurrió un error al obtener la id del Usuario",Toast.LENGTH_LONG).show();
                    }

                }else{
                    alertDialog.hideProgressMessage();
                    alertDialog.createMessage("¡Advertencia!", "Completa todos los campos", builder -> {
                        builder.setCancelable(true);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }
            });

            btnDelete.setOnClickListener(view ->

                alertDialog.createMessage("¿Estás seguro de eliminar el usuario?", "¡Esta acción no es reversible!", builder -> {
                    builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {

                        alertDialog.showProgressMessage("Eliminando Usuario...");
                        PrimaryView.userController.deleteUser(UID_USER).addOnCompleteListener(task -> {
                            alertDialog.hideProgressMessage();
                            if(task.isSuccessful()){
                                finish();
                            }else{
                                Toast.makeText(this, "Ocurrió un error al eliminar el Usuario",Toast.LENGTH_LONG).show();
                            }
                        });

                    });
                    builder.setNeutralButton("Cancelar", (dialogInterface, i) -> {});
                    builder.setCancelable(false);
                    builder.create().show();
                })

            );

            imageButton.setOnClickListener(view -> {
                Intent i = new Intent(this, DetAssistanceView.class);
                i.putExtra("uid",UID_USER);
                i.putExtra("nombre",USERNAME);
                startActivity(i);
            });

        }else{
            Toast.makeText(this, "Ocurrió un error al cargar el Usuario",Toast.LENGTH_LONG).show();
        }

    }
}