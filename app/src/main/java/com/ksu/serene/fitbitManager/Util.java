package com.ksu.serene.fitbitManager;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Util {
    public static String getCurrentDateTime() {
        String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    public static void setLocale(String lang, Activity activity) { //call this in onCreate()
        Locale myLocale = new Locale(lang);
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        Configuration configuration = activity.getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale(lang));
        activity.getResources().updateConfiguration(configuration, activity.getResources().getDisplayMetrics());

        //Intent refresh = new Intent(this, AndroidLocalize.class);
        //startActivity(refresh);
        //finish();
    }
}
