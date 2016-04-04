package com.example.owner.skymood;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.owner.skymood.adapters.HourlyAdapter;
import com.example.owner.skymood.model.HourlyWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class HourlyActivity extends AppCompatActivity {

    private ArrayList<HourlyWeather> hourlyWeather;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly);
        hourlyWeather = new ArrayList<HourlyWeather>();
        recycler = (RecyclerView) findViewById(R.id.recycler_hourly);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        HourlyAdapter adapter = new HourlyAdapter(this, hourlyWeather);
        recycler.setAdapter(adapter);

        new GetHoursTask().execute();
    }

    class GetHoursTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL("http://api.wunderground.com/api/b4d0925e0429238f/hourly/q/BG/Sofia.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Scanner sc = new Scanner(connection.getInputStream());
                StringBuilder body = new StringBuilder();
                while(sc.hasNextLine()){
                    body.append(sc.nextLine());
                }
                String info = body.toString();

                JSONObject jsonData = new JSONObject(info);
                JSONArray hourlyArray = (JSONArray) jsonData.get("hourly_forecast");
                for(int i = 0; i < hourlyArray.length(); i++){
                    JSONObject obj = (JSONObject)hourlyArray.getJSONObject(i);
                    String hour = obj.getJSONObject("FCTTIME").getString("hour");
                    String condition = obj.getString("condition");
                    String temp = obj.getJSONObject("temp").getString("metric");
                    String iconURL = obj.getString("icon_url");
                    Bitmap icon = BitmapFactory.decodeStream((InputStream) new URL(iconURL).getContent());
                    hourlyWeather.add(new HourlyWeather(hour, condition, temp, icon));
                }

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
            recycler.getAdapter().notifyDataSetChanged();
        }
    }
}
