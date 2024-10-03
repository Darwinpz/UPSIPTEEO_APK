package com.dpilaloa.upsipteeo.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.dpilaloa.upsipteeo.MainActivity;
import com.dpilaloa.upsipteeo.R;
import com.dpilaloa.upsipteeo.data.controllers.UserController;
import com.dpilaloa.upsipteeo.ui.adapters.ViewPageAdapter;
import com.dpilaloa.upsipteeo.ui.fragments.HomeFragment;
import com.dpilaloa.upsipteeo.ui.fragments.ProfileFragment;
import com.dpilaloa.upsipteeo.ui.fragments.UserFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class PrimaryActivity extends AppCompatActivity {

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

        preferences = getSharedPreferences(getString(R.string.app_name),MODE_PRIVATE);
        id = preferences.getString("uid","");
        rol =  preferences.getString("rol","");

        if(MainActivity.databaseReference == null){
            MainActivity.databaseReference = FirebaseDatabase.getInstance().getReference();
        } //Get Instance when DatabaseReference is lost

        if(!id.isEmpty()) {

            storageReference = FirebaseStorage.getInstance().getReference();
            userController = new UserController(MainActivity.databaseReference);

            ViewPager2 viewPager2 = findViewById(R.id.viewPagerPrimary);
            TabLayout tabLayout = findViewById(R.id.tabLayoutPrimary);

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
                            tab.setText(getString(R.string.tab_title_home));
                            break;
                        case 1:
                            tab.setText(getString(R.string.tab_title_users));
                            break;
                        case 2:
                            tab.setText(getString(R.string.tab_title_profile));
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
                            tab.setText(getString(R.string.tab_title_home));
                            break;
                        case 1:
                            tab.setText(getString(R.string.tab_title_profile));
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