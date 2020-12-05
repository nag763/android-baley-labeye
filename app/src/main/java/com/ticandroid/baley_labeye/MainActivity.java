package com.ticandroid.baley_labeye;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button profil;
    private Button listeMusees;
    private Button visites;
    private Button evaluer;
    private Button statistiques;
    private Button quitter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profil= (Button) findViewById(R.id.profil);
        profil.setOnClickListener(this);

        listeMusees= (Button) findViewById(R.id.listeMusees);
        listeMusees.setOnClickListener(this);

        visites= (Button) findViewById(R.id.visites);
        visites.setOnClickListener(this);

        evaluer= (Button) findViewById(R.id.evaluer);
        evaluer.setOnClickListener(this);

        statistiques= (Button) findViewById(R.id.statistiques);
        statistiques.setOnClickListener(this);

        quitter= (Button) findViewById(R.id.quitter);
        quitter.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v==quitter){
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this,"déconnecté",Toast.LENGTH_SHORT).show();
            Intent deconnexion = new Intent(MainActivity.this, StartActivity.class);
            startActivity(deconnexion);
            finish();
        }
    }
}