package com.example.owner.skymood;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.owner.skymood.model.GPSLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;

    private EditText edtLat;
    private EditText edtLng;

    private TextView txtLat;
    private TextView txtLong;
    private TextView classLat;
    private TextView classLong;

    LatLng prevPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        edtLat = (EditText) findViewById(R.id.edt_latitude);
        edtLng = (EditText) findViewById(R.id.edt_longitude);
        txtLat = (TextView) findViewById(R.id.current_lat);
        txtLong = (TextView) findViewById(R.id.current_long);
        classLat = (TextView) findViewById(R.id.class_lat);
        classLong = (TextView) findViewById(R.id.class_long);




        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        txtLat.setText(String.valueOf(location.getLatitude()));
        txtLong.setText(String.valueOf(location.getLongitude()));


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void setLocation(View view) {
        GPSLocation gps = new GPSLocation(LocationActivity.this);
        classLat.setText(String.valueOf(gps.getLatitude()));
        classLong.setText(String.valueOf(gps.getLongitude()));
    }
}
