package com.ticandroid.baley_labeye.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.activities.MuseumReaderActivity;
import com.ticandroid.baley_labeye.beans.MuseumBean;
import com.ticandroid.baley_labeye.holder.MuseumListHolder;

/**
 * This adapter's purpose is to bind the museum elements of the remote firestore into the given holder.
 *
 * @see com.firebase.ui.firestore.FirestoreRecyclerAdapter
 * @author Baley
 * @author Labeye
 */
public class MuseumListFSAdapter extends FirestoreRecyclerAdapter<MuseumBean, MuseumListHolder>  {

    /** Context that will be used in the clickable element. **/
    private transient final Context context;

    public static final String KEY_OF_EXTRA = "idToOpen";

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
        final String LOCATION = model.getPartialAdresse();
        // By default, the phone numbers from the csv are stored without the local prefix
        final String PHONE_NUMBER = model.getTelephoneWithPrefix();

        holder.setTextInTitleView(TITLE);
        holder.setTextInLocationView(LOCATION);
        holder.setPhoneNumber(PHONE_NUMBER);

        holder.itemView.setOnClickListener(k -> {
            Intent intent = new Intent(context, MuseumReaderActivity.class);
            // Get corresponding document in fs
            DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
            String documentId = documentSnapshot.getReference().getId();
            intent.putExtra(KEY_OF_EXTRA, documentId);
            Log.d(getClass().getName(), String.format("intent created from %s to %s with id = %s", context.getClass(), MuseumReaderActivity.class, documentId));
            context.startActivity(intent);

        });

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
