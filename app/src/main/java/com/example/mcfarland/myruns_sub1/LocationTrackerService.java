package com.example.mcfarland.myruns_sub1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by McFarland on 4/28/16.
 */
public class LocationTrackerService extends Service {

    private static final String TAG = "LOCATION TRACKER";
    public static final String LOCATION_BROADCAST = "com.example.mcfarland.myruns_sub1.LOCATION_BROADCAST";

    private static final int REQUEST_TIME = 3000;  // 3 seconds minimum
    private static final int REQUEST_DIST = 0;     // get location even if user hasn't moved

    private static LocationTrackerService tInstance;
    private ExerciseEntryManager EM;
    private boolean tracking = false;
    LocationManager mLocationManager;
    MyLocationListener myLocationListener;

    private NotificationManager mNM;
    private static final int NOTIFICATION_ID = 1;

    private final IBinder mBinder = new LocationBinder();

    @Override
    public void onCreate() {
        super.onCreate();

        tInstance = this;

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        myLocationListener = new MyLocationListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting Tracking Service");

        if (EM == null) {
            initEntry(intent);
        }
        myLocationListener.setEM(EM);
        tracking = true;

        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "returning binder");
        return mBinder;
    }

    public class LocationBinder extends Binder {
        LocationTrackerService getService() {
            // Return this instance of DownloadBinder so client can call public methods
            return tInstance;
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroying service");
        // Make sure our notification is gone.
        mNM.cancel(NOTIFICATION_ID);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        Log.d(TAG, "User Removed Task");
        stopTracking();
        stopSelf();
    }

    private void initEntry(Intent i) {
        if (i == null)
            return;

        Log.d(TAG,"Initializing entry with Input type: " +
                i.getIntExtra(getString(R.string.start_frag_input_type), -1) +
            " Activity type: " + i.getIntExtra(getString(R.string.start_frag_activity_type), -1) );

        EM = new ExerciseEntryManager(this,
                i.getIntExtra(getString(R.string.start_frag_activity_type), -1),
                i.getIntExtra(getString(R.string.start_frag_input_type), -1) );

        EM.setStartTime(Calendar.getInstance().getTimeInMillis());
    }

    public void startTracking() {
        Log.d(TAG, "Starting to track exercise");
        showNotification();
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, REQUEST_TIME, REQUEST_DIST,
                myLocationListener);
    }

    public void stopTracking() {
        Log.d(TAG,"Stopping exercise tracking");
        mNM.cancel(NOTIFICATION_ID);
        EM.setEndData(Calendar.getInstance().getTimeInMillis());
        stopLocationUpdates();
        tracking = false;
    }

    public void stopLocationUpdates() {
        mLocationManager.removeUpdates(myLocationListener);
    }

    // returns singleton reference
    public LocationTrackerService getInstance() {
        return tInstance;
    }

    public ExerciseEntryManager getEMinstance() {
        return EM;
    }

    /**
     * Display a notification in the notification bar.
     * from: MyService.java in binderdemo
     */
    private void showNotification() {

        // reload old activity instead of starting a new one
        Intent notifyIntent = new Intent(this, MapDisplayActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(this.getString(R.string.notification_title))
                .setContentText(
                        getResources().getString(R.string.notification_content))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent).build();
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags = notification.flags
                | Notification.FLAG_ONGOING_EVENT;

        mNM.notify(NOTIFICATION_ID, notification);

    }

    /* -------------------------------- LOCATION FUNCTIONS -------------------------------- */

    public class MyLocationListener implements LocationListener {

        private ExerciseEntryManager listenerEM;

        public void setEM(ExerciseEntryManager manager) {
            listenerEM = manager;
        }

        private void newLocationBroadcast() {
            Intent intent = new Intent();
            intent.setAction(LOCATION_BROADCAST);
            sendBroadcast(intent);
        }

        @Override
        public void onLocationChanged(Location location) {
            if (tracking) {
                Log.d(TAG, this + " Got location from " + location.getProvider() + ": " + location.getLatitude() + ", " + location.getLongitude());
                listenerEM.updateLocation(location);
                newLocationBroadcast();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "provider disabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "provider enabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

    }

}
