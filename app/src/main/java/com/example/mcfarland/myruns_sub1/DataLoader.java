package com.example.mcfarland.myruns_sub1;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by McFarland on 4/21/16.
 */
public class DataLoader extends AsyncTaskLoader<ArrayList<ExerciseEntry>> {
//Your code here.
    EEDataSource mHelper;

    private final static String TAG = "DATA LOADER";

    public DataLoader(Context context) {
        super(context);
        Log.d(TAG, "Creating Data Loader");

        mHelper = new EEDataSource(context);

    }

    @Override

    protected void onStartLoading() {
        Log.d(TAG, "Starting Load");
        forceLoad(); //Force an asynchronous load.
    }
    @Override
    public ArrayList<ExerciseEntry> loadInBackground() {
        Log.d(TAG, "Loading in Background");

        return mHelper.getAllEntries();
    }
}//end class
