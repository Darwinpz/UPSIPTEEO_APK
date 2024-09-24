package com.dpilaloa.upsipteeo.Fragmentos;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.dpilaloa.upsipteeo.Alertas;
import com.dpilaloa.upsipteeo.MainActivity;
import com.dpilaloa.upsipteeo.Principal;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.Reportes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Fragmento_Inicio extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_inicio,container,false);

        TextView txt_version = view.findViewById(R.id.txt_version);
        TextView txt_proceso = view.findViewById(R.id.txt_proceso);
        Button btn_ver_alertas = view.findViewById(R.id.btn_ver_alertas);
        Button btn_alerta = view.findViewById(R.id.btn_alerta);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_perfil);

        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(menuItem -> {
            startActivity(new Intent(getActivity(), Reportes.class));
            return false;
        });

        MainActivity.databaseReference.child("proceso").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    txt_proceso.setText(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {throw error.toException();}

        });

        try {

            String version = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0).versionName;
            txt_version.setText(getString(R.string.version));
            txt_version.append("\t"+version);

        } catch (PackageManager.NameNotFoundException e) {
            txt_version.setText("-");
        }

        btn_ver_alertas.setVisibility(Principal.rol.equals(getString(R.string.admin_one)) || Principal.rol.equals(getString(R.string.admin_two)) ? View.VISIBLE : View.GONE);
        btn_alerta.setVisibility(Principal.rol.equals(getString(R.string.admin_one)) || Principal.rol.equals(getString(R.string.admin_two)) ? View.VISIBLE : View.GONE);

        btn_ver_alertas.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), Alertas.class)));

        return view;

    }
}
