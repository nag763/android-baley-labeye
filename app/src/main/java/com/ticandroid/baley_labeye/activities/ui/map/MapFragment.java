package com.ticandroid.baley_labeye.activities.ui.map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
import java.util.Objects;

/**
 * Map Fragment used to display all the museums stored in the FS instance on a
 * osm map.
 *
 * @author Baley
 * @author Labeye
 * @see Fragment
 */
public class MapFragment extends Fragment {

    /**
     * Current map view being displayed.
     **/
    private transient MapView mMapView;


    private transient final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    /**
     * Draw a marker with the given geopoint.
     *
     * @param geoPoint geopoint where the marker needs to be drawn
     */
    private void drawMarker(GeoPoint geoPoint, String museumName, int numberOfVisits) {
        Marker marker = new Marker(mMapView);
        marker.setPosition(geoPoint);
        marker.setTitle(museumName);
        marker.setSnippet(String.format("Nombre de visites : %s", numberOfVisits));
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMapView.getOverlayManager().add(marker);
        Log.d(getClass().getName(), String.format("marker added to gp : %s", geoPoint.toString()));
    }

    /**
     * Parse the position as string to a array of double.
     *
     * @param position position to parse
     * @return position as double array
     */
    private double[] positionToDoubleArray(String position) {
        final String splitter = ",";
        try {
            final int numberOfSplittableRequired = 2;
            if (!position.contains(splitter)) {
                throw new ParseException("Array doesn't contain the splitter museums", 0);
            } else if (position.split(splitter).length != 2) {
                throw new ParseException("Array got too many splittable args", position.lastIndexOf(splitter));
            } else if (position.trim().isEmpty()) {
                throw new Exception("The string is empty");
            } else {
                return Arrays.stream(position.split(splitter)).mapToDouble(Double::parseDouble).toArray();
            }
        } catch (Exception e) {
            Log.e(getClass().getName(), "Exception occured\nException : %s", e);
            return null;
        }
    }

    /**
     * Default constructor.
     */
    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Creates an instance of the fragment.
     *
     * @return a new map fragment
     */
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
        // Create map view
        mMapView = root.findViewById(R.id.mapMuseumsView);
        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        // Focus on the geographical center of the metropolitan france
        final MapController mMapController = (MapController) mMapView.getController();
        final GeoPoint centerOfFrance = new GeoPoint(47.1539d, 0.22508d);
        mMapController.setCenter(centerOfFrance);
        mMapController.setZoom(7);
        // Add geomarkers for museums
        CollectionReference collectionReference = firebaseFirestore.collection("museums");
        Task<QuerySnapshot> task = collectionReference.get();
        task.addOnCompleteListener(k ->
                task.getResult().forEach(museums -> {
                            Query visiteCollection = firebaseFirestore
                                                            .collection("visites")
                                                            .whereEqualTo("idMusee", museums.getId());
                            Task<QuerySnapshot> task1 = visiteCollection.get();
                            task1.addOnCompleteListener(visits -> {
                                        try {
                                            final int numberOfVisits = visits
                                                                        .getResult()
                                                                        .size();
                                            final MuseumBean museumBean = Objects
                                                                        .requireNonNull(museums)
                                                                        .toObject(MuseumBean.class);
                                            final double[] position = positionToDoubleArray(museumBean.getCoordonneesFinales());
                                            drawMarker(new GeoPoint(position[0], position[1]), museumBean.getNomDuMusee(), numberOfVisits);

                                        } catch (Exception e) {
                                            Log.e(getClass().getName(), e.getMessage());
                                        }
                                    }
                            );
                        }
                ));


        return root;
    }
}