package com.viiup.android.flock.application;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viiup.android.flock.models.UserEventModel;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Context context;
    private GoogleMap mMap;
    private UiSettings mMapSettings;
    public List<UserEventModel> userEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.right_out);

        context = this;

        setContentView(R.layout.map_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        Gson gson = new Gson();
        String userEventsJson = getIntent().getStringExtra("userEventsJson");
        userEvents = gson.fromJson(userEventsJson, new TypeToken<List<UserEventModel>>() {
        }.getType());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TextView textViewSecondaryBar = (TextView) findViewById(R.id.secondaryBar);
        textViewSecondaryBar.setText(getResources().getString(R.string.nearby_events));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            // up button
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        if (checkAccessLocationPermission())
            mMap.setMyLocationEnabled(true);
        mMapSettings = mMap.getUiSettings();
        mMapSettings.setCompassEnabled(true);
        mMapSettings.setZoomControlsEnabled(true);

        LatLngBounds.Builder llbBuilder = new LatLngBounds.Builder();

        mMap.clear();

        if(userEvents != null) {
            for (UserEventModel userEvent : userEvents) {
                LatLng eventLatLng = new LatLng(userEvent.event.getEventLatitude(), userEvent.event.getEventLongitude());
                mMap.addMarker(new MarkerOptions().position(eventLatLng).title(userEvent.event.getEventName()).snippet(userEvent.event.getEventDescription()));
                llbBuilder.include(eventLatLng);
                LatLngBounds llBounds = llbBuilder.build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(llBounds, 200));
            }
        }

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);

        if (location != null) {
//            onLocationChanged(location);
            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12));
        }
//        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);
    }

//    @Override
//    public void onLocationChanged(Location location) {
//
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//        LatLng latLng = new LatLng(latitude, longitude);
//        mMap.addMarker(new MarkerOptions().position(latLng));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//    }

//    @Override
//    public void onProviderDisabled(String provider) {
//        // TODO Auto-generated method stub
//    }

//    @Override
//    public void onProviderEnabled(String provider) {
//        // TODO Auto-generated method stub
//    }

//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//        // TODO Auto-generated method stub
//    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    private boolean checkAccessLocationPermission() {
        String accessFineLocation = "android.permission.ACCESS_FINE_LOCATION";
        String accessCoarseLocation = "android.permission.ACCESS_COARSE_LOCATION";
        int permission = context.checkCallingOrSelfPermission(accessFineLocation);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            permission = context.checkCallingOrSelfPermission(accessCoarseLocation);
            return permission == PackageManager.PERMISSION_GRANTED;
        }
    }
}
