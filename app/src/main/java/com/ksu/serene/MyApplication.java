package com.ksu.serene;

import android.app.Application;
import android.content.ContextWrapper;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.Calendar;
import com.pixplicity.easyprefs.library.Prefs;

public class MyApplication extends Application {
    private static GoogleAccountCredential credential;

    private static Calendar client;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the Prefs class
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    public static GoogleAccountCredential getCredential() {
        return credential;
    }

    public static void setCredential(GoogleAccountCredential c) {
        credential = c;
    }

    public static Calendar getClient() {
        return client;
    }

    public static void setClient(Calendar c) {
        client = c;
    }
}
