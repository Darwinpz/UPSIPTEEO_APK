package com.dpilaloa.upsipteeo.Fragmentos;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.dpilaloa.upsipteeo.Principal;
import com.dpilaloa.upsipteeo.R;

public class Fragmento_Perfil extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_perfil,container,false);

        TextView txt_rol = view.findViewById(R.id.txt_rol);
        TextView txt_nombre = view.findViewById(R.id.txt_nombre);
        TextView txt_cedula = view.findViewById(R.id.txt_cedula);
        TextView txt_correo = view.findViewById(R.id.txt_correo);
        TextView txt_telefono = view.findViewById(R.id.txt_telefono);
        ImageView img_perfil = view.findViewById(R.id.img_perfil);
        Spinner spinner_canton = view.findViewById(R.id.spinner_canton);

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

                    int spinnerPosition = adapterspinner_canton.getPosition(user.canton);
                    spinner_canton.setSelection(spinnerPosition);

                    if (user.url_foto != null) {
                        Glide.with(this).load(user.url_foto).centerCrop().into(img_perfil);
                    }

                }

            });

        }else{
            Toast.makeText(view.getContext(), "Ocurrió un error al cargar el perfil",Toast.LENGTH_LONG).show();
        }

        return view;
    }
}
