package com.example.owner.skymood.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.owner.skymood.SwipeViewActivity;
import com.example.owner.skymood.fragments.CurrentWeatherFragment;
import com.example.owner.skymood.fragments.HourlyWeatherFragment;
import com.example.owner.skymood.fragments.Swideable;

/**
 * Created by Golemanovaa on 4.4.2016 г..
 */
public class CustomPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;

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
                break;

            case 1:
                //return new HourlyWeatherFragment();
                fragment = new HourlyWeatherFragment();
                fragment.setContext(context);
                break;
            case 2:
                //return MoreInfoOnWeatherConditionFragment();
                fragment = new CurrentWeatherFragment();
                fragment.setContext(context);
                break;
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
}
