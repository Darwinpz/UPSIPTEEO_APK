package com.dpilaloa.upsipteeo.Fragmentos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.dpilaloa.upsipteeo.Controladores.Alert_dialog;
import com.dpilaloa.upsipteeo.Controladores.Progress_dialog;
import com.dpilaloa.upsipteeo.Det_asistencia;
import com.dpilaloa.upsipteeo.MainActivity;
import com.dpilaloa.upsipteeo.Objetos.Ob_usuario;
import com.dpilaloa.upsipteeo.Principal;
import com.dpilaloa.upsipteeo.R;

public class Fragmento_Perfil extends Fragment {

    String NOMBRE_USUARIO = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_perfil,container,false);

        TextView txt_rol = view.findViewById(R.id.txt_rol);
        TextView txt_nombre = view.findViewById(R.id.txt_nombre);
        TextView txt_cedula = view.findViewById(R.id.txt_cedula);
        EditText txt_correo = view.findViewById(R.id.txt_correo);
        EditText txt_telefono = view.findViewById(R.id.txt_telefono);
        ImageView img_perfil = view.findViewById(R.id.img_perfil);
        Spinner spinner_canton = view.findViewById(R.id.spinner_canton);
        Button btn_actualizar = view.findViewById(R.id.btn_actualizar);
        Button btn_salir = view.findViewById(R.id.btn_salir);
        ImageButton imageButton = view.findViewById(R.id.btn_ver_asist);

        Progress_dialog dialog = new Progress_dialog(view.getContext());
        Alert_dialog alertDialog = new Alert_dialog(view.getContext());

        ArrayAdapter<CharSequence> adapterspinner_canton = ArrayAdapter.createFromResource(view.getContext(), R.array.cantones, android.R.layout.simple_spinner_item);
        adapterspinner_canton.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_canton.setAdapter(adapterspinner_canton);

        if(!Principal.id.isEmpty()) {

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


            Principal.ctlUsuarios.obtener_datos_perfil(Principal.id, user -> {

                if (user != null) {

                    txt_nombre.setText(user.nombre);
                    txt_correo.setText(user.correo);
                    txt_telefono.setText(user.celular);
                    txt_cedula.setText(user.cedula);
                    txt_rol.setText(user.rol);

                    NOMBRE_USUARIO = user.nombre;

                    int spinnerPosition = adapterspinner_canton.getPosition(user.canton);
                    spinner_canton.setSelection(spinnerPosition);

                    if (user.url_foto != null && !user.url_foto.isEmpty()) {
                        Glide.with(view.getContext()).load(user.url_foto).centerCrop().into(img_perfil);
                    }

                }

            });

            imageButton.setOnClickListener(view1 -> {
                startActivity(new Intent(view.getContext(), Det_asistencia.class)
                        .putExtra("uid",Principal.id)
                        .putExtra("nombre", NOMBRE_USUARIO));
            });

            img_perfil.setOnClickListener(view1 -> {



            });


            btn_actualizar.setOnClickListener(view1 -> {
                dialog.mostrar_mensaje("Actualizando...");
                if(!txt_correo.getText().toString().trim().isEmpty() && txt_correo.getError() == null &&
                   !txt_telefono.getText().toString().trim().isEmpty() && txt_telefono.getError() == null &&
                   !spinner_canton.getSelectedItem().toString().equals("Cantones")) {
                    Ob_usuario user = new Ob_usuario();
                    user.uid = Principal.id;
                    user.cedula = txt_cedula.getText().toString();
                    user.nombre = txt_nombre.getText().toString().toUpperCase();
                    user.correo = txt_correo.getText().toString().toLowerCase();
                    user.celular = txt_telefono.getText().toString();
                    user.canton = spinner_canton.getSelectedItem().toString();
                    user.rol = txt_rol.getText().toString();
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

            btn_salir.setOnClickListener(view1 -> {
                dialog.mostrar_mensaje("Cerrando Sesión...");
                Principal.ctlUsuarios.cerrar_sesion(Principal.preferences);
                dialog.ocultar_mensaje();
                startActivity(new Intent(view.getContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                requireActivity().finish();
            });

        }else{
            Toast.makeText(view.getContext(), "Ocurrió un error al cargar el perfil",Toast.LENGTH_LONG).show();
        }

        return view;
    }
}
