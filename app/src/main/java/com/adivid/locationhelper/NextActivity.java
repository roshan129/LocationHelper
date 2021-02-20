package com.adivid.locationhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.adivid.locationhelperlibrary.LocationHelper;

public class NextActivity extends AppCompatActivity {

    private static final String TAG = "NextActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);


        LocationHelper locationHelper = new LocationHelper(this);

        locationHelper.getLastLocationAtTimeInterval(1000, location -> {
            Log.d(TAG, "onCreate: next: " + location.getLatitude());
        });
    }
}