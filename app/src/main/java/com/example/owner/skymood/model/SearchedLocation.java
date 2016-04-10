package com.example.owner.skymood.model;

import java.io.Serializable;

/**
 * Created by owner on 05/04/2016.
 */
public class SearchedLocation implements Serializable{
    private long id;
    private String city;
    private String temp;
    private String condition;
    private String moreInfo;
    private String hourlyJSON;
    private String weeklyJSON;
    private String date;
    private String country;
    private String code;
    private String max;
    private String min;
    private String lastUpdate;
    private String icon;
    private String feelsLike;


    public SearchedLocation(String city, String temp, String condition, String moreInfo, String hourlyJSON, String weeklyJSON, String country, String code, String max, String min, String lastUpdate, String icon, String feelsLike) {
        this.city = city;
        this.temp = temp;
        this.condition = condition;
        this.moreInfo = moreInfo;
        this.hourlyJSON = hourlyJSON;
        this.weeklyJSON = weeklyJSON;
        this.country = country;
        this.code = code;
        this.max = max;
        this.min = min;
        this.lastUpdate = lastUpdate;
        this.icon = icon;
        this.feelsLike = feelsLike;
    }

    public SearchedLocation( String city, String temp, String condition, String moreInfo, String hourlyJSON, String weeklyJSON, String date, String country, String code, String max, String min, String lastUpdate, String icon, String feelsLike) {
        this(city, temp, condition, moreInfo, hourlyJSON, weeklyJSON, country, code, max, min, lastUpdate, icon, feelsLike);
        this.date = date;
    }

    public SearchedLocation(long id, String city, String temp, String condition, String moreInfo, String hourlyJSON, String weeklyJSON, String date, String country, String code, String max, String min, String lastUpdate, String icon, String feelsLike) {
        this(city, temp, condition, moreInfo, hourlyJSON, weeklyJSON, date, country, code, max, min, lastUpdate, icon);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getTemp() {
        return temp;
    }

    public String getCondition() {
        return condition;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public String getHourlyJSON() {
        return hourlyJSON;
    }

    public String getWeeklyJSON() {
        return weeklyJSON;
    }

    public String getDate() {
        return date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public String getMax() {
        return max;
    }

    public String getMin() {
        return min;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getIcon() {
        return icon;
    }

    public String getFeelsLike() {
        return feelsLike;
    }
}
