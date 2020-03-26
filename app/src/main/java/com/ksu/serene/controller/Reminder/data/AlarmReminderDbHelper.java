package com.ksu.serene.controller.Reminder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ksu.serene.model.Reminder;


public class AlarmReminderDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alarmreminder.db";

    private static final int DATABASE_VERSION = 1;

    public AlarmReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the reminder table
        String SQL_CREATE_ALARM_TABLE =  "CREATE TABLE " + Reminder.ReminderEntry.TABLE_NAME + " ("
                + Reminder.ReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Reminder.ReminderEntry.KEY_DOCUMENT_ID + " TEXT NOT NULL, "
                + Reminder.ReminderEntry.KEY_NAME + " TEXT NOT NULL, "
                + Reminder.ReminderEntry.KEY_DATE + " TEXT NOT NULL, "
                + Reminder.ReminderEntry.KEY_TIME + " TEXT NOT NULL, "
                + Reminder.ReminderEntry.KEY_DOSE + " TEXT NOT NULL, "
                + Reminder.ReminderEntry.KEY_REPEAT_TIME + " TEXT NOT NULL, "//intervals
                + Reminder.ReminderEntry.KEY_REPEAT_TYPE + " TEXT NOT NULL, "
                + Reminder.ReminderEntry.KEY_REPEAT_NO + " TEXT NOT NULL " + " );";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_ALARM_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
