package com.ksu.serene.Model;

import java.sql.Time;
import java.util.Date;

public class TherapySession extends Reminder {
    public TherapySession(String id, String name, Date day, Date time) {
        super(id, name, day, time);
    }
}
