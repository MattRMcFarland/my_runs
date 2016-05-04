package com.example.mcfarland.myruns_sub1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by McFarland on 4/20/16.
 */
public class EEDataSource {

    // Database fields
    private static SQLiteDatabase database;     // only one instance of database

    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_INPUT_TYPE,
            MySQLiteHelper.COLUMN_ACTIVITY_TYPE,
            MySQLiteHelper.COLUMN_DATE_TIME,
            MySQLiteHelper.COLUMN_DURATION,
            MySQLiteHelper.COLUMN_DISTANCE,
            MySQLiteHelper.COLUMN_AVG_PACE,
            MySQLiteHelper.COLUMN_AVG_SPEED,
            MySQLiteHelper.COLUMN_CALORIES,
            MySQLiteHelper.COLUMN_CLIMB,
            MySQLiteHelper.COLUMN_HEART_RATE,
            MySQLiteHelper.COLUMN_COMMENT,
            MySQLiteHelper.COLUMN_GPS_DATA };

    private static final String TAG = "EE DATA SOURCE";

    static Conversions converter;

    public EEDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        converter = new Conversions(context);
    }

    private void open() throws SQLException {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }

    private void close() {
        if (database.isOpen()) {
            dbHelper.close();
        }
    }

    public Long createEntry(ExerciseEntry entry) {
        ContentValues values = new ContentValues();

//        public static final String COLUMN_GPS_DATA = "gps_data";

        // put a exercise entry items into each field
        // use raw data.
        values.put(MySQLiteHelper.COLUMN_INPUT_TYPE, entry.getmInputType());
        values.put(MySQLiteHelper.COLUMN_ACTIVITY_TYPE, entry.getmActivityType());
        values.put(MySQLiteHelper.COLUMN_DATE_TIME, entry.getmDateTime().getTime().getTime()); // long
        values.put(MySQLiteHelper.COLUMN_DURATION, entry.getmDuration());
        values.put(MySQLiteHelper.COLUMN_DISTANCE, entry.getmDistance());
        values.put(MySQLiteHelper.COLUMN_AVG_PACE, entry.getmAvgPace());
        values.put(MySQLiteHelper.COLUMN_AVG_SPEED, entry.getmAvgSpeed());
        values.put(MySQLiteHelper.COLUMN_CALORIES, entry.getmCalories());
        values.put(MySQLiteHelper.COLUMN_CLIMB, entry.getmClimb());
        values.put(MySQLiteHelper.COLUMN_HEART_RATE, entry.getmHeartRate());
        values.put(MySQLiteHelper.COLUMN_COMMENT, entry.getmComment());

        if (entry.getmInputType() != ExerciseEntry.MANUAL_INPUT) {
            byte blob[] = converter.convertFromListToByteArray(entry.getLocationList());
            values.put(MySQLiteHelper.COLUMN_GPS_DATA, blob);
        }

        Long insertId = (long) -1;
        try {
            open();
            insertId = database.insert(MySQLiteHelper.EXERCISE_ENTRY_TABLE, null, values);
        } catch (SQLException e) {
            Log.d(TAG, "ERROR: failed to save entry into database");
        }

        return insertId;
    }

    public void deleteEntry(ExerciseEntry entry) {
        long id = entry.get_id();
        Log.d(TAG, "delete entry with id = " + id);

        try {
            open();
            database.delete(MySQLiteHelper.EXERCISE_ENTRY_TABLE, MySQLiteHelper.COLUMN_ID
                    + " = " + id, null);
            //close();
        } catch (SQLException e) {
            Log.d(TAG, "ERROR: failed to delete entry");
        }

    }

    public void deleteEntryWithID(long id) {
        Log.d(TAG, "deleting entry with id: " + id);
        try {
            open();
            database.delete(MySQLiteHelper.EXERCISE_ENTRY_TABLE, MySQLiteHelper.COLUMN_ID
                    + " = " + id, null);
            //close();
        } catch (SQLException e) {
            Log.d(TAG, "ERROR: failed to delete entry");
        }
    }


    public ArrayList<ExerciseEntry> getAllEntries() {
        Log.d(TAG,"Retrieving all entries");
        ArrayList<ExerciseEntry> entries = new ArrayList<ExerciseEntry>();

        try {
            open();
            Cursor cursor = database.query(MySQLiteHelper.EXERCISE_ENTRY_TABLE,
                    allColumns, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ExerciseEntry entry = cursorToEntry(cursor);
                Log.d(TAG, "\tgot entry = " + entry.toString());
                entries.add(entry);
                cursor.moveToNext();
            }
            // Make sure to close the cursor
            //cursor.close();
        } catch (SQLException e) {
            Log.d(TAG,"Error: couldn't open database");
        }

        return entries;
    }

    public ExerciseEntry getEntry(long rowID) {
        ExerciseEntry found = null;

        try {
            open();
            Cursor cursor = database.query(MySQLiteHelper.EXERCISE_ENTRY_TABLE,
                    allColumns, MySQLiteHelper.COLUMN_ID + " = " + rowID, null, null, null, null);
            //close();

            found = cursorToEntry(cursor);
        } catch (SQLException e) {
            Log.d(TAG, "ERROR: failed to retrieve entry with id " + rowID);
        }

        return found;
    }

    public void deleteAllEntries() {

        try {
            open();
            database.delete(MySQLiteHelper.EXERCISE_ENTRY_TABLE, null, null);
            //close();
        } catch (SQLException e) {
            Log.d(TAG, "Error deleting table");
        }
    }

    private ExerciseEntry cursorToEntry(Cursor cursor) {
        if (cursor.isBeforeFirst() || cursor.isAfterLast())
            cursor.moveToFirst();

        ExerciseEntry entry = new ExerciseEntry();

        entry.set_id(cursor.getLong(0));
        entry.setmInputType(cursor.getInt(1));
        entry.setmActivityType(cursor.getInt(2));
        entry.setmDateTime(cursor.getLong(3));      // make sure this works?
        entry.setmDuration(cursor.getInt(4));
        entry.setmDistance(cursor.getFloat(5));
        entry.setmAvgSpeed(cursor.getFloat(6));
        entry.setmAvgPace(cursor.getFloat(7));
        entry.setmCalories(cursor.getInt(8));
        entry.setmClimb(cursor.getFloat(9));
        entry.setmHeartRate(cursor.getInt(10));
        entry.setmComment(cursor.getString(11));

        if (entry.getmInputType() != ExerciseEntry.MANUAL_INPUT) {
            entry.setLocationList(converter.convertFromByteArrayToList(cursor.getBlob(12)));
        }

        return entry;
    }
}
