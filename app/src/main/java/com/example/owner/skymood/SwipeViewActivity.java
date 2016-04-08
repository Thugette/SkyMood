package com.example.owner.skymood;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.example.owner.skymood.adapters.CustomPagerAdapter;
import com.example.owner.skymood.fragments.HourlyWeatherFragment;
import com.example.owner.skymood.fragments.ICommunicatior;
import com.example.owner.skymood.fragments.MoreInfoFragment;

public class SwipeViewActivity extends FragmentActivity implements ICommunicatior{

    public static final int NUMBER_OF_PAGES = 3;

    CustomPagerAdapter adapter;
    ViewPager pager;


    private String[] tabs = {"Current", "Hourly", "More info"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_view);

        adapter = new CustomPagerAdapter(getSupportFragmentManager(), this);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
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
    public void setInfo(String city, String code, String min, String max, String date) {
        Log.e("didi", city + " "+ code);
        android.support.v4.app.Fragment fragment = adapter.getItem(1);
        ((HourlyWeatherFragment)fragment).setData(city, code);
        android.support.v4.app.Fragment fragment2 = adapter.getItem(2);
        ((MoreInfoFragment)fragment2).setInfo(city, code, date, min, max);
    }
}
