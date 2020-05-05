package com.ksu.serene.controller.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ksu.serene.R;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.fitbitManager.Util;

public class Questionnairs extends AppCompatActivity {

    int step = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnairs);

        SharedPreferences sp = getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
        String preferred_lng = sp.getString("PREFERRED_LANGUAGE", "en");
        Util.setLocale(preferred_lng, this);


        getSupportActionBar().hide();

        // Change status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));

        if (getIntent().getStringExtra("Not Connected")!= null && getIntent().getStringExtra("Not Connected").equals("MainActivity")) {
            //go to gad fragment
            GAD7 gad7 = new GAD7();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.Questionnairs, gad7);
            ft.commit();
        }

        //step = getExtras();

        if (step == 1) {

            if (findViewById(R.id.qContainer) != null) {

                if (savedInstanceState == null) {

                    Sociodemo SocioFragment = new Sociodemo();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.qContainer, SocioFragment, null);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            }
        }/*else {

            GAD7 GadFragment = new GAD7();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.qContainer, GadFragment, null);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }*/


    }// end onCreate

    private int getExtras() {

        if (getIntent().getExtras().getString("step") != null) {

        String step = getIntent().getExtras().getString("step");
        return Integer.parseInt(step);
        }

        return 1;
    }

    @Override
    public void onBackPressed(){

    }

}
