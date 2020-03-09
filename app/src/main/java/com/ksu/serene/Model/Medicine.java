package com.ksu.serene.Model;

import java.sql.Time;
import java.util.Date;

public class Medicine extends Reminder {
    private int doze;
    private long period;
    private String LastDay;

    public Medicine(String id, String name, String day,String LastDay ,String time, int doze, long period) {
        super(id, name, day, time);
        this.doze = doze;
        this.period = period;
        this.LastDay = LastDay;
    }

    public int getDoze() {
        return doze;
    }

    public long getPeriod() {
        return period;
    }

    public String getLastDay() {
        return LastDay;
    }
}
