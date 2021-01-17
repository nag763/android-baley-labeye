package com.ticandroid.baley_labeye.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ticandroid.baley_labeye.R;

/**
 * @author baley
 * start activity to show a button to register
 * and a button to login
 */
public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private transient Button register;
    private transient Button login = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //find the xml associated
        setContentView(R.layout.activity_start);
        //find the buttons associated
        register = findViewById(R.id.btn_register);
        login = findViewById(R.id.btn_login);
        //set an action on click on each button
        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    public void onClick(View v) {
        //if register clicked, it start the inscription activity
        if (v.equals(register)) {
            Intent inscription = new Intent(this, InscriptionActivity.class);
            startActivity(inscription);
            //else login, it start the connexion activity
        } else if (v.equals(login)) {
            Intent connexion = new Intent(this, ConnexionActivity.class);
            startActivity(connexion);

        }


    }

}


