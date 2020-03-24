package com.ksu.serene.model;

public class Medicine extends Reminder {
    private int doze;
    private long period;
    private int reminderInterval;
    private String LastDay, reminderType;


    public Medicine(String id, String name, String day,String LastDay ,String time, int doze, long period, int reminderInterval, String reminderType) {
        super(id, name, day, time);
        this.doze = doze;
        this.period = period;
        this.LastDay = LastDay;
        this.reminderInterval = reminderInterval;
        this.reminderType = reminderType;
    }

    public Medicine(String id, String name, String day,String LastDay ,String time, int doze, long period) {//for calendar fragment use
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

    public String getReminderType() {
        return reminderType; // mins/hours/days/weeks
    }

    public int getReminderInterval() {
        return reminderInterval; // will be displayed next to reminderType in the med detail page
    }


    /*public static final class MedicineEntry implements BaseColumns {

     *//*public static final String KEY_NAME = "name";
        public static final String KEY_DATE = "date";
        public static final String KEY_TIME = "time";*//*
        public static final String KEY_DOSE = "dose";
        public static final String KEY_REPEAT_NO = "repeat_no";
        public static final String KEY_PERIOD = "period";

    }*/

}
