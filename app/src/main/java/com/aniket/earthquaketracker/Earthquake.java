package com.aniket.earthquaketracker;

public class Earthquake {
    private double magnitude;
    private String place;
    private String date;
    private String time;
    private String url;

    public Earthquake(double magnitude, String place, String date, String time, String url) {
        this.magnitude = magnitude;
        this.place = place;
        this.date = date;
        this.time = time;
        this.url = url;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
