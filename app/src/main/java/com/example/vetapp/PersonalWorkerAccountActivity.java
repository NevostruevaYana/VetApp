package com.example.vetapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.vetapp.database.Schedule;
import com.example.vetapp.database.Service;
import com.example.vetapp.fragments.ClientTableFragment;
import com.example.vetapp.fragments.DrugRequestTableFragment;
import com.example.vetapp.fragments.DrugTableFragment;
import com.example.vetapp.fragments.PetTableFragment;
import com.example.vetapp.fragments.ProfileFragment;
import com.example.vetapp.fragments.ScheduleTableFragment;
import com.example.vetapp.fragments.ServiceTableFragment;
import com.example.vetapp.fragments.WorkerTableFragment;
import com.google.android.material.navigation.NavigationView;

import static com.example.vetapp.utils.Utils.APP_PREFERENCES_TOKEN;
import static com.example.vetapp.utils.Utils.mSettings;

public class PersonalWorkerAccountActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_TOKEN, null);
                editor.apply();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.my_profile:
                ProfileFragment profile_fragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, profile_fragment).commit();
                break;
            case R.id.menu_employees:
                WorkerTableFragment table_emp = new WorkerTableFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, table_emp).commit();
                break;
            case R.id.menu_clients:
                ClientTableFragment table_cl = new ClientTableFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, table_cl).commit();
                break;
            case R.id.menu_pets:
                PetTableFragment table_pet = new PetTableFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, table_pet).commit();
                break;
            case R.id.menu_drugs:
                DrugTableFragment table_drug = new DrugTableFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, table_drug).commit();
                break;
            case R.id.menu_requests:
                DrugRequestTableFragment table_drug_req = new DrugRequestTableFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, table_drug_req).commit();
                break;
            case R.id.menu_services:
                ServiceTableFragment table_s = new ServiceTableFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, table_s).commit();
                break;
            case R.id.menu_schedule:
                ScheduleTableFragment table_sch = new ScheduleTableFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, table_sch).commit();
                break;
            default:
                break;
        }
        return true;
    }
}