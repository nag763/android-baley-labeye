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


public class StatisticsAdminFragment extends Fragment {

    private static final int LAYOUT = R.layout.fragment_statistics_admin;
    private static final int RECYCLER_VIEW = R.id.recyclerView;
    private static final int SEARCH_BAR = R.id.searchBar;
    private static final String QUERY_PATH = "museums";
    private transient final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private transient StatListAdapter adapter;
    private transient FirestoreRecyclerOptions<MuseumBean> options;
    private transient int nbVisites;

    public static Fragment newInstance() {
        Bundle args = new Bundle();
        StatisticsAdminFragment fragment = new StatisticsAdminFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(LAYOUT, container, false);
        SearchView searchView = root.findViewById(SEARCH_BAR);
        searchView.setOnQueryTextListener(new SearchBarListener());
        options = generateQuery();
        adapter = new StatListAdapter(root.getContext(), options);
        TextView nbVist = root.findViewById(R.id.textTitle);

        // Place it in the recycler view
        RecyclerView recyclerView = root.findViewById(RECYCLER_VIEW);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        firebaseFirestore.collection("visites").get().addOnCompleteListener(task -> nbVisites = task.getResult().size());
        nbVist.setText(String.format("%s %s", nbVist.getText(), nbVisites));

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

    private FirestoreRecyclerOptions<MuseumBean> generateQuery() {
        Query query;
        query = firebaseFirestore.collection(QUERY_PATH)
                .orderBy("nomDuMusee");
        //Log.d(this.getClass().getName(), "options reseted with default "+query.toString());
        return new FirestoreRecyclerOptions.Builder<MuseumBean>().setQuery(query, MuseumBean.class).build();
    }

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

        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.d(this.getClass().getName(), String.format("%s searched", query));
            options = generateQuery(query);
            adapter.updateOptions(options);
            return true;
        }
    }
}