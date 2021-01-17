package com.ticandroid.baley_labeye.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.beans.MuseumBean;
import com.ticandroid.baley_labeye.beans.VisitBean;
import com.ticandroid.baley_labeye.holder.StatListHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baley labeye
 * statistics list adapter for the admin statistics fragment
 */
public class StatListAdapter extends FirestoreRecyclerAdapter<MuseumBean, StatListHolder> {
    /**
     * list of visit beans
     */
    private final transient List<VisitBean> visitBeans;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options options of the search
     */
    public StatListAdapter(@NonNull FirestoreRecyclerOptions<MuseumBean> options) {
        super(options);
        visitBeans = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("visites").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    VisitBean visitBean = new VisitBean();
                    visitBean.setNomDuMusee(doc.getString("nomDuMusee"));
                    visitBean.setDistance(doc.getDouble("distance"));
                    visitBean.setEvaluation(doc.getDouble("evaluation"));
                    visitBeans.add(visitBean);
                }
            }
        });


    }

    /**
     * on bind view holder
     * @param holder holder
     * @param position position
     * @param model model
     */
    @Override
    protected void onBindViewHolder(@NonNull StatListHolder holder, int position, @NonNull MuseumBean model) {
        final String title = model.getNomDuMusee();
        double distance = 0;
        double evaluation = 0;
        VisitBean[] visitsForMuseum = visitBeans.stream().filter(element -> element.getNomDuMusee().equals(model.getNomDuMusee())).toArray(VisitBean[]::new);
        if (visitsForMuseum.length != 0) {
            for (VisitBean visit : visitsForMuseum) {
                evaluation += visit.getEvaluation();
                distance += visit.getDistance();
            }
            evaluation = evaluation / visitsForMuseum.length;


        }

        holder.setTextInTitleView(title);
        holder.setTextInDistanceView(distance + "");
        holder.setTextInNoteView(evaluation + "");

    }

    /**
     * on create view holder
     * @param parent parent
     * @param viewType view type
     * @return stat list holder
     */
    @NonNull
    @Override
    public StatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_statistics, parent, false);
        Log.d(this.getClass().toString(), "view holder created");
        return new StatListHolder(view);
    }
}
