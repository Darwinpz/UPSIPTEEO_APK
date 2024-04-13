package com.dpilaloa.upsipteeo.Fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dpilaloa.upsipteeo.Adaptadores.Adapter_usuario;
import com.dpilaloa.upsipteeo.Principal;
import com.dpilaloa.upsipteeo.R;

import java.util.Objects;

public class Fragmento_Usuarios extends Fragment {

    Spinner spinner_rol;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_usuarios,container,false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_trabajadores);
        TextView txt_sinresultados = view.findViewById(R.id.txt_sinresultados);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        EditText buscador = view.findViewById(R.id.buscador);
        Adapter_usuario list_usuarios = new Adapter_usuario(view.getContext());

        spinner_rol = view.findViewById(R.id.spinner_rol);
        TextView txt_contador = view.findViewById(R.id.txt_contador);

        ArrayAdapter<CharSequence> adapterspinner_rol = ArrayAdapter.createFromResource(view.getContext(), R.array.rol, android.R.layout.simple_spinner_item);
        adapterspinner_rol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_rol.setAdapter(adapterspinner_rol);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(list_usuarios);

        if(!Principal.id.isEmpty()) {

            Principal.ctlUsuarios.VerUsuarios(list_usuarios, Principal.id , "Todos","", txt_sinresultados, progressBar, txt_contador);

            spinner_rol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Principal.ctlUsuarios.VerUsuarios(list_usuarios, Principal.id ,spinner_rol.getSelectedItem().toString(),buscador.getText().toString(), txt_sinresultados, progressBar, txt_contador);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}

            });

            buscador.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ocultar_teclado();
                    Principal.ctlUsuarios.VerUsuarios(list_usuarios, Principal.id ,spinner_rol.getSelectedItem().toString(),buscador.getText().toString(), txt_sinresultados, progressBar, txt_contador);
                    return true;
                }
                return false;
            });

        }else{
            Toast.makeText(view.getContext(), "Ocurrió un error al cargar el id",Toast.LENGTH_LONG).show();
        }

        return view;
    }

    public void ocultar_teclado(){
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(requireActivity().getCurrentFocus()).getWindowToken(), 0);

    }
}
