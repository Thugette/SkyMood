package com.example.owner.skymood;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.skymood.location.NetworkLocationHelper;
import com.example.owner.skymood.location.NetworkLocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private TextView city;
    private TextView date;
    private TextView temperature;
    private TextView condition;
    private TextView feelsLike;
    private TextView lastUpdate;
    private ImageView weatherImage;

    private double latitude;
    private double longtitude;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = (TextView) findViewById(R.id.cityNameTextView);
        date = (TextView) findViewById(R.id.dateAndTimeTextView);
        temperature = (TextView) findViewById(R.id.temperatureTextView);
        condition = (TextView) findViewById(R.id.conditionTextView);
        feelsLike = (TextView) findViewById(R.id.feelsLikeTextView);
        lastUpdate = (TextView) findViewById(R.id.lastUpdateTextView);
        weatherImage = (ImageView) findViewById(R.id.weatherImageView);

        //shared prefs

        //network location
        NetworkLocationHelper nlh = new NetworkLocationHelper(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(nlh)
                .addOnConnectionFailedListener(nlh)
                .build();
        nlh.setGoogleApiClient(mGoogleApiClient);

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
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

      //  LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      //  Location l = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        Log.e("HHH", location + "");

        if(location != null) {
            double lat = location.getLatitude();
            Log.e("HHH", lat + "");
            double lon = location.getLongitude();
            Log.e("HHH", lon + "");
        }

        //gps

        MyTask task = new MyTask();
        task.execute();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //connecting the client
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        //disconnecting client
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /*  public void getLocation(View view) {
        Intent intent = new Intent(MainActivity.this, AndroidLocationActivity.class);
        startActivity(intent);
    } */

    public void getCoordinatesByNetwork(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
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

        String locationProvider = LocationManager.NETWORK_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider, 0, 0, new NetworkLocationListener());
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        latitude = lastKnownLocation.getLatitude();
        Log.e("VVV", "latitude: " + latitude);
        longtitude = lastKnownLocation.getLongitude();
        Log.e("VVV", "longtitude: " + longtitude);

    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        String location;
        String conditionn;
        String iconUrl;
        String temp;
        String feelsLikee;
        Bitmap pic;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL("http://api.wunderground.com/api/b4d0925e0429238f/conditions/q/CA/San_Francisco.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Scanner sc = new Scanner(connection.getInputStream());
                StringBuilder body = new StringBuilder();
                while(sc.hasNextLine()){
                    body.append(sc.nextLine());
                }
                String info = body.toString();

                JSONObject jsonData = new JSONObject(info);
                JSONObject observation = (JSONObject) jsonData.get("current_observation");
                JSONObject locationObject = (JSONObject) observation.get("display_location");
                location = locationObject.getString("full");
                conditionn = observation.getString("weather");
                temp = observation.getString("temp_c") + "°";
                feelsLikee = "Feels like: " + observation.getString("feelslike_c") + "℃";
                iconUrl = observation.getString("icon_url");

                pic = BitmapFactory.decodeStream((InputStream) new URL(iconUrl).getContent());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            city.setText(location);
            temperature.setText(temp);
            condition.setText(conditionn);
            feelsLike.setText(feelsLikee);

            weatherImage.setImageBitmap(pic);
            weatherImage.setAdjustViewBounds(true);
        }
    }
}