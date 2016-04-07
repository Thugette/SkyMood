package com.example.owner.skymood;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.owner.skymood.adapters.CustomPagerAdapter;
import com.example.owner.skymood.fragments.HourlyWeatherFragment;
import com.example.owner.skymood.fragments.ICommunicatior;
import com.example.owner.skymood.fragments.MoreInfoFragment;
import com.example.owner.skymood.model.WeatherCondition;

import java.util.ArrayList;
import java.util.HashMap;

public class SwipeViewActivity extends FragmentActivity implements ICommunicatior{

    public static final int NUMBER_OF_PAGES = 3;

    CustomPagerAdapter adapter;
    ViewPager pager;


    WeatherCondition weatherInfo;
    String city;
    String code;


    private String[] tabs = {"Current", "Hourly", "More info"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_view);


        adapter = new CustomPagerAdapter(getSupportFragmentManager(), this);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
//        WeatherCondition weather = new WeatherCondition("moon", "dsa", 2,12, 23, 23, 23, 25, 45, 78, 45, "ads");
//        setInfo(weather);
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }

    }

    @Override
    public void getNewLocation(String city, String code) {
            adapter.setLocation(city, code);
    }

    @Override
    public void setInfo(String city, String code, String min, String max, String date) {
        adapter.setInfoWeather(city, code, min, max, date);
    }


}
