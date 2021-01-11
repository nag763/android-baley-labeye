package com.ticandroid.baley_labeye.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ticandroid.baley_labeye.R;

import java.util.HashMap;
import java.util.Map;


public class InscriptionActivity extends AppCompatActivity implements View.OnClickListener {
    private transient EditText lastName;
    private transient EditText firstName;
    private transient EditText phone;
    private transient EditText town;
    private transient EditText email;
    private transient EditText password;
    private transient Button register;
    private transient Button picture;
    private transient FirebaseAuth auth;
    private transient FirebaseFirestore db;
    private transient ImageView imageView;
    private transient Uri imageUri;
    private transient ProgressDialog progressDialog;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        lastName = findViewById(R.id.lastname);
        firstName = findViewById(R.id.firstname);
        phone = findViewById(R.id.phone);
        town = findViewById(R.id.town);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        imageView = findViewById(R.id.image);
        progressDialog = new ProgressDialog(this);
        picture = findViewById(R.id.picture);
        register = findViewById(R.id.register);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        register.setOnClickListener(this);
        picture.setOnClickListener(this);


    }

    private void pickImageFromGallery() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, IMAGE_PICK_CODE);
    }

    private void registerPicture() {
        if (imageUri != null) {
            StorageReference st = FirebaseStorage.getInstance().getReference("users/" + auth.getCurrentUser().getUid());
            st.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(InscriptionActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    Intent inscriptionF = new Intent(InscriptionActivity.this, MainActivity.class);
                    startActivity(inscriptionF);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(InscriptionActivity.this, "photo non ajoutée", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(InscriptionActivity.this, "Veuillez ajouter une photo", Toast.LENGTH_SHORT).show();

        }
    }

    private void registerUser(String lastName, String firstName, String phone, String town, String email, String password) {
        progressDialog.setMessage("Veuillez patienter");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Map<String, Object> data = new HashMap<>();
                data.put("lastName", lastName);
                data.put("firstName", firstName);
                data.put("phone", phone);
                data.put("town", town);
                db.collection("profils").document(auth.getCurrentUser().getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            registerPicture();

                        } else {
                            Toast.makeText(InscriptionActivity.this, "Veuillez ajouter une photo", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.equals(picture)) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, PERMISSION_CODE);
            } else {
                pickImageFromGallery();
            }
        } else if (v.equals(register)) {
            final String textLastName = lastName.getText().toString();
            final String textFirstName = firstName.getText().toString();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageView.setImageURI(data.getData());
            imageUri = data.getData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "permission refusée", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}