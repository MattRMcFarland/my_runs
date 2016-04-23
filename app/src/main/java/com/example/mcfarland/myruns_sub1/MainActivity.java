package com.example.mcfarland.myruns_sub1;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.mcfarland.myruns_sub1.view.SlidingTabLayout;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MAIN";
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    private ActionTabsViewPagerAdapter myViewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (hasPermissions() == false)
            finish();

        // Define SlidingTabLayout (shown at top)
        // and ViewPager (shown at bottom) in the layout.
        // Get their instances.
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        // create a fragment list in order.
        fragments = new ArrayList<Fragment>();

        fragments.add(new StartFragment());
        fragments.add(new HistoryFragment());
        fragments.add(new SettingsFragment());


        // use FragmentPagerAdapter to bind the slidingTabLayout (tabs with different titles)
        // and ViewPager (different pages of fragment) together
        myViewPageAdapter = new ActionTabsViewPagerAdapter(this.getFragmentManager(), fragments);
        mViewPager.setAdapter(myViewPageAdapter);

        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ;
            }

            @Override
            public void onPageSelected(int position) {
                switch(position) {
                    case 1:
                        HistoryFragment histFrag = (HistoryFragment) myViewPageAdapter.getItem(1);
                        histFrag.reloadDatabase();
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                ;
            }
        });

        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private boolean hasPermissions() {
        if (getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getApplicationContext(), "Enable Camera Permissions",
                    Toast.LENGTH_LONG).show();
            return false;

        } else if (getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getApplicationContext(), "Enable Storage Permissions",
                    Toast.LENGTH_LONG).show();
            return false;

        } else if (getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getApplicationContext(), "Enable Storage Permissions",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

 }
