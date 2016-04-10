package com.example.mcfarland.myruns_sub1;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by McFarland on 4/9/16.
 */

public class ExerciseEntry {

//    public enum InputType_t {MANUAL_INPUT, GPS_INPUT, AUTOMATIC_INPUT}
//
//    public enum ActivityType_t{ RUNNING_ACTIVITY, WALKING_ACTIVITY, STANDING_ACTIVITY,
//        CYLING_ACTIVITY, HIKING_ACTIVITY, DOWNHILL_SKIING_ACTIVITY, XC_SKIING_ACTIVITY,
//        SNOWBOARDING_ACTIVITY, SKATING_ACTIVITY, SWIMMING_ACTIVITY, MOUNTAIN_BIKING_ACTIVITY,
//        WHEELCHAIR_ACTIVITY, ELLIPTICAL_ACTIVITY, OTHER_ACTIVITY}

    public static final int MANUAL_INPUT = 0;
    public static final int GPS_INPUT = 1;
    public static final int AUTOMATIC_INPUT = 2;

    public static final int RUNNING_ACTIVITY = 0;
    public static final int WALKING_ACTIVITY = 1;
    public static final int STANDING_ACTIVITY = 2;
    public static final int CYLING_ACTIVITY = 3;
    public static final int HIKING_ACTIVITY = 4;
    public static final int DOWNHILL_SKIING_ACTIVITY = 5;
    public static final int XC_SKIING_ACTIVITY = 6;
    public static final int SNOWBOARDING_ACTIVITY = 7;
    public static final int SKATING_ACTIVITY = 8;
    public static final int SWIMMING_ACTIVITY = 9;
    public static final int MOUNTAIN_BIKING_ACTIVITY = 10;
    public static final int WHEELCHAIR_ACTIVITY = 11;
    public static final int ELLIPTICAL_ACTIVITY = 12;
    public static final int OTHER_ACTIVITY = 13;

    private Long id;

    private int mInputType;         // Manual, GPS or automatic
    private int mActivityType;   // Running, cycling etc.
    private Calendar mDateTime;             // When does this entry happen
    private int mDuration;                  // Exercise duration in seconds
    private double mDistance;               // Distance traveled. Either in meters or feet.
    private double mAvgPace;                // Average pace
    private double mAvgSpeed;               // Average speed
    private int mCalorie;                   // Calories burnt
    private double mClimb;                  // Climb. Either in meters or feet.
    private int mHeartRate;                 // Heart rate
    private String mComment;                // Comments
    //private ArrayList<LatLng> mLocationList; // Location list

    /* empty constructor */
    public ExerciseEntry(Long id, int inputType, int activityType) {
        this.id = id;
        mInputType = inputType;
        mActivityType = activityType;
    }

//    public void writeToParcel(Parcel out, int flags) {
//
//    }


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

    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public double getmDistance() {
        return mDistance;
    }

    public void setmDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    public double getmAvgPace() {
        return mAvgPace;
    }

    public void setmAvgPace(double mAvgPace) {
        this.mAvgPace = mAvgPace;
    }

    public double getmAvgSpeed() {
        return mAvgSpeed;
    }

    public void setmAvgSpeed(double mAvgSpeed) {
        this.mAvgSpeed = mAvgSpeed;
    }

    public int getmCalorie() {
        return mCalorie;
    }

    public void setmCalorie(int mCalorie) {
        this.mCalorie = mCalorie;
    }

    public double getmClimb() {
        return mClimb;
    }

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
}
