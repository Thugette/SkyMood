package com.example.owner.skymood.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.example.owner.skymood.R;
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
    private String city;
    private String code;
    private String min;
    private String max;
    private String date;
    private int id = 0;
    private FragmentManager mFragmentManager;
    private Swideable[] fragmentsArray = new Swideable[getCount()];

    public CustomPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        if(fragmentsArray[position]!=null)
            return (Fragment)fragmentsArray[position];
        Swideable fragment = null;
        switch(position){
            case 0:
                fragment = new CurrentWeatherFragment();
                fragment.setContext(context);
                break;

            case 1:
                fragment = new HourlyWeatherFragment();
                fragment.setContext(context);
                break;
            case 2:
                //return MoreInfoOnWeatherConditionFragment();
                fragment = new MoreInfoFragment();
                fragment.setContext(context);
                break;
        }
        fragmentsArray[position] =  fragment;
        return (Fragment)fragment;
    }

    @Override
    public int getCount() {
        return SwipeViewActivity.NUMBER_OF_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {  switch(position){
        case 0:
            return "\uD83C\uDF08 CURRENT";
        case 1:
            return "⛅ HOURLY";
        case 2:
            return "⚡ MORE INFO";
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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public Object instantiateItem(ViewGroup container, int position) {
        Swideable fragment = (Swideable) super.instantiateItem(container, position);
        fragmentsArray[position] = fragment;
        return fragment;
    }
}
