package com.materialsteam.materials;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.materialsteam.materials.auth.Login;
import com.materialsteam.materials.auth.SharedPrefManager;
import com.materialsteam.materials.fragments.AkunFragment;
import com.materialsteam.materials.fragments.HomeFragment;
import com.materialsteam.materials.fragments.TukangFragment;

public class BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(BottomNavigation.this, Login.class));
            return;
        }

        loadFragment(new HomeFragment());
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.home_menu:
                fragment = new HomeFragment();
                break;
            case R.id.tukang_menu:
                fragment = new TukangFragment();
                break;
            case R.id.akun_menu:
                fragment = new AkunFragment();
                break;
        }
        return loadFragment(fragment);
    }
}