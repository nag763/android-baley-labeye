package com.ticandroid.baley_labeye.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import static java.text.DateFormat.getDateTimeInstance;

/**
 * This activity's purpose is to display
 * elegantly the different informations
 * related to a museum.

 * @author Labeye
 * @see AppCompatActivity
 */
public class MuseumReaderActivity extends AppCompatActivity {

    /**
     * Firestore instance.
     **/
    private transient final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    /**
     * Ressources of the application.
     **/
    private transient Resources res;
    /**
     * Permissions needed in this app.
     **/
    private final static String[] PERMISSIONS_NEEDED =
            {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
    /**
     * Location manager used to get the user's position.
     **/
    private transient LocationManager mLocationManager;
    /**
     * Refresh button to refresh our position.
     **/
    private transient Button refreshButton;
    /**
     * Museum bean fetched from remote firestore instance.
     **/
    private transient MuseumBean museumBean;
    /**
     * Current context of the application.
     **/
    private transient Context context;
    /**
     * Current museum's fs document id.
     **/
    private transient String documentId;
    /**
     * Distance from user to museum.
     **/
    private transient double distanceToMuseum;
    /**
     * Duration from user to museum.
     **/
    private transient double durationToMuseum;
    /**
     * Key of the extra museum id.
     **/
    public static final String KEY_OF_EXTRA_MUSEUM_ID = "museumId";
    /**
     * Key of the extra museum id.
     **/
    public static final String KEY_OF_EXTRA_MUSEUM_NAME = "museumName";
    /**
     * Key of the extra user position.
     **/
    public static final String KEY_OF_EXTRA_USER_POSITION = "userPosition";
    /**
     * Key of the extra museum position.
     **/
    public static final String KEY_OF_EXTRA_MUSEUM_POSITION = "museumPosition";
    /**
     * Key of extra distance to museum.
     **/
    public static final String KEY_OF_EXTRA_DISTANCE = "distanceToMuseum";
    /**
     * Location listener used to get our user position.
     */
    private transient final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            String userPosition = String.format("%s,%s", location.getLongitude(), location.getLatitude());
            Log.d(getClass().getName(), String.format("Location changed to %s", userPosition));
            // Once the location is fetched, there is no need for new updates
            mLocationManager.removeUpdates(this);
            bindDistanceToMuseum(userPosition, museumBean);
        }
    };

    /**
     * Method to access the user's location.
     */
    private void accessLocation() {
        final TextView tvDistanceToMuseum = findViewById(R.id.tv_dst_to_museum_filler);
        Log.d(getClass().getName(), "Location accesser called");
        // If the user hasn't accepted the usage of its location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.w(getClass().getName(), "Location permission hasn't been accepted on user's device");
            requestPermissions(PERMISSIONS_NEEDED, 124);
            tvDistanceToMuseum.setText(res.getText(R.string.location_service_disabled));
            // If the location hasn't been enabled on the user's device
        } else if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.w(getClass().getName(), "Location is disabled on user's device");
            tvDistanceToMuseum.setText(res.getText(R.string.location_service_disabled));
            // If the location is enabled on user's device
        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);

            tvDistanceToMuseum.setText(res.getString(R.string.waiting_for_service));
            refreshButton.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_display);

        context = this.getApplication();
        res = getResources();

        // Fetching our document
        documentId = getIntent().getStringExtra(MuseumListFSAdapter.KEY_OF_EXTRA);
        DocumentReference museumDocument = firebaseFirestore.collection(res.getString(R.string.fs_museums)).document(documentId);
        Task<DocumentSnapshot> task = museumDocument.get();
        // Biding our view depending on the success
        task.addOnSuccessListener(k -> {
            museumBean = Objects.requireNonNull(task.getResult()).toObject(MuseumBean.class);
            if (null != museumBean) {
                Log.d(this.getClass().getName(), String.format("Document with id=%s fetched", documentId));
                bindView(museumBean);
                // Starting our location manager
                mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                refreshButton = findViewById(R.id.btn_destination);
                refreshButton.setOnClickListener(click -> accessLocation());
            } else {
                Toast.makeText(this, res.getString(R.string.document_is_null), Toast.LENGTH_LONG).show();
                Log.e(this.getClass().getName(), String.format(
                        "Document with id=%s couldn't be parsed in java class", documentId)
                );
                finish();
            }
        }).addOnFailureListener(k -> {
            Toast.makeText(this, res.getString(R.string.unable_to_fetch), Toast.LENGTH_LONG).show();
            Log.e(this.getClass().getName(), String.format("Document with id=%s couldn't be fetched", documentId));
            finish();
        });

    }

    /**
     * Bind our view with the given bean.
     *
     * @param museumBean museum bean to bind our view with
     */
    private void bindView(final MuseumBean museumBean) {
        ((TextView) findViewById(R.id.tv_museum_name)).setText(museumBean.getNomDuMusee());
        ((TextView) findViewById(R.id.tv_museum_location_filler)).setText(museumBean.getCompleteAdresse());
        ((TextView) findViewById(R.id.tv_museum_phone)).setText(museumBean.getTelephoneWithPrefix());
        ((TextView) findViewById(R.id.tv_curr_dt)).setText(getDateTimeInstance().format(new Date()));
    }

    /**
     * Bind the informations related to the
     * distance to museum in our view.
     *
     * @param userPosition the user's position
     * @param museumBean   the museum
     */
    public void bindDistanceToMuseum(String userPosition, MuseumBean museumBean) {

        OkHttpClient okHttpClient = new OkHttpClient();
        TextView tvDistanceToMuseum = findViewById(R.id.tv_dst_to_museum_filler);

        String json = String.format(
                "{\"locations\":[[%s],[%s]]," +
                        "\"metrics\":[\"duration\"," +
                        "\"distance\"],\"units\":\"km\"}",
                userPosition, museumBean.getInvertedCoordonneesFinales());

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(res.getString(R.string.ors_matrix_url))
                .addHeader(res.getString(R.string.key_ret_type), res.getString(R.string.value_ret_type))
                .addHeader(res.getString(R.string.key_token), res.getString(R.string.value_token))
                .post(requestBody)
                .build();

        Log.d(this.getClass().getName(), String.format(
                "Following request has been sent : %s\n%s",
                request.toString(), json)
        );

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(this.getClass().getName(), "Request couldn't succeed");
                runOnUiThread(() -> tvDistanceToMuseum.setText(res.getString(R.string.error_remote_orss)));
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Response response) throws IOException {
                String displayedMessage;
                if (!response.isSuccessful()) {
                    Log.w(this.getClass().getName(), String.format(
                            "Request with error code : %s\n%s",
                            response.code(), response.body().string())
                    );
                    displayedMessage = String.format(
                            "[%s] %s",
                            response.code(), res.getString(R.string.error_remote_orss)
                    );
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

                    } catch (JSONException e) {
                        Log.e(getClass().getName(), String.format("The following error happened\n%s", e));
                    } finally {
                        if (durationToMuseum == -1 || distanceToMuseum == -1) {
                            displayedMessage = res.getString(R.string.error_while_displaying);
                            Log.e(this.getClass().getName(), displayedMessage);
                        } else {
                            SimpleDateFormat formatter = new SimpleDateFormat(
                                    "HH' heures 'mm' minutes 'ss' secondes'", Locale.FRANCE
                            );
                            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                            displayedMessage = String.format(
                                    "Distance jusqu'à la destination : %s km\nTemps jusqu'à la destination : %s",
                                    distanceToMuseum, formatter.format(new Date((long) durationToMuseum * 1000))
                            );
                            Log.d(this.getClass().getName(), String.format("Displayed message : %s", displayedMessage));
                        }
                    }
                }
                runOnUiThread(() -> {
                    tvDistanceToMuseum.setText(displayedMessage);
                    refreshButton.setText(res.getString(R.string.route_to_destination));
                    refreshButton.setVisibility(View.VISIBLE);
                    refreshButton.setOnClickListener(k -> {
                                Intent intent = new Intent(context, RouteMapActivity.class);
                                intent.putExtra(KEY_OF_EXTRA_MUSEUM_ID, documentId);
                                intent.putExtra(KEY_OF_EXTRA_MUSEUM_NAME, museumBean.getNomDuMusee());
                                intent.putExtra(KEY_OF_EXTRA_USER_POSITION, userPosition);
                                intent.putExtra(KEY_OF_EXTRA_MUSEUM_POSITION, museumBean.getInvertedCoordonneesFinales());
                                intent.putExtra(KEY_OF_EXTRA_DISTANCE, distanceToMuseum);
                                startActivity(intent);
                            }
                    );
                });
            }
        });
    }

}