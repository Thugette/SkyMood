package com.example.owner.skymood.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.owner.skymood.R;
import com.example.owner.skymood.adapters.HourlyAdapter;
import com.example.owner.skymood.adapters.WeeklyAdapter;
import com.example.owner.skymood.model.HourlyWeather;
import com.example.owner.skymood.model.WeeklyWeather;

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

public class WeeklyWeatherFragment extends Fragment implements Swideable{

    private Context context;
    private ArrayList<WeeklyWeather> weatherArray;
    RecyclerView recyclerView;

    public WeeklyWeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weekly_weather, container, false);
        weatherArray = new ArrayList<WeeklyWeather>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_weekly);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        WeeklyAdapter adapter = new WeeklyAdapter(weatherArray, context);
        recyclerView.setAdapter(adapter);

        new GetWeeklyTask().execute();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }


    class GetWeeklyTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try{
                URL url = new URL("http://api.wunderground.com/api/b4d0925e0429238f/forecast7day/q/BG/Sofia.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Scanner sc = new Scanner(connection.getInputStream());
                StringBuilder body = new StringBuilder();
                while(sc.hasNextLine()){
                    body.append(sc.nextLine());
                }
                String info = body.toString();

                JSONObject jsonData = new JSONObject(info);
                JSONObject forecast = jsonData.getJSONObject("forecast");
                JSONObject simpleforecast = forecast.getJSONObject("simpleforecast");
                JSONArray forecastdayArray = (JSONArray) simpleforecast.get("forecastday");
                for(int i = 0; i < forecastdayArray.length(); i++) {
                    JSONObject obj = forecastdayArray.getJSONObject(i);
                    JSONObject date = obj.getJSONObject("date");

                    String day = date.getString("weekday");
                    JSONObject high = obj.getJSONObject("high");
                    String max = high.getString("celsius");
                    JSONObject low = obj.getJSONObject("low");
                    String min = low.getString("celsius");

                    String condition = obj.getString("conditions");

                    String iconURL = obj.getString("icon_url");
                    Bitmap icon = BitmapFactory.decodeStream((InputStream) new URL(iconURL).getContent());
                    weatherArray.add(new WeeklyWeather(day, min, max, condition, icon));
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
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
