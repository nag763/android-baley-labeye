package com.ticandroid.baley_labeye.activities.ui.map;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ticandroid.baley_labeye.BuildConfig;
import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.beans.MuseumBean;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    /**
     * Current map view being displayed
     **/
    private transient MapView mMapView;


    private transient final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    /**
     * Draw a marker with the given geopoint
     *
     * @param geoPoint geopoint where the marker needs to be drawn
     */
    private void drawMarker(GeoPoint geoPoint) {
        Marker marker = new Marker(mMapView);
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTextIcon("Hello world! ");
        mMapView.getOverlayManager().add(marker);
        Log.d(getClass().getName(), String.format("marker added to gp : %s", geoPoint.toString()));
    }

    /**
     * Parse the position as string to a array of double
     *
     * @param position position to parse
     * @return position as double array
     */
    private double[] positionToDoubleArray(String position) {
        final String SPLITTABLE = ",";
        try {
            if (!position.contains(SPLITTABLE)) {
                throw new ParseException("Array doesn't contain the splitter element", 0);
            } else if (position.split(SPLITTABLE).length != 2) {
                throw new ParseException("Array got too many splittable args", position.lastIndexOf(SPLITTABLE));
            } else if (position.trim().isEmpty()) {
                throw new NullPointerException("The string is empty");
            } else {
                return Arrays.stream(position.split(SPLITTABLE)).mapToDouble(Double::parseDouble).toArray();
            }
        } catch (ParseException | NullPointerException e) {
            Log.e(getClass().getName(), "Exception occured\nException : %s", e);
            return null;
        }
    }

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        super.onCreate(savedInstanceState);

        mMapView = root.findViewById(R.id.mapMuseumsView);
        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        MapController mMapController = (MapController) mMapView.getController();

        CollectionReference collectionReference = firebaseFirestore.collection("museums");
        Task<QuerySnapshot> task = collectionReference.get();
        task.addOnCompleteListener(k -> {
            task.getResult().forEach(element -> {
                try {
                    final MuseumBean museumBean = Objects.requireNonNull(element).toObject(MuseumBean.class);
                    final double[] position = positionToDoubleArray(museumBean.getCoordonneesFinales());
                    drawMarker(new GeoPoint(position[0], position[1]));
                } catch (Exception e){
                    Log.e(getClass().getName(), e.getMessage());
                }
            });
        });


        return root;
    }
}