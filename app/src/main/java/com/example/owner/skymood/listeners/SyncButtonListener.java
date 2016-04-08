package com.example.owner.skymood.listeners;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.owner.skymood.asyncTasks.APIDataGetterAsyncTask;
import com.example.owner.skymood.fragments.CurrentWeatherFragment;

/**
 * Created by Golemanovaa on 8.4.2016 г..
 */
public class SyncButtonListener implements View.OnClickListener {

    private CurrentWeatherFragment fragment;
    private ImageView weatherImage;
    private String countryCode;
    private String city;
    private String country;

    public SyncButtonListener(CurrentWeatherFragment fragment, ImageView weatherImage, String countryCode, String city, String country){
        this.fragment = fragment;
        this.weatherImage = weatherImage;
        this.countryCode = countryCode;
        this.city = city;
        this.country = country;
    }

    @Override
    public void onClick(View v) {
        if(fragment.isOnline()) {
            APIDataGetterAsyncTask task = new APIDataGetterAsyncTask(fragment, fragment.getContext(), weatherImage);
            task.execute(countryCode, city, country);
        } else {
            Toast.makeText(fragment.getContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }
    }

}
