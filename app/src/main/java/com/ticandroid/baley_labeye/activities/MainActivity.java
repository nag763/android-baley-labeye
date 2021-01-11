package com.ticandroid.baley_labeye.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.activities.ui.evaluer.EvaluerFragment;
import com.ticandroid.baley_labeye.activities.ui.museum.MuseumFragment;
import com.ticandroid.baley_labeye.activities.ui.profil.ProfilFragment;
import com.ticandroid.baley_labeye.activities.ui.statistics.StatisticsFragment;
import com.ticandroid.baley_labeye.activities.ui.visits.VisitsFragment;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private transient TextView textView;
    private transient ImageView imageView;
    private transient StorageReference stm;
    private transient FirebaseAuth auth;
    private transient DrawerLayout drawerLayout;
    private transient NavigationView navigationView;
    private transient Toolbar toolbar;
    private transient Fragment fragmentProfil;
    private transient Fragment fragmentMuseumList;
    private transient Fragment fragmentVisitList;
    private transient Fragment fragmentEvaluer;
    private transient Fragment fragmentStatistics;
    private transient int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        stm = FirebaseStorage.getInstance().getReference();
        this.configureNavigationView();
        this.configureToolbar();
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
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

    private void showFirstFragment() {
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (visibleFragment == null) {
            // 1.1 - Show News Fragment
            this.showProfilFragment();
            // 1.2 - Mark as selected the menu item corresponding to NewsFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }


    private void configureToolbar() {
        this.toolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavigationView() {
        this.navigationView = findViewById(R.id.nav_view);
        // NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        imageView = hView.findViewById(R.id.imageView);
        afficherImage();

        textView = hView.findViewById(R.id.textView);
        afficherNbMusees();
        //textView.setText(afficherNbMusees());
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void afficherNbMusees() {

        FirebaseFirestore.getInstance().collection("visites")
                .whereEqualTo("idProfil", Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("count", "count");
                count = Objects.requireNonNull(task.getResult()).size();
                textView.setText(count);
            }
        });


    }

    private void afficherImage() {


        StorageReference st = stm.child("users/" + Objects.requireNonNull(auth.getCurrentUser()).getUid());
        try {
            File localFile = File.createTempFile("image", "png");
            st.getFile(localFile).addOnSuccessListener(taskSnapshot -> st.getDownloadUrl().addOnSuccessListener(uri ->
                    Picasso.with(getBaseContext()).load(uri).into(imageView))
                    .addOnFailureListener(e -> {


                    }));
        } catch (IOException e) {
            Log.e(getClass().getName(), e.toString());
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
        switch (item.getOrder()) {
            case 0:
                this.showProfilFragment();
                break;
            case 1:
                this.showMuseumListFragment();
                break;
            case 2:
                this.showMuseumVisitFragment();
                break;
            case 3:
                this.showEvaluerFragment();
                break;
            case 4:
                this.showStatisticsFragment();
                break;
            case 5:
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

    private void showProfilFragment() {
        if (this.fragmentProfil == null) {
            this.fragmentProfil = ProfilFragment.newInstance();
        }
        this.startTransactionFragment(this.fragmentProfil);
    }

    private void showMuseumListFragment() {
        if (this.fragmentMuseumList == null) {
            this.fragmentMuseumList = MuseumFragment.newInstance();
        }
        this.startTransactionFragment(this.fragmentMuseumList);
    }

    private void showEvaluerFragment() {
        if (this.fragmentEvaluer == null) {
            this.fragmentEvaluer = EvaluerFragment.newInstance();
        }
        this.startTransactionFragment(this.fragmentEvaluer);
    }


    private void showMuseumVisitFragment() {
        if (this.fragmentVisitList == null) {
            this.fragmentVisitList = VisitsFragment.newInstance();
        }
        this.startTransactionFragment(this.fragmentVisitList);
    }

    private void showStatisticsFragment() {
        if (this.fragmentStatistics == null) {
            this.fragmentStatistics = StatisticsFragment.newInstance();
        }
        this.startTransactionFragment(this.fragmentStatistics);
    }

    private void startTransactionFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        transaction.replace(R.id.activity_main_frame_layout, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }


}