package com.ez4us.shieldapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {
    FusedLocationProviderClient fusedLocationProviderClient;

    //Getting the location and handling the Location updates
    LocationCallback locationCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the fusedLocationProvider Client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Actual intialization
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult); //For Location Change
                Log.d("mylog :", "latitude : " + locationResult.getLastLocation().getLatitude() +
                        "longitude is :" + locationResult.getLastLocation().getLongitude());
                Intent intent = new Intent("act_location");
                intent.putExtra("Latitude", locationResult.getLastLocation().getLatitude());
                intent.putExtra("Longitude", locationResult.getLastLocation().getLongitude());
                double lat1 = locationResult.getLastLocation().getLatitude();
                double long1 = locationResult.getLastLocation().getLongitude();

                String lat01 = Double.toString(lat1);
                String long01 = Double.toString(long1);

                String location = "www.google.com/maps/search/?api=1&query=" + lat01 +","+ long01;
                intent.putExtra("locationlink", location);

                sendBroadcast(intent);
            }
        };
    }

    @Override
    //WE ARE CALLING THE REQUEST LOCATION METHOD
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    //Whenever we calll this method the location is requested

    @SuppressLint("MissingPermission")
    private void requestLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

}
