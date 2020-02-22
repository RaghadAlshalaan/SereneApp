package com.ksu.serene.Model;

import java.sql.Time;
import java.util.Date;

public class Reminder {
    private String id;
    private String name;
    private String day;
    private String time;

    public Reminder(String id, String name, String day, String time) {
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
