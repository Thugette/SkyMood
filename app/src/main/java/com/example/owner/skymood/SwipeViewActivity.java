package com.example.owner.skymood;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.owner.skymood.adapters.CustomPagerAdapter;

public class SwipeViewActivity extends FragmentActivity {

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
}
