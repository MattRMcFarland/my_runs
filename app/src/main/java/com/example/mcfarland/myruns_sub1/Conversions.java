package com.example.mcfarland.myruns_sub1;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.util.ArrayList;

/**
 * Created by McFarland on 4/21/16.
 */
public class Conversions {

    private static final String TAG = "CONVERTER";

    private static final double KM_TO_MILE_RATIO = 0.621371;
    private static final double MILE_TO_KM_RATIO = 1.60934;
    private static final double METERS_TO_FEET_RATIO = 3.28084;
    private static final double MPS_TO_KMH = 3.6;
    private static final double MPS_TO_MPH = 2.23694;

    private static final int METRIC = 1;
    private static final int IMPERIAL = 2;
    private static final int DEFAULT_UNIT = 1;      // imperial

    private Context mContext;

    public Conversions(Context c) {
        mContext = c;
    }

    public double metersToKM(double meters) {
        return meters / 1000;
    }

    public double metersToFeet(double meters) {
        return meters * METERS_TO_FEET_RATIO;
    }

    public double kmToMeters(double kms) {
        return kms * 1000;
    }

    public double metersToMile(double meters) {
        return meters * KM_TO_MILE_RATIO / 1000;
    }

    public double mileToMeters(double miles) {
        return miles * MILE_TO_KM_RATIO * 1000;
    }

    public double metersPerSecondToKMperHour(double mps) {
        return mps * MPS_TO_KMH;
    }

    public double metersPerSecondToMilesPerHour(double mps) {
        return mps * MPS_TO_MPH;
    }

    /*
     * returns true if preference is metric
     */
    public boolean isPreferenceMetric() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Resources res = mContext.getResources();

        // default value is set to "1" -> Imperial units
        return Integer.parseInt(sharedPref.getString(res.getString(R.string.unit_preference_key), "1")) == METRIC;
    }

    // convert ArrayList<LatLng> to byte array
    public byte[] convertFromListToByteArray(ArrayList<LatLng> list) {
        if (list == null)
            return new byte[0];

        double[] doubleArray = new double[list.size() * 2];
        for (int i = 0; i < list.size(); i++) {
            doubleArray[2*i] = list.get(i).latitude;
            doubleArray[2*i+1] = list.get(i).longitude;
        }

        // create a byte buffer and then fill it with doubles
        ByteBuffer bb = ByteBuffer.allocate(doubleArray.length * Double.SIZE);
        DoubleBuffer db = bb.asDoubleBuffer();
        db.put(doubleArray);

        return bb.array();
    }

    // convert byte array to ArrayList<LatLng>
    public ArrayList<LatLng> convertFromByteArrayToList(byte[] blob) {
        if (blob == null)
            return new ArrayList<LatLng>();         // empty list

        ArrayList<LatLng> coordinates = new ArrayList<LatLng>();

        ByteBuffer bb = ByteBuffer.wrap(blob);
        DoubleBuffer db = bb.asDoubleBuffer();

        double[] doubleArray = new double[blob.length / Double.SIZE];
        db.get(doubleArray);

        for (int i = 0; i < blob.length / Double.SIZE; i = i + 2) {
            coordinates.add(new LatLng(doubleArray[i], doubleArray[i+1]));
        }

        return coordinates;
    }

}
