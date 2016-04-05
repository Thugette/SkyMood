package com.example.owner.skymood.model;

/**
 * Created by owner on 05/04/2016.
 */
public class SearchedLocation {
    private long id;
    private String city;
    private String temp;
    private String condition;
    private String moreInfo;
    private String hourlyJSON;
    private String weeklyJSON;
    private String date;

    public SearchedLocation(String city, String temp, String condition, String moreInfo, String hourlyJSON, String weeklyJSON, String date) {
        this.city = city;
        this.temp = temp;
        this.condition = condition;
        this.moreInfo = moreInfo;
        this.hourlyJSON = hourlyJSON;
        this.weeklyJSON = weeklyJSON;
        this.date = date;
    }

    public SearchedLocation(long id, String city, String temp, String condition, String moreInfo, String hourlyJSON, String weeklyJSON, String date) {
        this(city, temp, condition, moreInfo, hourlyJSON, weeklyJSON, date);
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
}
