package com.dpilaloa.upsipteeo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.dpilaloa.upsipteeo.Controllers.AlertDialogController;
import com.dpilaloa.upsipteeo.Objects.User;

import java.util.Objects;

public class Det_usuario extends AppCompatActivity {

    String UID_USUARIO = "", NOMBRE_USUARIO = "", URL_IMAGEN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_usuario);

        EditText txt_cedula = findViewById(R.id.txt_cedula);
        EditText txt_nombre = findViewById(R.id.txt_nombre);
        EditText txt_correo = findViewById(R.id.txt_correo);
        EditText txt_telefono = findViewById(R.id.txt_telefono);
        EditText txt_clave = findViewById(R.id.txt_clave);
        ImageView img_perfil = findViewById(R.id.img_perfil);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Button btn_actualizar = findViewById(R.id.btn_actualizar);
        Button btn_eliminar = findViewById(R.id.btn_eliminar);

        UID_USUARIO = Objects.requireNonNull(getIntent().getExtras()).getString("uid","");

        Spinner spinner_rol = findViewById(R.id.spinner_rol);
        Spinner spinner_canton = findViewById(R.id.spinner_canton);
        ImageButton imageButton = findViewById(R.id.btn_ver_asistencia);

        AlertDialogController alertDialog = new AlertDialogController(this);

        toolbar.setOnClickListener(view -> finish());

        ArrayAdapter<CharSequence> adapterspinner_rol = ArrayAdapter.createFromResource(this, R.array.rol, android.R.layout.simple_spinner_item);
        adapterspinner_rol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_rol.setAdapter(adapterspinner_rol);

        ArrayAdapter<CharSequence> adapterspinner_canton = ArrayAdapter.createFromResource(this, R.array.cantones, android.R.layout.simple_spinner_item);
        adapterspinner_canton.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_canton.setAdapter(adapterspinner_canton);

        if(!UID_USUARIO.isEmpty()){

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

            Principal.ctlUsuarios.getProfile(UID_USUARIO, user -> {

                if(user!=null){

                    txt_cedula.setText(user.ced);
                    txt_nombre.setText(user.name);
                    txt_correo.setText(user.email);
                    txt_telefono.setText(user.phone);
                    txt_clave.setText(user.password);

                    NOMBRE_USUARIO = user.name;

                    int spinnerPosition_rol = adapterspinner_rol.getPosition(user.rol);
                    spinner_rol.setSelection(spinnerPosition_rol);

                    int spinnerPosition_canton = adapterspinner_canton.getPosition(user.canton);
                    spinner_canton.setSelection(spinnerPosition_canton);

                    if (user.photo != null && !user.photo.isEmpty()) {
                        Glide.with(getApplicationContext()).load(user.photo).centerCrop().into(img_perfil);
                        URL_IMAGEN = user.photo;

                    }

                }

            });

            img_perfil.setOnClickListener(view1 -> {

                if( URL_IMAGEN!=null && !URL_IMAGEN.isEmpty()) {
                    startActivity(new Intent(this, Ver_imagen.class).putExtra("url", URL_IMAGEN));
                }else{
                    Toast.makeText(this,"Sin foto de perfil", Toast.LENGTH_SHORT).show();
                }

            });

            btn_actualizar.setOnClickListener(view1 -> {
                alertDialog.showProgressMessage("Actualizando...");
                if(!txt_cedula.getText().toString().trim().isEmpty() && txt_cedula.getError() == null &&
                        !txt_nombre.getText().toString().trim().isEmpty() && txt_nombre.getError() == null &&
                        !txt_correo.getText().toString().trim().isEmpty() && txt_correo.getError() == null &&
                        !txt_telefono.getText().toString().trim().isEmpty() && txt_telefono.getError() == null &&
                        !spinner_canton.getSelectedItem().toString().equals("Cantones") &&
                        !spinner_rol.getSelectedItem().toString().equals("Rol")) {
                    User user = new User();
                    user.uid = UID_USUARIO;
                    user.ced = txt_cedula.getText().toString();
                    user.name = txt_nombre.getText().toString().toUpperCase();
                    user.email = txt_correo.getText().toString().toLowerCase();
                    user.phone = txt_telefono.getText().toString();
                    user.canton = spinner_canton.getSelectedItem().toString();
                    user.rol = spinner_rol.getSelectedItem().toString();
                    user.password = txt_clave.getText().toString();

                    if(!TextUtils.isEmpty(UID_USUARIO)) {
                        Principal.ctlUsuarios.updateUser(user).addOnCompleteListener(task -> {
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

            btn_eliminar.setOnClickListener(view ->

                alertDialog.createMessage("¿Estás seguro de eliminar el usuario?", "¡Esta acción no es reversible!", builder -> {
                    builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {

                        alertDialog.showProgressMessage("Eliminando Usuario...");
                        Principal.ctlUsuarios.deleteUser(UID_USUARIO).addOnCompleteListener(task -> {
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
                Intent i = new Intent(this, Det_asistencia.class);
                i.putExtra("uid",UID_USUARIO);
                i.putExtra("nombre",NOMBRE_USUARIO);
                startActivity(i);
            });

        }else{
            Toast.makeText(this, "Ocurrió un error al cargar el Usuario",Toast.LENGTH_LONG).show();
        }

    }
}