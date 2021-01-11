package com.ticandroid.baley_labeye.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.activities.ui.map.MapFragment;
import com.ticandroid.baley_labeye.activities.ui.statistics.StatisticsAdminFragment;


public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Fragment fragmentMap;
    private Fragment fragmentStatistics;
    private static final int FRAGMENT_MAP = 0;
    private static final int FRAGMENT_STATISTICS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        StorageReference stm = FirebaseStorage.getInstance().getReference();
        this.configureNavigationView();
        this.configureToolbar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                    @Override
                    public void onDrawerClosed(View v) {
                        super.onDrawerClosed(v);
                    }

                    @Override
                    public void onDrawerOpened(View v) {
                        super.onDrawerOpened(v);
                    }
                };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        this.showFirstFragment();
    }

    private void configureToolbar() {
        this.toolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
    }

    private void configureNavigationView() {
        this.navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void showFirstFragment() {
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (visibleFragment == null) {
            // 1.1 - Show News Fragment
            this.showFragment(FRAGMENT_MAP);
            // 1.2 - Mark as selected the menu item corresponding to NewsFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.map:
                this.showFragment(FRAGMENT_MAP);
                break;
            case R.id.statisticsAdmin:
                this.showFragment(FRAGMENT_STATISTICS);
                break;
            case R.id.quitter:
                Intent deconnexion = new Intent(this, StartActivity.class);
                Toast.makeText(this, "déconnexion", Toast.LENGTH_SHORT).show();
                startActivity(deconnexion);
                finish();
                break;
            default:
                break;
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(int fragmentIdentifier) {
        switch (fragmentIdentifier) {
            case FRAGMENT_MAP:
                this.showMapFragment();
                break;
            case FRAGMENT_STATISTICS:
                this.showStatisticsFragment();
                break;
            default:
                break;
        }
    }

    private void showStatisticsFragment() {
        if (this.fragmentStatistics == null) {
            this.fragmentStatistics = StatisticsAdminFragment.newInstance();
        }
        this.startTransactionFragment(this.fragmentStatistics);
    }

    private void showMapFragment() {
        if (this.fragmentMap == null) {
            this.fragmentMap = MapFragment.newInstance();
        }
        this.startTransactionFragment(this.fragmentMap);
    }

    private void startTransactionFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        transaction.replace(R.id.activity_main_frame_layout, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }


}