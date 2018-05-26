package com.hnweb.clawpal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hnweb.clawpal.location.LocationSet;
import com.hnweb.clawpal.location.MyLocationListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.hnweb.clawpal.location.LocationSet.requestPermission;

/**
 * Created by Shree on 16-Dec-17.
 */

public class CustomLocation extends LocationSet {
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    MyLocationListener myLocationListener;
    double latitude = 0.0d;
    double longitude = 0.0d;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myLocationListener = new MyLocationListener(CustomLocation.this);
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext(), CustomLocation.this)) {
            fetchLocationData();
        } else {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), CustomLocation.this);
        }
    }
    public void fetchLocationData() {
        // check if myLocationListener enabled
        if (myLocationListener.canGetLocation()) {
            latitude = myLocationListener.getLatitude();
            longitude = myLocationListener.getLongitude();

            // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            myLocationListener.showSettingsAlert();
            latitude = myLocationListener.getLatitude();
            longitude = myLocationListener.getLongitude();

            // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

        }

        Geocoder geocoder = new Geocoder(CustomLocation.this, Locale.getDefault());
        List<Address> addresses = null;
        StringBuilder result = new StringBuilder();

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                //  result.append(address.getAddressLine(0)).append(" ");
                result.append(address.getAddressLine(1)).append(" ");
                // result.append(address.getAddressLine(2)).append("");
                //result.append(address.getAddressLine(3)).append(" ");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fetchLocationData();

                } else {

                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();

                }
                break;

        }
    }

}
