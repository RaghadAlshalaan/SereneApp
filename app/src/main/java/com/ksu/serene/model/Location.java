package com.ksu.serene.model;

import java.sql.Time;
import java.util.Date;

public class Location {
    private String name;
    private int timesVisited;
    private String AL_level;
    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double longitude;
    private Time arrivalTime, leaveTime;
    private Date date;

    public String getNearestLoc() {
        return nearestLoc;
    }

    public void setNearestLoc(String nearestLoc) {
        this.nearestLoc = nearestLoc;
    }

    private String nearestLoc;

    private long daysBetween;

    public Location(String name, String AL_level){
        this.name = name;
        this.AL_level = AL_level;
    }//constructor

    public Location(String name, String AL_level, long daysBetween , double latitude, double longitude, String nearest){
        this.name = name;
        this.AL_level = AL_level;
        this.daysBetween = daysBetween;
        this.latitude = latitude;
        this.longitude = longitude;

        if (nearest != null)
        nearestLoc = nearest;

    }//constructor


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAL_level() {
        return AL_level;
    }

    public void setAL_level(String AL_level) {
        this.AL_level = AL_level;
    }

    public long getDaysBetween() {
        return daysBetween;
    }
}
