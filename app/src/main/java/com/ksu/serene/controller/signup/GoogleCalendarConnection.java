package com.ksu.serene.controller.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ksu.serene.MainActivity;
import com.ksu.serene.R;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

public class GoogleCalendarConnection extends AppCompatActivity {


    private Button finish, back, connect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_calendar_connection);

        getSupportActionBar().hide();

        // Change status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));

        connect = findViewById(R.id.connectCalendar);
        finish = findViewById(R.id.finishBtn);
        back = findViewById(R.id.backBtn);


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] PERMISSIONS = {
                        Manifest.permission.ACCESS_FINE_LOCATION
                };

                Permissions.check(GoogleCalendarConnection.this/*context*/,
                        PERMISSIONS, null/*rationale*/, null/*options*/, new PermissionHandler() {
                            @Override
                            public void onGranted() {
                                // do your task.

                                Intent i = new Intent(GoogleCalendarConnection.this, MainActivity.class);
                                i.putExtra("first","1");
                                startActivity(i);
                                finish();

                            }
                        });
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleCalendarConnection.super.onBackPressed();
            }
        });


    }
}
