package com.example.owner.skymood.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import com.example.owner.skymood.asyncTasks.AutoCompleteStringFillerAsyncTask;
import com.example.owner.skymood.fragments.CurrentWeatherFragment;

/**
 * Created by Golemanovaa on 8.4.2016 г..
 */
public class TextChangedListener implements TextWatcher {

    private CurrentWeatherFragment fragment;
    private AutoCompleteTextView writeCityEditText;

    public TextChangedListener(CurrentWeatherFragment fragment, AutoCompleteTextView writeCityEditText){
        this.fragment = fragment;
        this.writeCityEditText = writeCityEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        int chars = writeCityEditText.getText().toString().length();
        if(chars >= 3){
            AutoCompleteStringFillerAsyncTask filler = new AutoCompleteStringFillerAsyncTask(fragment, fragment.getContext());
            filler.execute(writeCityEditText.getText().toString());
        }
    }

}
