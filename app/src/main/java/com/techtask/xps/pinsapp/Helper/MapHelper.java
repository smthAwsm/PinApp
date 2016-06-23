package com.techtask.xps.pinsapp.Helper;

import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techtask.xps.pinsapp.Activities.MainActivity;
import com.techtask.xps.pinsapp.Adapters.FragmentViewPagerAdapter;
import com.techtask.xps.pinsapp.Fragments.ErrorDialogFragment;
import com.techtask.xps.pinsapp.Fragments.PinsFragment;
import com.techtask.xps.pinsapp.Models.MarkerModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by XPS on 6/23/2016.
 */
public class MapHelper implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private final String CURRENT_USER_ID = AccessToken.getCurrentAccessToken().getUserId();
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private final long FASTEST_INTERVAL = 5000; /* 5 secs */
    private static GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private static MainActivity context;

    private static Map<String,MarkerModel> markerModelBinding = new HashMap<>();
    private static Map<MarkerModel,Marker> modelMarkerBinding = new HashMap<>();

    private MapHelper(){}
    private static MapHelper instance = new MapHelper();
    public static MapHelper getInstance(Context parentContext){

        if(parentContext instanceof MainActivity)
            context = (MainActivity)parentContext;
        return instance;
    };


    public void loadMapIfNeeded(FragmentViewPagerAdapter pagerAdapter){

        mapFragment = pagerAdapter.getMapFragment();
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    googleMap = map;
                    if (googleMap!=null)
                        setUpMap();
                }
            });
        } else Log.e("ERROR","Error - Map Fragment was null!!");
    }
    private void setUpMap() {
        if (googleMap != null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.setOnMapClickListener(mapClickListener);
            googleMap.setOnMarkerDragListener(markerDragListener);
            // Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
            getMyLocation();
        } else {
            Toast.makeText(context, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }
    private void getMyLocation() {
        if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            connectClient();
        }
    }
    private void connectClient() {
        if (isGooglePlayServicesAvailable() && googleApiClient != null) {
            googleApiClient.connect();
        }
    }
    private boolean isGooglePlayServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, context,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            if (errorDialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(context.getSupportFragmentManager(), "Location Updates");
            }
            return false;
        }
    }
    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
    private String getCompleteAddressString(LatLng latLng) {

        String strAdd = "";

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                }
                strAdd = strReturnedAddress.toString();
            } else {
                Log.w("Current location", "no Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Current location", "Canont get Address!");
        }
        return strAdd;
    }

    private void deleteMarker(Marker marker){
        MarkerModel markerModel = markerModelBinding.get(marker.getId());
        markerModel.delete();
        marker.remove();
        markerModelBinding.remove(marker.getId());
        modelMarkerBinding.remove(markerModel);
        context.markers.remove(markerModel);
        PinsFragment.getRecyclerAdapter().notifyDataSetChanged();
    }
    public static void deleteMarker(MarkerModel markerModel){
        Marker marker = modelMarkerBinding.get(markerModel);
        markerModel.delete();
        marker.remove();
        markerModelBinding.remove(marker.getId());
        modelMarkerBinding.remove(markerModel);
        context.markers.remove(markerModel);
        PinsFragment.getRecyclerAdapter().notifyDataSetChanged();
    }
    public static void showMarker(MarkerModel markerModel){
        context.viewPager.setCurrentItem(0,true);
        Marker marker = modelMarkerBinding.get(markerModel);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17);
        googleMap.animateCamera(cameraUpdate);
    }

    //region Marker Listeners
    GoogleMap.OnMarkerDragListener markerDragListener = new GoogleMap.OnMarkerDragListener() {
        @Override
        public void onMarkerDragStart(Marker marker) {
            deleteMarker(marker);
        }

        @Override
        public void onMarkerDrag(Marker marker) {

        }
        @Override
        public void onMarkerDragEnd(Marker marker) {

        }
    };
    GoogleMap.OnMapClickListener  mapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if(googleMap.getCameraPosition().zoom > 14) {
                String date = DATE_FORMAT.format(Calendar.getInstance().getTime());
                String address = getCompleteAddressString(latLng);

                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .draggable(true)
                        .position(latLng)
                        .title(address));

                String keyId = marker.getId();

                MarkerModel markerModel = new MarkerModel(CURRENT_USER_ID,latLng.latitude,latLng.longitude,address,date);
                context.markers.add(markerModel);
                markerModel.save();
                PinsFragment.getRecyclerAdapter().notifyDataSetChanged();

                markerModelBinding.put(keyId,markerModel);
                modelMarkerBinding.put(markerModel,marker);
            }
        }
    };
    //endregion
    //region APIClientCallbacks
    @Override
    public void onConnected(Bundle dataBundle) {
        List<MarkerModel> markerList = context.markers;
        if(markerList != null){
            for (MarkerModel markerModel : markerList){
                LatLng latLng = new LatLng(markerModel.getLatitude(),markerModel.getLongtitude());
                Marker marker = googleMap.addMarker(new MarkerOptions().draggable(true).position(latLng).title(markerModel.getTitle()));
                String keyId = marker.getId();
                markerModelBinding.put(keyId,markerModel);
                modelMarkerBinding.put(markerModel,marker);
            }
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            Toast.makeText(context, "GPS location was found!", Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            googleMap.animateCamera(cameraUpdate);
        } else {
            Toast.makeText(context, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
        startLocationUpdates();
    }
    @Override
    public void onLocationChanged(Location location) {
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(context, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(context, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(context,CONNECTION_FAILURE_RESOLUTION_REQUEST);
            }catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context,
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }
    //endregion

}
