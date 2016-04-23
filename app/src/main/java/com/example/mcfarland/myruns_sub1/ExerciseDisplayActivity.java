package com.example.mcfarland.myruns_sub1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ExerciseDisplayActivity extends ActionBarActivity {

    private static final String TAG = "EXERCISE DISPLAY ACTIV";

    public static final String ID_KEY = "id";
    public static final String INPUT_KEY = "input";
    public static final String ACTIVITY_KEY = "activity";
    public static final String DATE_KEY = "datetime";
    public static final String DURATION_KEY = "duration";
    public static final String DISTANCE_KEY = "distance";
    public static final String CALORIES_KEY = "calories";
    public static final String HEART_RATE_KEY = "heartrate";
    public static final String COMMENT_KEY = "comment";

    private long entryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exercise_display);

        /* set up views */
        setViews(getIntent().getExtras());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exercise_activity_display_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AD_menu_delete:
                Log.d(TAG,"clicked delete");
                onDeleteClicked();
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onDeleteClicked() {
        Toast.makeText(this, "Deleted Entry", Toast.LENGTH_SHORT).show();

        // async task to delete entry
        new DataDeleter().execute(this, entryId);
    }

    public void setViews(Bundle b) {

        Intent i = getIntent();

        entryId = i.getLongExtra(ID_KEY, -1);

        EditText inputText = (EditText) findViewById(R.id.AD_input_type);
        inputText.setText(b.getString(INPUT_KEY, "NA"));
        inputText.setKeyListener(null);

        EditText activityText = (EditText) findViewById(R.id.AD_activity_type);
        activityText.setText(b.getString(ACTIVITY_KEY, "NA"));
        activityText.setKeyListener(null);

        EditText dateText = (EditText) findViewById(R.id.AD_date_and_time);
        dateText.setText(b.getString(DATE_KEY, "NA"));
        dateText.setKeyListener(null);

        EditText durationText = (EditText) findViewById(R.id.AD_duration);
        durationText.setText(b.getString(DURATION_KEY, "NA"));
        durationText.setKeyListener(null);

        EditText distanceText = (EditText) findViewById(R.id.AD_distance);
        distanceText.setText(b.getString(DISTANCE_KEY, "NA"));
        distanceText.setKeyListener(null);

        EditText caloriesText = (EditText) findViewById(R.id.AD_calories);
        String calStr = b.getInt(CALORIES_KEY) + " cals";
        caloriesText.setText(calStr);
        caloriesText.setKeyListener(null);

        EditText hrText = (EditText) findViewById(R.id.AD_heart_rate);
        String hrStr = b.getInt(HEART_RATE_KEY) + " bpm";
        hrText.setText(hrStr);
        hrText.setKeyListener(null);

        EditText commentText = (EditText) findViewById(R.id.AD_comment);
        commentText.setText(b.getString(COMMENT_KEY, "NA"));
        commentText.setKeyListener(null);
    }
}
