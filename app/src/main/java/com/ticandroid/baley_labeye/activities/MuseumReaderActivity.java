package com.ticandroid.baley_labeye.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.adapter.MuseumListFSAdapter;
import com.ticandroid.baley_labeye.beans.MuseumBean;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import static java.text.DateFormat.getDateTimeInstance;

public class MuseumReaderActivity extends AppCompatActivity {

    private transient final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private transient Resources res;


    private final static String[] PERMISSIONS_NEEDED = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION };

    private transient String userPosition;

    // TODO : WAIT FOR LOCATION
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            userPosition = String.format("%s,%s", location.getLatitude(), location.getLongitude());
            Log.d(this.getClass().getName(), String.format("Location changed to %s", userPosition));
            // TODO : ONCE GOTTEN, STOP LISTENER
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_display);

        res = getResources();

        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // TODO : ASSERT THAT IT REQUESTS ONLY ONCE
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_NEEDED, 124);
            return;
        }
        // TODO : CHECK IF A ONCE TIME IS POSSIBLE
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);

        final String DOCUMENT_ID = getIntent().getStringExtra(MuseumListFSAdapter.KEY_OF_EXTRA);
        DocumentReference museumDocument = firebaseFirestore.collection(res.getString(R.string.fs_museums)).document(DOCUMENT_ID);

        Task<DocumentSnapshot> task = museumDocument.get();

        task.addOnSuccessListener(k -> {
            bindView(Objects.requireNonNull(task.getResult()).toObject(MuseumBean.class));
            Log.d(this.getClass().getName(), String.format("Document with id=%s fetched", DOCUMENT_ID));
        });

        task.addOnFailureListener(k -> {
            Toast.makeText(this, res.getString(R.string.unable_to_fetch), Toast.LENGTH_LONG).show();
            Log.e(this.getClass().getName(), String.format("Document with id=%s couldn't be fetched", DOCUMENT_ID));
            finish();
        });

    }

    private void bindView(MuseumBean museumBean) {
        ((TextView) findViewById(R.id.tvMuseumName)).setText(museumBean.getNomDuMusee());
        ((TextView) findViewById(R.id.tvMuseumLocation)).setText(museumBean.getCompleteAdresse());
        ((TextView) findViewById(R.id.tvMuseumPhone)).setText(museumBean.getTelephoneWithPrefix());
        ((TextView) findViewById(R.id.tvCurrDT)).setText(getDateTimeInstance().format(new Date()));

        OkHttpClient okHttpClient = new OkHttpClient();
        TextView tvDistanceToMuseum = findViewById(R.id.tvDistanceToMuseum);

        String json = String.format("{\"locations\":[[%s],[%s]]}", userPosition, museumBean.getCoordonneesFinales());

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(res.getString(R.string.orss_url))
                .addHeader(res.getString(R.string.key_ret_type), res.getString(R.string.value_ret_type))
                .addHeader(res.getString(R.string.key_token), res.getString(R.string.value_token))
                .post(requestBody)
                .build();

        Log.d(this.getClass().getName(), String.format("Following request has been sent : %s\n%s", request.toString(), json));

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.w(this.getClass().getName(), "Request couldn't succeed");
                tvDistanceToMuseum.setText(res.getString(R.string.error_remote_orss));
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String displayedMessage;
                if(!response.isSuccessful()){
                    Log.w(this.getClass().getName(), String.format("Request with error code : %s\n%s", response.code(), response.body().string()));
                    displayedMessage = String.format("[%s] %s", response.code(), res.getString(R.string.error_remote_orss));
                }else{
                    Log.d(this.getClass().getName(), String.format("[%s] Response successful", response.code()));
                    displayedMessage = response.body().string();
                }
                runOnUiThread(() -> tvDistanceToMuseum.setText(displayedMessage));
            }
        });
    }

}