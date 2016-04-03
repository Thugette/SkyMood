package com.example.owner.skymood;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = (TextView) findViewById(R.id.cityNameTextView);
        date = (TextView) findViewById(R.id.cityNameTextView);
        temperature = (TextView) findViewById(R.id.cityNameTextView);
        condition = (TextView) findViewById(R.id.cityNameTextView);
        feelsLike = (TextView) findViewById(R.id.cityNameTextView);
        lastUpdate = (TextView) findViewById(R.id.cityNameTextView);
        weatherImage = (ImageView) findViewById(R.id.weatherImageView);



    }

    public void getLocation(View view) {
        Intent intent = new Intent(MainActivity.this, AndroidLocationActivity.class);
        startActivity(intent);
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL("http://api.wunderground.com/api/b4d0925e0429238f/forecast/q/CA/San_Francisco.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Scanner sc = new Scanner(connection.getInputStream());
                StringBuilder body = new StringBuilder();
                while(sc.hasNextLine()){
                    body.append(sc.nextLine());
                }
                String info = body.toString();

                JSONObject infoJson = new JSONObject(info);
                JSONObject forecast = (JSONObject) infoJson.get("forecast");
                JSONObject simpleForecast = (JSONObject) forecast.get("simpleforecast");
                JSONArray forecastDay = simpleForecast.getJSONArray("forecastday");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}