package com.example.parkingreport.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;


/**
 * Utility class for retrieving the device's current GPS coordinates.
 *Supports both cached (last known) and real-time location fetching via FusedLocationProviderClient.
 *Applies basic filtering to eliminate null or invalid mock coordinates.
 * Authored by Larry Wang u7807744
 */
public class GPS {
    public interface GpsCallback {
        void onLocationReady(double lat, double lng);
    }

    public static void getCurrentLocation(Context context, GpsCallback callback) {
        //if no permission granted, ask for GPS permission
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (context instanceof Activity) {
                ActivityCompat.requestPermissions(
                        (Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1
                ); //request for GPS permission
            } else {
                Log.e("GPS", "Context is not an Activity. Cannot request permissions.");
                return;
            }

        }

        // Try to get last known location; fallback to real-time request if null or invalid
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (isValidLocation(location)) {
                callback.onLocationReady(location.getLatitude(), location.getLongitude());
            } else {
                requestFreshLocation(context, fusedLocationClient, callback);//request a real-time location
            }
        });
    }

     public static boolean isValidLocation(Location location) {
        if (location == null) return false;

        double lat = location.getLatitude();
        double lng = location.getLongitude();

         // Exclude the default San Jose(Google) latitude and longitude coordinates of the virtual machine
        return (lat != 0.0 || lng != 0.0) &&
                !(lat > 37.4 && lat < 37.5 && lng > -123 && lng < -121);
    }

    public static void requestFreshLocation(Context context, FusedLocationProviderClient client, GpsCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; //exit when permissions insufficient
        }

        // Request a single high-accuracy location update
        LocationRequest request = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setMaxUpdates(1)
                .build();


        client.requestLocationUpdates(request, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                Location loc = result.getLastLocation();
                if (isValidLocation(loc)) {
                    callback.onLocationReady(loc.getLatitude(), loc.getLongitude());
                }
            }
        }, Looper.getMainLooper());
    }
}
