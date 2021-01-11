package com.ticandroid.baley_labeye.activities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.ticandroid.baley_labeye.BuildConfig;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.adapter.StepListAdapter;
import com.ticandroid.baley_labeye.beans.StepBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.ticandroid.baley_labeye.utils.Caster.positionToDoubleArray;

/**
 * Display the route to a point given as intent.
 *
 * @author Labeye
 * @see AppCompatActivity
 */
public class RouteMapActivity extends AppCompatActivity {

    /**
     * Context of the application.
     **/
    private transient Context context;
    /**
     * Ressources of the application.
     **/
    private static Resources res;
    /**
     * Current map view being displayed.
     **/
    private transient MapView mMapView;
    /**
     * Recycler view to display the instructions.
     **/
    private transient RecyclerView recyclerView;
    /**
     * Current museum id.
     **/
    private transient String museumId;
    /**
     * Current museum name.
     **/
    private transient String museumName;
    /**
     * Distance between user to museum.
     */
    private transient double distanceToMuseum;
    /**
     * List of steps to be displayed.
     **/
    private transient final List<StepBean> stepList = new ArrayList<>();
    /**
     * List of geopoints associed with the steps.
     **/
    private transient final List<GeoPoint> geoPointList = new ArrayList<>();

    /**
     * Draw a marker with the given geopoint.
     *
     * @param geoPoint geopoint where the marker needs to be drawn
     */
    private void drawMarker(GeoPoint geoPoint) {
        Marker marker = new Marker(mMapView);
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMapView.getOverlayManager().add(marker);
        Log.d(getClass().getName(), String.format("marker added to gp : %s", geoPoint.toString()));
    }

    /**
     * Switch the UI by switching the components visibility.
     */
    private void switchVisibilities() {
        final int mapVisibility = this.mMapView.getVisibility();
        this.mMapView.setVisibility(recyclerView.getVisibility());
        this.recyclerView.setVisibility(mapVisibility);
    }

    /**
     * Add the route to firestore.
     */
    private void addRouteToFirestore() {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        final Map<String, Object> data = new HashMap<String, Object>() {
        };
        data.put("nomDuMusee", museumName);
        data.put("date", Timestamp.now());
        data.put("instructions", stepList);
        data.put("georoute", geoPointList);
        data.put("evaluation", null);
        data.put("distance", distanceToMuseum);
        data.put("idMusee", museumId);
        data.put("idProfil", auth.getUid());

        // Setting a unique id combining both the uuid and museum id to avoid duplicates
        FirebaseFirestore
                .getInstance()
                .collection("visites")
                .document(String.format("%s::%s", auth.getUid(), museumId))
                .set(data).addOnCompleteListener(task -> {
            Toast.makeText(context, "Chemin ajouté à votre profil", Toast.LENGTH_LONG).show();
            // No need to save the document twice
            findViewById(R.id.fltBtnSaveInDb).setVisibility(View.GONE);
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Necessary to use the osmdroid lib
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        res = getResources();
        context = this;

        recyclerView = findViewById(R.id.rvSteps);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        museumId = getIntent().getStringExtra(MuseumReaderActivity.KEY_OF_EXTRA_MUSEUM_ID);
        museumName = getIntent().getStringExtra(MuseumReaderActivity.KEY_OF_EXTRA_MUSEUM_NAME);
        distanceToMuseum = getIntent().getDoubleExtra(MuseumReaderActivity.KEY_OF_EXTRA_DISTANCE, -1);
        // Get positions and cast them to string in order to use them in the controller
        final String userPosition = getIntent().getStringExtra(MuseumReaderActivity.KEY_OF_EXTRA_USER_POSITION);
        final double[] userPositionAsDouble = Objects.requireNonNull(positionToDoubleArray(userPosition));
        final String museumPosition = getIntent().getStringExtra(MuseumReaderActivity.KEY_OF_EXTRA_MUSEUM_POSITION);
        final double[] museumPositionAsDouble = Objects.requireNonNull(positionToDoubleArray(museumPosition));

        mMapView = findViewById(R.id.mapview);
        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        MapController mMapController = (MapController) mMapView.getController();

        GeoPoint userGpt = new GeoPoint(userPositionAsDouble[1], userPositionAsDouble[0]);
        GeoPoint museumGpt = new GeoPoint(museumPositionAsDouble[1], museumPositionAsDouble[0]);

        drawMarker(userGpt);
        drawMarker(museumGpt);

        mMapController.setZoom(13);
        mMapController.setCenter(museumGpt);
        mMapController.animateTo(userGpt);

        addContextualContent(userPosition, museumPosition);
    }

    /**
     * Draws the route line between the spots
     * and display the itinary between the arrival
     * and the departure.
     *
     * @param userPosition   begining of the route
     * @param museumPosition arrival point
     */
    private void addContextualContent(String userPosition, String museumPosition) {

        String body = String.format(
                "{\"coordinates\":[[%s],[%s]],"
                        + "\"language\":\"fr\"," +
                        "\"units\":\"km\"}",
                userPosition, museumPosition);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);

        Request request = new Request.Builder()
                .url(res.getString(R.string.ors_directions_url))
                .addHeader(res.getString(R.string.key_ret_type), res.getString(R.string.value_ret_type))
                .addHeader(res.getString(R.string.key_token), res.getString(R.string.value_token))
                .post(requestBody)
                .build();

        Log.d(getClass().getName(), String.format(
                "Following request has been sent : %s\n%s",
                request.toString(), body)
        );


        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(getClass().getName(), "Request couldn't succeed");
                Toast.makeText(context, res.getString(R.string.error_remote_orss), Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (!response.isSuccessful()) {
                    Log.w(getClass().getName(), String.format(
                            "Request with error code : %s\n%s",
                            response.code(), response.body().string())
                    );
                    Toast.makeText(context, res.getString(R.string.error_remote_orss), Toast.LENGTH_LONG).show();
                } else {
                    Log.d(getClass().getName(), "Request is successful");
                    final JSONObject jsonReader;
                    try {

                        String jsonData = response.body().string();
                        jsonReader = new JSONObject(jsonData);

                        // Getting our instructions from our json
                        JSONArray arrayOfIndications = jsonReader
                                .getJSONArray("features")
                                .getJSONObject(0)
                                .getJSONObject("properties")
                                .getJSONArray("segments")
                                .getJSONObject(0)
                                .getJSONArray("steps");
                        for (int i = 0; i < arrayOfIndications.length(); i++) {
                            stepList.add(new StepBean(arrayOfIndications.getJSONObject(i)));
                        }
                        // Getting our geosteps from the json
                        JSONArray arrayOfGeoSteps = jsonReader
                                .getJSONArray("features")
                                .getJSONObject(0)
                                .getJSONObject("geometry")
                                .getJSONArray("coordinates");
                        for (int i = 0; i < arrayOfGeoSteps.length(); i++) {
                            JSONArray locationArray = arrayOfGeoSteps.getJSONArray(i);
                            final double longitudeOfPoint = locationArray.getDouble(0);
                            final double latitudeOfPoint = locationArray.getDouble(1);
                            geoPointList.add(new GeoPoint(latitudeOfPoint, longitudeOfPoint));
                        }

                    } catch (JSONException e) {
                        Log.e(getClass().getName(), e.getMessage());
                    } finally {
                        if (geoPointList.isEmpty() || stepList.isEmpty()) {
                            Log.e(this.getClass().getName(), "One of the list is empty");
                            Toast.makeText(context, res.getString(R.string.nothing_fetched), Toast.LENGTH_LONG).show();
                        } else {
                            Log.i(getClass().getName(), String.format(
                                    "%s points added to map" +
                                            "\n%s instructions added to the recyclerview",
                                    geoPointList.size(), stepList.size()
                            ));
                            // Updating our view
                            runOnUiThread(() -> {
                                Polyline polyline = new Polyline();
                                // TODO : Remove warning
                                polyline.setColor(Color.BLUE);
                                mMapView.getOverlayManager().add(polyline);
                                geoPointList.forEach(polyline::addPoint);

                                findViewById(R.id.fltBtnMapInstructions).setOnClickListener(k -> switchVisibilities());

                                findViewById(R.id.fltBtnSaveInDb).setOnClickListener(_k -> addRouteToFirestore());

                                recyclerView.setAdapter(new StepListAdapter(stepList.toArray(new StepBean[0])));
                            });
                        }
                    }
                }
            }
        });
    }

}