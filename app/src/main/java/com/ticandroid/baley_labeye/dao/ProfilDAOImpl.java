package com.ticandroid.baley_labeye.dao;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
public class ProfilDAOImpl  {
    private StorageReference stm;
    private FirebaseAuth auth;
    private Uri downloadUrl;
    public ProfilDAOImpl(){

    }
    public Uri getDownloadUrl(){return this.downloadUrl;}
    public void setAuth(){
        auth=FirebaseAuth.getInstance();
    }
    public void setStm(){
        stm= FirebaseStorage.getInstance().getReference();
    }


   /* public void afficherImage(){

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
                        downloadUrl=uri;
                       // Picasso.with(getClass()).load(uri).into(profilBean.getImage());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                    }
                });
            }
        });
    }*/
}
