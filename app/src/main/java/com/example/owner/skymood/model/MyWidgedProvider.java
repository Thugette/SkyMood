package com.example.owner.skymood.model;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import com.example.owner.skymood.R;
import com.example.owner.skymood.fragments.CurrentWeatherFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Golemanovaa on 10.4.2016 г..
 */
public class MyWidgedProvider extends AppWidgetProvider {


    private static final String ACTION_CLICK = "ACTION_CLICK";
    private String city;
    private String country;
    private String countryCode;
    private String temp;
    private String icon;
    private String condition;
    private int iconId;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        LocationPreference pref = LocationPreference.getInstance(context);
        this.city = pref.getCity();
        this.country = pref.getCountry();
        this.countryCode = pref.getCountryCode();

        // iterate through all of our widgets (in case the user has placed multiple widgets)
        for (int i = 0; i < appWidgetIds.length; i++) {
           // InfoGetter task = new InfoGetter();
           // task.execute();
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            remoteViews.setTextViewText(R.id.widget_city, city);
            remoteViews.setTextViewText(R.id.widget_country, country);
            this.condition = pref.getCondition();
            remoteViews.setTextViewText(R.id.condition, this.condition);
            this.temp = pref.getTemperature();
            remoteViews.setTextViewText(R.id.degree, this.temp + "℃");

            Field field = null;
            try {
                field = R.drawable.class.getDeclaredField(pref.getIcon());
                iconId = field.getInt(this);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            remoteViews.setImageViewResource(R.id.icon, iconId);

            //update when the update button is clicked
            Intent intent = new Intent(context, MyWidgedProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            //the widgets that should be updated (all of the app widgets)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // request the AppWidgetManager object to update the app widget
            remoteViews.setOnClickPendingIntent(R.id.syncWidget, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private class InfoGetter extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL("http://api.wunderground.com/api/" + CurrentWeatherFragment.API_KEY + "/conditions/q/" + countryCode + "/" + city + ".json");
                HttpURLConnection connection = connection = (HttpURLConnection) url.openConnection();connection.connect();

                Scanner sc = new Scanner(connection.getInputStream());
                StringBuilder body = new StringBuilder();
                while(sc.hasNextLine()){
                    body.append(sc.nextLine());
                }
                String info = body.toString();

                JSONObject jsonData = new JSONObject(info);
                JSONObject observation = (JSONObject) jsonData.get("current_observation");
                condition = observation.getString("weather");
                temp = observation.getString("temp_c");
                icon = observation.getString("icon");

                Field field = R.drawable.class.getDeclaredField("mydrawable_name");
                iconId = field.getInt(this);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
