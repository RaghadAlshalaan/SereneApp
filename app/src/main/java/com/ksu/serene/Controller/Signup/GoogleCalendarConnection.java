package com.ksu.serene.Controller.Signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ksu.serene.MainActivity;
import com.ksu.serene.R;

public class GoogleCalendarConnection extends AppCompatActivity {

    private Button finish, back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_calendar_connection);

        getSupportActionBar().hide();

        finish = findViewById(R.id.finishBtn);
        back = findViewById(R.id.backBtn);



        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(GoogleCalendarConnection.this, MainActivity.class);
                i.putExtra("first","1");
                startActivity(i);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : back to gad
                getFragmentManager().popBackStackImmediate();
            }
        });



    }
}
