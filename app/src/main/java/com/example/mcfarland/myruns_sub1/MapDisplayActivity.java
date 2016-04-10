package com.example.mcfarland.myruns_sub1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MapDisplayActivity extends Activity {

    private static final String TAG = "MAP DISPLAY ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);

        /* create click listeners */
        Button saveButton = (Button)findViewById(R.id.map_display_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveClicked(view);
            }
        });

        Button cancelButton = (Button)findViewById(R.id.map_display_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelClicked(view);
            }
        });
    }

    public void onSaveClicked(View v) {
        Log.d(TAG, "saved map");
        finish();
    }

    public void onCancelClicked(View v){
        Log.d(TAG, "cancelled map");
        finish();
    }
}
