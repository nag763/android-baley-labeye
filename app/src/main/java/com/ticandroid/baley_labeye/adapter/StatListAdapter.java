package com.ticandroid.baley_labeye.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.beans.MuseumBean;
import com.ticandroid.baley_labeye.beans.VisitBean;
import com.ticandroid.baley_labeye.holder.MuseumListHolder;
import com.ticandroid.baley_labeye.holder.StatListHolder;

import java.util.ArrayList;
import java.util.stream.Stream;

public class StatListAdapter extends FirestoreRecyclerAdapter<MuseumBean, StatListHolder> {
    private transient final Context context;
    //private transient CollectionReference visits;
    private transient ArrayList<VisitBean> visitBeans;

   // private transient VisitBean visitBean;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public StatListAdapter(Context context,@NonNull FirestoreRecyclerOptions<MuseumBean> options) {
        super(options);
        this.context = context;
        visitBeans = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("visites").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        VisitBean visitBean = new VisitBean();
                        visitBean.setNomDuMusee(doc.getString("nomDuMusee"));
                        visitBean.setDistance( doc.getDouble("distance"));
                        visitBean.setEvaluation( doc.getDouble("evaluation"));
                        visitBeans.add(visitBean);
                    }
                }
            }
        });
       // nbVisits=visitBeans.size();


    }


    @Override
    protected void onBindViewHolder(@NonNull StatListHolder holder, int position, @NonNull MuseumBean model) {
        final String TITLE = model.getNomDuMusee();
        double DISTANCE=0;
        double EVALUATION=0;
        VisitBean[] visitsForMuseum = visitBeans.stream().filter(element -> element.getNomDuMusee().equals(model.getNomDuMusee())).toArray(VisitBean[]:: new);
        if(visitsForMuseum.length!= 0){
            for (VisitBean visit: visitsForMuseum) {
                EVALUATION += visit.getEvaluation();
                DISTANCE += visit.getDistance();
            }
            EVALUATION = EVALUATION/visitsForMuseum.length;
            //nbVisits=visitsForMuseum.length;
            //Log.d("visites",""+nbVisits);
        }
        holder.setTextInTitleView(TITLE);
        holder.setTextInDistanceView(DISTANCE+"");
        holder.setTextInNoteView(EVALUATION+"");


    }

    @NonNull
    @Override
    public StatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_statistics, parent, false);
        Log.d(this.getClass().toString(), "view holder created");
        return new StatListHolder(view);
    }
}
