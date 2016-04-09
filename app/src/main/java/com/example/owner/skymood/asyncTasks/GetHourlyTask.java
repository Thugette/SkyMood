package com.example.owner.skymood.asyncTasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.owner.skymood.SwipeViewActivity;
import com.example.owner.skymood.fragments.HourlyWeatherFragment;
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
import java.util.Scanner;

/**
 * Created by owner on 09/04/2016.
 */
public class GetHourlyTask extends AsyncTask<String, Void, Void> {

    private final static String API_KEY = "9d48021d05e97609";
    private Context context;
    private SwipeViewActivity activity;
    private Fragment fragment;
    private ArrayList<HourlyWeather> hourlyWeather;

    public GetHourlyTask(Context context, Fragment fragment, ArrayList<HourlyWeather> hourlyWeather) {
        Log.e("didi", "task created: " + System.currentTimeMillis());
        this.context = context;
        this.fragment = fragment;
        activity = (SwipeViewActivity)context;
        this.hourlyWeather = hourlyWeather;
    }

    protected Void doInBackground(String... params) {
        try {

            String city = params[0];
            String code = params[1];

            URL url = new URL("http://api.wunderground.com/api/"+API_KEY+"/hourly/q/"+code+"/"+city+".json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            Scanner sc = new Scanner(connection.getInputStream());
            StringBuilder body = new StringBuilder();
            while(sc.hasNextLine()){
                body.append(sc.nextLine());
            }
            String info = body.toString();
            Log.e("DIDI", info);
            JSONObject jsonData = new JSONObject(info);
            activity.setHourlyJSON(info);
            JSONArray hourlyArray = (JSONArray) jsonData.get("hourly_forecast");

            if(!hourlyWeather.isEmpty())
                hourlyWeather.removeAll(hourlyWeather);

            for(int i = 0; i < hourlyArray.length(); i++){
                JSONObject obj = hourlyArray.getJSONObject(i);
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
        ((HourlyWeatherFragment)fragment).getAdapter().notifyDataSetChanged();
        Log.e("DIDI", "adapter notified");

    }
}
