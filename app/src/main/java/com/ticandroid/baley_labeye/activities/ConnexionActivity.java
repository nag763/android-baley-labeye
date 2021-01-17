package com.ticandroid.baley_labeye.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.utils.ErrorHandler;

/**
 * @author baley
 * activity connexion to login the user to the app
 */
public class ConnexionActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * email of the user
     */
    private transient EditText email;
    /**
     * password of the user
     */
    private transient EditText password;
    /**
     * auth of the current user
     */
    private transient FirebaseAuth auth;
    /**
     * progress dialog
     */
    private transient ProgressDialog progressDialog;
    /**
     * context
     */
    private transient Context context;
    /**
     * instance of the firestore database
     */
    private transient final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    /**
     * on creating the activity
     * fetch the xml content
     * @param savedInstanceState instance saved
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_connexion);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        Button login = findViewById(R.id.btn_login);
        auth = FirebaseAuth.getInstance();
        login.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

    }

    /**
     * On click listener to call the loginUser method to login the user
     * @param v view
     */
    @Override
    public void onClick(View v) {
        final String txtEmail = email.getText().toString();
        final String txtPassword = password.getText().toString();
        loginUser(txtEmail, txtPassword);
    }

    /**
     * login the user with email and password
     * @param email email
     * @param password password
     */
    private void loginUser(String email, String password) {
        progressDialog.setMessage("Veuillez patienter");
        progressDialog.show();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                Toast.makeText(ConnexionActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();

                FirebaseUser user = auth.getCurrentUser();
                DocumentReference document = firebaseFirestore.collection("profils").document(user.getUid());
                Task<DocumentSnapshot> task2 = document.get();
                task2.addOnCompleteListener(k -> {
                    try {
                        //if the user is admin redirection to the main activity of the admin
                        if (task2.getResult().getBoolean("isAdmin")) {
                            startActivity(new Intent(ConnexionActivity.this, AdminActivity.class));
                        } else {
                            //else redirection to the main activity of a user who is not admin
                            startActivity(new Intent(ConnexionActivity.this, MainActivity.class));
                        }
                    } catch (Exception e) {
                        startActivity(new Intent(ConnexionActivity.this, MainActivity.class));
                    } finally {
                        finish();
                    }
                }).addOnFailureListener(_failure -> ErrorHandler.failure((AppCompatActivity) context));
            } else {
                progressDialog.dismiss();
                Toast.makeText(ConnexionActivity.this, "Connexion refusée", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(ConnexionActivity.this, "Connexion refusée", Toast.LENGTH_SHORT).show();
        });

    }
}
