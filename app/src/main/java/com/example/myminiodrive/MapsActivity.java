package com.example.myminiodrive;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        DriveFirstActivity.pgd.cancel();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng bucharest = new LatLng(44, 26);
        LatLng brasov = new LatLng(45, 25);
        LatLng craiova = new LatLng(44, 23);
        mMap.addMarker(new MarkerOptions().position(bucharest).title("Server in Bucharest"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bucharest, 5));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.addMarker(new MarkerOptions().position(craiova).title("Server in Craiova"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(craiova,5));
        mMap.addMarker(new MarkerOptions().position(brasov).title("Server in Brasov"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(brasov,5));

    }
}