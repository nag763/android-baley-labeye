package com.ticandroid.baley_labeye.activities.ui.evaluer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.beans.MuseumBean;

import java.util.HashMap;
import java.util.Map;


public class EvaluerFragment extends Fragment implements RatingBar.OnRatingBarChangeListener {
    private transient RatingBar ratingBar;
    private transient static MuseumBean museumBean;
    private transient TextView number;

    public static Fragment newInstance() {
        return (new EvaluerFragment());
    }

    private transient String idVisite;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.clearDisappearingChildren();
        View root = inflater.inflate(R.layout.fragment_evaluer, container, false);
        Button button = root.findViewById(R.id.getRating);
        number = root.findViewById(R.id.number);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        ratingBar = root.findViewById(R.id.rating);
        ratingBar.setOnRatingBarChangeListener(this);


        FirebaseFirestore.getInstance().collection("visites").whereEqualTo("idProfil", auth.getCurrentUser().getUid())
                .orderBy("date", Query.Direction.DESCENDING).limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        ((TextView) root.findViewById(R.id.museum)).setText(doc.get("nomDuMusee").toString());
                        if (doc.get("evaluation") != null) {
                            ((TextView) root.findViewById(R.id.number)).setText(doc.get("evaluation").toString() + "/" + ratingBar.getNumStars());
                            double d = (double) doc.get("evaluation");
                            float f = (float) d;
                            ratingBar.setRating(f);
                        } else {
                            ratingBar.setRating((float) 3.5);
                            number.setText(ratingBar.getRating() + "/" + ratingBar.getNumStars());
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
                notes.put("evaluation", ratingBar.getRating());
                db.collection("visites").document(idVisite).set(notes, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "note enregistrée", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return root;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        number.setText(ratingBar.getRating() + "/" + ratingBar.getNumStars());
    }
}