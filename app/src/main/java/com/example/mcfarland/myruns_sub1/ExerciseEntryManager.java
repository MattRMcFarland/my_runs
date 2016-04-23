package com.example.mcfarland.myruns_sub1;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by McFarland on 4/21/16.
 */
public class ExerciseEntryManager {

    private static final String TAG = "EXERCISE ENTRY MANAGER";
    private Context mContext;
    private ExerciseEntry mExerciseEntry;
    private Conversions converter;
    private static Resources res;

    public static final String DATE_FORMAT_STRING = "hh:mm:ss MMM dd yyyy";

    // creates a new exercise entry
    ExerciseEntryManager(Context c, int activity_type, int input_type) {
        mContext = c;
        mExerciseEntry = new ExerciseEntry(activity_type, input_type);
        res = mContext.getResources();
        converter = new Conversions(mContext);
    }

    // loads Exercise Entry from database
    ExerciseEntryManager(Context c, Cursor cursor) {
        mContext = c;
        converter = new Conversions(mContext);
        res = mContext.getResources();

        mExerciseEntry = new ExerciseEntry();

        mExerciseEntry.set_id(cursor.getLong(0));
        mExerciseEntry.setmInputType(cursor.getInt(1));
        mExerciseEntry.setmActivityType(cursor.getInt(2));
        mExerciseEntry.setmDateTime(cursor.getLong(3));      // make sure this works?
        mExerciseEntry.setmDuration(cursor.getInt(4));
        mExerciseEntry.setmDistance(cursor.getFloat(5));
        mExerciseEntry.setmAvgSpeed(cursor.getFloat(6));
        mExerciseEntry.setmAvgPace(cursor.getFloat(7));
        mExerciseEntry.setmCalories(cursor.getInt(8));
        mExerciseEntry.setmClimb(cursor.getFloat(9));
        mExerciseEntry.setmHeartRate(cursor.getInt(10));
        mExerciseEntry.setmComment(cursor.getString(11));

        // TODO: need to get location list
    }

    ExerciseEntryManager(Context c, ExerciseEntry entry) {
        mContext = c;
        mExerciseEntry = entry;
        converter = new Conversions(mContext);
        res = mContext.getResources();
    }

    // save to database and load from database (delete from database?)
    public void addEntryToDatabase() {

        // AsyncTask write to database
        new DataWriter().execute(mContext, mExerciseEntry);
    }

    public ExerciseEntry getEntry() {
        return mExerciseEntry;
    }

    // --------------- getters and setters --------------- //
    // can also do conversions here between raw data and user preferences

    public String getDistanceUnitString() {
        if (converter.isPreferenceMetric())
            return "kilometers";
        else
            return "miles";
    }

    public long getID() {
        return mExerciseEntry.get_id();
    }

    public void setID(Long id) {
        mExerciseEntry.set_id(id);
    }

    public int getInputType() {
        return mExerciseEntry.getmInputType();
    }

    public String getInputTypeString() {
        return res.getStringArray(R.array.input_types)[mExerciseEntry.getmInputType()];
    }

    public void setInputType(int InputType) {
        mExerciseEntry.setmInputType(InputType);
    }

    public int getActivityType() {
        return mExerciseEntry.getmActivityType();
    }

    public String getActivityTypeString() {
        return res.getStringArray(R.array.activity_types)[mExerciseEntry.getmActivityType()];
    }

    public void setActivityType(int ActivityType) {
        mExerciseEntry.setmActivityType(ActivityType);
    }

    public Calendar getDateTime() {
        return mExerciseEntry.getmDateTime();
    }

    public String getDateString(){
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.US);
        return sdf.format(mExerciseEntry.getmDateTime().getTime());
    }

    public void setDateTime(Calendar DateTime) {
        mExerciseEntry.setmDateTime(DateTime);
    }

    public Double getDuration() {

        return mExerciseEntry.getmDuration() / 60.0;
    }

    public String getDurationString() {
        int total_seconds = mExerciseEntry.getmDuration();
        int mins = total_seconds / 60;
        int seconds = total_seconds % 60;
        return String.format("%d mins %d secs", mins, seconds);
    }

    /*
     * expects Duration in minutes -- store as seconds
     */
    public void setDurationFromSeconds(double Duration) {
        mExerciseEntry.setmDuration((int)Duration);
    }

    public void setDurationFromMinutes(double Duration) {
        mExerciseEntry.setmDuration((int)Duration * 60);
    }

    public void setDurationFromMinutes(int Duration) {
        mExerciseEntry.setmDuration(Duration * 60);
    }

    /*
     * should return distance in current preference (miles / kilometers) from meters
     */
    public double getDistance() {

        if (converter.isPreferenceMetric()) {
            return converter.metersToKM(mExerciseEntry.getmDistance());
        } else {
            return converter.metersToMile(mExerciseEntry.getmDistance());
        }
    }

    public String getDistanceString() {
        if (converter.isPreferenceMetric()) {
            return String.format("%.2f kilometers", converter.metersToKM(mExerciseEntry.getmDistance()));
        } else {
            return String.format("%.2f miles", converter.metersToMile(mExerciseEntry.getmDistance()));
        }
    }

    /*
     * sets internal meters distance from current preference distance
     */
    public void setDistance(double Distance) {

        if (converter.isPreferenceMetric()) {
            mExerciseEntry.setmDistance(converter.kmToMeters(Distance));
        } else {
            mExerciseEntry.setmDistance(converter.mileToMeters(Distance));
        }
    }

    public double getAvgPace() {
        return mExerciseEntry.getmDuration();
    }

    public void setAvgPace(double AvgPace) {

        // conversion
        mExerciseEntry.setmAvgPace(AvgPace);
    }

    public double getAvgSpeed() {

        // conversion?
        return mExerciseEntry.getmAvgSpeed();
    }

    public void setAvgSpeed(double AvgSpeed) {

        // conversion?
        mExerciseEntry.setmAvgSpeed(AvgSpeed);
    }

    public int getCalorie() {
        return mExerciseEntry.getmCalories();
    }

    public void setCalorie(int Calories) {
        mExerciseEntry.setmCalories(Calories);
    }

    /*
     * returns conversion distance in meters / feet depending on preference
     */
    public double getClimb() {

        if (converter.isPreferenceMetric()) {
            return mExerciseEntry.getmClimb();
        } else {
            return converter.metersToFeet(mExerciseEntry.getmClimb());
        }

    }

    /*
     * do conversion and save as meters
     */
    public void setClimb(double Climb) {

        // TODO: CONVERSION
        mExerciseEntry.setmClimb(Climb);
    }

    public int getHeartRate() {
        return mExerciseEntry.getmHeartRate();
    }

    public void setHeartRate(int HeartRate) {
        mExerciseEntry.setmHeartRate(HeartRate);
    }

    public String getComment() {
        return mExerciseEntry.getmComment();
    }

    public void setComment(String Comment) {
        mExerciseEntry.setmComment(Comment);
    }
}
