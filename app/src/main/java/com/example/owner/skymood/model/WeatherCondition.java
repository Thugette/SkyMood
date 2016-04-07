package com.example.owner.skymood.model;

import java.io.Serializable;

/**
 * Created by owner on 03/04/2016.
 */
public class WeatherCondition implements Serializable{
//    private String city;
//    private String country;
    private String day;
    private String date;
//    private double longitude;
//    private double latitude;
    private double temp;
    private double feels;
    private double maxTemp;
    private double minTemp;
   // private String description; //fcttext_metric (from json)
    private double pressure;
    private double humidity;
    private double visibility;
    private double windSpeed;
    private String sunrise;
    private String sunset;
    private String moonPhase;
    private double uv;

   // private String iconUrl;


    public WeatherCondition(String moonPhase, String day, String date, String sunset, double uv, double temp, double feels, double maxTemp, double minTemp, double pressure, double humidity, double visibility, double windSpeed, String sunrise) {
        this.moonPhase = moonPhase;
        this.date = date;
        this.day = day;
        this.sunset = sunset;
        this.temp = temp;
        this.feels = feels;
        this.uv = uv;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.sunrise = sunrise;
    }

    public double getTemp() {
        return temp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getVisibility() {
        return visibility;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getMoonPhase() {
        return moonPhase;
    }

    public double getFeels() {
        return feels;
    }

    public double getUv() {
        return uv;
    }
}
