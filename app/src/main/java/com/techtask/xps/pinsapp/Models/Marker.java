package com.techtask.xps.pinsapp.Models;

import com.orm.SugarRecord;

/**
 * Created by XPS on 6/12/2016.
 */
public class Marker extends SugarRecord{

    private int ownerId;
    private double latitude;
    private double longtitude;
    private String title;

    public Marker() {
    }

    public Marker(int ownerId,double latitude,double longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public Marker(int ownerId,double latitude,double longtitude,String title) {
        this.latitude = latitude;
        this.longtitude = longtitude;
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



}
