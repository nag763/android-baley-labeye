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
import com.ticandroid.baley_labeye.activities.ui.profil.ProfilFragment;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    private static final int FRAGMENT_PROFIL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        auth = FirebaseAuth.getInstance();
        stm= FirebaseStorage.getInstance().getReference();
        this.configureNavigationView();
        this.configureToolbar();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    }

    private void configureToolbar() {
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void configureNavigationView(){
        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        // NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        imageView = (ImageView) hView.findViewById(R.id.imageView);
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
            case R.id.quitter:
                Intent deconnexion = new Intent(this, StartActivity.class);
                Toast.makeText(this, "déconnexion", Toast.LENGTH_SHORT).show();
                startActivity(deconnexion);
                finish();
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
            default:
                break;
        }
    }

        private void showProfilFragment () {
        if (this.fragmentProfil == null) this.fragmentProfil = ProfilFragment.newInstance();
        this.startTransactionFragment(this.fragmentProfil);
    }

        private void startTransactionFragment (Fragment fragment){
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }



}