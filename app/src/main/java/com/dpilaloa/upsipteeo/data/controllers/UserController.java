package com.dpilaloa.upsipteeo.data.controllers;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dpilaloa.upsipteeo.data.interfaces.UserInterface;
import com.dpilaloa.upsipteeo.ui.adapters.UserAdapter;
import com.dpilaloa.upsipteeo.data.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UserController {

    DatabaseReference databaseReference;

    public UserController(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public Task<Void> createUser(User user){
        return databaseReference.child("users").push().setValue(user);
    }

    public Task<Void> deleteUser(String uid){
        return databaseReference.child("users").child(uid).removeValue();
    }

    public Task<Void> updateUser(User user) {

        Map<String, Object> data = new HashMap<>();
        data.put("ced", user.ced);
        data.put("name", user.name.toUpperCase());
        data.put("email", user.email.toLowerCase());
        data.put("phone", user.phone);
        data.put("canton", user.canton);
        data.put("rol", user.rol);
        data.put("password",user.password);

        return databaseReference.child("users").child(user.uid).updateChildren(data);

    }

    public Task<Void> updatePhoto(String uid, String photo) {

        return databaseReference.child("users").child(uid).updateChildren(Collections.singletonMap("photo", photo));

    }


    public void getProfile(String uid, UserInterface userInterface){

        databaseReference.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    User user = new User();
                    user.uid = uid;
                    user.ced = dataSnapshot.child("ced").getValue(String.class);
                    user.name = dataSnapshot.child("name").getValue(String.class);
                    user.canton = dataSnapshot.child("canton").getValue(String.class);
                    user.phone = dataSnapshot.child("phone").getValue(String.class);
                    user.rol = dataSnapshot.child("rol").getValue(String.class);
                    user.password = dataSnapshot.child("password").getValue(String.class);
                    user.email = dataSnapshot.child("email").getValue(String.class);
                    user.photo = dataSnapshot.child("photo").getValue(String.class);

                    userInterface.getUser(user);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {throw databaseError.toException();}

        });

    }


    public void getUsers(UserAdapter userAdapter, String uid, String rol, String filter, TextView textViewResult, ProgressBar progressBar, TextView txtCount) {

        progressBar.setVisibility(View.VISIBLE);
        textViewResult.setVisibility(View.VISIBLE);
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userAdapter.clear();
                int contador = 0;

                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        User user = new User();
                        user.uid = snapshot.getKey();
                        user.ced = snapshot.child("ced").getValue(String.class);
                        user.name = snapshot.child("name").getValue(String.class);
                        user.canton = snapshot.child("canton").getValue(String.class);
                        user.phone = snapshot.child("phone").getValue(String.class);
                        user.rol = snapshot.child("rol").getValue(String.class);
                        user.photo = snapshot.child("photo").getValue(String.class);

                        if (user.ced !=null && user.ced.trim().contains(filter.trim().toLowerCase()) ||
                                user.name !=null && user.name.toLowerCase().trim().contains(filter.trim().toLowerCase()) ||
                                user.canton!=null && user.canton.toLowerCase().trim().contains(filter.trim().toLowerCase())) {

                            if(user.rol!=null && (rol.equals("Rol") || user.rol.equals(rol))) {
                                assert user.uid != null;
                                if (!user.uid.equals(uid)) {
                                    userAdapter.add(user);
                                    contador++;
                                }
                            }

                        }

                    }

                    txtCount.setText(String.valueOf(contador));
                    txtCount.append("\tUsuarios");
                    textViewResult.setVisibility(userAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);

                } else {
                    textViewResult.setVisibility(View.VISIBLE);
                }

                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {throw error.toException();}

        });

    }


    public void logOut(SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("uid","");
        editor.putString("rol","");
        editor.apply();
    }

    public boolean valCed(String ced){

        if(ced != null && ced.length() == 10) {
            int suma = 0;
            for (int i = 0; i < 9; i++) {
                int coefficient = ((i % 2) == 0) ? 2 : 1;
                int cal = Integer.parseInt(String.valueOf(ced.charAt(i))) * coefficient;
                suma += (cal >= 10) ? cal - 9 : cal;
            }
            int res = suma % 10;
            int valor = (res == 0) ? 0 : (10 - res);
            return Integer.parseInt(String.valueOf(ced.charAt(9))) == valor;
        }
        return false;
    }

    public boolean valEmail(String email){
        Pattern patron = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.([a-zA-Z]{2,4})+$");
        return patron.matcher(email).matches();
    }

    public boolean valUser(String username){
        Pattern patron = Pattern.compile("^[ a-zA-ZáéíóúÁÉÍÓÚüÜñÑ]+$");
        return patron.matcher(username).matches();
    }

    public boolean valPhone(String phone) {
        return phone != null &&
                phone.length() == 10 &&
                Pattern.compile("^(0|593)?9[0-9]\\d{7}$").matcher(phone).matches();
    }

}
