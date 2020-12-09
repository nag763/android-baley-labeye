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

public class MuseumListActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_museum_list;
    private static final int RECYCLER_VIEW = R.id.recyclerView;
    private static final String QUERY_PATH = "museums";
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private MuseumListFSAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(this.getClass().toString(), "start of onCreate method");
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        Query query = firebaseFirestore.collection(QUERY_PATH);

        FirestoreRecyclerOptions<MuseumBean> options = new FirestoreRecyclerOptions.Builder<MuseumBean>().setQuery(query, MuseumBean.class).build();
        adapter = new MuseumListFSAdapter(this, options);

        RecyclerView recyclerView = findViewById(RECYCLER_VIEW);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        Log.d(this.getClass().toString(), "end of onCreate method");
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        Log.d(this.getClass().toString(), "listening stopped");
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

        Log.d(this.getClass().toString(), "listening started");
    }

}