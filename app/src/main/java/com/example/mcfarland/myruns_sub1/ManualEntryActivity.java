package com.example.mcfarland.myruns_sub1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class ManualEntryActivity extends Activity {

    private static final String TAG = "MANUAL_ENTRY_ACTIVITY";
    private static final String EXERCISE_ENTRY_TAG = "CURRENT_EXERCISE_ENTRY";

    private ListView mEntryFields;
    public ExerciseEntry mNewEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);

        if (savedInstanceState == null) {
            Log.d(TAG,"making a new exercise entry");
            Intent intent = getIntent();
            mNewEntry = new ExerciseEntry((long) 0,
                    intent.getIntExtra(getString(R.string.start_frag_input_type),0),
                    intent.getIntExtra(getString(R.string.start_frag_activity_type),0));

        }

        //mNewEntry = new ExerciseEntry( (long)0, 0, 0);
        // TODO: need to actually save state saving with Parcelable
//        if (savedInstanceState == null) {
//            Log.d(TAG, "making new exercise instance");
//            mNewEntry = new ExerciseEntry( (long)0, 0, 0);
//        } else {
//            Log.d(TAG, "getting saved exercise state");
//            mNewEntry = savedInstanceState.getParcelable(EXERCISE_ENTRY_TAG);
//        }

        /* create and initialize ExerciseEntry object -- get activity type from parent intent */
        mEntryFields = (ListView) findViewById(R.id.manual_entry_list);

        OnItemClickListener mEntryFieldHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {

//                Toast.makeText(getApplicationContext(), ((TextView) v).getText() + " position: " + position,
//                        Toast.LENGTH_SHORT).show();

                switch(position) {
                    case 0: // date
                        MyRunsDialogFragment.newInstance(MyRunsDialogFragment.DIALOG_MANUAL_ENTRY_DATE)
                                .show(getFragmentManager(), getString(R.string.dialog_tag_date_picker));
                        break;

                    case 1: // time
                        MyRunsDialogFragment.newInstance(MyRunsDialogFragment.DIALOG_MANUAL_ENTRY_TIME)
                                .show(getFragmentManager(), getString(R.string.dialog_tag_time_picker));
                        // make time dialog
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable(EXERCISE_ENTRY_TAG, mNewEntry);
    }

    public ExerciseEntry getNewEntry() {
        return mNewEntry;
    }

    public void onClickSave(View V) {
        Log.d(TAG, "saving entry");
        Toast.makeText(getApplicationContext(), "Saved Entry",
                Toast.LENGTH_SHORT).show();

        finish();
    }

    public void onClickCancel(View V) {
        Log.d(TAG, "cancelling entry");
        Toast.makeText(getApplicationContext(), "Entry Discarded",
                Toast.LENGTH_SHORT).show();

        finish();
    }

}


