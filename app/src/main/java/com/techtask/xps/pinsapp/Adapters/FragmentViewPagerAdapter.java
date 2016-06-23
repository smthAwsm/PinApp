package com.techtask.xps.pinsapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.android.gms.maps.SupportMapFragment;
import com.techtask.xps.pinsapp.Fragments.PinsFragment;

/**
 * Created by XPS on 6/11/2016.
 */
public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {

    private String tabTitles[] = new String[] { "Map", "Pins" };
    private static SupportMapFragment mapFragment;

    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
        mapFragment = new SupportMapFragment();
        mapFragment.setRetainInstance(true);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:{
                return mapFragment;
            }
            case 1:{
                return new PinsFragment();
            }
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    public static SupportMapFragment getMapFragment(){
        if(mapFragment != null)
            return mapFragment;
        else return null;
    }
}
