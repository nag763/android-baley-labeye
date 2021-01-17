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
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.activities.ui.map.MapFragment;
import com.ticandroid.baley_labeye.activities.ui.statistics.StatisticsAdminFragment;

/**
 * admin activity with the navbar for the menu.
 * @author baley
 */
public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //fetch xml content
    /**
     * drawer layout.
     */
    private transient DrawerLayout drawerLayout;
    /**
     * navigation view.
     */
    private transient NavigationView navigationView;
    /**
     * toolbar.
     */
    private transient Toolbar toolbar;
    /**
     * fragment map.
     */
    private transient Fragment fragmentMap;
    /**
     * fragment statistics.
     */
    private transient Fragment fragmentStatistics;

    /**
     * on create the activity.
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the content view with the xml
        setContentView(R.layout.activity_admin);
        //method called to configure the navigation view
        this.configureNavigationView();
        //methode called to configure the toolbar
        this.configureToolbar();
        drawerLayout = findViewById(R.id.drawer_layout);
        //action bar drawer to open or close the drawer
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
        //show the first fragment, the map of all museums
        this.showFirstFragment();
    }

    /**
     * method to configure the toolbar.
     */
    private void configureToolbar() {
        this.toolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * method to configure the navigation view.
     */
    private void configureNavigationView() {
        this.navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * method to show the first fragment.
     */
    private void showFirstFragment() {
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (visibleFragment == null) {
            // 1.1 - Show News Fragment
            this.showMapFragment();
            // 1.2 - Mark as selected the menu item corresponding to NewsFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    /**
     * method to close or open the drawer when the user
     * click on the back button.
     */
    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * method to select the item the user wants to see.
     * @param item item
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map:
                this.showMapFragment();
                break;
            case R.id.stats_admin:
                this.showStatisticsFragment();
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

    /**
     * method to show the statistics fragment
     * call the method to make a transaction of fragment.
     */
    private void showStatisticsFragment() {
        if (this.fragmentStatistics == null) {
            this.fragmentStatistics = StatisticsAdminFragment.newInstance();
        }
        this.startTransactionFragment(this.fragmentStatistics);
    }

    /**
     * method to show the map fragment
     * call the method to make a transaction of fragment.
     */
    private void showMapFragment() {
        if (this.fragmentMap == null) {
            this.fragmentMap = MapFragment.newInstance();
        }
        this.startTransactionFragment(this.fragmentMap);
    }

    /**
     * method to start the transaction of fragment.
     * @param fragment fragment
     */
    private void startTransactionFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.activity_main_frame_layout, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }


}