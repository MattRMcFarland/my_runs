package com.example.mcfarland.myruns_sub1;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by McFarland on 4/21/16.
 */
public class ExerciseEntryManager {

    private static final String TAG = "EXERCISE ENTRY MANAGER";
    private Context mContext;
    private ExerciseEntry mExerciseEntry;
    private static Conversions converter;
    private static Resources res;
    private static EEDataSource dataSource;

    public static final String DATE_FORMAT_STRING = "hh:mm:ss MMM dd yyyy";

    /* Auxillary Entry Data */
    private boolean hasFirstLocation = false;
    private long startTime = 0;
    private double curSpeed = 0;
    private double lastHeight = 0;
    private ArrayList<Double> paceList = new ArrayList<>();

    // empty constructor
    ExerciseEntryManager(Context c) {
        mContext = c;
        dataSource = new EEDataSource(mContext);
        res = mContext.getResources();
        converter = new Conversions(mContext);
    }

    // creates a new exercise entry
    ExerciseEntryManager(Context c, int activity_type, int input_type) {
        mContext = c;
        dataSource = new EEDataSource(mContext);
        res = mContext.getResources();
        converter = new Conversions(mContext);

        // instance exercise entry
        mExerciseEntry = new ExerciseEntry(input_type, activity_type);
    }

    // loads Exercise Entry from database
    ExerciseEntryManager(Context c, Cursor cursor) {
        mContext = c;
        dataSource = new EEDataSource(mContext);
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
        mExerciseEntry.setLocationList(converter.convertFromByteArrayToList(cursor.getBlob(12)));
    }

    // load entry into manager (for loaders)
    ExerciseEntryManager(Context c, ExerciseEntry entry) {
        mContext = c;
        dataSource = new EEDataSource(mContext);
        mExerciseEntry = entry;
        converter = new Conversions(mContext);
        res = mContext.getResources();
    }

    /*
     * Queries the database and loads this exercise entry
     *
     * returns true if entry is found
     * returns false if not found
     */
    public boolean loadEntry(long id) {
        mExerciseEntry = dataSource.getEntry(id);

        return mExerciseEntry != null;
    }

    // asynchronously add entry to database
    public void addEntryToDatabase() {
        Log.d(TAG,"Saving entry to database");

        // AsyncTask write to database
        new DataWriter().execute(mContext, mExerciseEntry);
    }

    public void deleteEntryFromDatabase() {
        if (mExerciseEntry != null) {
            Log.d(TAG, "Deleting entry from database");
            new DataDeleter().execute(mContext, mExerciseEntry.get_id());
        }
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
        mExerciseEntry.setmDuration((int) Duration);
    }

    public void setDurationFromMinutes(double Duration) {
        mExerciseEntry.setmDuration((int) Duration * 60);
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
            return String.format("%.2f Kilometers", converter.metersToKM(mExerciseEntry.getmDistance()));
        } else {
            return String.format("%.2f Miles", converter.metersToMile(mExerciseEntry.getmDistance()));
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

    public String getCurrentPaceString() {
        StringBuilder sb = new StringBuilder(100);
        if (converter.isPreferenceMetric()) {
            sb.append(String.format("%.2f", converter.metersPerSecondToKMperHour(curSpeed)));
            sb.append(" km/h");

        } else {
            sb.append(String.format("%.2f", converter.metersPerSecondToMilesPerHour(curSpeed)));
            sb.append(" mi/h");
        }
        return sb.toString();
    }

    public double getAvgSpeed() {

        // always meters / second
        return mExerciseEntry.getmAvgSpeed();
    }

    public void setAvgSpeed(double AvgSpeed) {

        // alwasy meters / second
        mExerciseEntry.setmAvgSpeed(AvgSpeed);
    }

    public String getAvgSpeedString() {
        StringBuilder sb = new StringBuilder(100);
        if (converter.isPreferenceMetric()) {
            sb.append(String.format("%.2f", converter.metersPerSecondToKMperHour(mExerciseEntry.getmAvgSpeed())));
            sb.append(" km/h");

        } else {
            sb.append(String.format("%.2f", converter.metersPerSecondToMilesPerHour(mExerciseEntry.getmAvgSpeed())));
            sb.append(" mi/h");
        }
        return sb.toString();
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

        mExerciseEntry.setmClimb(Climb);
    }

    public String getClimbString() {
        StringBuilder sb = new StringBuilder(100);
        if (converter.isPreferenceMetric()) {
            sb.append(String.format("%.2f", converter.metersToKM(mExerciseEntry.getmClimb())));
            sb.append(" Kilometers");

        } else {
            sb.append(String.format("%.2f", converter.metersToMile(mExerciseEntry.getmClimb())));
            sb.append(" Miles");
        }
        return sb.toString();
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

    public ArrayList<LatLng> getLocationList() {
        return mExerciseEntry.getLocationList();
    }

    public LatLng getFirstPos() {
        if (mExerciseEntry.getLocationList().size() > 0)
            return mExerciseEntry.getLocationList().get(0);
        else
            return null;
    }

    public LatLng getLastPos() {
        int size = mExerciseEntry.getLocationList().size();
        if (size > 0)
            return mExerciseEntry.getLocationList().get(size - 1);
        else
            return null;
    }

    public boolean hasFirstLocation() {
        return hasFirstLocation;
    }

    public void setHasFirstLocation(boolean hasFirstLocation) {
        this.hasFirstLocation = hasFirstLocation;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public double getCurSpeed() {
        return curSpeed;
    }

    public void setCurSpeed(double curSpeed) {
        this.curSpeed = curSpeed;
    }

    public double getLastHeight() {
        return lastHeight;
    }

    public void setLastHeight(double lastHeight) {
        this.lastHeight = lastHeight;
    }

    public void logPrintLocationList() {
        Log.d(TAG, "List has # entries: " + mExerciseEntry.getLocationList().size());

        for (LatLng pos : mExerciseEntry.getLocationList()){
            Log.d(TAG, "\tLocation: " + pos.toString());
        }
    }

    /*
     * updates distance, climb, average speed, and pace information for current entry
     */
    public void updateLocation(Location newLocation) {
        LatLng newPos = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());

        curSpeed = newLocation.getSpeed();
        paceList.add(curSpeed);

        if (hasFirstLocation) {

            // calculate distance travelled and add that to current distance
            LatLng oldPos = mExerciseEntry.getLastPos();
            float [] results = new float[3];
            Location.distanceBetween(
                    oldPos.latitude, oldPos.longitude,
                    newPos.latitude, newPos.longitude, results);
            mExerciseEntry.setmDistance(results[0] + mExerciseEntry.getmDistance());

            // calculate climb distance if user climbed
            if (newLocation.hasAltitude()) {
                if (lastHeight < newLocation.getAltitude()) {
                    mExerciseEntry.setmClimb(newLocation.getAltitude() - lastHeight);
                }
                lastHeight = newLocation.getAltitude();
            }

            // update average speed (meters / second)
            mExerciseEntry.setmAvgSpeed(mExerciseEntry.getmDistance() /
                    ((Calendar.getInstance().getTimeInMillis() - startTime) / 1000) );

            // update calories
            mExerciseEntry.setmCalories((int) mExerciseEntry.getmDistance() / 15);

        } else {
            hasFirstLocation = true;

            // set height
            lastHeight = newLocation.getAltitude();
        }

        mExerciseEntry.addLocation(newPos);
    }

    /*
     * when exercise is complete, this calculates several end of exercise metrics
     */
    public void setEndData(long endTime) {
        Log.d(TAG,"Setting end of exercise data");

        // total duration
        mExerciseEntry.setmDuration( (int) (endTime - startTime) / 1000 );

        // avg speed
        mExerciseEntry.setmAvgSpeed(mExerciseEntry.getmDistance() /
                ((endTime - startTime) / 1000) );

        // avg pace
        if (paceList.size() > 0) {
            Double sum = 0.0;
            for (Double pace : paceList) {
                sum += pace;
            }
            mExerciseEntry.setmAvgPace(sum / paceList.size());
        }

        // calories
        mExerciseEntry.setmCalories((int) mExerciseEntry.getmDistance() / 15);

    }


}
