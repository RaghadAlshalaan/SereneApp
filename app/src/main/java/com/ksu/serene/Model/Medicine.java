package com.ksu.serene.Model;

import java.sql.Time;
import java.util.Date;

public class Medicine extends Reminder {
    private int doze;
    private int period;

    public Medicine(String id, String name, Date day, Time time, int doze, int period) {
        super(id, name, day, time);
        this.doze = doze;
        this.period = period;
    }

    public int getDoze() {
        return doze;
    }

    public int getPeriod() {
        return period;
    }
}
