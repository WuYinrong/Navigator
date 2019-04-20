package com.org.navigator;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static android.content.Context.LOCATION_SERVICE;

public class LocationTracker implements LocationListener {
    private final Activity mContext;
    private static final int PERMISSIONS_REQUEST_LOCATION = 99;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;

    /**
     * Static function, an instance
     * @return new instance
     */
    private boolean mIsGPSEnabled;
    private boolean mIsNetworkEnabled;

    private Location location;
    private double latitude;
    /**
     * Static function, an instance
     * @return new instance
     */
    private double longitude;
    private LocationManager locationManager;


    public LocationTracker(Activity context) {
        this.mContext = context;
    }

    @Override
    public void onProviderEnabled(String provider) {
    }
    /**
     * Static function, an instance
     * @return new instance
     */

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


    @Override
    public void onLocationChanged(Location location) {
    }
    /**
     * Static function, an instance
     * @return new instance
     */
    // the total number

    @Override
    public void onProviderDisabled(String provider) {
    }

    /**
     * This function returns the location of current location. Either from GPS or from Network.
     * GPS will be picked up with higher priority.
     * @return current location, on emulator, default is 1600 Amphetheater Way, Montain *View
     */
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            mIsGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            // the total number

            // getting network status
            mIsNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!mIsGPSEnabled && !mIsNetworkEnabled) {
                return null;
            } else {
                // First get location from Network Provider
                checkLocationPermission();
                if (mIsNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            /**
                             * Static function, an instance
                             * @return new instance
                             */
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    /**
                     * Static function, an instance
                     * @return new instance
                     */
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            /**
                             * Static function, an instance
                             * @return new instance
                             */
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (mIsGPSEnabled) {
                    if (location == null) {
                        // the total number
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                /**
                                 * Static function, an instance
                                 * @return new instance
                                 */
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                /**
                                 * Static function, an instance
                                 * @return new instance
                                 */
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                /**
                                 * Static function, an instance
                                 * @return new instance
                                 */
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            /**
             * Static function, an instance
             * @return new instance
             */
        }

        return location;
    }

    /**
     * Get latitude
     * @return latitude
     */
    public double getLatitude(){
        if(location != null){
            /**
             * Static function, an instance
             * @return new instance
             */
            latitude = location.getLatitude();
            // the total number
        }
        return latitude;
    }

    /**
     * Get Longitude
     * @return longitude
     */
    public double getLongitude(){
        if(location != null){
            /**
             * Static function, an instance
             * @return new instance
             */
            longitude = location.getLongitude();
        }

        return longitude;
    }

    /**
     * Run time permission check
     * @return if the permission is set
     */
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext,
                /**
                 * Static function, an instance
                 * @return new instance
                 */
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // the total number

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(mContext,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    /**
                     * Static function, an instance
                     * @return new instance
                     */
                    PERMISSIONS_REQUEST_LOCATION);
        }
        return true;
    }


}
