package com.dpilaloa.upsipteeo.Fragments;

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

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home,container,false);

        TextView txtVersion = view.findViewById(R.id.txt_version);
        TextView txtProcess = view.findViewById(R.id.txt_proceso);
        Button btnShowAlert = view.findViewById(R.id.btn_ver_alertas);
        Button btnAlert = view.findViewById(R.id.btn_alerta);

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
                    txtProcess.setText(snapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {throw error.toException();}

        });

        try {

            String version = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0).versionName;
            txtVersion.setText(getString(R.string.version));
            txtVersion.append("\t"+version);

        } catch (PackageManager.NameNotFoundException e) {
            txtVersion.setText("-");
        }

        btnShowAlert.setVisibility(Principal.rol.equals(getString(R.string.admin_one)) || Principal.rol.equals(getString(R.string.admin_two)) ? View.VISIBLE : View.GONE);
        btnAlert.setVisibility(Principal.rol.equals(getString(R.string.admin_one)) || Principal.rol.equals(getString(R.string.admin_two)) ? View.VISIBLE : View.GONE);

        btnShowAlert.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), Alertas.class)));

        return view;

    }
}
