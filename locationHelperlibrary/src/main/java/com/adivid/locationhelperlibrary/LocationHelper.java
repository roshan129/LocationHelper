package com.adivid.locationhelperlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationProvider;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.adivid.locationhelperlibrary.utils.LocationManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationHelper {

    private static final String TAG = "LocationHelper";
    private static Context context;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public LocationHelper(Activity activity) {
        context = activity.getApplicationContext();
        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context);

    }

    public static String getCustomFormattedString(String string) {
        return string + "-" + string;
    }

    public void getLastKnownLocation(LocationManager locationManager) {
        final Location[] sendLocation = new Location[1];
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            Log.d(TAG, "getLastKnownLocation: inside success");
            sendLocation[0] = location;
            Log.d(TAG, "getLastKnownLocation: location: " + location);
            Log.d(TAG, "getLastKnownLocation: " + sendLocation[0]);

            locationManager.getLastLocation(location);

        }).addOnFailureListener(command -> {
            Log.d(TAG, "getLastKnownLocation: exception " + command.toString());
            sendLocation[0] = null;
            locationManager.getLastLocation(null);
        });
    }
}


