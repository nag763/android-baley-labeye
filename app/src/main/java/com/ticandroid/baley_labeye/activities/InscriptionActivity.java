package com.ticandroid.baley_labeye.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

/**
 * @author baley
 * This activity is used to register the user
 */
public class InscriptionActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * lastname of the user
     */
    private transient EditText lastName;
    /**
     * firstname of the user
     */
    private transient EditText firstName;
    /**
     * phone of the user
     */
    private transient EditText phone;
    /**
     * town of the user
     */
    private transient EditText town;
    /**
     * email of the user
     */
    private transient EditText email;
    /**
     * password of the user
     */
    private transient EditText password;
    /**
     * register button
     */
    private transient Button register;
    /**
     * picture button
     */
    private transient Button picture;
    //auth and the database
    /**
     * auth of the user
     */
    private transient FirebaseAuth auth;
    /**
     * database
     */
    private transient FirebaseFirestore db;
    /**
     * image of the user selected
     */
    private transient ImageView imageView;
    /**
     * uri of the image selected
     */
    private transient Uri imageUri;
    /**
     * progress dialog
     */
    private transient ProgressDialog progressDialog;
    //code to check if the user allows the app to take a picture from the phone
    /**
     * image pick code
     */
    private static final int IMAGE_PICK_CODE = 1000;
    /**
     * permission code
     */
    private static final int PERMISSION_CODE = 1001;

    /**
     * on create method
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //find element from the xml file
        setContentView(R.layout.activity_inscription);
        lastName = findViewById(R.id.et_lastname);
        firstName = findViewById(R.id.et_firstname);
        phone = findViewById(R.id.et_phone);
        town = findViewById(R.id.et_town);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        imageView = findViewById(R.id.img_user);
        progressDialog = new ProgressDialog(this);
        picture = findViewById(R.id.btn_upload_pic);
        register = findViewById(R.id.btn_register);
        //instance of the current user and the database
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        register.setOnClickListener(this);
        picture.setOnClickListener(this);


    }

    /**
     * method to pick an image from gallery after the authorisation
     */
    private void pickImageFromGallery() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, IMAGE_PICK_CODE);
    }

    /**
     * store the picture in the database after the user is registered
     * On success, the user is redirected in the main activity
     * On failure, the picture is not registered but the user is registered
     */
    private void registerPicture() {
        if (imageUri != null) {
            StorageReference st = FirebaseStorage.getInstance().getReference("users/" + auth.getCurrentUser().getUid());
            st.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();
                Toast.makeText(InscriptionActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                Intent inscriptionF = new Intent(InscriptionActivity.this, MainActivity.class);
                startActivity(inscriptionF);
                finish();
            }).addOnFailureListener(e -> Toast.makeText(InscriptionActivity.this, "photo non ajoutée", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(InscriptionActivity.this, "Veuillez ajouter une photo", Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * register the user with email and password
     * then store in firestore the personal information of the user
     * @param lastName lastname
     * @param firstName firstname
     * @param phone phone
     * @param town town
     * @param email email
     * @param password password
     */
    private void registerUser(String lastName, String firstName, String phone, String town, String email, String password) {
        progressDialog.setMessage("Veuillez patienter");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            Map<String, Object> data = new HashMap<>();
            data.put("lastName", lastName);
            data.put("firstName", firstName);
            data.put("phone", phone);
            data.put("town", town);
            db.collection("profils").document(auth.getCurrentUser().getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //if the user is correctly registered, the picture is registered
                        registerPicture();

                    } else {
                        Toast.makeText(InscriptionActivity.this, "Veuillez ajouter une photo", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    /**
     * on click to check if the user wants to take a picture or
     * to register
     * @param v view
     */
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

    /**
     * when the user took a picture
     * it comes back to the inscription activity with the picture
     * @param requestCode request code
     * @param resultCode result code
     * @param data data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageView.setImageURI(data.getData());
            imageUri = data.getData();
        }
    }

    /**
     * request the permission of the user to see if the user accepted the app to take a picture
     * @param requestCode request code
     * @param permissions permissions
     * @param grantResults grant results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                Toast.makeText(this, "permission refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }
}