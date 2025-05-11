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


public class GPS {
    public interface GpsCallback {
        void onLocationReady(double lat, double lng);
    }

    public static void getCurrentLocation(Context context, GpsCallback callback) {
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

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (isValidLocation(location)) {
                callback.onLocationReady(location.getLatitude(), location.getLongitude());
            } else {
                requestFreshLocation(context, fusedLocationClient, callback);
            }
        });
    }

    private static boolean isValidLocation(Location location) {
        if (location == null) return false;

        double lat = location.getLatitude();
        double lng = location.getLongitude();

        return (lat != 0.0 || lng != 0.0) &&
                !(lat > 37.4 && lat < 37.5 && lng > -123 && lng < -121); // Exclude the default San Jose latitude and longitude coordinates of the virtual machine
    }

    public static void requestFreshLocation(Context context, FusedLocationProviderClient client, GpsCallback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; //exit when permissions insufficient
        }

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
