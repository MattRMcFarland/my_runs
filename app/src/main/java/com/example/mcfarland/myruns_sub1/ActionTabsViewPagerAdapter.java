package com.example.mcfarland.myruns_sub1;

import android.app.Fragment;
//import android.support.v4.app.Fragment;
import android.app.FragmentManager;
//import android.support.v4.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;

/*
 * Created by Fanglin Chen on 12/18/14.
 */

//XD: FragmentPagerAdapter - Implementation of PagerAdapter that represents each page as a Fragment
// that is persistently kept in the fragment manager
//XD: FragmentPagerAdapter - http://developer.android.com/intl/es/reference/android/support/v4/app/FragmentPagerAdapter.html
//XD: Why adapter (e.g. ArrayAdapter or FragmentPagerAdapter)? An Adapter object acts as a bridge between an
// AdapterView and the underlying data for that view. The Adapter provides access to the data items.
public class ActionTabsViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public static final int START = 0;
    public static final int HISTORY = 1;
    public static final int SETTINGS = 2;
    public static final String UI_TAB_START = "START";
    public static final String UI_TAB_HISTORY = "HISTORY";
    public static final String UI_TAB_SETTINGS = "SETTINGS";

    public ActionTabsViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public Fragment getItem(int pos){
        return fragments.get(pos);
    }

    public int getCount(){
        return fragments.size();
    }

    //This method may be called by the ViewPager to obtain a title string to describe the specified page.
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case START:
                return UI_TAB_START;
            case HISTORY:
                return UI_TAB_HISTORY;
            case SETTINGS:
                return UI_TAB_SETTINGS;
            default:
                break;
        }
        return null;
    }
}
