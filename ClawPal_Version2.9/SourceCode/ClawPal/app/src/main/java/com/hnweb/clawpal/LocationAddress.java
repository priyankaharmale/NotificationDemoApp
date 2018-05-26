package com.hnweb.clawpal;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by HNWeb-11 on 7/14/2016.
 */

public class LocationAddress {
    private static final String TAG = "LocationAddress";
    static SharedPreferences pref;
    static Context mcontext;
    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {
        mcontext=context;
        pref = mcontext.getSharedPreferences("MyPref", mcontext.MODE_PRIVATE);
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = null;

                        addressList = geocoder.getFromLocation(
                                latitude, longitude, 1);

                    if (addressList != null && addressList.size() > 0) {

                        Address address = addressList.get(0);
                        String city=address.getLocality();


                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append(",");
                        }
                       // sb.append(address.getLocality());
                       // sb.append(address.getPostalCode());

                        sb.append(address.getCountryName());

                        String c_add=sb.toString();
                        Log.i("c_add",c_add);
                        SharedPreferences.Editor editor;
                        editor = pref.edit();

                        editor.putString("Current_add", c_add);

                        editor.commit();
                        result = city;

                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();

                        ///////for location longitude and lattitude
                        /*result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n\nAddress:\n" + result;*/
                        ///////////////////only address////////////////
                        result = "" + result;
                        bundle.putString("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Latitude: " + latitude + " Longitude: " + longitude +
                                "\n Unable to get address for this lat-long.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}