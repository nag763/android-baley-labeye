package com.ticandroid.baley_labeye.activities.ui.museum;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.adapter.MuseumListFSAdapter;
import com.ticandroid.baley_labeye.beans.MuseumBean;

/**
 * Museum Fragment used to display the list of museum available on the app.
 *
 * @author Baley
 * @author Labeye
 * @see Fragment
 */
public class MuseumFragment extends Fragment {


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
    private static final String QUERY_PATH = "museums";
    /**
     * Firestore instance.
     **/
    private transient final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    /**
     * Museum adapater to display the element.
     **/
    private transient MuseumListFSAdapter adapter;
    /**
     * Options to be displayed.
     */
    private transient FirestoreRecyclerOptions<MuseumBean> options;

    /**
     * Creates a new museum fragment instance.
     *
     * @return a new MuseumFragment
     */
    public static MuseumFragment newInstance() {
        return new MuseumFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(LAYOUT, container, false);

        Log.d(this.getClass().toString(), "start of onCreate method");

        // Search view initilizaition
        SearchView searchView = root.findViewById(SEARCH_BAR);
        searchView.setOnQueryTextListener(new SearchBarListener());

        // Fetch firestore data
        options = generateQuery();
        adapter = new MuseumListFSAdapter(root.getContext(), options);

        // Place it in the recycler view
        RecyclerView recyclerView = root.findViewById(RECYCLER_VIEW);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
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
    private FirestoreRecyclerOptions<MuseumBean> generateQuery(String startsWith) {
        Log.d(this.getClass().toString(), String.format(
                "method with %s called",
                startsWith == null ? "null" : startsWith)
        );
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
     * Generates a query to fetch all data.
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
     * Class used to add a listener to our searchbar.
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

        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.d(this.getClass().getName(), String.format("%s searched", query));
            options = generateQuery(query);
            adapter.updateOptions(options);
            return true;
        }
    }


}