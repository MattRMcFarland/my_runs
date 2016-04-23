package com.example.mcfarland.myruns_sub1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.util.Calendar;

public class ManualEntryActivity extends ActionBarActivity {

    private static final String TAG = "MANUAL_ENTRY_ACTIVITY";
    private static final String EXERCISE_ENTRY_TAG = "CURRENT_EXERCISE_ENTRY";

    private ListView mEntryFields;
    public ExerciseEntryManager EM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        if (savedInstanceState == null) {
            Log.d(TAG,"making a new exercise entry");
            Intent intent = getIntent();
            EM = new ExerciseEntryManager(this,
                    intent.getIntExtra(getString(R.string.start_frag_input_type),0),
                    intent.getIntExtra(getString(R.string.start_frag_activity_type),0));

        } else {
            Log.d(TAG,"reloading saved exercise entry");
            ExerciseEntry savedEntry = savedInstanceState.getParcelable(EXERCISE_ENTRY_TAG);
            EM = new ExerciseEntryManager(this,savedEntry);
        }

        /* create and initialize ExerciseEntry object -- get activity type from parent intent */
        mEntryFields = (ListView) findViewById(R.id.manual_entry_list);

        OnItemClickListener mEntryFieldHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {

                switch(position) {
                    case 0: // date
                        MyRunsDialogFragment.newInstance(MyRunsDialogFragment.DIALOG_MANUAL_ENTRY_DATE)
                                .show(getFragmentManager(), getString(R.string.dialog_tag_date_picker));
                        break;

                    case 1: // time
                        MyRunsDialogFragment.newInstance(MyRunsDialogFragment.DIALOG_MANUAL_ENTRY_TIME)
                                .show(getFragmentManager(), getString(R.string.dialog_tag_time_picker));
                        break;

                    case 2: // duration
                        MyRunsDialogFragment.newInstance(MyRunsDialogFragment.DIALOG_MANUAL_ENTRY_DURATION)
                                .show(getFragmentManager(), getString(R.string.dialog_tag_duration));
                        break;

                    case 3: // distance
                        MyRunsDialogFragment.newInstance(MyRunsDialogFragment.DIALOG_MANUAL_ENTRY_DISTANCE)
                                .show(getFragmentManager(), getString(R.string.dialog_tag_distance));
                        break;

                    case 4: // Calories
                        MyRunsDialogFragment.newInstance(MyRunsDialogFragment.DIALOG_MANUAL_ENTRY_CALORIES)
                                .show(getFragmentManager(), getString(R.string.dialog_tag_calories));
                        break;

                    case 5: // HR
                        MyRunsDialogFragment.newInstance(MyRunsDialogFragment.DIALOG_MANUAL_ENTRY_HR)
                                .show(getFragmentManager(), getString(R.string.dialog_tag_HR));
                        break;

                    case 6: // comment
                        MyRunsDialogFragment.newInstance(MyRunsDialogFragment.DIALOG_MANUAL_ENTRY_COMMENT)
                                .show(getFragmentManager(), getString(R.string.dialog_tag_comment));
                        break;

                }
            }
        };

        mEntryFields.setOnItemClickListener(mEntryFieldHandler);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(EXERCISE_ENTRY_TAG, EM.getEntry());
    }

    public void onClickSave(View V) {
        Log.d(TAG, "saving entry");


        EM.addEntryToDatabase();


        finish();
    }

    public void onClickCancel(View V) {
        Log.d(TAG, "cancelling entry");
        Toast.makeText(getApplicationContext(), "Entry Discarded",
                Toast.LENGTH_SHORT).show();

        finish();
    }

    // -------------- setting information for mNewEntry -------------- //

    public void setDate(int year, int month, int day) {
        Calendar c = EM.getDateTime();
        c.set(year, month, day);
        EM.setDateTime(c);
        Log.d(TAG,"setting date: " + c.getTime().toString());
    }

    public void setTime(int hour, int minute) {
        Calendar c = EM.getDateTime();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        EM.setDateTime(c);
        Log.d(TAG,"setting time: " + c.getTime().toString());
    }

    public void setDuration(String dialogString) {

        try {
            int duration = Integer.parseInt(dialogString);
            EM.setDurationFromMinutes(duration);
            Log.d(TAG,"setting duration: " + duration);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    public void setDistance(String dialogString) {

        try {
            double distance = Double.parseDouble(dialogString);
            EM.setDistance(distance);
            Log.d(TAG,"setting distance: " + distance);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    public void setCalories(String dialogString) {

        try {
            int calories = Integer.parseInt(dialogString);
            EM.setCalorie(calories);
            Log.d(TAG, "setting calories: " + calories);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    public void setHeartRate(String dialogString) {

        try {
            int HR = Integer.parseInt(dialogString);
            EM.setHeartRate(HR);
            Log.d(TAG, "setting Heart Rate: " + HR);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
        }
    }

    public void setComment(String dialogString) {
        EM.setComment(dialogString);
        Log.d(TAG,"setting comment: " + dialogString);
    }
}


