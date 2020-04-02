package com.ksu.serene.controller.main.report;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;
import com.ksu.serene.model.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class LocationHeatMap extends FragmentActivity
        implements OnMapReadyCallback {

    private final double lat = MainActivity.lastLocation.getLatitude();
    private final double lng = MainActivity.lastLocation.getLongitude();

    private GoogleMap mMap;

    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    private FirebaseAuth mAuth;
    private String userId;
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private boolean i = true;

    ArrayList<WeightedLatLng> list = new ArrayList<>();;

    /**
     * Alternative radius for convolution
     */
    private static final int ALT_HEATMAP_RADIUS = 10;

    /**
     * Alternative opacity of heatmap overlay
     */
    private static final double ALT_HEATMAP_OPACITY = 0.4;

    /**
     * Alternative heatmap gradient (blue -> red)
     * Copied from Javascript version
     */
    private static final int[] ALT_HEATMAP_GRADIENT_COLORS = {
            Color.argb(0, 0, 255, 255),// transparent
            Color.argb(255 / 3 * 2, 0, 255, 255),
            Color.rgb(0, 191, 255),
            Color.rgb(0, 0, 127),
            Color.rgb(255, 0, 0)
    };

    public static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = {
            0.0f, 0.10f, 0.20f, 0.60f, 1.0f
    };

    public static final Gradient ALT_HEATMAP_GRADIENT = new Gradient(ALT_HEATMAP_GRADIENT_COLORS,
            ALT_HEATMAP_GRADIENT_START_POINTS);

    private boolean mDefaultGradient = true;
    private boolean mDefaultRadius = true;
    private boolean mDefaultOpacity = true;

    /**
     * Maps name of data set to data (list of LatLngs)
     * Also maps to the URL of the data set for attribution
     */
    //private HashMap<String, DataSet> mLists = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_heat_map);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-25, 143), 4));

        addHeatMap();
        setInitialLocation(lat,lng);
    }

    private void setInitialLocation(double lat, double lng){

        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate CU = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.moveCamera(CU);

    }

    private void addHeatMap() {
        //List<LatLng> list = null;

        // Get the data: latitude/longitude positions of police stations.
//        if(i) {
//            try {
//                list = readItems();
//                i = false;
//            } catch (JSONException e) {
//                Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
//            }
//        }

        if ( !PatientReport.locations.isEmpty() ){
        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(PatientReport.locations)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
            i = false;
        }
        else { Log.e("UGH","Heatmap failed");
             }
    }

    private ArrayList<WeightedLatLng> readItems() throws JSONException {


        //list = new ArrayList<>();


//        firebaseFirestore.collection("PatientLocations")
//                .whereEqualTo("patientID", userId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if(task.isSuccessful()){
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                //TODO :check for location date if it's within selected duration && include anxiety level
//
////                                Date loc_date = ((Timestamp)doc.get(i).get("time")).toDate();//date received
//
//                                String name = document.get("name").toString();
//                                double lat = (double) document.get("lat");
//                                double lng = (double) document.get("lng");
//                                double anxietyLevel = Double.parseDouble(document.get("anxietyLevel").toString());
//
//                                list.add(new WeightedLatLng(new LatLng(lat, lng),anxietyLevel));
//
//                            }// for every location belonging to this patient (for loop)
//
//
//                        }// end if
//
//                    }// onComplete
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });//addOnCompleteListener

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("PatientLocations").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());

                                double lat = (double) document.get("lat");
                                double lng = (double) document.get("lng");
                                double anxietyLevel = Double.parseDouble(document.get("anxietyLevel").toString());

                                LatLng current = new LatLng(lat,lng);

                                boolean found = false;

                                for(WeightedLatLng lis : list){

                                    if(lis.getPoint().equals(current)){
                                        if(lis.getIntensity() < anxietyLevel ){
                                            list.remove(lis);
                                            list.add(new WeightedLatLng(current, anxietyLevel));
                                            found = true;
                                        }
                                    }

                                }

                                if (!found){
                                    list.add(new WeightedLatLng(current, anxietyLevel));
                                }

                            }

                            list.add(new WeightedLatLng(new LatLng(24.77742, 46.67151),2));
                            list.add(new WeightedLatLng(new LatLng(24.77742, 46.67151),2));
                            list.add(new WeightedLatLng(new LatLng(24.77742, 46.67151),3));
                            list.add(new WeightedLatLng(new LatLng(24.77742, 46.67151),3));
                            list.add(new WeightedLatLng(new LatLng(24.77742, 46.67151),3));
                            list.add(new WeightedLatLng(new LatLng(24.77742, 46.67151),3));
                            list.add(new WeightedLatLng(new LatLng(24.77742, 46.67151),1));
                            list.add(new WeightedLatLng(new LatLng(24.77742, 46.67151),1));
                            list.add(new WeightedLatLng(new LatLng(24.77742, 46.67151),1));
                            list.add(new WeightedLatLng(new LatLng(24.77742, 46.67151),2));
                            list.add(new WeightedLatLng(new LatLng(24.77742, 46.67151),2));


                            list.add(new WeightedLatLng(new LatLng(24.760549, 46.603598),2));
                            list.add(new WeightedLatLng(new LatLng(24.724097, 46.636926),2));


                            list.add(new WeightedLatLng(new LatLng(24.7232795, 46.5985618),1));
                            list.add(new WeightedLatLng(new LatLng(24.7232795, 46.5985618),3));
                            list.add(new WeightedLatLng(new LatLng(24.7232795, 46.5985618),3));
                            list.add(new WeightedLatLng(new LatLng(24.7232795, 46.5985618),1));
                            list.add(new WeightedLatLng(new LatLng(24.7232795, 46.5985618),1));
                            list.add(new WeightedLatLng(new LatLng(24.7232795, 46.5985618),1));
                            list.add(new WeightedLatLng(new LatLng(24.7232795, 46.5985618),1));



                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        addHeatMap();

                    }

                });


        return list;


    }

}
