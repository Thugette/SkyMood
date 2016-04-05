package com.example.owner.skymood.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.owner.skymood.location.NetworkLocationListener;

/**
 * Created by Golemanovaa on 5.4.2016 г..
 */
public class LocationPreference {

    private static LocationPreference instance = null;
    public static final String LOCATION = "location";
    SharedPreferences pref;
    Context context;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "SkyModePreferences";

    private LocationPreference(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static LocationPreference getInstance(Context context){
        if(instance == null){
            instance = new LocationPreference(context);
        }
        return instance;
    }

    public void setPreferredLocation(String location){
        editor.putString(LOCATION, location);
        editor.commit();
    }

    public boolean isSetLocation(){
        return pref.contains(LOCATION);
    }

    public String getLocation(){
        return pref.getString(LOCATION, null);
    }

}
