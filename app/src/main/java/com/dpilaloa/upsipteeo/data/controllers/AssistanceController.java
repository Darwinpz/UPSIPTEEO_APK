package com.dpilaloa.upsipteeo.data.controllers;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dpilaloa.upsipteeo.data.interfaces.DbErrorInterface;
import com.dpilaloa.upsipteeo.data.models.Assistance;
import com.dpilaloa.upsipteeo.ui.adapters.AssistanceAdapter;
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

    public void getAssistance(AssistanceAdapter assistanceAdapter, String uid, String photo, TextView textViewResult, ProgressBar progressBar, TextView txtCount, DbErrorInterface dbErrorInterface) {

        progressBar.setVisibility(View.VISIBLE);
        textViewResult.setVisibility(View.VISIBLE);

        databaseReference.child("users").child(uid).child("assistance").orderByChild("dateTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                assistanceAdapter.clear();
                int count = 0;

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        if(snapshot.child("date").exists() && snapshot.child("time").exists()) {

                            Assistance assistance = new Assistance();
                            assistance.uid = snapshot.getKey();
                            assistance.date = snapshot.child("date").getValue(String.class);
                            assistance.time = snapshot.child("time").getValue(String.class);
                            Long date = snapshot.child("dateTime").getValue(Long.class);
                            assistance.dateTime = date != null ? date : 0L;
                            assistance.photo = photo;
                            assistanceAdapter.add(assistance);

                            count++;

                        }
                    }
                    textViewResult.setVisibility(assistanceAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                } else {
                    textViewResult.setVisibility(View.VISIBLE);
                }

                txtCount.setText(String.valueOf(count));
                txtCount.append("\tAsistencias");

                assistanceAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {dbErrorInterface.onProcessError(error);}

        });

    }

    public Task<Void> createAssistance(String uid, Assistance assistance) {

        Map<String, Object> data = new HashMap<>();
        data.put("date",assistance.date);
        data.put("time",assistance.time);
        data.put("dateTime",assistance.dateTime);

        return databaseReference.child("users").child(uid).child("assistance").push().setValue(data);

    }

    public Task<Void> deleteAssistance(String uidUser, String uidAssistance){
        return databaseReference.child("users").child(uidUser).child("assistance").child(uidAssistance).removeValue();
    }


}
