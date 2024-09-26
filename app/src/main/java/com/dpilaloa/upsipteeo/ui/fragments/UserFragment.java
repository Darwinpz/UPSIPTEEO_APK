package com.dpilaloa.upsipteeo.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import com.dpilaloa.upsipteeo.ui.adapters.UserAdapter;
import com.dpilaloa.upsipteeo.ui.activities.AddUserActivity;
import com.dpilaloa.upsipteeo.ui.activities.PrimaryActivity;
import com.dpilaloa.upsipteeo.R;

import java.util.Objects;

public class UserFragment extends Fragment {

    Spinner spinnerRol;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user,container,false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewUsers);
        TextView txtResult = view.findViewById(R.id.textViewNotResult);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        EditText editTextSearch = view.findViewById(R.id.editTextSearch);
        UserAdapter userAdapter = new UserAdapter(view.getContext());
        Button btnAddUser = view.findViewById(R.id.buttonAddUser);

        spinnerRol = view.findViewById(R.id.spinnerRol);
        TextView txtCount = view.findViewById(R.id.textViewCount);

        ArrayAdapter<CharSequence> adapterSpinnerRol = ArrayAdapter.createFromResource(view.getContext(), R.array.rol, android.R.layout.simple_spinner_item);
        adapterSpinnerRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRol.setAdapter(adapterSpinnerRol);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(userAdapter);

        if(!PrimaryActivity.id.isEmpty()) {

            PrimaryActivity.userController.getUsers(userAdapter, PrimaryActivity.id , "Rol","", txtResult, progressBar, txtCount);

            spinnerRol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    PrimaryActivity.userController.getUsers(userAdapter, PrimaryActivity.id ,spinnerRol.getSelectedItem().toString(),editTextSearch.getText().toString(), txtResult, progressBar, txtCount);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}

            });

            editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    PrimaryActivity.userController.getUsers(userAdapter, PrimaryActivity.id ,spinnerRol.getSelectedItem().toString(),editTextSearch.getText().toString(), txtResult, progressBar, txtCount);
                    return true;
                }
                return false;
            });

            btnAddUser.setOnClickListener(view1 -> {

                Intent i = new Intent(view.getContext(), AddUserActivity.class);
                startActivity(i);
                //guardar();

            });

        }else{
            Toast.makeText(view.getContext(), "Ocurrió un error al cargar el id",Toast.LENGTH_LONG).show();
        }

        return view;
    }

    public void hideKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(requireActivity().getCurrentFocus()).getWindowToken(), 0);

    }

    /*public void guardar(){

        String trabajadores = "";

        try {

            JSONArray jsonArray = new JSONArray(trabajadores);

            for(int i=0; i <jsonArray.length();i++){
                JSONObject object = jsonArray.getJSONObject(i);
                User user = new User();
                user.cedula = object.getString("cedula");
                user.nombre = object.getString("nombre").toUpperCase();
                user.rol = "RECEPTOR DE ACTAS";
                user.canton = "MACHALA";
                user.celular = object.getString("celular");
                user.correo = object.getString("correo");
                user.clave = "Cne.2024";
                PrimaryActivity.ctlUsuarios.crear_usuarios(user);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/

}
