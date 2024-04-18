package com.dpilaloa.upsipteeo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import com.dpilaloa.upsipteeo.Controladores.Alert_dialog;
import com.dpilaloa.upsipteeo.Controladores.Progress_dialog;
import com.dpilaloa.upsipteeo.Objetos.Ob_usuario;

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

        Progress_dialog dialog = new Progress_dialog(this);
        Alert_dialog alertDialog = new Alert_dialog(this);

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
                    if(editable.toString().trim().length() == 10){
                        if(!Principal.ctlUsuarios.Validar_Cedula(editable.toString().trim())){
                            txt_cedula.setError("Cédula Incorrecta");
                        }
                    }else{
                        txt_cedula.setError("Ingresa 10 dígitos");
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
                    if(!Principal.ctlUsuarios.validar_usuario(editable.toString().trim())){
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
                    if(!Principal.ctlUsuarios.validar_correo(editable.toString().trim())){
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
                    if(editable.toString().trim().length() == 10) {
                        if (!Principal.ctlUsuarios.validar_celular(editable.toString().trim())) {
                            txt_telefono.setError("Ingresa un celular válido");
                        }
                    }else{
                        txt_telefono.setError("Ingresa 10 dígitos");
                    }
                }
            });

            Principal.ctlUsuarios.obtener_datos_perfil(UID_USUARIO,user -> {

                if(user!=null){

                    txt_cedula.setText(user.cedula);
                    txt_nombre.setText(user.nombre);
                    txt_correo.setText(user.correo);
                    txt_telefono.setText(user.celular);
                    txt_clave.setText(user.clave);

                    NOMBRE_USUARIO = user.nombre;

                    int spinnerPosition_rol = adapterspinner_rol.getPosition(user.rol);
                    spinner_rol.setSelection(spinnerPosition_rol);

                    int spinnerPosition_canton = adapterspinner_canton.getPosition(user.canton);
                    spinner_canton.setSelection(spinnerPosition_canton);

                    if (user.url_foto != null && !user.url_foto.isEmpty()) {
                        Glide.with(getApplicationContext()).load(user.url_foto).centerCrop().into(img_perfil);
                        URL_IMAGEN = user.url_foto;

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
                dialog.mostrar_mensaje("Actualizando...");
                if(!txt_cedula.getText().toString().trim().isEmpty() && txt_cedula.getError() == null &&
                        !txt_nombre.getText().toString().trim().isEmpty() && txt_nombre.getError() == null &&
                        !txt_correo.getText().toString().trim().isEmpty() && txt_correo.getError() == null &&
                        !txt_telefono.getText().toString().trim().isEmpty() && txt_telefono.getError() == null &&
                        !spinner_canton.getSelectedItem().toString().equals("Cantones") &&
                        !spinner_rol.getSelectedItem().toString().equals("Rol")) {
                    Ob_usuario user = new Ob_usuario();
                    user.uid = UID_USUARIO;
                    user.cedula = txt_cedula.getText().toString();
                    user.nombre = txt_nombre.getText().toString().toUpperCase();
                    user.correo = txt_correo.getText().toString().toLowerCase();
                    user.celular = txt_telefono.getText().toString();
                    user.canton = spinner_canton.getSelectedItem().toString();
                    user.rol = spinner_rol.getSelectedItem().toString();
                    user.clave = txt_clave.getText().toString();
                    Principal.ctlUsuarios.update_usuario(user);
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("Correcto", "Usuario Actualizado Correctamente", builder -> {
                        builder.setCancelable(false);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }else{
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("¡Advertencia!", "Completa todos los campos", builder -> {
                        builder.setCancelable(true);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }
            });

            btn_eliminar.setOnClickListener(view -> {

                alertDialog.crear_mensaje("¿Estás seguro de eliminar el usuario?", "¡Esta acción no es reversible!", builder -> {
                    builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                        dialog.mostrar_mensaje("Eliminando Usuario...");
                        Principal.ctlUsuarios.eliminar_usuario(UID_USUARIO);
                        dialog.ocultar_mensaje();
                        finish();
                    });
                    builder.setNeutralButton("Cancelar", (dialogInterface, i) -> {});
                    builder.setCancelable(false);
                    builder.create().show();
                });

            });

            imageButton.setOnClickListener(view -> {
                Intent i = new Intent(this, Det_asistencia.class);
                i.putExtra("uid",UID_USUARIO);
                i.putExtra("nombre",NOMBRE_USUARIO);
                startActivity(i);
            });

        }else{
            Toast.makeText(this, "Ocurrió un error al cargar el usuario",Toast.LENGTH_LONG).show();
        }

    }
}