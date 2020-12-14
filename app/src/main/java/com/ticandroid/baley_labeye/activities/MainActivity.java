package com.ticandroid.baley_labeye.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.ticandroid.baley_labeye.R;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private transient ImageView image;
    private transient Button buttonProfil;
    private transient Button listeMusees;
    private transient Button visites;
    private transient Button evaluer;
    private transient Button statistiques;
    private transient Button quitter;
    private transient StorageReference stm;
    private transient FirebaseAuth auth;
    String msg=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        image = (ImageView) findViewById(R.id.image);

        buttonProfil = (Button) findViewById(R.id.profil);
        buttonProfil.setOnClickListener(this);

        listeMusees = (Button) findViewById(R.id.listeMusees);
        listeMusees.setOnClickListener(this);

        visites = (Button) findViewById(R.id.visites);
        visites.setOnClickListener(this);

        evaluer = (Button) findViewById(R.id.evaluer);
        evaluer.setOnClickListener(this);

        statistiques = (Button) findViewById(R.id.statistiques);
        statistiques.setOnClickListener(this);

        quitter = (Button) findViewById(R.id.quitter);
        quitter.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        stm= FirebaseStorage.getInstance().getReference();
        afficherImage();
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

                        Picasso.with(getBaseContext()).load(uri).into(image);
                        Toast.makeText(MainActivity.this, "image chargee", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this, "image non chargee", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {

        Log.d(this.getClass().toString(), String.format("%s clicked", v.toString()));
        if (v.equals(listeMusees)){
            startActivity(new Intent(this, MuseumListActivity.class));
        }
        else if(v.equals(buttonProfil)){
            Intent profil = new Intent(MainActivity.this, ProfilActivity.class);
            startActivity(profil);
            finish();
        }
        else if (v.equals(quitter)) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "déconnecté", Toast.LENGTH_SHORT).show();
            Intent deconnexion = new Intent(MainActivity.this, StartActivity.class);
            startActivity(deconnexion);
            finish();
        }
    }
}
