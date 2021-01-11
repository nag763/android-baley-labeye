package com.ticandroid.baley_labeye.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ticandroid.baley_labeye.R;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private transient Button register;
    private transient Button login = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        register = (Button) findViewById(R.id.btn_register);
        login = (Button) findViewById(R.id.btn_login);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.equals(register)) {
            Intent inscription = new Intent(this, InscriptionActivity.class);
            startActivity(inscription);
            //finish();
        } else if (v.equals(login)) {
            Intent connexion = new Intent(this, ConnexionActivity.class);
            startActivity(connexion);
            // finish();
        }


    }

}


