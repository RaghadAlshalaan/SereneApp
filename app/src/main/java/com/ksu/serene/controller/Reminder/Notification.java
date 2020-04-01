package com.ksu.serene.controller.Reminder;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Notification {

    private String name;
    private String type;
    private String time;
    private String documentID;//document id for details page when notification is clicked
    private String notificationID;
    private boolean read;

    public Notification(String name, String type, String time, String documentID) {//receive from reminderalarmservice
        this.name = name;
        this.type = type;
        this.time = time;
        this.documentID = documentID;
        read = false;
    }
    public Notification(String name, String type, String time, String documentID, String notificationID, boolean read) {//read stored notification from firebase
        this.name = name;
        this.type = type;
        this.time = time;
        this.documentID = documentID;
        this.notificationID = notificationID;
        this.read = read;
    }


    public String getMessage() {
        switch (type) {
            case "med":
                return "Don't forget to take " + name;
            case "app":
                return "Don't forget to attend " + name;
        }
        return "";
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getDocumentID() {
        return documentID;
    }

    public static void addNotification(Notification n) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();
        String notificationID = randomString(20);

        Map<String, Object> not = new HashMap<>();
        not.put("name", n.getName());
        not.put("type", n.getType());
        not.put("time", n.getTime().substring(11));
        not.put("day", n.getTime().substring(0,10));
        not.put("notificationID", notificationID);
        not.put("documentID", n.getDocumentID());
        not.put("read", n.isRead());
        not.put("userID", userID);

// Add a new document with a generated ID

        db.collection("Notifications").document(notificationID)
                .set(not)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public static String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public String getNotificationID() {
        return notificationID;
    }
}