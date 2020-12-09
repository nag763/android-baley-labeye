package com.ticandroid.baley_labeye.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.adapter.MuseumListFSAdapter;
import com.ticandroid.baley_labeye.beans.MuseumBean;

/**
 * Activity to display the whole list of museums
 *
 *
 * @see androidx.appcompat.app.AppCompatActivity
 *
 * @author Baley
 * @author Labeye
 */
public class MuseumListActivity extends AppCompatActivity {

    /** Current activity's layout **/
    private static final int LAYOUT = R.layout.activity_museum_list;
    /** Current activity's used recycler view **/
    private static final int RECYCLER_VIEW = R.id.recyclerView;
    /** Firesore query path to fetch museum elements **/
    private static final String QUERY_PATH = "museums";
    /** Firestore instance **/
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    /** Museum adapater to display the element **/
    private MuseumListFSAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(this.getClass().toString(), "start of onCreate method");
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        // Fetch firestore data
        Query query = firebaseFirestore.collection(QUERY_PATH);
        FirestoreRecyclerOptions<MuseumBean> options = new FirestoreRecyclerOptions.Builder<MuseumBean>().setQuery(query, MuseumBean.class).build();
        adapter = new MuseumListFSAdapter(this, options);

        // Place it in the recycler view
        RecyclerView recyclerView = findViewById(RECYCLER_VIEW);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        Log.d(this.getClass().toString(), "end of onCreate method");
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        Log.i(this.getClass().toString(), "listening stopped");
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        Log.i(this.getClass().toString(), "listening started");
    }

}