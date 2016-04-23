package com.example.mcfarland.myruns_sub1;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by McFarland on 4/21/16.
 */
public class Conversions {

    private static final String TAG = "CONVERTER";

    private static final double KM_TO_MILE_RATIO = 0.621371;
    private static final double MILE_TO_KM_RATIO = 1.60934;
    private static final double METERS_TO_FEET_RATIO = 3.28084;

    private static final int METRIC = 1;
    private static final int IMPERIAL = 2;
    private static final int DEFAULT_UNIT = 1;      // imperial

    private Context mContext;

    public Conversions(Context c) {
        mContext = c;
    }

    public double metersToKM(double meters) {
        return meters * 1000;
    }

    public double metersToFeet(double meters) {
        return meters * METERS_TO_FEET_RATIO;
    }

    public double kmToMeters(double kms) {
        return kms / 1000;
    }

    public double metersToMile(double meters) {
        return meters * KM_TO_MILE_RATIO * 1000;
    }

    public double mileToMeters(double miles) {
        return miles * MILE_TO_KM_RATIO / 1000;
    }

    /*
     * returns true if preference is metric
     */
    public boolean isPreferenceMetric() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Resources res = mContext.getResources();
        return Integer.parseInt(sharedPref.getString(res.getString(R.string.unit_preference_key), "")) == METRIC;
    }

    // convert ArrayList<LatLng> to byte array
    public int[] convertFromListToByteArray(ArrayList<LatLng> list) {
        int[] blob = new int[list.size() * 2];

        return blob;
    }

    // convert byte array to ArrayList<LatLng>
    public ArrayList<LatLng> convertFromByteArrayToList(byte[] blob) {
        ArrayList<LatLng> coordinates = new ArrayList<LatLng>();

        return coordinates;
    }

}
