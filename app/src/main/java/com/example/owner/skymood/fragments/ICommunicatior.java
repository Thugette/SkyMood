package com.example.owner.skymood.fragments;

import com.example.owner.skymood.model.WeatherCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by owner on 06/04/2016.
 */
public interface ICommunicatior {

    void getNewLocation(String city, String code);
    void setInfo(String city, String code, String min, String max, String date);
}
