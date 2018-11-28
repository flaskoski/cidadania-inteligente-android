package com.laskoski.f.felipe.cidadania_inteligente.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.AppInviteContent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.laskoski.f.felipe.cidadania_inteligente.R;
import com.laskoski.f.felipe.cidadania_inteligente.model.LocationTask;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionProgress;

import java.io.IOException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationTaskActivity extends TaskActivity implements OnMapReadyCallback {

    private GoogleMap mMap = null;
    private LocationManager locationManager;
    private CircleOptions destinationCircle;
    private LocationTask task;

    @Override
    public LocationTask getTaskDetails() {
        Intent taskDetails = getIntent();
        return (LocationTask) taskDetails.getSerializableExtra("task");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_task);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initialize ActionBar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.colorActionBar));

        //Get info from Task
        this.task = new LocationTask();
        this.task = getTaskDetails();
        this.taskResult.putExtra("taskId", task.get_id());

        TextView destinationLabel = findViewById(R.id.lb_destination);
        destinationLabel.setText("Vá até " + task.getAddress());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                if (mMap != null) {
                    setUserLocation();
                    setDestination();
                }
            }

        }
    }

    public void goToUserLocation(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        Location lastKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnowLocation.getLatitude(), lastKnowLocation.getLongitude()), 16));
    }
    public void goToDestination(View v){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        if(destinationCircle != null)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(destinationCircle.getCenter().latitude, destinationCircle.getCenter().longitude), 15));
    }
    public void finishTask(View v){
        taskResult.putExtra("taskStatus", MissionProgress.TASK_COMPLETED);
        setResult(RESULT_OK, taskResult);
        finish();
    }



    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
          //  mMap.clear();
            MarkerOptions mp = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker());
            mp.position(new LatLng(location.getLatitude(), location.getLongitude()));
            mMap.addMarker(mp);
            if(destinationCircle != null) {
                mMap.addCircle(destinationCircle);
                Button btConfirmar = findViewById(R.id.btConfirm);
                if(checkIfUserArrivedAtDestination(location)){
                    btConfirmar.setVisibility(View.VISIBLE);
                }
                else btConfirmar.setVisibility(View.GONE);

            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private Boolean checkIfUserArrivedAtDestination(Location userLocation) {
        //if inside the circle
        if( (Math.pow(Math.abs(destinationCircle.getCenter().latitude -  userLocation.getLatitude()),2)
                + Math.pow(Math.abs(destinationCircle.getCenter().longitude -  userLocation.getLongitude()),2))
                <= Math.pow(destinationCircle.getRadius(),2))
            return true;
        return false;

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
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        //Initialize Location
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if(Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            //else there is already permission
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                setUserLocation();
                setDestination();
            }
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            setUserLocation();
            setDestination();
        }
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    /**
     * Be sure to have the GPS permission GRANTED before using this function.
     */
    private void setUserLocation() {
        @SuppressLint("MissingPermission") Location lastKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng userLocation = new LatLng(lastKnowLocation.getLatitude(), lastKnowLocation.getLongitude());
        //mMap.clear();
        Address address = null;
        mMap.addMarker(new MarkerOptions().position(userLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        mMap.setMinZoomPreference(6);
        mMap.setMaxZoomPreference(20);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                sendTaskProgressBack();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }

    private void sendTaskProgressBack() {
        taskResult.putExtra("taskStatus", MissionProgress.TASK_NOT_STARTED);
        setResult(RESULT_OK, taskResult);
        finish();
    }


    private void setDestination(){
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            Address address = geocoder.getFromLocationName(this.task.getAddress(), 1).get(0);
            //"Av. do Matão, 1010, Cidade Universitária, São Paulo-SP"
            //var "polygonSize" should be from task
            Double polygonSize;
            if(this.task.getDestinationSize() != null)
                polygonSize = this.task.getDestinationSize();
            else polygonSize = LocationTask.SIZE_DEFAULT;
            List<LatLng> destinationCoords = new ArrayList<>();
            destinationCoords.add(new LatLng(address.getLatitude()+polygonSize, address.getLongitude()+polygonSize));
            destinationCoords.add(new LatLng(address.getLatitude()+polygonSize, address.getLongitude()-polygonSize));
            destinationCoords.add(new LatLng(address.getLatitude()-polygonSize, address.getLongitude()-polygonSize));
            destinationCoords.add(new LatLng(address.getLatitude()-polygonSize, address.getLongitude()+polygonSize));
            mMap.addPolygon(new PolygonOptions().addAll(destinationCoords).strokeColor(Color.RED));

            LatLng destinationCoord = new LatLng(address.getLatitude(), address.getLongitude());
            destinationCircle = new CircleOptions().center(destinationCoord).radius(polygonSize).strokeColor(Color.RED).fillColor(Color.argb(150,100,100,100));
            mMap.addCircle(destinationCircle);

        } catch (IOException e) {
            Toast.makeText(this, "Erro - Local de destino não encontrado!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        sendTaskProgressBack();
    }

}
