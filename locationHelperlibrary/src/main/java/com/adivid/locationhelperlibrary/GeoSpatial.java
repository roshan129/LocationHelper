package com.adivid.locationhelperlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class GeoSpatial extends AppCompatActivity {

    private static final String TAG = "GeoSpatial";
    private final Context context;
    private final Activity activity;
    private final LocationCallback locationCallback;
    private final FusedLocationProviderClient fusedLocationProviderClient;

    public GeoSpatial(Activity activity) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.d(TAG, "onLocationResult: thread: " + Thread.currentThread());
                    Log.d(TAG, "onLocationResult: locationCallback: " + location.getLatitude());
                    Log.d(TAG, "onLocationResult: longitude: " + location.getLongitude());
                    Log.d(TAG, "onLocationResult: altitude: " + location.getAltitude());
                    Log.d(TAG, "onLocationResult: accuracy: " + location.getAccuracy());
                    Log.d(TAG, "onLocationResult: speed: " + location.getSpeed());
                    Log.d(TAG, "onLocationResult: bearing: " + location.getBearing());
                }
            }
        };
    }

    public void createLocationRequest(int TIME_IN_MILLIS){
        Log.d(TAG, "createLocationRequest: thread: " + Thread.currentThread());
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(TIME_IN_MILLIS);
        locationRequest.setFastestInterval(TIME_IN_MILLIS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        Log.d(TAG, "createLocationRequest: task: " + task.isSuccessful());

        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.d(TAG, "onSuccess: ");
                Log.d(TAG, "onSuccess: states" +
                        locationSettingsResponse.getLocationSettingsStates());
                startLocationUpdates(locationRequest);

            }
        });

        task.addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: ");
                if (e instanceof ResolvableApiException) {
                    Log.d(TAG, "onFailure: inside if");
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(activity,
                                101);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates(LocationRequest locationRequest) {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

}
