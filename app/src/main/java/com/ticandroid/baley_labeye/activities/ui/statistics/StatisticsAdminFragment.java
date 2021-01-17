package com.ticandroid.baley_labeye.activities.ui.statistics;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.adapter.StatListAdapter;
import com.ticandroid.baley_labeye.beans.MuseumBean;

/**
 * @author baley labeye
 * statistics admin fragment to see the statistics of the museums.
 */
public class StatisticsAdminFragment extends Fragment {
    /**
     * layout of the fragment.
     */
    private static final int LAYOUT = R.layout.fragment_statistics_admin;
    /**
     * recycler view of the fragment.
     */
    private static final int RECYCLER_VIEW = R.id.recycler_view;
    /**
     * search bar of the fragment.
     */
    private static final int SEARCH_BAR = R.id.search_bar;
    /**
     * query path museums.
     */
    private static final String QUERY_PATH = "museums";
    /**
     * instance of firestore.
     */
    private transient final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    /**
     * adapter of the list result for each search the admin wants.
     */
    private transient StatListAdapter adapter;
    /**
     * recycler option of firestore.
     */
    private transient FirestoreRecyclerOptions<MuseumBean> options;
    /**
     * number of visits of museums.
     */
    private transient int nbVisites;

    /**
     * new instance of the fragment.
     * @return fragment
     */
    public static Fragment newInstance() {
        Bundle args = new Bundle();
        StatisticsAdminFragment fragment = new StatisticsAdminFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * on create view.
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState saved instance state
     * @return root
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(LAYOUT, container, false);
        //search bar of the xml
        SearchView searchView = root.findViewById(SEARCH_BAR);
        searchView.setOnQueryTextListener(new SearchBarListener());
        options = generateQuery();
        adapter = new StatListAdapter(options);
        //text view number of visits of the xml
        TextView nbVist = root.findViewById(R.id.lbl_title);

        // Place it in the recycler view
        RecyclerView recyclerView = root.findViewById(RECYCLER_VIEW);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        firebaseFirestore.collection("visites").get().addOnCompleteListener(task -> nbVisites = task.getResult().size());
        nbVist.setText(String.format("%s %s", nbVist.getText(), nbVisites));

        return root;
    }

    /**
     * on stop method to stop the listening of the search bar.
     */
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        Log.i(this.getClass().toString(), "listening stopped");
    }

    /**
     * on start method to start listening the search bar.
     */
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        Log.i(this.getClass().toString(), "listening started");
    }

    /**
     * recycler options of museum bean to fetch data which
     * starts with the string the user entered in the search bar.
     * @param startsWith start with
     * @return new options
     */
    private FirestoreRecyclerOptions<MuseumBean> generateQuery(String startsWith) {
        Log.d(this.getClass().toString(), String.format("method with %s called", startsWith == null ? "null" : startsWith));
        FirestoreRecyclerOptions<MuseumBean> newOptions;
        if (startsWith == null || startsWith.length() == 0) {
            newOptions = generateQuery();
        } else {
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
     * method to generate the query.
     * @return a set query
     */
    private FirestoreRecyclerOptions<MuseumBean> generateQuery() {
        Query query;
        query = firebaseFirestore.collection(QUERY_PATH)
                .orderBy("nomDuMusee");
        //Log.d(this.getClass().getName(), "options reseted with default "+query.toString());
        return new FirestoreRecyclerOptions.Builder<MuseumBean>().setQuery(query, MuseumBean.class).build();
    }

    /**
     * class search bar listener to listen what is printed in the search bar.
     */
    private class SearchBarListener implements SearchView.OnQueryTextListener {

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

        /**
         * method to update the options searched.
         * @param query query
         * @return true
         */
        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.d(this.getClass().getName(), String.format("%s searched", query));
            options = generateQuery(query);
            adapter.updateOptions(options);
            return true;
        }
    }
}