package com.ticandroid.baley_labeye.activities.ui.profil;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class ProfilFragment extends Fragment {

    private transient ImageView image;
    private transient StorageReference stm;
    private transient FirebaseAuth auth;
    private transient ProfilBean profilBean;

    public static Fragment newInstance() {
        return (new ProfilFragment());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        container.clearDisappearingChildren();
        View root = inflater.inflate(R.layout.fragment_profil, container, false);

        image = root.findViewById(R.id.image);
        auth = FirebaseAuth.getInstance();
        stm = FirebaseStorage.getInstance().getReference();

        afficherImage();
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("profils").document(auth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot docSnap = task.getResult();
                    if (docSnap.exists()) {
                        profilBean = Objects.requireNonNull(docSnap).toObject(ProfilBean.class);
                        if (profilBean != null) {
                            ((TextView) root.findViewById(R.id.firstname)).setText(profilBean.getFirstName());
                            ((TextView) root.findViewById(R.id.lastname)).setText(profilBean.getLastName());
                            ((TextView) root.findViewById(R.id.phone)).setText("Telephone : " + profilBean.getPhone());
                            ((TextView) root.findViewById(R.id.town)).setText("Ville : " + profilBean.getTown());

                        }
                    }
                }
            }
        });
        return root;
    }

    private void afficherImage() {


        StorageReference st = stm.child("users/" + auth.getCurrentUser().getUid());
        try {
            File localFile = File.createTempFile("image", "png");
            st.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    st.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Picasso.with(getActivity()).load(uri).into(image);
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}