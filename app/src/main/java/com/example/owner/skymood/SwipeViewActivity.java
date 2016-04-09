package com.example.owner.skymood;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.owner.skymood.adapters.CustomPagerAdapter;
import com.example.owner.skymood.fragments.HourlyWeatherFragment;
import com.example.owner.skymood.fragments.ICommunicatior;
import com.example.owner.skymood.fragments.MoreInfoFragment;

public class SwipeViewActivity extends AppCompatActivity implements ICommunicatior{

    public static final int NUMBER_OF_PAGES = 3;

    CustomPagerAdapter adapter;
    ViewPager pager;

    private String moreInfoJSON;
    private String hourlyJSON;
    private String weeklyJSON;

    private String[] tabs = {"Current", "Hourly", "More info"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter = new CustomPagerAdapter(getSupportFragmentManager(), this);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        android.support.v4.app.Fragment fragment = adapter.getItem(1);
        ((HourlyWeatherFragment)fragment).setData(city, code);
        android.support.v4.app.Fragment fragment2 = adapter.getItem(2);
        ((MoreInfoFragment)fragment2).setInfo(city, code, date, min, max);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.sky_mood:
                intent = new Intent(this, SwipeViewActivity.class);
                startActivity(intent);
                return true;
            case R.id.searched_locations:
                intent = new Intent(this, SearchedLocationsActivity.class);
                startActivity(intent);
                return true;
            case R.id.my_locations:
                intent = new Intent(this, MyLocationsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getMoreInfoJSON() {
        return moreInfoJSON;
    }

    public void setMoreInfoJSON(String moreInfoJSON) {
        this.moreInfoJSON = moreInfoJSON;
    }

    public String getHourlyJSON() {
        return hourlyJSON;
    }

    public void setHourlyJSON(String hourlyJSON) {
        this.hourlyJSON = hourlyJSON;
    }

    public String getWeeklyJSON() {
        return weeklyJSON;
    }

    public void setWeeklyJSON(String weeklyJSON) {
        this.weeklyJSON = weeklyJSON;
    }
}
