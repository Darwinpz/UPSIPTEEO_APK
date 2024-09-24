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

import com.dpilaloa.upsipteeo.Adaptadores.Adapter_asistencia;
import com.dpilaloa.upsipteeo.Controladores.Ctl_asistencia;
import com.dpilaloa.upsipteeo.Fragmentos.Fragmento_add_asistencia;

import java.util.Objects;

public class Det_asistencia extends AppCompatActivity {

    String UID_USUARIO = "", NOMBRE_USUARIO = "";
    public static Ctl_asistencia ctlAsistencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_asistencia);

        Adapter_asistencia list_asistencia = new Adapter_asistencia(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_asistencia);
        TextView txt_sinresultados = findViewById(R.id.txt_sinresultados);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView txt_contador = findViewById(R.id.txt_contador);
        TextView txt_nombre = findViewById(R.id.txt_nombre);
        Button btn_add_asistencia = findViewById(R.id.btn_add_asistencia);
        Toolbar toolbar = findViewById(R.id.toolbar);

        ctlAsistencia = new Ctl_asistencia(MainActivity.databaseReference);
        toolbar.setOnClickListener(view -> finish());

        UID_USUARIO = Objects.requireNonNull(getIntent().getExtras()).getString("uid","");
        NOMBRE_USUARIO = Objects.requireNonNull(getIntent().getExtras()).getString("nombre","");
        txt_nombre.setText(NOMBRE_USUARIO);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(list_asistencia);

        if(!UID_USUARIO.isEmpty()) {
            ctlAsistencia.VerAsistencia(list_asistencia,UID_USUARIO,txt_sinresultados,progressBar,txt_contador);

            btn_add_asistencia.setVisibility(Principal.rol.equals("TICS") ? View.VISIBLE : View.GONE);

            btn_add_asistencia.setOnClickListener(view -> {
                Fragmento_add_asistencia.UID = UID_USUARIO;
                Fragmento_add_asistencia fragment_add_asistencia = new Fragmento_add_asistencia();
                fragment_add_asistencia.show(getSupportFragmentManager(),"ASISTENCIA");
            });

        }else{
            Toast.makeText(this, "Ocurrió un error al cargar el id",Toast.LENGTH_LONG).show();
        }

    }
}