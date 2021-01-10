package com.ticandroid.baley_labeye.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ticandroid.baley_labeye.R;

public class ConnexionActivity extends AppCompatActivity implements View.OnClickListener {
    private transient EditText email;
    private transient EditText password;
    private transient Button login;
    private transient FirebaseAuth auth;
    private transient TextView title;
    // TODO : Fix that
    private transient ProgressDialog progressDialog;

    private transient final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();
        login.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        progressDialog = new ProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        final String txtEmail = email.getText().toString();
        final String txtPassword = password.getText().toString();
        loginUser(txtEmail, txtPassword);
    }

    private void loginUser(String email, String password) {
        progressDialog.setMessage("Veuillez patienter");
        progressDialog.show();
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(ConnexionActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();

                    FirebaseUser user= auth.getCurrentUser();
                    DocumentReference document = firebaseFirestore.collection("profils").document(user.getUid());
                    Task<DocumentSnapshot> task2 = document.get();
                    task2.addOnCompleteListener(k -> {
                        try {
                            if (task2.getResult().getBoolean("isAdmin")) {
                                startActivity(new Intent(ConnexionActivity.this, AdminActivity.class));
                            } else {
                                startActivity(new Intent(ConnexionActivity.this, MainActivity2.class));
                            }
                        } catch (NullPointerException e) {
                            startActivity(new Intent(ConnexionActivity.this, MainActivity2.class));
                        } finally {
                            finish();
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(ConnexionActivity.this, "Connexion refusée", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(ConnexionActivity.this, "Connexion refusée", Toast.LENGTH_SHORT).show();
        });

    }
}
