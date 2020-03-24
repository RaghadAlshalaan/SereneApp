package com.ksu.serene.controller.Reminder;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;


public class AlarmScheduler extends Activity {

    /**
     * Schedule a reminder alarm at the specified time for the given task.
     *
     * @param context Local application or activity context

     * @param reminderTask Uri referencing the task in the content provider
     */

    public void setAlarm(Context context, long alarmTime, Uri reminderTask) { //for appointments
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context, reminderTask); //create notification here


        if (Build.VERSION.SDK_INT >= 23) {

            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation); //schedule when the notification is triggered

           /* //get period of uri, decrease period by one day and assign again to uri, set another next alarm
            int period=0;
            Cursor cursor = null;
            //find uri and set cursor there
            if(reminderTask != null){
                cursor = context.getContentResolver().query(reminderTask, null, null, null, null);
            }

            try {
                if (cursor != null && cursor.moveToLast()) { //should move to period column
                    period = Integer.parseInt(Reminder.getColumnString(cursor, Reminder.ReminderEntry.KEY_PERIOD));//grab period if it's not null
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            if (period>0){//if period is more than 0, schedule another alarm
                --period;
                ContentValues updateValues = new ContentValues();
                updateValues.put(Reminder.ReminderEntry.KEY_PERIOD,period);//new value of period

                String selectionClause = Reminder.ReminderEntry.KEY_PERIOD +  " LIKE ?";//column value to update in URI

                int rowsUpdated = context.getContentResolver().update(reminderTask, updateValues, selectionClause,null);
                if (rowsUpdated==1)
                    setAlarm(context, alarmTime+60000, reminderTask);//set another alarm for next minute

            }//re-set alarm for repeating reminders*/


        } else if (Build.VERSION.SDK_INT >= 19) {

            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        } else {

            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        }
    }

    public void setRepeatAlarm(Context context, long alarmTime, Uri reminderTask, long RepeatTime) { //for medicine reminders
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context, reminderTask);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation);//repeatTime = interval


    }
    //under delete reminder
    public void cancelAlarm(Context context, Uri reminderTask) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context, reminderTask);

        manager.cancel(operation);

    }

}