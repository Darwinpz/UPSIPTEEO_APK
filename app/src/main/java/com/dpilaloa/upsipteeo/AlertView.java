package com.dpilaloa.upsipteeo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AlertView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Spinner spinnerState = findViewById(R.id.spinner_estado);
        Button btnMap = findViewById(R.id.btn_mapa);

        toolbar.setOnClickListener(view -> finish());

        ArrayAdapter<CharSequence> adapterSpinnerState = ArrayAdapter.createFromResource(this, R.array.state, android.R.layout.simple_spinner_item);
        adapterSpinnerState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(adapterSpinnerState);

        btnMap.setOnClickListener(view ->  startActivity(new Intent(this, MapView.class)));

    }
}