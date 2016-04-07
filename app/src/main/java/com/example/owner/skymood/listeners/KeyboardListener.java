package com.example.owner.skymood.listeners;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.skymood.fragments.CurrentWeatherFragment;

import java.util.HashMap;

/**
 * Created by Golemanovaa on 8.4.2016 г..
 */
public class KeyboardListener implements TextView.OnEditorActionListener {

    private CurrentWeatherFragment fragment;
    private AutoCompleteTextView writeCityEditText;
    private InputMethodManager keyboard;
    private HashMap<String, String> cities;

    public KeyboardListener(CurrentWeatherFragment fragment, AutoCompleteTextView writeCityEditText,
                            InputMethodManager keyboard, HashMap<String, String> cities){
        this.fragment = fragment;
        this.writeCityEditText = writeCityEditText;
        this.keyboard = keyboard;
        this.cities = cities;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if(writeCityEditText != null && !writeCityEditText.getText().toString().isEmpty()
                && writeCityEditText.getText().toString().contains(",")) {
            String location = writeCityEditText.getText().toString();
            String countryCode = cities.get(location);
            String[] parts = location.split(",");
            String city = parts[0];
            String country = parts[1].trim();
            fragment.setLocation(city, country, countryCode);
            fragment.getWeatherInfoByCity(city);
        } else {
            Toast.makeText(fragment.getContext(), "You must specify a country", Toast.LENGTH_SHORT).show();
            writeCityEditText.setVisibility(View.GONE);
            keyboard.hideSoftInputFromWindow(writeCityEditText.getWindowToken(), 0);
            fragment.changeVisibility(View.VISIBLE);
        }
        keyboard.hideSoftInputFromWindow(writeCityEditText.getWindowToken(), 0);
        return false;
    }

}
