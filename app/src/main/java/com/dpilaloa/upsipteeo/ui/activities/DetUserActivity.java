package com.dpilaloa.upsipteeo.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.data.controllers.AlertDialogController;
import com.dpilaloa.upsipteeo.data.models.User;
import com.dpilaloa.upsipteeo.utils.ValEditTextWatcher;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class DetUserActivity extends AppCompatActivity {

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

        ArrayAdapter<CharSequence> adapterSpinnerRol = getCharSequenceArrayAdapter();
        spinner_rol.setAdapter(adapterSpinnerRol);

        ArrayAdapter<CharSequence> adapterSpinnerCanton = ArrayAdapter.createFromResource(this, R.array.canton, android.R.layout.simple_spinner_item);
        adapterSpinnerCanton.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_canton.setAdapter(adapterSpinnerCanton);

        if(!UID_USER.isEmpty()){

            editTextCed.addTextChangedListener(new ValEditTextWatcher(editTextCed,
                    input -> PrimaryActivity.userController.valCed(input),"Ingresa una cédula válida"));

            editTextName.addTextChangedListener(new ValEditTextWatcher(editTextName,
                    input -> PrimaryActivity.userController.valUser(input),"Ingresa un nombre válido"));

            editTextEmail.addTextChangedListener(new ValEditTextWatcher(editTextEmail,
                    input -> PrimaryActivity.userController.valEmail(input),"Ingresa un correo válido"));

            editTextPhone.addTextChangedListener(new ValEditTextWatcher(editTextPhone,
                    input -> PrimaryActivity.userController.valPhone(input),"Ingresa un teléfono válido"));

            PrimaryActivity.userController.getProfile(UID_USER, user -> {

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

            boolean isAdminOneDiffUid = PrimaryActivity.rol.equals(getString(R.string.admin_one)) && !getString(R.string.admin_uid).equals(UID_USER);
            boolean isAdminTwoDiffUid = (isAdminOneDiffUid) || (PrimaryActivity.rol.equals(getString(R.string.admin_two)) &&
                            !getString(R.string.admin_uid).equals(UID_USER) && !ROL_USER.equals(getString(R.string.admin_one)));

            btnDelete.setVisibility( isAdminOneDiffUid ? View.VISIBLE : View.GONE);
            btnUpdate.setVisibility( isAdminTwoDiffUid ? View.VISIBLE : View.GONE);

            editTextCed.setEnabled(isAdminTwoDiffUid);
            editTextName.setEnabled(isAdminTwoDiffUid);
            editTextEmail.setEnabled(isAdminTwoDiffUid);
            editTextPhone.setEnabled(isAdminTwoDiffUid);
            spinner_canton.setEnabled(isAdminTwoDiffUid);
            spinner_rol.setEnabled(isAdminTwoDiffUid);

            textViewPassword.setVisibility(isAdminOneDiffUid? View.VISIBLE : View.GONE);
            textInputLayoutPassword.setVisibility(isAdminOneDiffUid? View.VISIBLE : View.GONE);

            img_perfil.setOnClickListener(view1 -> {

                if(!TextUtils.isEmpty(URL_PHOTO)) {
                    startActivity(new Intent(this, ImageActivity.class).putExtra("url", URL_PHOTO));
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
                        PrimaryActivity.userController.updateUser(user).addOnCompleteListener(task -> {
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
                        PrimaryActivity.userController.deleteUser(UID_USER).addOnCompleteListener(task -> {
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
                Intent i = new Intent(this, DetAssistanceActivity.class);
                i.putExtra("uid",UID_USER);
                i.putExtra("nombre",USERNAME);
                startActivity(i);
            });

        }else{
            Toast.makeText(this, "Ocurrió un error al cargar el Usuario",Toast.LENGTH_LONG).show();
        }

    }

    @NonNull
    private ArrayAdapter<CharSequence> getCharSequenceArrayAdapter() {
        ArrayAdapter<CharSequence> adapterSpinnerRol = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.rol)) {
            @Override
            public boolean isEnabled(int position) {return PrimaryActivity.rol.equals(getString(R.string.admin_one)) || position != 1;}
            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setTextColor((PrimaryActivity.rol.equals(getString(R.string.admin_one)) || position != 1) ? Color.BLACK : Color.GRAY);
                return view;
            }
        };

        adapterSpinnerRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapterSpinnerRol;
    }

}