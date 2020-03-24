package com.ksu.serene.model;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Date;

public class Reminder {
    private String id;
    private String name;
    private String day;
    private String time;
    private Date calenderdate;

    public static final String CONTENT_AUTHORITY = "com.ksu.serene.Controller";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_VEHICLE = "reminder-path";

    public Reminder(String id, String name, String day, String time) {
        this.id = id;
        this.name = name;
        this.day = day;
        this.time = time;
    }

    public Reminder (int year, int month, int day) {
        calenderdate.setDate(day);
        calenderdate.setMonth(month);
        calenderdate.setYear(year);
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

    public Date getCalenderdate() {
        return calenderdate;
    }

    public static final class ReminderEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VEHICLE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE;

        public final static String TABLE_NAME = "reminder";

        public final static String _ID = BaseColumns._ID;

        public static final String KEY_NAME = "name";
        public static final String KEY_DATE = "date";
        public static final String KEY_TIME = "time";
        public static final String KEY_DOSE = "dose";
        public static final String KEY_REPEAT_TIME = "repeat_time";
        public static final String KEY_REPEAT_TYPE = "repeat_type";
        public static final String KEY_REPEAT_NO = "repeat_no";

    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }
}
