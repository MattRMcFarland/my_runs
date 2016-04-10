package com.example.mcfarland.myruns_sub1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class StartFragment extends Fragment {

    private final static String TAG = "START FRAGMENT";

    /* ----- UI buttons ------ */
    Spinner mInputSpinner;
    Spinner mActivitySpinner;
    private Button mButtonStart;
    private Button mButtonSync;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "creating view");

        /* get view */
        View mStartView = inflater.inflate(R.layout.start_frag_layout, container, false);

        /* set input spinner */
        mInputSpinner = (Spinner) mStartView.findViewById(R.id.start_frag_input_spinner);
        ArrayAdapter<CharSequence> inputAdapter =
                ArrayAdapter.createFromResource(mStartView.getContext(),
                        R.array.input_types,
                        android.R.layout.simple_spinner_item);
        inputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mInputSpinner.setAdapter(inputAdapter);

        /* set activity spinner */
        mActivitySpinner = (Spinner) mStartView.findViewById(R.id.start_frag_activity_spinner);
        ArrayAdapter<CharSequence> activityAdapter =
                ArrayAdapter.createFromResource(mStartView.getContext(),
                        R.array.activity_types,
                        android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mActivitySpinner.setAdapter(activityAdapter);

        /* set start button listen */
        mButtonStart = (Button) mStartView.findViewById(R.id.start_frag_start_button);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickStart(v);
            }
        });

        /* set sync button listener */
        mButtonSync = (Button) mStartView.findViewById(R.id.start_frag_sync_button);
        mButtonSync.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onClickSync(v);
            }
        });

        // Inflate the layout for this fragment
        return mStartView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "creating fragment");
    }

    public void onClickStart(View V) {
        Log.d(TAG, "clicked Start");

        Intent i;

        switch(mInputSpinner.getSelectedItemPosition()) {
            case 0:
                // if manual entry -> start manual entry activity
                Log.d(TAG, "starting manual entry");
                i = new Intent(this.getActivity(), ManualEntryActivity.class);
                i.putExtra(getString(R.string.start_frag_input_type),
                        mInputSpinner.getSelectedItemPosition());
                i.putExtra(getString(R.string.start_frag_activity_type),
                        mActivitySpinner.getSelectedItemPosition());

                startActivity(i);
                break;

            case 1:
                // if in GPS mode -> start MapDisplayActivity
                Log.d(TAG, "starting GPS entry activity");
                break;

            case 2:
                // if in Automatic mode -> start MapDisplayActivity
                Log.d(TAG, "starting Automatic entry activity");
                break;
        }

    }

    public void onClickSync(View V) {
        Log.d(TAG, "clicked Sync");
    }


}
