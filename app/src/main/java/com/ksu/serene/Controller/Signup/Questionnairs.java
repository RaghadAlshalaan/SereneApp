package com.ksu.serene.Controller.Signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.ksu.serene.R;

public class Questionnairs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnairs);

        getSupportActionBar().hide();

        if (findViewById(R.id.qContainer)!= null){

            if( savedInstanceState == null ) {

                Sociodemo SocioFragment = new Sociodemo();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.qContainer, SocioFragment, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            } else return;
        }

    }
}
