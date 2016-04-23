package com.example.mcfarland.myruns_sub1;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by McFarland on 4/23/16.
 */
public class DataDeleter extends AsyncTask<Object, Void, Void> {

    Context mContext;

    /*
     * Expects a context argument and then a Long id argument
     */
    @Override
    protected Void doInBackground(Object... params) {
        mContext = (Context) params[0];
        EEDataSource dataSource = new EEDataSource(mContext);
        dataSource.deleteEntryWithID((long) params[1]);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Toast.makeText(mContext, "Deleted Entry", Toast.LENGTH_SHORT).show();
    }
}
