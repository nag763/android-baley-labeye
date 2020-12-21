package com.ticandroid.baley_labeye.activities.ui.visits;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.adapter.MuseumListFSAdapter;
import com.ticandroid.baley_labeye.adapter.VisitListFSAdapter;
import com.ticandroid.baley_labeye.beans.MuseumBean;
import com.ticandroid.baley_labeye.beans.VisitBean;

import java.util.Objects;

public class VisitsFragment extends Fragment {


    /**
     * Current activity's layout.
     **/
    private static final int LAYOUT = R.layout.fragment_museum;
    /**
     * Current activity's used recycler view.
     **/
    private static final int RECYCLER_VIEW = R.id.recyclerView;
    /**
     * Search bar used to filter our data.
     **/
    private static final int SEARCH_BAR = R.id.searchBar;
    /**
     * Firesore query path to fetch museum elements.
     **/
    private static final String QUERY_PATH = "vistes";
    /**
     * Firestore instance.
     **/
    private transient final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    /**
     * Museum adapater to display the element.
     **/
    private transient VisitListFSAdapter adapter;
    /**
     * Options to be displayed.
     */
    private transient FirestoreRecyclerOptions<VisitBean> options;

    public static VisitsFragment newInstance() {

        Bundle args = new Bundle();

        VisitsFragment fragment = new VisitsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(LAYOUT, container, false);


        Log.d(this.getClass().toString(), "start of onCreate method");

        // Search view initilizaition
        SearchView searchView = root.findViewById(SEARCH_BAR);
        searchView.setOnQueryTextListener(new searchBarListener());

        // Fetch firestore data
        options = generateQuery();
        adapter = new VisitListFSAdapter(root.getContext(), options);

        // Place it in the recycler view
        RecyclerView recyclerView = root.findViewById(RECYCLER_VIEW);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        Log.d(this.getClass().toString(), "end of onCreate method");
        return root;
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        Log.i(this.getClass().toString(), "listening stopped");
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        Log.i(this.getClass().toString(), "listening started");
    }

    /**
     * Generates a query to fetch some specific data.
     *
     * @param startsWith sequence at the begining of the research
     * @return the options matching the sequence
     */
    private FirestoreRecyclerOptions<VisitBean> generateQuery(String startsWith) {
        Log.d(this.getClass().toString(), String.format("method with %s called", startsWith == null ? "null" : startsWith));
        FirestoreRecyclerOptions<VisitBean> newOptions;
        if (startsWith == null || startsWith.length() == 0) {
            newOptions = generateQuery();
        } else {
            Query query;
            // The purpose is to make a query 'BEGIN WITH'
            query = firebaseFirestore.collection(QUERY_PATH)
                    .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                    .collection(QUERY_PATH)
                    .orderBy("nomMusee")
                    // The \uf8ff sequence is an escape sequence for any
                    .startAt(startsWith)
                    .endAt(String.format("%s\uf8ff", startsWith));
            Log.d(this.getClass().getName(), String.format("options updated with %s parameter", startsWith));
            newOptions = new FirestoreRecyclerOptions.Builder<VisitBean>().setQuery(query, VisitBean.class).build();
        }
        return newOptions;
    }

    /**
     * Generates a query to fetch all data.
     *
     * @return all the options
     */
    private FirestoreRecyclerOptions<VisitBean> generateQuery() {
        Query query;
        query = firebaseFirestore.collection("profils")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection("visites");
        Log.d(this.getClass().getName(), "options reseted with default "+query.toString());
        return new FirestoreRecyclerOptions.Builder<VisitBean>().setQuery(query, VisitBean.class).build();
    }

    /**
     * Class used to add a listener to our searchbar.
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