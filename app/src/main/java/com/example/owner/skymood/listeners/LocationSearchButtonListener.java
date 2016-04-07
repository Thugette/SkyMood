package com.example.owner.skymood.listeners;

import android.view.View;
import android.widget.Toast;

import com.example.owner.skymood.fragments.CurrentWeatherFragment;

/**
 * Created by Golemanovaa on 8.4.2016 г..
 */
public class LocationSearchButtonListener implements View.OnClickListener {

    private CurrentWeatherFragment fragment;

    public LocationSearchButtonListener(CurrentWeatherFragment fragment){
        this.fragment = fragment;
    }

    @Override
    public void onClick(View v) {
        if(fragment.isOnline()){
            fragment.findLocation();
        } else {
            Toast.makeText(fragment.getContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }
    }

}
