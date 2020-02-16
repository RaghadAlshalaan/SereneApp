package com.ksu.serene;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener{

    Timer timer;
    boolean click = false;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        LinearLayout splashlin = findViewById(R.id.splash);
        splashlin.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (click)
                    return;
                if (checkUserLogin()) {
                    //user is login
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreen.this, WelcomePage.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.splash:
                if (checkUserLogin()) {
                    //User is logged in
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashScreen.this, WelcomePage.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }

    }

    private boolean checkUserLogin() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            return true;
        } else
            return false;
    }

}
