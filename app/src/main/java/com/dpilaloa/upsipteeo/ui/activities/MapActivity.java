package com.dpilaloa.upsipteeo.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dpilaloa.upsipteeo.MainActivity;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.data.models.Alert;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        MainActivity.databaseReference.child("alerts").orderByChild("dateTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    googleMap.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Alert alert = new Alert();
                        alert.uid = snapshot.getKey();
                        Double latitude = snapshot.child("latitude").getValue(Double.class);
                        Double longitude = snapshot.child("longitude").getValue(Double.class);
                        alert.latitude = latitude !=null ? latitude : 0.0;
                        alert.longitude = longitude !=null ? longitude : 0.0;
                        alert.type = snapshot.child("type").getValue(String.class);
                        alert.state = snapshot.child("state").getValue(String.class);

                        if (alert.state != null && alert.state.equals("NUEVO")) {
                            LatLng latLng = new LatLng(alert.latitude, alert.longitude);
                            MarkerOptions cda = new MarkerOptions();
                            cda.position(latLng).title(alert.type).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).draggable(false);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                            Objects.requireNonNull(googleMap.addMarker(cda)).showInfoWindow();
                        }

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {Toast.makeText(getApplicationContext(), getString(R.string.msgSearchDbError),Toast.LENGTH_SHORT).show();}

        });

    }
}