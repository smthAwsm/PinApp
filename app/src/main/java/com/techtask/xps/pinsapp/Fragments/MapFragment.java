package com.techtask.xps.pinsapp.Fragments;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.MarkerOptions;
import com.techtask.xps.pinsapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XPS on 6/11/2016.
 */
public class MapFragment extends SupportMapFragment {

    private List<MarkerOptions> mMarkers;

    public static MapFragment create(GoogleMapOptions options, ArrayList<MarkerOptions> markers) {

        MapFragment fragment = new MapFragment();

        Bundle args = new Bundle();
        args.putParcelable("MapOptions", options);
        args.putParcelableArrayList("markers", markers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<Parcelable> list = getArguments().getParcelableArrayList("markers");
        mMarkers = new ArrayList<MarkerOptions>(list.size());
        for (Parcelable parcelable : list) {
            mMarkers.add((MarkerOptions) parcelable);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        GoogleMap mMap = getMap();
        //add the markers
        if (mMap != null) {
            for (MarkerOptions marker : mMarkers) {
                mMap.addMarker(marker);
            }
        }
    }
}
