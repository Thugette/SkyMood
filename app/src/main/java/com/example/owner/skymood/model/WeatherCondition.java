package com.example.owner.skymood.model;

/**
 * Created by owner on 03/04/2016.
 */
public class WeatherCondition {
    private String city;
    private String country;
    private String date;
    private double longitude;
    private double latitude;
    private double temp;
    private double maxTemp;
    private double minTemp;
    private String description; //fcttext_metric (from json)
    private double pressure;
    private double humidity;
    private double visibility;
    private double windSpeed;

    private String iconUrl;
}
