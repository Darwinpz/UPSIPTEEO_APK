package com.dpilaloa.upsipteeo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.dpilaloa.upsipteeo.Adapters.ViewPageAdapter;
import com.dpilaloa.upsipteeo.Controllers.UserController;
import com.dpilaloa.upsipteeo.Fragments.HomeFragment;
import com.dpilaloa.upsipteeo.Fragments.ProfileFragment;
import com.dpilaloa.upsipteeo.Fragments.UserFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class PrimaryView extends AppCompatActivity {

    public static StorageReference storageReference;
    private static final int DOUBLE_CLICK_INTERVAL = 2000;
    private boolean doubleBackToExitPressedOnce = false;
    public static SharedPreferences preferences;
    public static String id = "";
    public static String rol = "";
    public static UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        preferences = getSharedPreferences("upsipteeo",MODE_PRIVATE);
        id = preferences.getString("uid","");
        rol =  preferences.getString("rol","");

        if(!id.isEmpty()) {

            userController = new UserController(MainActivity.databaseReference);
            storageReference = FirebaseStorage.getInstance().getReference();

            ViewPager2 viewPager2 = findViewById(R.id.view_pager);
            TabLayout tabLayout = findViewById(R.id.tablayout);

            ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager(), getLifecycle());

            if(rol.equals(getString(R.string.admin_one))|| rol.equals(getString(R.string.admin_two))) {
                viewPageAdapter.addFragment(new HomeFragment());
                viewPageAdapter.addFragment(new UserFragment());
                viewPageAdapter.addFragment(new ProfileFragment());
                viewPager2.setAdapter(viewPageAdapter);
                viewPager2.setOffscreenPageLimit(3);
                new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(R.string.tab_title_home);
                            break;
                        case 1:
                            tab.setText(R.string.tab_title_users);
                            break;
                        case 2:
                            tab.setText(R.string.tab_title_profile);
                            break;
                    }

                }).attach();

            }else{
                viewPageAdapter.addFragment(new HomeFragment());
                viewPageAdapter.addFragment(new ProfileFragment());
                viewPager2.setAdapter(viewPageAdapter);
                viewPager2.setOffscreenPageLimit(2);
                new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText(R.string.tab_title_home);
                            break;
                        case 1:
                            tab.setText(R.string.tab_title_profile);
                            break;
                    }

                }).attach();
            }
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if(doubleBackToExitPressedOnce){
                    finish();
                }else{
                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(getBaseContext(), "Presione de nuevo para salir", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, DOUBLE_CLICK_INTERVAL);
                }

            }
        });

    }

}