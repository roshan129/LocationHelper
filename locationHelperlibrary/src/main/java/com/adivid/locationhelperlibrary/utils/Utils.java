package com.adivid.locationhelperlibrary.utils;

import android.Manifest;
import android.content.Context;
import android.os.Build;

import pub.devrel.easypermissions.EasyPermissions;

public class Utils {

    public static boolean hasLocationPermissions(Context context) {
        return EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        );

    }

}
