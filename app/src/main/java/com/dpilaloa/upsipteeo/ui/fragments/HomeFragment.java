package com.dpilaloa.upsipteeo.ui.fragments;

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

import com.dpilaloa.upsipteeo.ui.activities.AlertActivity;
import com.dpilaloa.upsipteeo.ui.activities.PrimaryActivity;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.ui.activities.ReportActivity;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home,container,false);

        TextView txtVersion = view.findViewById(R.id.txt_version);
        TextView txtProcess = view.findViewById(R.id.textViewProcess);
        Button btnShowAlert = view.findViewById(R.id.buttonShowAlerts);
        Button btnAlert = view.findViewById(R.id.buttonAlert);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_profile);

        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(menuItem -> {
            startActivity(new Intent(getActivity(), ReportActivity.class));
            return false;
        });

        PrimaryActivity.userController.getProcess(txtProcess::setText,
                databaseError -> txtProcess.setText(getString(R.string.process)));

        try {
            String version = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0).versionName;
            txtVersion.setText(getString(R.string.version));
            txtVersion.append("\t"+version);
        } catch (PackageManager.NameNotFoundException e) {
            txtVersion.setText("-");
        }

        btnShowAlert.setVisibility(PrimaryActivity.rol.equals(getString(R.string.admin_one)) || PrimaryActivity.rol.equals(getString(R.string.admin_two)) ? View.VISIBLE : View.GONE);
        btnAlert.setVisibility(PrimaryActivity.rol.equals(getString(R.string.admin_one)) || PrimaryActivity.rol.equals(getString(R.string.admin_two)) ? View.VISIBLE : View.GONE);

        btnShowAlert.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), AlertActivity.class)));

        return view;

    }
}
