package com.ticandroid.baley_labeye.activities.ui.profil;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.beans.ProfilBean;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @author baley
 * profil fragment of the user
 * show the personal information of the user.
 */
public class ProfilFragment extends Fragment {
    /**
     * image of the user.
     */
    private transient ImageView image;
    /**
     * storage reference of the image.
     */
    private transient StorageReference stm;
    /**
     * auth of the user.
     */
    private transient FirebaseAuth auth;
    /**
     * bean profil used to fetch data in the bean.
     */
    private transient ProfilBean profilBean;

    /**
     * new instance of the fragment.
     * @return  profil fragment
     */
    public static Fragment newInstance() {
        return (new ProfilFragment());
    }

    /**
     * on create view.
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState saved instance state
     * @return root
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        container.clearDisappearingChildren();
        View root = inflater.inflate(R.layout.fragment_profil, container, false);
        //find the image in xml
        image = root.findViewById(R.id.img_user);
        //get the current user
        auth = FirebaseAuth.getInstance();
        //get the reference of the user for the picture
        stm = FirebaseStorage.getInstance().getReference();
        //print the picture of the user
        afficherImage();
        //get the document reference of the user in firestore
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("profils").document(auth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot docSnap = task.getResult();
                if (docSnap.exists()) {
                    //put data in a profil bean
                    profilBean = Objects.requireNonNull(docSnap).toObject(ProfilBean.class);
                    if (profilBean != null) {
                        ((TextView) root.findViewById(R.id.et_firstname)).setText(profilBean.getFirstName());
                        ((TextView) root.findViewById(R.id.et_lastname)).setText(profilBean.getLastName());
                        ((TextView) root.findViewById(R.id.et_phone)).setText(String.format("Telephone : %s", profilBean.getPhone()));
                        ((TextView) root.findViewById(R.id.et_town)).setText(String.format("Ville : %s", profilBean.getTown()));

                    }
                }
            }
        });
        return root;
    }

    /**
     * method to print the picture of the user.
     */
    private void afficherImage() {

        //get the link of the storage reference of the user
        StorageReference st = stm.child("users/" + auth.getCurrentUser().getUid());
        try {
            File localFile = File.createTempFile("image", "png");
            st.getFile(localFile).addOnSuccessListener(taskSnapshot -> st.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //load the picture in the activity
                    Picasso.with(getActivity()).load(uri).into(image);
                }
            }));
        } catch (IOException e) {
            Log.e(getClass().getName(), e.toString());
        }


    }

}