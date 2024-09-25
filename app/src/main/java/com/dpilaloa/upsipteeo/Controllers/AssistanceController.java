package com.dpilaloa.upsipteeo.Controllers;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dpilaloa.upsipteeo.Adapters.AssistanceAdapter;
import com.dpilaloa.upsipteeo.Objects.Assistance;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AssistanceController {

    DatabaseReference databaseReference;

    public AssistanceController(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public void getAssistance(AssistanceAdapter assistanceAdapter, String uid, TextView textViewResult, ProgressBar progressBar, TextView txtCount) {

        progressBar.setVisibility(View.VISIBLE);
        textViewResult.setVisibility(View.VISIBLE);

        databaseReference.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                assistanceAdapter.clear();
                int contador = 0;

                if (dataSnapshot.exists()) {

                    if(dataSnapshot.child("asistencia").exists()) {

                        for (DataSnapshot snapshot : dataSnapshot.child("asistencia").getChildren()) {

                            if(snapshot.child("fecha").exists() && snapshot.child("hora").exists()) {

                                Assistance assistance = new Assistance();
                                assistance.uid = snapshot.getKey();
                                assistance.date = snapshot.child("fecha").getValue(String.class);
                                assistance.time = snapshot.child("hora").getValue(String.class);
                                assistance.photo = dataSnapshot.child("foto").getValue(String.class);
                                assistanceAdapter.add(assistance);

                                contador++;

                            }
                        }

                    }

                    txtCount.setText(String.valueOf(contador));
                    txtCount.append("\tAsistencias");
                    textViewResult.setVisibility(assistanceAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);

                } else {
                    textViewResult.setVisibility(View.VISIBLE);
                }

                assistanceAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {throw error.toException();}

        });

    }

    public Task<Void> createAssistance(String uid, Assistance assistance) {

        Map<String, Object> data = new HashMap<>();
        data.put("fecha",assistance.date);
        data.put("hora",assistance.time);

        return databaseReference.child("usuarios").child(uid).child("asistencia").push().setValue(data);

    }

    public Task<Void> deleteAssistance(String uidUser, String uidAssistance){
        return databaseReference.child("usuarios").child(uidUser).child("asistencia").child(uidAssistance).removeValue();
    }


}
