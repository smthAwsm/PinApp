package com.techtask.xps.pinsapp.Models;

import com.orm.SugarRecord;

/**
 * Created by XPS on 6/12/2016.
 */
public class MarkerModel extends SugarRecord{

    private String ownerId;
    private double latitude;
    private double longtitude;
    private String date;
    private String title;

    public MarkerModel() {
    }

    public MarkerModel(int ownerId, double latitude, double longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public MarkerModel(String ownerId, double latitude, double longtitude, String title, String date) {
        this.ownerId = ownerId;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.date = date;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public double getLongtitude() {
        return longtitude;
    }
    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {

        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
