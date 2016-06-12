
/**
 * Created by XPS on 6/11/2016.
 */

package com.techtask.xps.pinsapp.Activities;

import android.app.Dialog;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
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
import com.techtask.xps.pinsapp.Adapters.FragmentViewPagerAdapter;
import com.techtask.xps.pinsapp.Fragments.PinsFragment;
import com.techtask.xps.pinsapp.Models.MarkerModel;
import com.techtask.xps.pinsapp.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
                                                        GoogleApiClient.OnConnectionFailedListener,LocationListener
{
    private SupportMapFragment mapFragment;
    private static GoogleMap googleMap;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private long UPDATE_INTERVAL = 60000;  /* 60 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 secs */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final String CURRENT_USER_ID = AccessToken.getCurrentAccessToken().getUserId();
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
    private static ViewPager viewPager;
    private AppBarLayout appBarLayout;

    public static List<MarkerModel> markers;
    private static Map<String,MarkerModel> markerModelBinding = new HashMap<>();
    private static Map<MarkerModel,Marker> modelMarkerBinding = new HashMap<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        URL img_url = null;
        String id = AccessToken.getCurrentAccessToken().getUserId();


        try {
            img_url = new URL("http://graph.facebook.com/"+id+"/picture?type=normal");
            Log.w("RESPONXXXXXE",img_url.openConnection().getInputStream().toString());
        } catch (Exception e) {
            Log.e("MalformedURLException","%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%555");
            e.printStackTrace();
        }




        FragmentViewPagerAdapter pagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        setupActionBar(pagerAdapter);
        markers = MarkerModel.find(MarkerModel.class,"owner_Id = ?",CURRENT_USER_ID);
        loadMapIfNeeded(pagerAdapter);
    }

    private void setupActionBar(FragmentViewPagerAdapter pagerAdapter){

        final Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Parallax Tabs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);


        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                //"/"+AccessToken.getCurrentAccessToken().getUserId()+"?fields=id,name,picture",
                "/"+AccessToken.getCurrentAccessToken().getUserId(),
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                    Log.e("AZAZAAZ",response.toString());
                     //response
                    }
                }
        ).executeAsync();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.header);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
                int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimaryDark);
                collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
            }
        });

        appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);
        appBarLayout.setExpanded(false);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        appBarLayout.setExpanded(false);
                        break;
                    case 1:
                         break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.logoutItem){
        finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private  void loadMapIfNeeded(FragmentViewPagerAdapter pagerAdapter){

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
        } else Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
    }

    protected void setUpMap() {
        if (googleMap != null) {
             Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
             googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
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
                        markers.add(markerModel);
                        markerModel.save();
                        PinsFragment.getRecyclerAdapter().notifyDataSetChanged();

                        markerModelBinding.put(keyId,markerModel);
                        modelMarkerBinding.put(markerModel,marker);
                    }
                }
            });

            googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
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
            });

             getMyLocation();
        } else {
            Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void deleteMarker(Marker marker){
        MarkerModel markerModel = markerModelBinding.get(marker.getId());
        markerModel.delete();
        marker.remove();
        markerModelBinding.remove(marker.getId());
        modelMarkerBinding.remove(markerModel);
        markers.remove(markerModel);
        PinsFragment.getRecyclerAdapter().notifyDataSetChanged();
    }

    public static void deleteMarker(MarkerModel markerModel){
        Marker marker = modelMarkerBinding.get(markerModel);
        markerModel.delete();
        marker.remove();
        markerModelBinding.remove(marker.getId());
        modelMarkerBinding.remove(markerModel);
        markers.remove(markerModel);
        PinsFragment.getRecyclerAdapter().notifyDataSetChanged();
    }

    public static void showMarker(MarkerModel markerModel){
        viewPager.setCurrentItem(0,true);
        Marker marker = modelMarkerBinding.get(markerModel);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17);
        googleMap.animateCamera(cameraUpdate);

    }
    private void getMyLocation() {
        if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            connectClient();
        }
    }

    protected void connectClient() {
        if (isGooglePlayServicesAvailable() && googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            if (errorDialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }
            return false;
        }
    }

    @Override
    public void onConnected(Bundle dataBundle) {

        if(markers != null){
            for (MarkerModel markerModel : markers){
                LatLng latLng = new LatLng(markerModel.getLatitude(),markerModel.getLongtitude());
                Marker marker = googleMap.addMarker(new MarkerOptions().draggable(true).position(latLng).title(markerModel.getTitle()));
                String keyId = marker.getId();
                markerModelBinding.put(keyId,markerModel);
                modelMarkerBinding.put(markerModel,marker);
            }
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            Toast.makeText(this, "GPS location was found!", Toast.LENGTH_SHORT).show();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            googleMap.animateCamera(cameraUpdate);
            } else {
            Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
        }
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
           String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this,CONNECTION_FAILURE_RESOLUTION_REQUEST);
			    }catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
        }
    }


    public static class ErrorDialogFragment extends DialogFragment {

        private Dialog mDialog;

        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    private String getCompleteAddressString(LatLng latLng) {

        String strAdd = "";      

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
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
                Log.w("My Current location", "no Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current location", "Canont get Address!");
        }
        return strAdd;
    }


}
