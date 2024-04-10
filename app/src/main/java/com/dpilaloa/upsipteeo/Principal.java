package com.dpilaloa.upsipteeo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.dpilaloa.upsipteeo.Adaptadores.ViewPageAdapter;
import com.dpilaloa.upsipteeo.Fragmentos.Fragmento_Inicio;
import com.dpilaloa.upsipteeo.Fragmentos.Fragmento_Perfil;
import com.dpilaloa.upsipteeo.Fragmentos.Fragmento_Usuarios;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPager2 viewPager2 = (ViewPager2) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);

        ViewPageAdapter adaptador = new ViewPageAdapter(getSupportFragmentManager(), getLifecycle());

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

    }
}