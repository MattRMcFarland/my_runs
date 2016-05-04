package com.example.mcfarland.myruns_sub1;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryFragment extends ListFragment implements LoaderManager.LoaderCallbacks<ArrayList<ExerciseEntry>> {

    private static final String TAG = "HISTORY FRAGMENT";
    private Context mContext;
    private MyListAdapter mAdapter;


    public void onCreate(Bundle bundle) {
        Log.d(TAG,"creating history fragment");
        super.onCreate(bundle);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"creating history fragment view");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onResume() {
        Log.d(TAG,"resuming fragment");
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, "clicked position " + position);

        ExerciseEntry raw_entry = (ExerciseEntry) l.getItemAtPosition(position);
        ExerciseEntryManager EM = new ExerciseEntryManager(mContext,raw_entry);

        switch (EM.getInputType()) {
            case ExerciseEntry.MANUAL_INPUT:
                startManualDisplay(EM);
                break;

            case ExerciseEntry.GPS_INPUT:
            case ExerciseEntry.AUTOMATIC_INPUT:
                startMapDisplay(EM.getID());
                break;
        }
    }


    private void startManualDisplay(ExerciseEntryManager EM) {

        Log.d(TAG, "Starting manual entry display of exercise entry: " + EM.getID());
        Intent i = new Intent(mContext, ExerciseDisplayActivity.class);

        // store relevant display data
        i.putExtra(ExerciseDisplayActivity.ID_KEY, EM.getID());                     // need ID for deletion
        i.putExtra(ExerciseDisplayActivity.INPUT_KEY, EM.getInputTypeString());
        i.putExtra(ExerciseDisplayActivity.ACTIVITY_KEY, EM.getActivityTypeString());
        i.putExtra(ExerciseDisplayActivity.DATE_KEY, EM.getDateString());
        i.putExtra(ExerciseDisplayActivity.DURATION_KEY, EM.getDurationString());
        i.putExtra(ExerciseDisplayActivity.DISTANCE_KEY, EM.getDistanceString());
        i.putExtra(ExerciseDisplayActivity.CALORIES_KEY, EM.getCalorie());
        i.putExtra(ExerciseDisplayActivity.HEART_RATE_KEY, EM.getHeartRate());
        i.putExtra(ExerciseDisplayActivity.COMMENT_KEY, EM.getComment());

        // start display
        startActivity(i);
    }

    private void startMapDisplay(long exerciseID) {

        Log.d(TAG, "Starting map display of exercise entry: " + exerciseID);
        Intent i = new Intent(mContext, MapDisplayActivity.class);

        i.putExtra(MapDisplayActivity.MAP_DISPLAY_ID_KEY, exerciseID);

        startActivity(i);
    }

    @Override
    public Loader<ArrayList<ExerciseEntry>> onCreateLoader(int i, Bundle bundle) {
        Log.d(TAG, "creating new loader");
        return new DataLoader(mContext); // DataLoader is your AsyncTaskLoader.
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ExerciseEntry>> loader, ArrayList<ExerciseEntry> exerciseEntries) {
        Log.d(TAG, "Load finished");
        //Put your code here.
        mAdapter = new MyListAdapter(mContext, exerciseEntries);
        setListAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ExerciseEntry>> loader) {
        Log.d(TAG, "resetting loader");
    }

    public void reloadDatabase() {
        if (isAdded())
            getLoaderManager().restartLoader(0, null, this);
    }

    public class MyListAdapter extends ArrayAdapter<ExerciseEntry> {

        private final static String TAG = "MyListAdapter";
        Context mContext;

        public MyListAdapter(Context c, ArrayList<ExerciseEntry> entries) {
            super(c, 0, entries);
            mContext = c;
            Log.d(TAG, "creating list adapter");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ExerciseEntryManager EM = new ExerciseEntryManager(mContext, getItem(position));

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_entry, parent, false);

            // Find fields to populate in inflated template
            TextView tvFirstLine = (TextView) convertView.findViewById(R.id.history_entry_firstline);
            TextView tvSecondLine = (TextView) convertView.findViewById(R.id.history_entry_secondline);

            StringBuilder firstLine = new StringBuilder("");
            firstLine.append(EM.getInputTypeString() + ": ");
            firstLine.append(EM.getActivityTypeString() + ", ");
            firstLine.append(EM.getDateString());
            tvFirstLine.setText(firstLine);

            StringBuilder secondLine = new StringBuilder("");
            secondLine.append(EM.getDistanceString() + " ");
            secondLine.append(EM.getDurationString());
            tvSecondLine.setText(secondLine);

            return convertView;
        }

    }

}
