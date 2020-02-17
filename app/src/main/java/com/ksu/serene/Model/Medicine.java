package com.ksu.serene.Model;

import java.sql.Time;
import java.util.Date;

public class Medicine extends Reminder {
    private int doze;
    private int period;
    private Date lastDay;

    public Medicine(String id, String name, Date day,Date lastDay ,Time time, int doze, int period) {
        super(id, name, day, time);
        this.doze = doze;
        this.period = period;
        this.lastDay = lastDay;
    }

    public int getDoze() {
        return doze;
    }

    public int getPeriod() {
        return period;
    }

    public Date getLastDay() {
        return lastDay;
    }
}
