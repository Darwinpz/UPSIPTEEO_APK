package com.dpilaloa.upsipteeo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.dpilaloa.upsipteeo.Adaptadores.ViewPageAdapter;
import com.dpilaloa.upsipteeo.Controladores.Ctl_usuarios;
import com.dpilaloa.upsipteeo.Fragmentos.Fragmento_Inicio;
import com.dpilaloa.upsipteeo.Fragmentos.Fragmento_Perfil;
import com.dpilaloa.upsipteeo.Fragmentos.Fragmento_Usuarios;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DatabaseReference;

public class Principal extends AppCompatActivity {

    private static final int DOUBLE_CLICK_INTERVAL = 2000;
    private boolean doubleBackToExitPressedOnce = false;
    public static SharedPreferences preferences;
    public static String id = "";
    public static String rol = "";
    public static Ctl_usuarios ctlUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        preferences = getSharedPreferences("upsipteeo",MODE_PRIVATE);
        id = preferences.getString("uid","");
        rol =  preferences.getString("rol","");

        if(!id.isEmpty()) {
            ctlUsuarios = new Ctl_usuarios(MainActivity.databaseReference);

            ViewPager2 viewPager2 = findViewById(R.id.view_pager);
            TabLayout tabLayout = findViewById(R.id.tablayout);

            ViewPageAdapter adaptador = new ViewPageAdapter(getSupportFragmentManager(), getLifecycle());

            if(rol.equals("TICS" )|| rol.equals("TECNICO SUPERVISOR")) {
                adaptador.addFragment(new Fragmento_Inicio());
                adaptador.addFragment(new Fragmento_Usuarios());
                adaptador.addFragment(new Fragmento_Perfil());
                viewPager2.setAdapter(adaptador);
                viewPager2.setOffscreenPageLimit(3);
                new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(R.string.title_home);
                            break;
                        case 1:
                            tab.setText(R.string.title_users);
                            break;
                        case 2:
                            tab.setText(R.string.title_profile);
                            break;
                    }

                }).attach();

            }else{
                adaptador.addFragment(new Fragmento_Inicio());
                adaptador.addFragment(new Fragmento_Perfil());
                viewPager2.setAdapter(adaptador);
                viewPager2.setOffscreenPageLimit(2);
                new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(R.string.title_home);
                            break;
                        case 1:
                            tab.setText(R.string.title_profile);
                            break;
                    }

                }).attach();
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Presione de nuevo para salir", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, DOUBLE_CLICK_INTERVAL);
    }

}