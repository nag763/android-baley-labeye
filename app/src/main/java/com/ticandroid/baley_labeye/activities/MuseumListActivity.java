package com.ticandroid.baley_labeye.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.adapter.MuseumListFSAdapter;
import com.ticandroid.baley_labeye.beans.MuseumBean;

import org.w3c.dom.Text;

/**
 * Activity to display the whole list of museums
 *
 * @author Baley
 * @author Labeye
 * @see androidx.appcompat.app.AppCompatActivity
 */
public class MuseumListActivity extends AppCompatActivity {

    /**
     * Current activity's layout
     **/
    private static final int LAYOUT = R.layout.activity_museum_list;
    /**
     * Current activity's used recycler view
     **/
    private static final int RECYCLER_VIEW = R.id.recyclerView;
    /**
     * Search bar used to filter our data
     **/
    private static final int SEARCH_BAR = R.id.searchBar;
    /**
     * Firesore query path to fetch museum elements
     **/
    private static final String QUERY_PATH = "museums";
    /**
     * Firestore instance
     **/
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    /**
     * Museum adapater to display the element
     **/
    private MuseumListFSAdapter adapter;
    /**
     * Options to be displayed
     */
    private FirestoreRecyclerOptions<MuseumBean> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(this.getClass().toString(), "start of onCreate method");
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        // Search view initilizaition
        SearchView searchView = findViewById(SEARCH_BAR);
        searchView.setOnQueryTextListener(new searchBarListener());

        // Fetch firestore data
        Query query = firebaseFirestore.collection(QUERY_PATH);
        options = generateQuery();
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

    /**
     * Generates a query to fetch some specific data
     *
     * @param startsWith sequence at the begining of the research
     * @return the options matching the sequence
     */
    private FirestoreRecyclerOptions<MuseumBean> generateQuery(String startsWith) {
        Log.d(this.getClass().toString(), String.format("method with %s called", startsWith == null ? "null" : startsWith));
        FirestoreRecyclerOptions<MuseumBean> newOptions;
        if (startsWith == null || startsWith.length() == 0)
            newOptions = generateQuery();
        else {
            Query query;
            // The purpose is to make a query 'BEGIN WITH'
            query = firebaseFirestore.collection(QUERY_PATH)
                    .orderBy("nomDuMusee")
                    // The \uf8ff sequence is an escape sequence for any
                    .startAt(startsWith)
                    .endAt(String.format("%s\uf8ff", startsWith));
            Log.d(this.getClass().getName(), String.format("options updated with %s parameter", startsWith));
            newOptions = new FirestoreRecyclerOptions.Builder<MuseumBean>().setQuery(query, MuseumBean.class).build();
        }
        return newOptions;
    }

    /**
     * Generates a query to fetch all data
     *
     * @return all the options
     */
    private FirestoreRecyclerOptions<MuseumBean> generateQuery() {
        Query query;
        query = firebaseFirestore.collection(QUERY_PATH).orderBy("nomDuMusee");
        Log.d(this.getClass().getName(), "options reseted with default");
        return new FirestoreRecyclerOptions.Builder<MuseumBean>().setQuery(query, MuseumBean.class).build();
    }

    /**
     * Class used to add a listener to our searchbar
     */
    private class searchBarListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextChange(String newText) {
            Log.d(this.getClass().toString(), String.format("%s input change", newText));
            // If the entry is empty, that means, the user wants to see everything
            if (null == newText || newText.length() == 0) {
                options = generateQuery();
                adapter.updateOptions(options);
                Log.d(this.getClass().getName(), "UI updated in consequence");
            }
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.d(this.getClass().getName(), String.format("%s searched", query));
            options = generateQuery(query);
            adapter.updateOptions(options);
            return true;
        }
    }

}