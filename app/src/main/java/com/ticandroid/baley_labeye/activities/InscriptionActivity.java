package com.ticandroid.baley_labeye.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.ticandroid.baley_labeye.R;

public class InscriptionActivity extends AppCompatActivity implements View.OnClickListener {
    private transient EditText email;
    private transient EditText password;
    private transient Button register;
    private transient FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        auth = FirebaseAuth.getInstance();
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String textEmail = email.getText().toString();
        final String textPassword = password.getText().toString();

        if (TextUtils.isEmpty(textEmail) || TextUtils.isEmpty(textPassword)) {
            Toast.makeText(this, "champs manquants", Toast.LENGTH_SHORT).show();
        } else {
            registerUser(textEmail, textPassword);
        }
    }

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(InscriptionActivity.this, "inscription reussie", Toast.LENGTH_SHORT).show();
                    Intent inscriptionF = new Intent(InscriptionActivity.this, MainActivity.class);
                    startActivity(inscriptionF);
                    finish();
                } else {
                    Toast.makeText(InscriptionActivity.this, "inscription ratée", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}