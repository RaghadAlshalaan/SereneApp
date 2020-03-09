package com.ksu.serene.controller.main.drafts;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.ksu.serene.model.VoiceDraft;
import com.ksu.serene.R;

import java.util.concurrent.TimeUnit;

public class CustomAudioDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Context context;
    public Dialog d;
    public SeekBar seekBar;
    public ImageView pause;
    public ImageView forward;
    public ImageView backward;
    public Uri audioUri;
    public String draftId;
    public TextView speed;
    public MediaPlayer mediaPlayer;
    public TextView remainingTime;
    public TextView currentTime;
    public String title;
    public TextView close;
    public TextView titleTxt;
    public TextView closeTxt;
    public com.ksu.serene.controller.main.drafts.voiceDraftAdapter voiceDraftAdapter;


    public CustomAudioDialogClass(Context a, VoiceDraft draft) {
        super(a);
        context = a;
        audioUri = Uri.parse(draft.getAudio());
        title = draft.getTitle();
        draftId = draft.getId();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.play_draft_audio_dialog);

        //init
        seekBar = findViewById(R.id.seekbar);
        pause = findViewById(R.id.pause);
        forward = findViewById(R.id.forward);
        backward = findViewById(R.id.backward);
        speed = findViewById(R.id.speed);
        remainingTime = findViewById(R.id.remainingTime);
        currentTime = findViewById(R.id.currentTime);
        close = findViewById(R.id.cancel);
        titleTxt = findViewById(R.id.title);
        closeTxt = findViewById(R.id.delete);
        voiceDraftAdapter = new voiceDraftAdapter();


        pause.setOnClickListener(this);
        forward.setOnClickListener(this);
        backward.setOnClickListener(this);
        speed.setOnClickListener(this);
        close.setOnClickListener(this);
        closeTxt.setOnClickListener(this);


        mediaPlayer = MediaPlayer.create(context, audioUri);
        seekBar.setMax(mediaPlayer.getDuration());


        defaultTimer();
        SeekBar();
        titleTxt.setText(title);


        Runnable mUpdateSeekbar = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                seekBar.postDelayed(this, 50);
                currentTime.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
                remainingTime.setText(milliSecondsToTimer(mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition()));
                SeekBar();

                Log.e("LOG", "Current position:" + mediaPlayer.getCurrentPosition() + "Duration:" + mediaPlayer.getDuration());
            }// run

        };

        seekBar.postDelayed(mUpdateSeekbar, 100);
    }// onCreate()


    private void doDismiss() {
        dismiss();
    }


    private String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }// milliSecondsToTimer

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pause:
                // toggle pause/ start button
                if (mediaPlayer.isPlaying()) {
                    pause.setImageResource(R.drawable.ic_play_button);
                    mediaPlayer.pause();
                } else {
                    pause.setImageResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }// else
                break;
            case R.id.forward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 15000);
                break;
            case R.id.backward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 15000);
                break;
            case R.id.speed:
                if (mediaPlayer.isPlaying()) {
                    if (speed.getText().toString().equals("X2")) {
                        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(1.5f));
                        speed.setText("X1");
                    } else {
                        mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(1f));
                        speed.setText("X2");

                    }
                }// if
                break;
            case R.id.cancel:
                defaultTimer();
                mediaPlayer.stop();
                dismiss();
                break;
            case R.id.delete:
                showDialogWithOkCancelButton("Do you want to delete the draft?");
                break;
            default:
                break;
        }//switch
    }//onClick

    private void defaultTimer() {
        currentTime.setText("00:00");
        int duration = mediaPlayer.getDuration();
        String time = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
        remainingTime.setText(time);
    }//end default timer


    private void SeekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null && mediaPlayer.isPlaying())
                    mediaPlayer.seekTo(progress);
            }// onProgressChanged
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.start();
                mediaPlayer.pause();
                seekBar.setProgress(0);
                defaultTimer();
                pause.setImageResource(R.drawable.ic_play_button);
            }// onCompletion

        });

    }// seekBar


    private void showDialogWithOkCancelButton(String msg) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //voiceDraftAdapter.deleteDraft();
                        dialog.dismiss();
                        doDismiss();
                    }// onClick
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }// showDialogWithOkButton


}// class