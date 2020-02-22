package com.ksu.serene.Model;

import java.sql.Time;
import java.util.Date;

public class TherapySession extends Reminder {
    public TherapySession(String id, String name, String day, String time) {
        super(id, name, day, time);
    }
}
