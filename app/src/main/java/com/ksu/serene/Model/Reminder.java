package com.ksu.serene.Model;

import java.sql.Time;
import java.util.Date;

public class Reminder {
    private String id;
    private String name;
    private Date day;
    private Date time;

    public Reminder(String id, String name, Date day, Date time) {
        this.id = id;
        this.name = name;
        this.day = day;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
