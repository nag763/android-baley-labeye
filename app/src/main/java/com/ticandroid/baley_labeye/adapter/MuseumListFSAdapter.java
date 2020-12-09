package com.ticandroid.baley_labeye.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.beans.MuseumBean;
import com.ticandroid.baley_labeye.holder.MuseumListHolder;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

public class MuseumListFSAdapter extends FirestoreRecyclerAdapter<MuseumBean, MuseumListHolder>  {

    private final Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MuseumListFSAdapter(Context context, @NonNull FirestoreRecyclerOptions<MuseumBean> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MuseumListHolder holder, int position, @NonNull MuseumBean model) {

        holder.setTextInTitleView(model.getNomDuMusee());
        holder.setPhoneNumber(model.getTelephone());
        holder.setTextInLocationView(model.getAdresse());

    }

    @NonNull
    @Override
    public MuseumListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list_museum, parent, false);
        return new MuseumListHolder(view);
    }
}
