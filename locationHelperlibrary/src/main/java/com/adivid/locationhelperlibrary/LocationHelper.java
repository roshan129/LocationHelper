package com.adivid.locationhelperlibrary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.adivid.locationhelperlibrary.utils.LocationManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LocationHelper {

    private static Context context;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private Handler handler;
    private Runnable runnable;
    public boolean isTrackingOn = false;

    public LocationHelper(Activity activity) {
        context = activity.getApplicationContext();
        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(context);

    }

    public void getLastKnownLocation(LocationManager locationManager) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            locationManager.getLastLocation(location);

        }).addOnFailureListener(command -> {
            locationManager.getLastLocation(null);
        });
    }

    @SuppressLint("MissingPermission")
    public void getLastLocationAtTimeInterval(int timeInMillis, LocationManager locationManager) {
        try {
            if (!isTrackingOn) {
                isTrackingOn = true;

                handler = new Handler(Looper.getMainLooper());
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        isTrackingOn = true;
                        handler.postDelayed(() -> {
                            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                                locationManager.getLastLocation(location);
                                handler.postDelayed(this, timeInMillis);
                            }).addOnFailureListener(command -> {
                                locationManager.getLastLocation(null);
                            });
                        }, timeInMillis);
                    }
                };
                handler.postDelayed(runnable, 1000);
            }
        } catch (Exception e) {
            locationManager.getLastLocation(null);
        }
    }

    public void stopLocationTimeInterval() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
            isTrackingOn = false;
        }
    }

}


