package com.ksu.serene.fitbitManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util {
    public static String getCurrentDateTime() {
        String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }
}
