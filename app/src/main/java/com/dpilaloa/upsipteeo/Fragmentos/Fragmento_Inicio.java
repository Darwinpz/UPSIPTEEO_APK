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
import androidx.fragment.app.Fragment;

import com.dpilaloa.upsipteeo.Alertas;
import com.dpilaloa.upsipteeo.Principal;
import com.dpilaloa.upsipteeo.R;

public class Fragmento_Inicio extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_inicio,container,false);

        TextView txt_version = view.findViewById(R.id.txt_version);
        Button btn_ver_alertas = view.findViewById(R.id.btn_ver_alertas);
        Button btn_alerta = view.findViewById(R.id.btn_alerta);

        try {
            String version = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0).versionName;
            txt_version.setText("Versión "+version);
        } catch (PackageManager.NameNotFoundException e) {
            txt_version.setText("0.0");
        }

        btn_ver_alertas.setVisibility(Principal.rol.equals("TICS") || Principal.rol.equals("TECNICO SUPERVISOR") ? View.VISIBLE : View.GONE);
        btn_alerta.setVisibility(Principal.rol.equals("TICS") || Principal.rol.equals("TECNICO SUPERVISOR") ? View.VISIBLE : View.GONE);

        btn_ver_alertas.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), Alertas.class)));

        return view;

    }
}
