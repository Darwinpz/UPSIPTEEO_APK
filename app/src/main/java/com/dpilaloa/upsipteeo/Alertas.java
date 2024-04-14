package com.dpilaloa.upsipteeo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Alertas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Spinner spinner_estado = findViewById(R.id.spinner_estado);
        Button btn_mapa = findViewById(R.id.btn_mapa);

        toolbar.setOnClickListener(view -> finish());

        ArrayAdapter<CharSequence> adapterspinner_estado = ArrayAdapter.createFromResource(this, R.array.estados, android.R.layout.simple_spinner_item);
        adapterspinner_estado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_estado.setAdapter(adapterspinner_estado);

        btn_mapa.setOnClickListener(view -> {
            startActivity(new Intent(this, Mapa.class));
        });


    }
}