package com.ksu.serene.fitbitManager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ServiceWorker extends androidx.work.Worker {


    public ServiceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("AppInfo", "DailyWorker - doWork() - " + Util.getCurrentDateTime());

        Data outputData = new Data.Builder()
                .putString("msg", "This is output message")
                .build();

        return Result.success(outputData);
    }

}
