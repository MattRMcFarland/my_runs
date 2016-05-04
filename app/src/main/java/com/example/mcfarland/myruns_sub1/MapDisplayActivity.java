package com.example.mcfarland.myruns_sub1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapDisplayActivity extends ActionBarActivity {

    private static final String TAG = "MAP DISPLAY ACTIVITY";

    private static final String TRACKING_KEY = "istracking_key";
    private static final String RUN_ID_KEY = "run_id_key";

    public static final String MAP_DISPLAY_ID_KEY = "run_id";

    private GoogleMap mMap;
    private static final int ZOOM_LEVEL = 15;
    private ExerciseEntryManager EM;
    private long ID = -1;

    private TextView mInputTypeText;
    private TextView mAverageSpeedText;
    private TextView mCurrentSpeedText;
    private TextView mClimbText;
    private TextView mCalorieText;
    private TextView mDistanceText;

    private Button saveButton;
    private Button cancelButton;

    private LocationTrackerService mService;
    private boolean bound = false;
    private boolean tracking = false;

    public ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(getClass().getName(), "onServiceConnected");
            // get the service reference
            LocationTrackerService.LocationBinder binder = (LocationTrackerService.LocationBinder) service;
            mService = binder.getService();
            mService.startTracking();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(getClass().getName(), "onServiceDisconnected");
            bound = false;
        }

    };

    private IntentFilter locationFilter = new IntentFilter(LocationTrackerService.LOCATION_BROADCAST);
    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Updating UI");
            EM = mService.getEMinstance();
            updateUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);
        Intent i = getIntent();

        Log.d(TAG, "Creating Activity");

        mInputTypeText = (TextView) findViewById(R.id.MD_input_type);
        mAverageSpeedText = (TextView) findViewById(R.id.MD_avg_speed);
        mCurrentSpeedText = (TextView) findViewById(R.id.MD_cur_speed);
        mClimbText = (TextView) findViewById(R.id.MD_climb);
        mCalorieText = (TextView) findViewById(R.id.MD_calorie);
        mDistanceText = (TextView) findViewById(R.id.MD_distance);

        saveButton = (Button)findViewById(R.id.map_display_save_button);
        cancelButton = (Button)findViewById(R.id.map_display_cancel_button);

        // load old id and and tracking state
        if (savedInstanceState != null) {
            ID = savedInstanceState.getLong(RUN_ID_KEY, -1);
            tracking = savedInstanceState.getBoolean(TRACKING_KEY, false);
            Log.d(TAG,"Reestablishing previous exercise view (ID " + ID + ", tracking " + tracking + ")");
        }

        // not reloading an activity instance -- check if starting a new history activity
        if (ID < 0) {
            ID = i.getLongExtra(MAP_DISPLAY_ID_KEY, -1);
        }

        // need to discern if displaying run or making a new one
        if (ID >= 0 && !tracking) {

            Log.d(TAG, "Loading entry with id: " + ID);
            EM = new ExerciseEntryManager(this);
            if (!EM.loadEntry(ID)) {
                Log.d(TAG,"couldn't load entry with id: " + ID);
                Toast.makeText(this,"Failed to load entry", Toast.LENGTH_SHORT).show();
                finish();
            }
            EM.logPrintLocationList();

            updateUI();

        } else {

            tracking = true;

            int input_type = i.getIntExtra(getString(R.string.start_frag_input_type), -1);
            int activity_type = i.getIntExtra(getString(R.string.start_frag_activity_type), -1);
            Log.d(TAG, "Starting tracking service with Input " + input_type + " Activity " + activity_type);

            /* start new service that will create an instance of the new exercise entry */
            Intent service = new Intent(this, LocationTrackerService.class);

            service.putExtra(getString(R.string.start_frag_activity_type), activity_type);
            service.putExtra(getString(R.string.start_frag_input_type), input_type);
            startService(service);

        }

        // modify UI for history / new tracking activity
        if (tracking) {
            setOnClickListeners();
        } else {
            removeButtons();
        }

        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!tracking) {
            getMenuInflater().inflate(R.menu.exercise_activity_display_menu, menu);
        }

        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AD_menu_delete:
                Toast.makeText(this, "Deleted Entry", Toast.LENGTH_SHORT).show();
                EM.deleteEntryFromDatabase();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

     /* -------------------------------- LIFECYCLE FUNCTIONS ------------------------------ */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "Saving instance state");
        outState.putLong(RUN_ID_KEY, ID);
        outState.putBoolean(TRACKING_KEY, tracking);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Bind to LocalService
        if (tracking) {
            Log.d(TAG, "Binding to service and Registering receiver");
            Intent intent = new Intent(this, LocationTrackerService.class);
            registerReceiver(locationReceiver, locationFilter);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onPause() {

        // Unbind from the service
        if (bound) {
            Log.d(TAG, "Unbinding to service and Unregistering receiver");
            unbindService(mConnection);
            unregisterReceiver(locationReceiver);
            bound = false;
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {

        if (bound) {
            Log.d(TAG, "Destroying activity");
            unbindService(mConnection);
            unregisterReceiver(locationReceiver);
            bound = false;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        if (tracking) {
            mService.stopTracking();
            tracking = false;
        }

        if (bound) {
            Log.d(TAG, "Stopping location tracking service");
            stopService(new Intent(this, LocationTrackerService.class));
            unbindService(mConnection);
            unregisterReceiver(locationReceiver);
            bound = false;
        }

        super.onBackPressed();
    }

    /* -------------------------------- UI FUNCTIONS ------------------------------ */

    private void removeButtons() {
        Log.d(TAG, "Removing buttons from view");
        LinearLayout ll = (LinearLayout) saveButton.getParent();

        ll.removeView(saveButton);
        ll.removeView(cancelButton);
    }

    private void setOnClickListeners() {
        Log.d(TAG, "Adding button listeners");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveClicked(view);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelClicked(view);
            }
        });
    }

    private void updateUI() {
        if (mMap == null)
            setUpMapIfNeeded();

        mMap.clear();

        if (tracking)
            EM = mService.getInstance().getEMinstance();

        // set text
        updateText();

        if (EM.getLocationList().size() > 0) {
            mMap.addMarker(new MarkerOptions().position(EM.getFirstPos())
                    .title("Start")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            if (!tracking) {
                mMap.addMarker(new MarkerOptions().position(EM.getLastPos())
                        .title("End")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            } else {
                mMap.addMarker(new MarkerOptions().position(EM.getLastPos())
                        .title("Current")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }


            // draw polyline
            mMap.addPolyline(new PolylineOptions()
                    .addAll(EM.getLocationList())
                    .width(5)
                    .color(Color.RED));

            // recenter map if the current location isn't displayed
            if (!mMap.getProjection().getVisibleRegion().latLngBounds.contains(EM.getLastPos())) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(EM.getLastPos(),ZOOM_LEVEL));
            }

        } else {
            Toast.makeText(this, "No Location data", Toast.LENGTH_LONG).show();
        }
    }

    private void updateText() {
        mInputTypeText.setText("Type: " + EM.getActivityTypeString());
        mAverageSpeedText.setText("Avg Speed: " + EM.getAvgSpeedString());

        if (tracking) {
            mCurrentSpeedText.setText("Cur Speed: " + EM.getCurrentPaceString());
        } else {
            mCurrentSpeedText.setText("Cur Speed: n/a");
        }

        mCalorieText.setText("Calories: " + EM.getCalorie());
        mClimbText.setText("Climb: " + EM.getClimbString());
        mDistanceText.setText("Distance: " + EM.getDistanceString());
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            mMap.moveCamera(CameraUpdateFactory.zoomTo(ZOOM_LEVEL));
        }
    }

    public void onSaveClicked(View v) {
        Log.d(TAG,"Clicked Save");

        mService.stopTracking();
        mService.getEMinstance().addEntryToDatabase();
        Toast.makeText(this, "Saved Map Entry", Toast.LENGTH_SHORT).show();

        if (bound) {
            Log.d(TAG, "Stopping location tracking service");
            endService();
        }

        finish();
    }

    public void onCancelClicked(View v){
        Log.d(TAG,"Clicked Cancel");

        if (tracking) {
            mService.stopTracking();
            tracking = false;
        }

        if (bound) {
            Log.d(TAG, "Stopping location tracking service");
            endService();
        }

        finish();
    }

    private void endService() {
        if (bound) {
            bound = false;
            unbindService(mConnection);
            unregisterReceiver(locationReceiver);
            stopService(new Intent(this, LocationTrackerService.class));
        }
    }

}
