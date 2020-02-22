package com.ksu.serene.Model;

import java.sql.Time;
import java.util.Date;

public class Location {
    private String name;
    private int timesVisited;
    private String AL_level;
    private long latitude, longitude;
    private Time arrivalTime, leaveTime;
    private Date date;

    private long daysBetween;

    public Location(String name, String AL_level){
        this.name = name;
        this.AL_level = AL_level;
    }//constructor

    public Location(String name, String AL_level, long daysBetween){
        this.name = name;
        this.AL_level = AL_level;
        this.daysBetween = daysBetween;
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
