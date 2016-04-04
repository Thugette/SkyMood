package com.example.owner.skymood;

import android.Manifest;
import android.content.Intent;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.owner.skymood.location.NetworkLocationListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements NetworkLocationListener.LocationReceiver {

    private TextView city;
    private TextView date;
    private TextView temperature;
    private TextView condition;
    private TextView feelsLike;
    private TextView lastUpdate;
    private ImageView weatherImage;

    private Location location;
    private LocationManager locationManager;
    private NetworkLocationListener listener;
    private double latitude;
    private double longtitude;

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

        //TODO: shared prefs

        //network location
        getNetworkLocation();

        //TODO: gps
      //  getGpsLocation();


        MyTask task = new MyTask();
        task.execute();

    }

    public void getLocation(View view) {
        Intent intent = new Intent(this, HourlyActivity.class);
        startActivity(intent);
    }

    public void getGpsLocation() {
        listener = new NetworkLocationListener(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
    }

    public void getNetworkLocation() {
        listener = new NetworkLocationListener(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
    }

    @Override
    public void receiveLocation(Location location) {
        Log.e("VVV", "method entered");
        this.location = location;
        this.latitude = location.getLatitude();
        Log.e("VVV", latitude + "");
        this.longtitude = location.getLongitude();
        Log.e("VVV", longtitude + "");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.removeUpdates(listener);
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
                Log.e("didi", ""+latitude);
                Log.e("didi", ""+longtitude);
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longtitude+"&APPID=186859f63d164736ac379bc15a658e8b&units=metric");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Scanner sc = new Scanner(connection.getInputStream());
                StringBuilder body = new StringBuilder();
                while(sc.hasNextLine()){
                    body.append(sc.nextLine());
                }
                String info = body.toString();

                JSONObject jsonData = new JSONObject(info);
                JSONArray weather = (JSONArray) jsonData.get("weather");
                JSONObject main = (JSONObject) jsonData.get("main");
                //JSONObject locationObject = (JSONObject) observation.get("display_location");
                location = jsonData.getString("name");
                conditionn = weather.getString(2);
                temp = main.getString("temp") + "°";
                //feelsLikee = "Feels like: " + main.getString("feelslike_c") + "℃";
                iconUrl = "http://openweathermap.org/img/w/"+weather.getString(3)+".png";

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