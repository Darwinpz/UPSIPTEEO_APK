package com.dpilaloa.upsipteeo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class Det_usuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_usuario);

        TextView txt_nombre = findViewById(R.id.txt_nombre);
        TextView txt_cedula = findViewById(R.id.txt_cedula);
        TextView txt_correo = findViewById(R.id.txt_correo);
        TextView txt_telefono = findViewById(R.id.txt_telefono);
        ImageView img_perfil = findViewById(R.id.img_perfil);
        Toolbar toolbar = findViewById(R.id.toolbar);

        String UID_USUARIO = Objects.requireNonNull(getIntent().getExtras()).getString("uid","");

        Spinner spinner_rol = findViewById(R.id.spinner_rol);
        Spinner spinner_canton = findViewById(R.id.spinner_canton);

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

                    int spinnerPosition_rol = adapterspinner_rol.getPosition(user.rol);
                    spinner_rol.setSelection(spinnerPosition_rol);

                    int spinnerPosition_canton = adapterspinner_canton.getPosition(user.canton);
                    spinner_canton.setSelection(spinnerPosition_canton);

                    if (user.url_foto != null) {
                        Glide.with(this).load(user.url_foto).centerCrop().into(img_perfil);
                    }

                }

            });

        }else{
            Toast.makeText(this, "Ocurrió un error al cargar el usuario",Toast.LENGTH_LONG).show();
        }


    }
}