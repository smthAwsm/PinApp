package com.techtask.xps.pinsapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techtask.xps.pinsapp.Fragments.MapFragment;
import com.techtask.xps.pinsapp.Fragments.PageFragment;
import com.techtask.xps.pinsapp.Fragments.PinsFragment;

import java.util.ArrayList;

/**
 * Created by XPS on 6/11/2016.
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private String tabTitles[] = new String[] { "Map", "Pins" };

    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:{
                GoogleMapOptions mapOptions = new GoogleMapOptions();
                mapOptions.compassEnabled(true);
                mapOptions.mapType(GoogleMap.MAP_TYPE_HYBRID);
                return MapFragment.create(mapOptions,new ArrayList<MarkerOptions>());
                }
            case 1:{
                return new PinsFragment();
            }
        }

        //Log.e("QAZXSWED","@@@@@@@@@@@@@@@@@@" + position + "%%%%%%%%%%%%%%%%%%%%%%%%%");
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
