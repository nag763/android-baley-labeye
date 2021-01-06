package com.ticandroid.baley_labeye.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.beans.VisitBean;
import com.ticandroid.baley_labeye.holder.MuseumListHolder;
import com.ticandroid.baley_labeye.holder.StatListHolder;

public class StatListAdapter extends FirestoreRecyclerAdapter<VisitBean, StatListHolder> {
    private transient final Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public StatListAdapter(Context context,@NonNull FirestoreRecyclerOptions<VisitBean> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull StatListHolder holder, int position, @NonNull VisitBean model) {
        final String TITLE = model.getNomDuMusee();
        final double DISTANCE = model.getDistance();
        final double EVALUATION = model.getEvaluation();
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
