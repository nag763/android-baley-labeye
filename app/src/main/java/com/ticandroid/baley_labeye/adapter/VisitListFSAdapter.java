package com.ticandroid.baley_labeye.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.beans.VisitBean;
import com.ticandroid.baley_labeye.holder.VisitListHolder;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Visit list FS adapter used to display the visits made by the user.
 *
 * @author Baley
 * @author Labeye
 * @see FirestoreRecyclerAdapter
 */
public class VisitListFSAdapter extends FirestoreRecyclerAdapter<VisitBean, VisitListHolder> {

    /**
     * Date formatter used to display our date.
     **/
    private final static SimpleDateFormat FORMATTER = new SimpleDateFormat("'visité le' dd MMMM yyyy", Locale.FRANCE);

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options list of elements to display
     */
    public VisitListFSAdapter(@NonNull FirestoreRecyclerOptions<VisitBean> options) {
        super(options);
        Log.d(getClass().toString(), "created");
    }

    @Override
    protected void onBindViewHolder(@NonNull VisitListHolder holder, int position, @NonNull VisitBean model) {
        final String title = model.getNomDuMusee();
        final String visitedOn = FORMATTER.format(model.getDate().toDate());

        holder.setTextInTitleView(title);
        holder.setTextInVisitedOnView(visitedOn);

        Log.d(getClass().toString(), String.format("card with %s;%s binded", title, visitedOn));
    }

    @NonNull
    @Override
    public VisitListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list_visits, parent, false);
        Log.d(toString(), "view holder created");
        return new VisitListHolder(view);
    }
}
