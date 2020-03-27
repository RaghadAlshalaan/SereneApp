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

import com.ksu.serene.controller.main.calendar.PatientAppointmentDetailPage;
import com.ksu.serene.controller.main.calendar.PatientMedicineDetailPage;
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


        //find uri and set cursor there
        if(uri != null){
            cursor = getContentResolver().query(uri, null, null, null, null);
        }


        String description = "", documentID = "", nullRepeatNOCheck="", documentKey;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                description = Reminder.getColumnString(cursor, Reminder.ReminderEntry.KEY_NAME);//grab name of med/app
                documentID = Reminder.getColumnString(cursor, Reminder.ReminderEntry.KEY_DOCUMENT_ID);//get firebase docID for reminder
                nullRepeatNOCheck = Reminder.getColumnString(cursor, Reminder.ReminderEntry.KEY_REPEAT_NO);

            }
            /*if (cursor != null && cursor.moveToNext()){
                documentID = Reminder.getColumnString(cursor, Reminder.ReminderEntry.KEY_DOCUMENT_ID);//get firebase docID for reminder
            }
            if (cursor != null && cursor.moveToLast()){
                nullRepeatNOCheck = Reminder.getColumnString(cursor, Reminder.ReminderEntry.KEY_REPEAT_NO);
            }*/
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Intent action;
        //assign action to a specific page, if repeatno is null, then it is an appointment since it's nonrepeating
        if (nullRepeatNOCheck.isEmpty()){
            documentKey = "AppointmentID";
            action = new Intent(this, PatientAppointmentDetailPage.class);
        } else {
            documentKey = "MedicineID";
            action = new Intent(this, PatientMedicineDetailPage.class);
        }

        //action after user clicks on notification
        action.putExtra(documentKey, documentID);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification note = new NotificationCompat.Builder(this,"Serene_Notification_Channel")
                .setContentTitle("Notification")
                .setContentText(description)
                .setSmallIcon(R.drawable.small_serene_logo)
                .setContentIntent(operation)//click action
                .setAutoCancel(true)
                .build();

        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Serene_Notification_Channel")
                .setSmallIcon(0)
                .setContentTitle("Notification")
                .setContentText(description)
                .setContentIntent(operation)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);*/

        manager.notify(NOTIFICATION_ID, note);

        Log.d("TAG", description+" Notification fired!!");


        //bottom code will execute in every alarm trigger, useful for repeating alarms
        int repeatNo=0;

        if (!(nullRepeatNOCheck.isEmpty()))
            repeatNo = Integer.parseInt(nullRepeatNOCheck);//grab repeatNo if it's not null


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

            PendingIntent CancelOperation =
                    ReminderAlarmService.getReminderPendingIntent(getApplicationContext(), uri);

            alarmmanager.cancel(CancelOperation);
        }

    }
}