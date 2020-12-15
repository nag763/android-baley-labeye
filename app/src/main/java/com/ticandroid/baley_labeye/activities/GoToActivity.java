package com.ticandroid.baley_labeye.activities;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.ticandroid.baley_labeye.BuildConfig;
import com.ticandroid.baley_labeye.R;

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
import java.util.Arrays;
import java.util.List;

public class GoToActivity extends AppCompatActivity {

    private transient MapView mMapView;
    private transient MapController mMapController;
    private transient String userPosition;
    private transient String museumPosition;
    private static Resources res;

    private void drawMarker(GeoPoint geoPoint){
        Marker marker = new Marker(mMapView);
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMapView.getOverlayManager().add(marker);
    }

    private double[] positionToDoubleArray(String position) {
        // That's aesthetic as fuck
        return Arrays.stream(position.split(",")).mapToDouble(Double::parseDouble).toArray();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to);
        res = getResources();

        userPosition = getIntent().getStringExtra(MuseumReaderActivity.KEY_OF_EXTRA_USER_POSITION);
        final double[] userPositionAsDouble = positionToDoubleArray(userPosition);
        museumPosition = getIntent().getStringExtra(MuseumReaderActivity.KEY_OF_EXTRA_MUSEUM_POSITION);
        final double[] museumPositionAsDouble = positionToDoubleArray(museumPosition);

        mMapView = findViewById(R.id.mapview);
        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        mMapController = (MapController) mMapView.getController();
        mMapController.setZoom(13);

        GeoPoint userGpt = new GeoPoint(userPositionAsDouble[1], userPositionAsDouble[0]);
        GeoPoint museumGpt = new GeoPoint(museumPositionAsDouble[1], museumPositionAsDouble[0]);

        drawMarker(userGpt);
        drawMarker(museumGpt);

        mMapController.setCenter(userGpt);

        drawMap(userPosition, museumPosition);
    }

    private void drawMap(String userPosition, String museumPosition) {
        OkHttpClient okHttpClient = new OkHttpClient();
        TextView tvDistanceToMuseum = findViewById(R.id.tvDistanceToMuseum);

        String json = String.format(
                "{\"coordinates\":[[%s],[%s]],"
                 + "\"language\":\"fr\"}",
                userPosition, museumPosition);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(res.getString(R.string.ors_directions_url))
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

            @Override
            public void onResponse(Response response) throws IOException {
                List<GeoPoint> geoPointList = new ArrayList<>();

                if (!response.isSuccessful()) {
                    Log.w(this.getClass().getName(), String.format(
                            "Request with error code : %s\n%s",
                            response.code(), response.body().string())
                    );
                } else {
                    Log.d(this.getClass().getName(), "Request is successful");
                    final JSONObject jsonReader;
                    try {
                        String jsonData = response.body().string();
                        jsonReader = new JSONObject(jsonData);
                        JSONArray jsonArray = jsonReader
                                .getJSONArray("features")
                                .getJSONObject(0)
                                .getJSONObject("geometry")
                                .getJSONArray("coordinates");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONArray locationArray = jsonArray.getJSONArray(i);
                            final double longitudeOfPoint = locationArray.getDouble(0);
                            final double latitudeOfPoint = locationArray.getDouble(1);
                            // The order changes between the two
                            geoPointList.add(new GeoPoint(latitudeOfPoint, longitudeOfPoint));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (geoPointList.isEmpty()) {
                            Log.e(this.getClass().getName(), "No points added to the map");
                        } else {
                            Log.i(this.getClass().getName(), String.format(
                                    "%s points added to map", geoPointList.size()
                            ));

                            runOnUiThread(() -> {
                                Polyline polyline = new Polyline();
                                polyline.setColor(Color.BLUE);
                                mMapView.getOverlayManager().add(polyline);
                                geoPointList.forEach(polyline::addPoint);
                            });
                        }
                    }
                }
            }
        });
    }
}