package com.ticandroid.baley_labeye.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import static java.text.DateFormat.getDateTimeInstance;

public class MuseumReaderActivity extends AppCompatActivity {

    private transient final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private transient Resources res;


    private final static String[] PERMISSIONS_NEEDED = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION };

    private LocationManager mLocationManager;


    private transient static MuseumBean museumBean;

    // TODO : WAIT FOR LOCATION
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            String userPosition = String.format("%s,%s", location.getLongitude(), location.getLatitude());
            Log.d(this.getClass().getName(), String.format("Location changed to %s", userPosition));
            // TODO : ONCE GOTTEN, STOP LISTENER
            mLocationManager.removeUpdates(this);
            bindDistanceToMuseum(userPosition, museumBean);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_display);

        res = getResources();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // TODO : ASSERT THAT IT REQUESTS ONLY ONCE
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_NEEDED, 124);
            return;
        }
        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            ((TextView) findViewById(R.id.tvDistanceToMuseum)).setText(res.getString(R.string.location_service_disabled));
            Log.d(this.getClass().getName(), "Location is disabled on user's device");
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
        }

        final String DOCUMENT_ID = getIntent().getStringExtra(MuseumListFSAdapter.KEY_OF_EXTRA);
        DocumentReference museumDocument = firebaseFirestore.collection(res.getString(R.string.fs_museums)).document(DOCUMENT_ID);

        Task<DocumentSnapshot> task = museumDocument.get();

        task.addOnSuccessListener(k -> {
            museumBean = Objects.requireNonNull(task.getResult()).toObject(MuseumBean.class);
            if (null != museumBean){
                bindView(museumBean);
                Log.d(this.getClass().getName(), String.format("Document with id=%s fetched", DOCUMENT_ID));
            } else {
                Toast.makeText(this, res.getString(R.string.document_is_null), Toast.LENGTH_LONG).show();
                Log.e(this.getClass().getName(), String.format("Document with id=%s couldn't be parsed in java class", DOCUMENT_ID));
                finish();
            }
        });

        task.addOnFailureListener(k -> {
            Toast.makeText(this, res.getString(R.string.unable_to_fetch), Toast.LENGTH_LONG).show();
            Log.e(this.getClass().getName(), String.format("Document with id=%s couldn't be fetched", DOCUMENT_ID));
            finish();
        });

    }

    private void bindView(final MuseumBean museumBean) {
        ((TextView) findViewById(R.id.tvMuseumName)).setText(museumBean.getNomDuMusee());
        ((TextView) findViewById(R.id.tvMuseumLocation)).setText(museumBean.getCompleteAdresse());
        ((TextView) findViewById(R.id.tvMuseumPhone)).setText(museumBean.getTelephoneWithPrefix());
        ((TextView) findViewById(R.id.tvCurrDT)).setText(getDateTimeInstance().format(new Date()));
    }

    public void bindDistanceToMuseum(String userPosition, MuseumBean museumBean){

        OkHttpClient okHttpClient = new OkHttpClient();
        TextView tvDistanceToMuseum = findViewById(R.id.tvDistanceToMuseum);

        String json = String.format("{\"locations\":[[%s],[%s]],\"metrics\":[\"duration\",\"distance\"],\"units\":\"km\"}", userPosition, museumBean.getInvertedCoordonneesFinales());

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

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Response response) throws IOException {
                String displayedMessage;
                double durationToMuseum = -1;
                double distanceToMuseum = -1;
                if(!response.isSuccessful()) {
                    Log.w(this.getClass().getName(), String.format("Request with error code : %s\n%s", response.code(), response.body().string()));
                    displayedMessage = String.format("[%s] %s", response.code(), res.getString(R.string.error_remote_orss));
                } else {
                    final JSONObject jsonReader;
                    try {
                        String jsonData = response.body().string();
                        jsonReader = new JSONObject(jsonData);
                        JSONArray jsonArray = jsonReader.getJSONArray("durations");
                        // We only need the first element of the array (the second is a dup)
                        durationToMuseum = jsonArray.getJSONArray(0).getDouble(1);

                        jsonArray = jsonReader.getJSONArray("distances");
                        distanceToMuseum = jsonArray.getJSONArray(0).getDouble(1);

                        Log.w(this.getClass().getName(), String.format("Distance to museum : %s", durationToMuseum));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if(durationToMuseum == -1) {
                            displayedMessage = "Erreur lors de l'affichage";
                        } else {
                            // Error, one hour of too much is displayed
                            SimpleDateFormat formatter = new SimpleDateFormat("HH' heures 'mm' minutes 'ss' secondes'", Locale.FRANCE);
                            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                            displayedMessage = String.format("Distance jusqu'à la destination : %s km\nTemps jusqu'à la destination : %s", distanceToMuseum, formatter.format(new Date((long) durationToMuseum * 1000)));
                        }
                    }

                }
                Log.d(this.getClass().getName(), String.format("Displayed message : %s", displayedMessage));
                runOnUiThread(() -> tvDistanceToMuseum.setText(displayedMessage));
            }
        });
    }

}