package com.dpilaloa.upsipteeo;

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

import com.dpilaloa.upsipteeo.Adapters.AssistanceAdapter;
import com.dpilaloa.upsipteeo.Controllers.AssistanceController;
import com.dpilaloa.upsipteeo.Fragments.AssistanceAddFragment;

import java.util.Objects;

public class Det_asistencia extends AppCompatActivity {

    String UID_USUARIO = "", NOMBRE_USUARIO = "";
    public static AssistanceController ctlAsistencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_asistencia);

        AssistanceAdapter list_asistencia = new AssistanceAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_asistencia);
        TextView txt_sinresultados = findViewById(R.id.txt_sinresultados);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView txt_contador = findViewById(R.id.txt_contador);
        TextView txt_nombre = findViewById(R.id.txt_nombre);
        Button btn_add_asistencia = findViewById(R.id.btn_add_asistencia);
        Toolbar toolbar = findViewById(R.id.toolbar);

        ctlAsistencia = new AssistanceController(MainActivity.databaseReference);
        toolbar.setOnClickListener(view -> finish());

        UID_USUARIO = Objects.requireNonNull(getIntent().getExtras()).getString("uid","");
        NOMBRE_USUARIO = Objects.requireNonNull(getIntent().getExtras()).getString("nombre","");
        txt_nombre.setText(NOMBRE_USUARIO);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(list_asistencia);

        if(!UID_USUARIO.isEmpty()) {
            ctlAsistencia.getAssistance(list_asistencia,UID_USUARIO,txt_sinresultados,progressBar,txt_contador);

            btn_add_asistencia.setVisibility(Principal.rol.equals(getString(R.string.admin_one)) ? View.VISIBLE : View.GONE);

            btn_add_asistencia.setOnClickListener(view -> {
                AssistanceAddFragment.UID = UID_USUARIO;
                AssistanceAddFragment fragment_add_asistencia = new AssistanceAddFragment();
                fragment_add_asistencia.show(getSupportFragmentManager(),"ASISTENCIA");
            });

        }else{
            Toast.makeText(this, "Ocurrió un error al cargar el id",Toast.LENGTH_LONG).show();
        }

    }
}