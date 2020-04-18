package com.ksu.serene.controller.main.report;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;


public class LocationHeatMap extends FragmentActivity
        implements OnMapReadyCallback {

    private final double lat = MainActivity.lastLocation.getLatitude();
    private final double lng = MainActivity.lastLocation.getLongitude();

    private GoogleMap mMap;

    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;


    private boolean i = true;


    /**
     * Alternative radius for convolution
     */
    private static final int ALT_HEATMAP_RADIUS = 10;


    public static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = {
            0.0f, 0.10f, 0.20f, 0.60f, 1.0f
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_heat_map);

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

        if ( !PatientReport.locations.isEmpty() ){
        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = new HeatmapTileProvider.Builder()
                .weightedData(PatientReport.locations)
                .build();

        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
            i = false;
        }

    }


}
