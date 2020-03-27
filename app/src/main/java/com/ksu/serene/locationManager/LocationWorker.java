package com.ksu.serene.locationManager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ksu.serene.fitbitManager.Util;

public class LocationWorker extends Worker {

    public LocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);



    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("AppInfo", "LocationWorker - doWork() - " + Util.getCurrentDateTime());
        Context context = getApplicationContext();

        Data outputData = new Data.Builder()
                .putString("msg", "This is output message")
                .build();

        return Result.success(outputData);
    }

}
