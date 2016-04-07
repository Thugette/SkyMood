package com.example.owner.skymood.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.owner.skymood.SwipeViewActivity;
import com.example.owner.skymood.fragments.CurrentWeatherFragment;
import com.example.owner.skymood.fragments.HourlyWeatherFragment;
import com.example.owner.skymood.fragments.MoreInfoFragment;
import com.example.owner.skymood.fragments.Swideable;
import com.example.owner.skymood.model.WeatherCondition;

/**
 * Created by Golemanovaa on 4.4.2016 г..
 */
public class CustomPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    WeatherCondition infoWeather;
    String city;
    String code;
    String min;
    String max;
    String date;

    public CustomPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Swideable fragment = null;
        switch(position){
            case 0:
                fragment = new CurrentWeatherFragment();
                fragment.setContext(context);
                return (Fragment) fragment;

            case 1:
                //return new HourlyWeatherFragment();
                fragment = new HourlyWeatherFragment();
                fragment.setContext(context);
                ((HourlyWeatherFragment)fragment).setData(city, code);
                return (Fragment) fragment;
            case 2:
                //return MoreInfoOnWeatherConditionFragment();
                fragment = new MoreInfoFragment();
                fragment.setContext(context);
                ((MoreInfoFragment)fragment).setInfo(city, code, date, min, max);
                return (Fragment) fragment;
        }
        return (Fragment)fragment;
    }

    @Override
    public int getCount() {
        return SwipeViewActivity.NUMBER_OF_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {  switch(position){
        case 0:
            return "CURRENT";
        case 1:
            return "HOURLY";
        case 2:
            return "MORE INFO";
    }
        return null;
    }

    public void setInfoWeather(String city, String code, String min, String max, String date) {
        this.city = city;
        this.code = code;
        this.min = min;
        this.max = max;
        this.date = date;
    }

    public void setLocation(String city, String code){
        this.city = city;
        this.code = code;
    }
}
