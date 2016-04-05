package com.example.owner.skymood.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.owner.skymood.location.NetworkLocationListener;

/**
 * Created by Golemanovaa on 5.4.2016 г..
 */
public class LocationPreference {

    private static LocationPreference instance = null;
    SharedPreferences pref;
    Context context;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "SkyModePreferences";
    public static final String LOCATION = "location";
    public static final String TEMPERATURE = "temperature";
    public static final String CONDITION = "condition";
    public static final String MORE_INFO = "more info";
    public static final String LAST_UPDATE = "last update";
    public static final String ICON = "icon";

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

    public void setPreferredLocation(String location, String temperature, String condition, String moreInfo, String lastUpdate, String icon){
        editor.putString(LOCATION, location);
        editor.putString(TEMPERATURE, temperature);
        editor.putString(CONDITION, condition);
        editor.putString(MORE_INFO, moreInfo);
        editor.putString(LAST_UPDATE, lastUpdate);
        editor.putString(ICON, icon);
        editor.commit();
    }

    public boolean isSetLocation(){
        return pref.contains(LOCATION);
    }

    public String getTemperature(){
        return pref.getString(TEMPERATURE, null);
    }

    public String getCondition(){
        return pref.getString(CONDITION, null);
    }

    public String getMoreInfo(){
        return pref.getString(MORE_INFO, null);
    }

    public String getLastUpdate(){
        return pref.getString(LAST_UPDATE, null);
    }

    public String getIcon(){
        return pref.getString(ICON, null);
    }

    public String getLocation(){
        return pref.getString(LOCATION, null);
    }



}
