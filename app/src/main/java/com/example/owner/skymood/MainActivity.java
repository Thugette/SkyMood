package com.example.owner.skymood;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
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

        MyTask task = new MyTask();
        task.execute();

    }

  /*  public void getLocation(View view) {
        Intent intent = new Intent(MainActivity.this, AndroidLocationActivity.class);
        startActivity(intent);
    } */

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
                temp = observation.getString("temp_c");
                feelsLikee = "Feels like: " + observation.getString("feelslike_c");
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