package com.ticandroid.baley_labeye.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.beans.ProfilBean;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ProfilActivity extends AppCompatActivity {
    private transient ImageView image;
    private transient TextView profil;
    private transient TextView firstName;
    private transient TextView lastName;
    private transient TextView phone;
    private transient TextView town;
    private transient StorageReference stm;
    private transient FirebaseAuth auth;
    private transient static ProfilBean profilBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        profil = (TextView) findViewById(R.id.profil);
        image = (ImageView) findViewById(R.id.image);
        auth = FirebaseAuth.getInstance();
        stm= FirebaseStorage.getInstance().getReference();
        firstName = (TextView) findViewById(R.id.firstname);
        lastName = (TextView) findViewById(R.id.lastname);
        phone = (TextView) findViewById(R.id.phone);
        town = (TextView) findViewById(R.id.town);

        afficherImage();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("profils").document(auth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot docSnap = task.getResult();
                    if(docSnap.exists()){
                        profilBean = Objects.requireNonNull(docSnap).toObject(ProfilBean.class);
                        if(profilBean!=null){
                            bindView(profilBean);
                        }
                    }
                }
            }
        });
    }
    private void afficherImage(){


        StorageReference st = stm.child("users/"+auth.getCurrentUser().getUid());
        File localFile = null;
        try{

            localFile = File.createTempFile("image","png");
        } catch (IOException e) {

            e.printStackTrace();
        }

        st.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                st.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Picasso.with(getBaseContext()).load(uri).into(image);
                        Toast.makeText(ProfilActivity.this, "image chargee", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ProfilActivity.this, "image non chargee", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
    private void bindView(final ProfilBean profilBean) {
        ((TextView) findViewById(R.id.firstname)).setText(profilBean.getFirstname());
        ((TextView) findViewById(R.id.lastname)).setText(profilBean.getLastname());
        ((TextView) findViewById(R.id.phone)).setText(profilBean.getPhone());
        ((TextView) findViewById(R.id.town)).setText(profilBean.getTown());
    }
}