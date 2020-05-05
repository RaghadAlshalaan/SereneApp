package com.ksu.serene.controller.main.drafts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.ksu.serene.R;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.controller.liveChart.utils.Utils;


import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import www.sanju.motiontoast.MotionToast;


public class AddVoiceDraftPage extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO = 0;
    private static final int MY_PERMISSION_REQUEST_ID = 1;
    private static final String AUDIO_FILE_PATH =
            Environment.getExternalStorageDirectory().getPath() + "/recorded_audio.wav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_recording);
        getSupportActionBar().hide();

        SharedPreferences sp = getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
        String preferred_lng = sp.getString("PREFERRED_LANGUAGE", "en");
        Utils.setLocale(preferred_lng, this);


        Util.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        Util.requestPermission(this, Manifest.permission.RECORD_AUDIO);

    }// onCreate()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (resultCode == RESULT_OK) {
                //todo: remove toast
                //Toast.makeText(this, "Audio recorded successfully!", Toast.LENGTH_SHORT).show();
                Resources res = getResources();
                String text = String.format(res.getString(R.string.audio_recorded_successfully));

                MotionToast.Companion.darkToast(
                        AddVoiceDraftPage.this,
                        text,
                        MotionToast.Companion.getTOAST_SUCCESS(),
                        MotionToast.Companion.getGRAVITY_BOTTOM(),
                        MotionToast.Companion.getLONG_DURATION(),
                        ResourcesCompat.getFont( AddVoiceDraftPage.this, R.font.montserrat));

                finish();
            } else if (resultCode == RESULT_CANCELED) {
               // requestPermission();
                //Toast.makeText(this, "Audio was not recorded", Toast.LENGTH_SHORT).show();

            }
        }
    }// onActivityResult()

    // Override in Activity to check the request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_ID: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted, start OmRecorder
                }
                return;
            }
        }
    }// onRequestPermissionsResult

    public void recordAudio(View v) {

            AndroidAudioRecorder.with(this)
                    // Required
                    .setFilePath(AUDIO_FILE_PATH)
                    .setColor(ContextCompat.getColor(this, R.color.recorder_bg))
                    .setRequestCode(REQUEST_RECORD_AUDIO)

                    // Optional
                    .setSource(AudioSource.MIC)
                    .setChannel(AudioChannel.STEREO)
                    .setSampleRate(AudioSampleRate.HZ_48000)
                    .setAutoStart(false)
                    .setKeepDisplayOn(true)
                    .record();

    }

    public boolean requestPermission(){
        // Check if permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_ID);

            return true;
        } else {
            // already granted, start OmRecorder

            return true;

        }
    }// requestPermission

}