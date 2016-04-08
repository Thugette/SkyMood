package com.example.owner.skymood.listeners;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.owner.skymood.asyncTasks.APIDataGetterAsyncTask;
import com.example.owner.skymood.fragments.CurrentWeatherFragment;

/**
 * Created by Golemanovaa on 8.4.2016 г..
 */
public class MyLocationsSpinnerListener implements AdapterView.OnItemSelectedListener {

    private CurrentWeatherFragment fragment;
    private ImageView weatherImage;

    public MyLocationsSpinnerListener(CurrentWeatherFragment fragment, ImageView weatherImage){
        this.fragment = fragment;
        this.weatherImage = weatherImage;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //TODO correct logic, getting from DB
        if (!((String) parent.getItemAtPosition(position)).equals("My Locations")) {
            if (fragment.isOnline()) {
                fragment.setCity((String) parent.getItemAtPosition(position));
                APIDataGetterAsyncTask task = new APIDataGetterAsyncTask(fragment, fragment.getContext(), weatherImage);
                //TODO remove next line
                String countryCode = "BG";

                task.execute(countryCode, "Plovdiv", "Bulgaria");
            } else {
                //TODO check if there is information in DB

                //if there isn't info in db
                Toast.makeText(fragment.getContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

}
