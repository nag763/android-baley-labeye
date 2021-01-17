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

/**
 * This activity is the main activity where you can
 * navigate between all fragments of the app.
 * @author baley
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//instance of all components needed connected with the xml
    /**
     * text which shows the number of museums visited.
     */
    private transient TextView textView;
    /**
     * image of the user.
     */
    private transient ImageView imageView;
    /**
     * storage reference of the image of the user.
     */
    private transient StorageReference stm;
    /**
     * auth of the user.
     */
    private transient FirebaseAuth auth;
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
     * fragment profil.
     */
    private transient Fragment fragmentProfil;
    /**
     * fragment of the museum list.
     */
    private transient Fragment fragmentMuseumList;
    /**
     * fragment of the visits list.
     */
    private transient Fragment fragmentVisitList;
    /**
     * fragment evaluate.
     */
    private transient Fragment fragmentEvaluer;
    /**
     * fragment of statistics.
     */
    private transient Fragment fragmentStatistics;
    /**
     * number of museums visited by the user.
     */
    private transient int count = 0;

    /**
     * on create.
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // instance of the user
        auth = FirebaseAuth.getInstance();
        stm = FirebaseStorage.getInstance().getReference();
        //configuration of the navigation view method called
        this.configureNavigationView();
        //configuration of the toolbar mehod called
        this.configureToolbar();
        drawerLayout = findViewById(R.id.drawer_layout);
        //action bar drawer toggle used to fix when nav drawer is opened or closed
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

    /**
     * method called to show the first fragment when the user is connected : profil fragment.
     */
    private void showFirstFragment() {
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (visibleFragment == null) {
            // 1.1 - Show News Fragment
            this.showProfilFragment();
            // 1.2 - Mark as selected the menu item corresponding to NewsFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    /**
     * configuration of the toolbar.
     */
    private void configureToolbar() {
        this.toolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * configuration of the navigation view.
     */
    private void configureNavigationView() {
        this.navigationView = findViewById(R.id.nav_view);
        //header view
        View hView = navigationView.getHeaderView(0);
        //image of the user
        imageView = hView.findViewById(R.id.imageView);
        afficherImage();
        //print number of museum
        textView = hView.findViewById(R.id.textView);
        afficherNbMusees();
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * print number of museum visited by the user.
     */
    private void afficherNbMusees() {

        FirebaseFirestore.getInstance().collection("visites")
                .whereEqualTo("idProfil", Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("count", "count");
                count = Objects.requireNonNull(task.getResult()).size();
                textView.setText(String.valueOf(count));
            }
        });


    }

    /**
     * print the image of the user.
     */
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

    /**
     * onBackPressed to open or close the drawer.
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
     * show the fragment selected by the user.
     * @param item item
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(getClass().getName(), String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            case R.id.profil:
                this.showProfilFragment();
                break;
            case R.id.listeMusees:
                this.showMuseumListFragment();
                break;
            case R.id.visites:
                this.showMuseumVisitFragment();
                break;
            case R.id.evaluer:
                this.showEvaluerFragment();
                break;
            case R.id.statistics:
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
     * show the profil fragment.
     */
    private void showProfilFragment() {
        if (this.fragmentProfil == null) {
            this.fragmentProfil = ProfilFragment.newInstance();
        }
        this.startTransactionFragment(this.fragmentProfil);
    }

    /**
     * show the museum fragment.
     */
    private void showMuseumListFragment() {
        if (this.fragmentMuseumList == null) {
            this.fragmentMuseumList = MuseumFragment.newInstance();
        }
        this.startTransactionFragment(this.fragmentMuseumList);
    }

    /**
     * show the evaluate fragment.
     */
    private void showEvaluerFragment() {
        if (this.fragmentEvaluer == null) {
            this.fragmentEvaluer = EvaluerFragment.newInstance();
        }
        this.startTransactionFragment(this.fragmentEvaluer);
    }

    /**
     * show the museum fragment.
     */
    private void showMuseumVisitFragment() {
        if (this.fragmentVisitList == null) {
            this.fragmentVisitList = VisitsFragment.newInstance();
        }
        this.startTransactionFragment(this.fragmentVisitList);
    }

    /**
     * show the statistics fragment.
     */
    private void showStatisticsFragment() {
        if (this.fragmentStatistics == null) {
            this.fragmentStatistics = StatisticsFragment.newInstance();
        }
        this.startTransactionFragment(this.fragmentStatistics);
    }

    /**
     * start the transaction of the fragment selected.
     * @param fragment fragment
     */
    private void startTransactionFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        transaction.replace(R.id.activity_main_frame_layout, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }


}