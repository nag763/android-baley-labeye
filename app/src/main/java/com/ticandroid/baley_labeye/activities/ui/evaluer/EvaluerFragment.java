package com.ticandroid.baley_labeye.activities.ui.evaluer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.activities.ui.profil.ProfilFragment;
import com.ticandroid.baley_labeye.beans.MuseumBean;
import com.ticandroid.baley_labeye.beans.ProfilBean;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class EvaluerFragment extends Fragment implements RatingBar.OnRatingBarChangeListener {
    private transient Button button;
    private RatingBar ratingBar;
    private transient TextView museum;
    private transient StorageReference stm;
    private transient FirebaseAuth auth;
    private transient static MuseumBean museumBean;
    private transient TextView number;
    public static Fragment newInstance() {
        return (new EvaluerFragment());
    }
    private String idVisite;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.clearDisappearingChildren();
        View root = inflater.inflate(R.layout.fragment_evaluer, container, false);
        museum = root.findViewById(R.id.museum);
        button = root.findViewById(R.id.getRating);
        number = root.findViewById(R.id.number);
        auth = FirebaseAuth.getInstance();
        stm= FirebaseStorage.getInstance().getReference();
        ratingBar = root.findViewById(R.id.rating);
       // ratingBar.setRating((float) 3.5);
        //number.setText(ratingBar.getRating()+"/"+ratingBar.getNumStars());
        ratingBar.setOnRatingBarChangeListener(this);



        FirebaseFirestore.getInstance().collection("visites").whereEqualTo("idProfil",auth.getCurrentUser().getUid())
                .orderBy("date", Query.Direction.DESCENDING).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        ((TextView) root.findViewById(R.id.museum)).setText(doc.get("nomDuMusee").toString());
                        if(doc.get("evaluation")!=null) {
                            ((TextView) root.findViewById(R.id.number)).setText(doc.get("evaluation").toString() + "/" + ratingBar.getNumStars());
                            double d =(double) doc.get("evaluation");
                            float f = (float) d;
                            ratingBar.setRating(f);
                        }
                        else{
                            ratingBar.setRating((float) 3.5);
                            number.setText(ratingBar.getRating()+"/"+ratingBar.getNumStars());
                        }
                        idVisite = doc.getId().toString();
                    }
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> notes = new HashMap<>();
                notes.put("evaluation",ratingBar.getRating());
                db.collection("visites").document(idVisite).set(notes, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(),"note enregistrée", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return root;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        number.setText(ratingBar.getRating()+"/"+ratingBar.getNumStars());
    }
}