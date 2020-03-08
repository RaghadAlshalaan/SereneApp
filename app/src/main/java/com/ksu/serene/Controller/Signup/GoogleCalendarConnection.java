package com.ksu.serene.Controller.Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ksu.serene.MainActivity;
import com.ksu.serene.R;

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
                Intent i = new Intent(GoogleCalendarConnection.this, MainActivity.class);
                i.putExtra("first","1");
                startActivity(i);
                finish();
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
