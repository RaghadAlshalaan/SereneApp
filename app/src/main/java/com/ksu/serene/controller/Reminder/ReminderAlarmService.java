package com.ksu.serene.controller.Reminder;


import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.ksu.serene.model.Medicine;
import com.ksu.serene.model.Reminder;
import com.ksu.serene.R;


public class ReminderAlarmService extends IntentService {
    private static final String TAG = ReminderAlarmService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;

    Cursor cursor;
    //This is a deep link intent, and needs the task stack
    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.setData(uri);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri uri = intent.getData(); // medicine/appointment uri

        //action after user clicks on notification
        /*Intent action = new Intent(this, Add_Medicine_Page.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);*/

        //find uri and set cursor there
        if(uri != null){
            cursor = getContentResolver().query(uri, null, null, null, null);
        }


        String description = "";
        try {
            if (cursor != null && cursor.moveToFirst()) {
                description = Reminder.getColumnString(cursor, Reminder.ReminderEntry.KEY_NAME);//grab name of med/app
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Notification note = new NotificationCompat.Builder(this,"Serene_Notification_Channel")
                .setContentTitle("Notification")
                .setContentText(description)
                .setSmallIcon(R.drawable.small_serene_logo)
                //.setContentIntent(operation)
                .setAutoCancel(true)
                .build();

        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Serene_Notification_Channel")
                .setSmallIcon(0)
                .setContentTitle("Notification")
                .setContentText(description)
                .setContentIntent(operation)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);*/

        manager.notify(NOTIFICATION_ID, note);
        Log.d("TAG", "Notification fired!!");


        //bottom code will execute in every alarm trigger, useful for repeating alarms
        int repeatNo=0;
        String nullFieldChecker;

        if(uri != null){
            cursor = getContentResolver().query(uri, null, null, null, null);
        }

        try {
            if (cursor != null && cursor.moveToLast()) { //should move to repeatNo column
                nullFieldChecker = Reminder.getColumnString(cursor, Reminder.ReminderEntry.KEY_REPEAT_NO);
                if (nullFieldChecker!=null){
                    repeatNo = Integer.parseInt(nullFieldChecker);//grab repeatNo if it's not null
                }
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            repeatNo=0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (repeatNo>0){//if repeatNo is more than 0, schedule another alarm
            --repeatNo;
            ContentValues updateValues = new ContentValues();
            updateValues.put(Reminder.ReminderEntry.KEY_REPEAT_NO,repeatNo);//new value of repeatNo

            String selectionClause = Reminder.ReminderEntry.KEY_REPEAT_NO +  " LIKE ?";//column value to update in URI

            int rowsUpdated = getContentResolver().update(uri, updateValues, selectionClause,null);
            //repeat

        }//re-set alarm for repeating reminders
        else{// if repeatNo=0
            //cancel reminder
            AlarmManager alarmmanager = AlarmManagerProvider.getAlarmManager(getApplicationContext());

            PendingIntent operation =
                    ReminderAlarmService.getReminderPendingIntent(getApplicationContext(), uri);

            alarmmanager.cancel(operation);
        }

    }
}