package com.example.owner.skymood.listeners;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.owner.skymood.fragments.CurrentWeatherFragment;

import java.util.ArrayList;

/**
 * Created by Golemanovaa on 8.4.2016 г..
 */
public class SearchButtonListener implements View.OnClickListener {

    private Context context;
    private CurrentWeatherFragment fragment;
    private AutoCompleteTextView writeCityEditText;
    private InputMethodManager keyboard;

    public SearchButtonListener(Context context, CurrentWeatherFragment fragment, AutoCompleteTextView writeCityEditText, InputMethodManager keyboard){
        this.context = context;
        this.fragment = fragment;
        this.writeCityEditText = writeCityEditText;
        this.keyboard = keyboard;
    }

    @Override
    public void onClick(View v) {
        if(fragment.isOnline()) {
            if (writeCityEditText.getVisibility() == View.GONE) {
                fragment.changeVisibility(View.GONE);

                Animation slide = new AnimationUtils().loadAnimation(context, android.R.anim.fade_in);
                slide.setDuration(1000);
                writeCityEditText.startAnimation(slide);
                writeCityEditText.setVisibility(View.VISIBLE);
                writeCityEditText.setFocusable(true);
                writeCityEditText.requestFocus();

                keyboard = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(writeCityEditText, 0);
            } else {
                writeCityEditText.setVisibility(View.GONE);
                keyboard.hideSoftInputFromWindow(writeCityEditText.getWindowToken(), 0);
                fragment.changeVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(context, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }
    }

}
