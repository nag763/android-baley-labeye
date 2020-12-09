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
import com.ticandroid.baley_labeye.beans.MuseumBean;
import com.ticandroid.baley_labeye.holder.MuseumListHolder;

/**
 * This adapter's purpose is to bind the museum elements of the remote firestore into the given holder
 *
 * @see com.firebase.ui.firestore.FirestoreRecyclerAdapter
 * @author Baley
 * @author Labeye
 */
public class MuseumListFSAdapter extends FirestoreRecyclerAdapter<MuseumBean, MuseumListHolder>  {

    /** Context that will be used in the clickable element **/
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
        Log.d(this.getClass().toString(), "created");
    }

    @Override
    protected void onBindViewHolder(@NonNull MuseumListHolder holder, int position, @NonNull MuseumBean model) {

        final String TITLE =  model.getNomDuMusee();
        final String LOCATION = String.format("%s, %s",model.getAdr(), model.getDepartement());
        // By default, the phone numbers from the csv are stored without the local prefix
        final String PHONE_NUMBER = String.format("0%s", model.getTelephone1());

        holder.setTextInTitleView(TITLE);
        holder.setTextInLocationView(LOCATION);
        holder.setPhoneNumber(PHONE_NUMBER);

        Log.d(this.getClass().toString(), String.format("card with %s;%s;%s binded", TITLE, LOCATION, PHONE_NUMBER));
    }

    @NonNull
    @Override
    public MuseumListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list_museum, parent, false);
        Log.d(this.getClass().toString(), "view holder created");
        return new MuseumListHolder(view);
    }
}
