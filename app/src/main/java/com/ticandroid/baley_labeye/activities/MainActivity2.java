package com.ticandroid.baley_labeye.activities;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.activities.ui.evaluer.EvaluerFragment;
import com.ticandroid.baley_labeye.activities.ui.museum.MuseumFragment;
import com.ticandroid.baley_labeye.activities.ui.profil.ProfilFragment;
import com.ticandroid.baley_labeye.activities.ui.visits.VisitsFragment;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import java.io.File;
import java.io.IOException;


public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private ImageView imageView;
    private transient StorageReference stm;
    private transient FirebaseAuth auth;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Fragment fragmentProfil;
    private Fragment fragmentMuseumList;
    private Fragment fragmentVisitList;
    private Fragment fragmentEvaluer;
    private static final int FRAGMENT_PROFIL = 0;
    private static final int FRAGMENT_LISTE_MUSEE = 1;
    private static final int FRAGMENT_LIST_VISITS = 2;
    private static final int FRAGMENT_EVALUER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        auth = FirebaseAuth.getInstance();
        stm= FirebaseStorage.getInstance().getReference();
        this.configureNavigationView();
        this.configureToolbar();
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle =new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }
            @Override
            public void onDrawerOpened(View v){
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        this.showFirstFragment();
    }
    private void showFirstFragment(){
        Fragment visibleFragment = getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);
        if (visibleFragment == null){
            // 1.1 - Show News Fragment
            this.showFragment(FRAGMENT_PROFIL);
            // 1.2 - Mark as selected the menu item corresponding to NewsFragment
            this.navigationView.getMenu().getItem(0).setChecked(true);
        }
    }


    private void configureToolbar() {
        this.toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void configureNavigationView(){
        this.navigationView = findViewById(R.id.nav_view);
        // NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        imageView = hView.findViewById(R.id.imageView);
        afficherImage();
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void afficherImage(){


        StorageReference st = stm.child("users/"+auth.getCurrentUser().getUid());
        File localFile = null;
        try{

            localFile = File.createTempFile("image","png");
        } catch (IOException e) {

            e.printStackTrace();
        }

        st.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                st.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.with(getBaseContext()).load(uri).into(imageView);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                    }
                });
            }
        });
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
    public boolean onNavigationItemSelected (@NonNull MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.profil:
                this.showFragment(FRAGMENT_PROFIL);
                break;
            case R.id.listeMusees:
                this.showFragment(FRAGMENT_LISTE_MUSEE);
                break;
            case R.id.visites:
                this.showFragment(FRAGMENT_LIST_VISITS);
                break;
            case R.id.evaluer:
                this.showFragment(FRAGMENT_EVALUER);
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

        private void showFragment ( int fragmentIdentifier){
        switch (fragmentIdentifier) {
            case FRAGMENT_PROFIL:
                this.showProfilFragment();
                break;
            case FRAGMENT_LISTE_MUSEE:
                this.showMuseumListFragment();
                break;
            case FRAGMENT_LIST_VISITS:
                this.showMuseumVisitFragment();
                break;

            case FRAGMENT_EVALUER:
                this.showEvaluerFragment();
                break;
            default:
                break;
        }
    }

        private void showProfilFragment () {
        if (this.fragmentProfil == null) this.fragmentProfil = ProfilFragment.newInstance();
        this.startTransactionFragment(this.fragmentProfil);
    }

        private void showMuseumListFragment() {
            if (this.fragmentMuseumList == null) this.fragmentMuseumList = MuseumFragment.newInstance();
            this.startTransactionFragment(this.fragmentMuseumList);
        }
        private void showEvaluerFragment(){
            if(this.fragmentEvaluer == null) this.fragmentEvaluer = EvaluerFragment.newInstance();
            this.startTransactionFragment(this.fragmentEvaluer);
        }


    private void showMuseumVisitFragment() {
        if (this.fragmentVisitList == null) this.fragmentVisitList = VisitsFragment.newInstance();
        this.startTransactionFragment(this.fragmentVisitList);
    }

        private void startTransactionFragment (Fragment fragment){

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


            transaction.replace(R.id.activity_main_frame_layout, fragment);
            transaction.addToBackStack(null);

            transaction.commit();
    }



}