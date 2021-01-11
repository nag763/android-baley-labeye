package com.ticandroid.baley_labeye.activities.ui.statistics;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ticandroid.baley_labeye.R;

public class StatisticsFragment extends Fragment {


    private transient TextView nombre;
    private transient TextView distance;
    private transient FirebaseAuth auth;
    private transient int count = 0;
    private transient double distanceParcourue = 0;

    public static Fragment newInstance() {
        return (new StatisticsFragment());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        nombre = root.findViewById(R.id.nombre);
        distance = root.findViewById(R.id.distance);
        auth = FirebaseAuth.getInstance();

        afficherNbMusees();


        return root;
    }

    private void afficherNbMusees() {

        FirebaseFirestore.getInstance().collection("visites")
                .whereEqualTo("idProfil", auth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("count", "count");
                    if (count == 0) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Log.d("count", " " + count);
                            count += 1;
                            double d = (double) doc.get("distance");
                            distanceParcourue = Double.sum(distanceParcourue, d);
                            // distanceParcourue = distanceParcourue+doc.get("distance");
                        }
                    }
                    nombre.setText(count + " musées");
                    distance.setText(distanceParcourue + " Km");
                }
            }
        });


    }
}