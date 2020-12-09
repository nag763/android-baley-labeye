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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ticandroid.baley_labeye.R;

import java.util.HashMap;
import java.util.Map;

public class InscriptionActivity extends AppCompatActivity implements View.OnClickListener {
    private transient EditText lastname;
    private transient EditText firstname;
    private transient EditText phone;
    private transient EditText town;
    private transient EditText email;
    private transient EditText password;
    private transient Button register;
    private transient FirebaseAuth auth;
    private transient FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        lastname = findViewById(R.id.lastname);
        firstname = findViewById(R.id.firstname);
        phone = findViewById(R.id.phone);
        town = findViewById(R.id.town);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final String textLastName = lastname.getText().toString();
        final String textFirstName = firstname.getText().toString();
        final String textPhone = phone.getText().toString();
        final String textTown = town.getText().toString();
        final String textEmail = email.getText().toString();
        final String textPassword = password.getText().toString();

        if (TextUtils.isEmpty(textLastName) || TextUtils.isEmpty(textFirstName)
            || TextUtils.isEmpty(textPhone) || TextUtils.isEmpty(textTown)
            || TextUtils.isEmpty(textEmail) || TextUtils.isEmpty(textPassword)) {
            Toast.makeText(this, "champs manquants", Toast.LENGTH_SHORT).show();
        } else {
            registerUser(textLastName, textFirstName, textPhone, textTown, textEmail, textPassword);
        }
    }

    private void registerUser(String lastName, String firstName, String phone, String town, String email, String password) {
       auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
           @Override
           public void onSuccess(AuthResult authResult) {
                Map<String, Object> data = new HashMap<>();
                data.put("lastname",lastName);
                data.put("firstname",firstName);
                data.put("phone", phone);
                data.put("town", town);
                db.collection("profils").document(auth.getCurrentUser().getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(InscriptionActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                            Intent inscriptionF = new Intent(InscriptionActivity.this, MainActivity.class);
                            startActivity(inscriptionF);
                            finish();
                        }else{
                            Toast.makeText(InscriptionActivity.this, "Inscription ratée", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
       }
        });
    }
}