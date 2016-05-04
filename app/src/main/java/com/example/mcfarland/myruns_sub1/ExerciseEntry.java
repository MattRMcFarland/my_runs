package com.example.mcfarland.myruns_sub1;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Calendar;
import java.util.ArrayList;

/**
 * Created by McFarland on 4/9/16.
 */

import com.google.android.gms.maps.model.LatLng;

public class ExerciseEntry implements Parcelable {

    private final static String TAG = "EXERCISE ENTRY";

    // use to index into R.array.input_types to get string
    public static final int MANUAL_INPUT = 0;
    public static final int GPS_INPUT = 1;
    public static final int AUTOMATIC_INPUT = 2;

    // use to index into R.array.activity_types to get activity string
    public static final int RUNNING_ACTIVITY = 0;
    public static final int WALKING_ACTIVITY = 1;
    public static final int STANDING_ACTIVITY = 2;
    public static final int CYCLING_ACTIVITY = 3;
    public static final int HIKING_ACTIVITY = 4;
    public static final int DOWNHILL_SKIING_ACTIVITY = 5;
    public static final int XC_SKIING_ACTIVITY = 6;
    public static final int SNOWBOARDING_ACTIVITY = 7;
    public static final int SKATING_ACTIVITY = 8;
    public static final int ROWING_ACTIVITY = 9;
    public static final int SWIMMING_ACTIVITY = 10;
    public static final int MOUNTAIN_BIKING_ACTIVITY = 11;
    public static final int WHEELCHAIR_ACTIVITY = 12;
    public static final int ELLIPTICAL_ACTIVITY = 13;
    public static final int OTHER_ACTIVITY = 14;

    private long _id = -1;

    private int mInputType;                 // Manual, GPS or automatic
    private int mActivityType;              // Running, cycling etc.
    private Calendar mDateTime;             // When does this entry happen
    private int mDuration;                  // Exercise duration in seconds
    private double mDistance;               // Distance traveled. Stored in Meters.
    private double mAvgPace;                // Average pace
    private double mAvgSpeed;               // Average speed
    private int mCalories;                  // Calories burnt
    private double mClimb;                  // Climb. Stored in meters.
    private int mHeartRate;                 // Heart rate
    private String mComment;                // Comments
    private ArrayList<LatLng> mLocationList; // Location list

    public ExerciseEntry() {
        mInputType = 0;
        mActivityType = 0;
        mDateTime = Calendar.getInstance();
        mDuration = 0;
        mDistance = 0.0;
        mAvgPace = 0.0;
        mAvgSpeed = 0.0;
        mCalories = 0;
        mClimb = 0.0;
        mHeartRate = 0;
        mComment = "";

        mLocationList = new ArrayList<LatLng>();
    }

    public ExerciseEntry(int inputType, int activityType) {
        mInputType = inputType;
        mActivityType = activityType;

        mDateTime = Calendar.getInstance();
        mDuration = 0;
        mDistance = 0.0;
        mAvgPace = 0.0;
        mAvgSpeed = 0.0;
        mCalories = 0;
        mClimb = 0.0;
        mHeartRate = 0;
        mComment = "";

        mLocationList = new ArrayList<LatLng>();
    }

    public ExerciseEntry(Long id, int inputType, int activityType) {
        this._id = id;
        mInputType = inputType;
        mActivityType = activityType;

        mDateTime = Calendar.getInstance();
        mDuration = 0;
        mDistance = 0.0;
        mAvgPace = 0.0;
        mAvgSpeed = 0.0;
        mCalories = 0;
        mClimb = 0.0;
        mHeartRate = 0;
        mComment = "";

        mLocationList = new ArrayList<LatLng>();
    }

    /* -------------------------- PARCEL FUNCTIONS -------------------------- */

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(_id);
        out.writeInt(mInputType);
        out.writeInt(mActivityType);
        out.writeLong(mDateTime.getTimeInMillis());
        out.writeInt(mDuration);
        out.writeDouble(mDistance);
        out.writeDouble(mAvgPace);
        out.writeDouble(mAvgSpeed);
        out.writeInt(mCalories);
        out.writeDouble(mClimb);
        out.writeInt(mHeartRate);
        out.writeString(mComment);

        // note: not saving the location list because we wouldn't use that in manual entry activity
    }

    private ExerciseEntry(Parcel in) {
        _id = in.readLong();
        mInputType = in.readInt();
        mActivityType = in.readInt();

        mDateTime = Calendar.getInstance();
        mDateTime.setTimeInMillis(in.readLong());

        mDuration = in.readInt();
        mDistance = in.readDouble();
        mAvgPace = in.readDouble();
        mAvgSpeed = in.readDouble();
        mCalories = in.readInt();
        mClimb = in.readDouble();
        mHeartRate = in.readInt();
        mComment = in.readString();

        mLocationList = null;
        // not saving location list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ExerciseEntry> CREATOR
            = new Parcelable.Creator<ExerciseEntry>() {

        @Override
        public ExerciseEntry createFromParcel(Parcel in) {
            return new ExerciseEntry(in);
        }

        @Override
        public ExerciseEntry[] newArray(int size) {
            return new ExerciseEntry[size];
        }
    };

    /* -------------------------- Utility and Getters / Setters -------------------------- */

    public String toString() {
        return "EE state:" +
                " \t_id = " + _id +
                ", \tinput type: " + mInputType +
                ", \tactivity type: " + mActivityType +
                ", \tdate time: " + mDateTime.getTime().toString() +
                ", \tduration: " + mDuration +
                ", \tdistance: " + mDistance +
                ", \tavg page: " + mAvgPace +
                ", \tavg speed: " + mAvgSpeed +
                ", \tcalories: " + mCalories +
                ", \tclimb: " + mClimb +
                ", \theart rate: " + mHeartRate +
                ", \tcomment: " + mComment +
                ", \tGPS entries: " + mLocationList.size();
    }

    public long get_id() {
        return _id;
    }

    public void set_id(Long id) {
        _id = id;
    }

    public int getmInputType() {
        return mInputType;
    }

    public void setmInputType(int mInputType) {
        this.mInputType = mInputType;
    }

    public int getmActivityType() {
        return mActivityType;
    }

    public void setmActivityType(int mActivityType) {
        this.mActivityType = mActivityType;
    }

    public Calendar getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(Calendar mDateTime) {
        this.mDateTime = mDateTime;
    }

    // alternative way to set time
    public void setmDateTime(Long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        this.mDateTime = c;
    }

    /*
     * returns raw duration in seconds
     */
    public int getmDuration() {
        return mDuration;
    }

    /*
     * mDuration -- duration in seconds
     */
    public void setmDuration(int mDuration) {

        this.mDuration = mDuration;
    }

    /*
     * returns raw distance in meters
     */
    public double getmDistance() {
        return mDistance;
    }

    /*
     * mDistance -- travel distance in meters
     */
    public void setmDistance(double mDistance) {
        this.mDistance = mDistance;
    }


    /*
     * returns raw meters / second
     */
    public double getmAvgPace() {
        return mAvgPace;
    }

    /*
     * mAvgPage held as raw : meters / seconds
     */
    public void setmAvgPace(double mAvgPace) {
        this.mAvgPace = mAvgPace;
    }

    // different than avg pace?
    public double getmAvgSpeed() {
        return mAvgSpeed;
    }

    public void setmAvgSpeed(double mAvgSpeed) {
        this.mAvgSpeed = mAvgSpeed;
    }

    public int getmCalories() {
        return mCalories;
    }

    public void setmCalories(int mCalories) {
        this.mCalories = mCalories;
    }

    /*
     * returns climb in meters
     */
    public double getmClimb() {
        return mClimb;
    }

    /*
     * raw climb value in meters
     */
    public void setmClimb(double mClimb) {
        this.mClimb = mClimb;
    }

    public int getmHeartRate() {
        return mHeartRate;
    }

    public void setmHeartRate(int mHeartRate) {
        this.mHeartRate = mHeartRate;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }


    public LatLng getLastPos() {
        if (mLocationList.size() > 0)
            return mLocationList.get(mLocationList.size() - 1);
        else
            return null;
    }

    /* -------------------------- LOCATION LIST -------------------------- */

    public void addLocation(LatLng pos) {
        mLocationList.add(pos);
    }

    public void setLocationList(ArrayList<LatLng> locList) {
        mLocationList = locList;
    }

    public ArrayList<LatLng> getLocationList() {
        return mLocationList;
    }
}
