package com.ticandroid.baley_labeye;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ConnexionActivity extends AppCompatActivity implements View.OnClickListener {
    private transient EditText email;
    private transient EditText password;
    private transient Button login;
    private transient FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String txt_email = email.getText().toString();
        String txt_password = password.getText().toString();
        loginUser(txt_email, txt_password);
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(ConnexionActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                Intent connexionF = new Intent(ConnexionActivity.this, MainActivity.class);
                startActivity(connexionF);
                finish();
            }
        });
    }
}