package com.ksu.serene;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class WelcomePage extends AppCompatActivity {

    private Button logIn;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        logIn = findViewById(R.id.LOGIN);
        register = findViewById(R.id.REGISTER);

        logIn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomePage.this, LogInPage.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(WelcomePage.this, register.class);
                //startActivity(intent);
            }
        });

    }
}
