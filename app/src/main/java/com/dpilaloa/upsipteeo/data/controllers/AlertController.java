package com.dpilaloa.upsipteeo.data.controllers;

import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dpilaloa.upsipteeo.data.interfaces.DbErrorInterface;
import com.dpilaloa.upsipteeo.data.models.Alert;
import com.dpilaloa.upsipteeo.ui.adapters.AlertAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class AlertController {

    DatabaseReference databaseReference;

    public AlertController(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public void getAlerts(AlertAdapter alertAdapter, TextView textViewResult, ProgressBar progressBar, TextView txtCount, DbErrorInterface dbErrorInterface) {

        progressBar.setVisibility(View.VISIBLE);
        textViewResult.setVisibility(View.VISIBLE);

        databaseReference.child("alerts").orderByChild("dateTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                alertAdapter.clear();
                int count = 0;

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Alert alert = new Alert();
                        alert.uid = snapshot.getKey();
                        alert.date = snapshot.child("date").getValue(String.class);
                        alert.time = snapshot.child("time").getValue(String.class);
                        alert.name = snapshot.child("name").getValue(String.class);
                        alert.state = snapshot.child("state").getValue(String.class);
                        alert.canton = snapshot.child("canton").getValue(String.class);
                        alert.site = snapshot.child("site").getValue(String.class);
                        alert.type = snapshot.child("type").getValue(String.class);
                        alertAdapter.add(alert);

                        count++;

                    }
                    textViewResult.setVisibility(alertAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                } else {
                    textViewResult.setVisibility(View.VISIBLE);
                }

                txtCount.setText(TextUtils.concat(String.valueOf(count) , " ", "Alertas" ));

                alertAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {dbErrorInterface.onProcessError(error);}

        });

    }

}
