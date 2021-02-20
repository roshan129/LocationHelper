package com.adivid.locationhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.adivid.locationhelperlibrary.GeoSpatial;
import com.adivid.locationhelperlibrary.LocationHelper;
import com.adivid.locationhelperlibrary.utils.LocationManager;

public class MainActivity extends AppCompatActivity {

    private int TIME_IN_MILLIS = 5000;
    private LocationHelper locationHelper;
    private static final String TAG = "MainActivity";

    private GeoSpatial geoSpatial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationHelper = new LocationHelper(this);

        geoSpatial = new GeoSpatial(this);

        geoSpatial.createLocationRequest(TIME_IN_MILLIS);

        locationHelper.getLastKnownLocation(location -> {
            Log.d(TAG, "onCreate: latitude" + location.getLatitude());
            Log.d(TAG, "onCreate: longitude" + location.getLongitude());
        });

        /*if(location!= null) {
            Log.d(TAG, "onCreate: getLatitude:  " + location.getLatitude());
            Log.d(TAG, "onCreate: getLongitude: " + location.getLongitude());
        }*/

        findViewById(R.id.buttonLocation).setOnClickListener(v -> {
            locationHelper.getLastLocationAtTimeInterval(1000, location ->
                    Log.d(TAG, "getLastLocation: time1: "+ location.getLongitude()));

        });

        findViewById(R.id.buttonLocationStop).setOnClickListener(v -> {

            locationHelper.stopLocationTimeInterval();
            Log.d(TAG, "onCreate: stopped");
        });

        findViewById(R.id.buttonNext).setOnClickListener(v -> {
            startActivity(new Intent(this, NextActivity.class));
        });

    }

    @Override
    protected void onPause() {
        //geoSpatial.stopLocationUpdates();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        geoSpatial.stopLocationUpdates();
        super.onDestroy();
    }
}