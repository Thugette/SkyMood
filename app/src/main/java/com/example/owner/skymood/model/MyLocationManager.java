package com.example.owner.skymood.model;

import android.content.Context;

import com.example.owner.skymood.model.DAO.MyLocationDAO;

import java.util.ArrayList;

/**
 * Created by owner on 05/04/2016.
 */
public class MyLocationManager {
    private static MyLocationManager ourInstance;
    private MyLocationDAO locationDAO;

    private MyLocationManager(Context context) {
        this.locationDAO = MyLocationDAO.getInstance(context);
    }

    public static MyLocationManager getInstance(Context context) {
        if(ourInstance == null)
            ourInstance = new MyLocationManager(context);
        return ourInstance;
    }

    public ArrayList<String> getAllMyLocations(){
        return locationDAO.getAllMyLocations();
    }

    public long insertMyLocation(String city, String country){
        return locationDAO.insertMyLocation(city, country);
    }

    public String selectMyCity(String city) {
        return locationDAO.selectMyCity(city);
    }

}
