package com.dpilaloa.upsipteeo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dpilaloa.upsipteeo.MainActivity;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.data.controllers.AlertController;
import com.dpilaloa.upsipteeo.ui.adapters.AlertAdapter;

public class AlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Spinner spinnerState = findViewById(R.id.spinner_estado);
        Button btnMap = findViewById(R.id.btn_mapa);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewAlerts);
        TextView txtResult = findViewById(R.id.textViewNotResult);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView txtCount = findViewById(R.id.textViewCount);

        AlertController alertController = new AlertController(MainActivity.databaseReference);
        AlertAdapter alertAdapter = new AlertAdapter(this);

        toolbar.setOnClickListener(view -> finish());

        ArrayAdapter<CharSequence> adapterSpinnerState = ArrayAdapter.createFromResource(this, R.array.alertState, android.R.layout.simple_spinner_item);
        adapterSpinnerState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(adapterSpinnerState);

        btnMap.setOnClickListener(view ->  startActivity(new Intent(this, MapActivity.class)));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(alertAdapter);

        alertController.getAlerts(alertAdapter,txtResult,progressBar,txtCount, databaseError -> {

        });

    }
}