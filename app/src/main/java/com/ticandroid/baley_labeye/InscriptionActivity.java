package com.ticandroid.baley_labeye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class InscriptionActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email;
    private EditText password;
    private Button register;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        auth = FirebaseAuth.getInstance();
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String text_email = email.getText().toString();
        String text_password = password.getText().toString();

        if(TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_password)){
            Toast.makeText(this,"champs manquants",Toast.LENGTH_SHORT).show();
        } else{
            registerUser(text_email,text_password);
        }
    }

    private void registerUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(InscriptionActivity.this,"inscription reussie",Toast.LENGTH_SHORT).show();
                    Intent inscriptionF = new Intent(InscriptionActivity.this,MainActivity.class);
                    startActivity(inscriptionF);
                    finish();
                } else{
                    Toast.makeText(InscriptionActivity.this,"inscription ratée",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}