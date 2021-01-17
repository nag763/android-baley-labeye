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
import com.ticandroid.baley_labeye.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author baley
 * this user can evaluate the last museum visited
 * the last museum will be shown
 * the user will see the note he gave to the museum and can
 * change it
 */
public class EvaluerFragment extends Fragment implements RatingBar.OnRatingBarChangeListener {
    private transient RatingBar ratingBar;
    private transient TextView number;

    public static Fragment newInstance() {
        return (new EvaluerFragment());
    }

    private transient String idVisite;

    /**
     * on create view
     * fetch the xml content
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState saved instance state
     * @return root
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        container.clearDisappearingChildren();
        //select the xml
        View root = inflater.inflate(R.layout.fragment_evaluer, container, false);
        Button button = root.findViewById(R.id.get_rating);
        number = root.findViewById(R.id.number);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        ratingBar = root.findViewById(R.id.rating);
        ratingBar.setOnRatingBarChangeListener(this);

        //get the last visit of the user
        FirebaseFirestore.getInstance().collection("visites").whereEqualTo("idProfil", auth.getCurrentUser().getUid())
                .orderBy("date", Query.Direction.DESCENDING).limit(1)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            //print the museum's name
                            ((TextView) root.findViewById(R.id.museum)).setText(doc.get("nomDuMusee").toString());
                            Object evaluation = doc.get("evaluation");
                            //if the user evaluated the museum
                            //print the evaluation he did
                            if (evaluation != null) {
                                ((TextView) root.findViewById(R.id.number)).setText(String.format(Locale.FRANCE, "%s/%d", evaluation, ratingBar.getNumStars()));
                                double d = (double) evaluation;
                                float f = (float) d;
                                ratingBar.setRating(f);
                                //else print a 3,5 stars and the user can change it
                            } else {
                                ratingBar.setRating((float) 3.5);
                                number.setText(String.format(Locale.FRANCE, "%s/%d", ratingBar.getRating(), ratingBar.getNumStars()));
                            }
                            idVisite = doc.getId();
                        }
                    }
                });
        //set the on click listener of the button evaluate
        //store the evaluation in the database
        button.setOnClickListener(v -> {
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
        });

        return root;
    }

    /**
     * on rating changed print the rate selected
     * @param ratingBar rating bar
     * @param rating rating
     * @param fromUser from user
     */
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        number.setText(String.format(Locale.FRANCE, "%s/%d", ratingBar.getRating(), ratingBar.getNumStars()));
    }
}