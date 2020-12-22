package com.ticandroid.baley_labeye.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.activities.MuseumReaderActivity;
import com.ticandroid.baley_labeye.beans.VisitBean;
import com.ticandroid.baley_labeye.holder.VisitListHolder;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Visit list holder
 *
 * @see FirestoreRecyclerAdapter
 *
 * @author Baley
 * @author Labeye
 */
public class VisitListFSAdapter extends FirestoreRecyclerAdapter<VisitBean, VisitListHolder> {


    /**
     * Context that will be used in the clickable element.
     **/
    private transient final Context context;
    /** Date formatter used to display our date **/
    private final static SimpleDateFormat formatter = new SimpleDateFormat("'visité le' dd MMMM yyyy", Locale.FRANCE);
    // TODO : Pass it as string res
    public static final String KEY_OF_EXTRA = "idToOpen";

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public VisitListFSAdapter(Context context, @NonNull FirestoreRecyclerOptions<VisitBean> options) {
        super(options);
        this.context = context;
        Log.d(this.getClass().toString(), "created");
    }

    @Override
    protected void onBindViewHolder(@NonNull VisitListHolder holder, int position, @NonNull VisitBean model) {
        final String TITLE = model.getNomDuMusee();
        final String VISITED_ON = formatter.format(model.getDate().toDate());

        holder.setTextInTitleView(TITLE);
        holder.setTextInVisitedOnView(VISITED_ON);

        Log.d(this.getClass().toString(), String.format("card with %s;%s binded", TITLE, VISITED_ON));
    }

    @NonNull
    @Override
    public VisitListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list_visits, parent, false);
        Log.d(this.getClass().toString(), "view holder created");
        return new VisitListHolder(view);
    }
}
