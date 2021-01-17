package com.ticandroid.baley_labeye.activities.ui.statistics;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ticandroid.baley_labeye.R;

import java.util.Locale;

/**
 * @author baley
 * fragment statistics to show which distance the user
 * made and the number of museums visited by the user
 */
public class StatisticsFragment extends Fragment {

    /**
     * number of museums visited
     */
    private transient TextView nombre;
    /**
     * distance made by the user to visit museums
     */
    private transient TextView distance;
    /**
     * current user auth
     */
    private transient FirebaseAuth auth;
    /**
     * count the number of museums visited
     */
    private transient int count = 0;
    /**
     * count the distance made by the user
     */
    private transient double distanceParcourue = 0;

    /**
     * new instance of the fragment
     * @return new StatisticsFragment
     */
    public static Fragment newInstance() {
        return (new StatisticsFragment());
    }

    /**
     * on create view
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState saved instance
     * @return root
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //select the xml
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        nombre = root.findViewById(R.id.nombre);
        distance = root.findViewById(R.id.distance);
        //get instance of the current user
        auth = FirebaseAuth.getInstance();

        afficherNbMusees();


        return root;
    }

    /**
     * method to print the number of museums visited by the user
     */
    private void afficherNbMusees() {

        FirebaseFirestore.getInstance().collection("visites")
                .whereEqualTo("idProfil", auth.getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("count", "count");
                if (count == 0) {
                    //for each museum visited the number is increased by 1
                    //the distance is increased by the distance for each museum
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Log.d("count", " " + count);
                        count += 1;
                        double d = (double) doc.get("distance");
                        distanceParcourue = Double.sum(distanceParcourue, d);
                    }
                }
                //set the text with the number of museums visited and the distance made
                nombre.setText(String.format(Locale.FRANCE, "%d musées", count));
                distance.setText(String.format(Locale.FRANCE, "%s Km", distanceParcourue));
            }
        });


    }
}