package com.example.mcfarland.myruns_sub1;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by McFarland on 4/23/16.
 */
public class DataWriter extends AsyncTask<Object, Void, Void> {

    private static final String TAG = "ASYNC WRITER";
    Context mContext;
    EEDataSource dataSource;

    /*
     * expects first parameter to be a context argument
     * expects second paramter to be the exercise entry
     */
    @Override
    protected Void doInBackground(Object... params) {
        Log.d(TAG, "writing new entry to database");

        mContext = (Context) params[0];
        dataSource = new EEDataSource(mContext);
        dataSource.createEntry((ExerciseEntry) params[1]);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Toast.makeText(mContext, "Wrote Entry", Toast.LENGTH_SHORT).show();
    }
}
