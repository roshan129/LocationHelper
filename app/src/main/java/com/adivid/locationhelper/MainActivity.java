package com.adivid.locationhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.adivid.locationhelperlibrary.LocationHelper;
import com.adivid.locationhelperlibrary.utils.LocationManager;

public class MainActivity extends AppCompatActivity {

    LocationHelper locationHelper;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationHelper = new LocationHelper(this);

        locationHelper.getLastKnownLocation(location -> {
            Log.d(TAG, "onCreate: latitude" + location.getLatitude());
            Log.d(TAG, "onCreate: longitude" + location.getLongitude());
        });

        /*if(location!= null) {
            Log.d(TAG, "onCreate: getLatitude:  " + location.getLatitude());
            Log.d(TAG, "onCreate: getLongitude: " + location.getLongitude());
        }*/

        Log.d(TAG, "onCreate: " + LocationHelper.getCustomFormattedString("abc"));


        locationHelper.getLastLocationAtTimeInterval(1000, location ->
                Log.d(TAG, "getLastLocation: time: "+ location.getLongitude()));


    }
}