package com.ksu.serene.Controller.Signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ksu.serene.R;

public class Questionnairs extends AppCompatActivity {

    int step = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnairs);

        getSupportActionBar().hide();

        // Change status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));

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
        } /*else {

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
