package com.dpilaloa.upsipteeo.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dpilaloa.upsipteeo.MainActivity;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.ui.adapters.AssistanceAdapter;
import com.dpilaloa.upsipteeo.data.controllers.AssistanceController;
import com.dpilaloa.upsipteeo.ui.fragments.AssistanceAddFragment;

import java.util.Objects;

public class DetAssistanceActivity extends AppCompatActivity {

    String UID_USER = "", USERNAME = "";
    public static AssistanceController assistanceController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_assistance);

        AssistanceAdapter assistanceAdapter = new AssistanceAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_asistencia);
        TextView txtResult = findViewById(R.id.textViewNotResult);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView txtCount = findViewById(R.id.textViewCount);
        TextView txtName = findViewById(R.id.textViewName);
        Button btnAddAssistance = findViewById(R.id.btn_add_asistencia);
        Toolbar toolbar = findViewById(R.id.toolbar);

        assistanceController = new AssistanceController(MainActivity.databaseReference);
        toolbar.setOnClickListener(view -> finish());

        UID_USER = Objects.requireNonNull(getIntent().getExtras()).getString("uid","");
        USERNAME = Objects.requireNonNull(getIntent().getExtras()).getString("nombre","");
        txtName.setText(USERNAME);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(assistanceAdapter);

        if(!UID_USER.isEmpty()) {
            assistanceController.getAssistance(assistanceAdapter,UID_USER,txtResult,progressBar,txtCount);

            btnAddAssistance.setVisibility(PrimaryActivity.rol.equals(getString(R.string.admin_one)) ? View.VISIBLE : View.GONE);

            btnAddAssistance.setOnClickListener(view -> {
                AssistanceAddFragment.UID = UID_USER;
                AssistanceAddFragment assistanceAddFragment = new AssistanceAddFragment();
                assistanceAddFragment.show(getSupportFragmentManager(),"ASISTENCIA");
            });

        }else{
            Toast.makeText(this, "Ocurrió un error al cargar el id",Toast.LENGTH_LONG).show();
        }

    }
}