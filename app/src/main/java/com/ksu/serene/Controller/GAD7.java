package com.ksu.serene.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.ksu.serene.R;

public class GAD7 extends Fragment {

    private String GAD7Scalescore;
    private String fullName, email,password;
    private RadioGroup group1, group2, group3, group4, group5, group6, group7;
    private int score1, score2, score3, score4, score5, score6,score7, finalScore;
    private Button next;
    private TextView arrow;
    private String radio1, radio2, radio3, radio4, radio5, radio6, radio7;
    private boolean newOrOld;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if(bundle != null){
            fullName = getArguments().getString("fullName");
            email = getArguments().getString("email");
            password = getArguments().getString("password");

        }


        View view = inflater.inflate(R.layout.gad7_screen, container, false);

        group1 = view.findViewById(R.id.radio1);
        group2 = view.findViewById(R.id.radio2);
        group3 = view.findViewById(R.id.radio3);
        group4 = view.findViewById(R.id.radio4);
        group5 = view.findViewById(R.id.radio5);
        group6 = view.findViewById(R.id.radio6);
        group7 = view.findViewById(R.id.radio7);
        next = view.findViewById(R.id.button);
        arrow = view.findViewById(R.id.backArrow);

        radio1 = "";
        radio2 = "";
        radio3 = "" ;
        radio4 = "";
        radio5 = "";
        radio6 = "";
        radio7=  "";

        //we will allow the user to only check one radio button, hence to calculate the GAD score, i added the radio string to
        //check if the radio group has been selected
        group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton11) {
                    score1 = 0;
                    radio1="notNull";
                } else if (checkedId == R.id.radioButton12) {
                    score1 = 1;
                    radio1="notNull";
                } else if (checkedId == R.id.radioButton13){
                    score1 = 2;
                    radio1="notNull";
                } else if(checkedId == R.id.radioButton14){
                    score1 = 3;
                    radio1="notNull";
                }
            }
        });
        group2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton21) {
                    score2 = 0;
                    radio2="notNull";
                } else if (checkedId == R.id.radioButton22) {
                    score2 = 1;
                    radio2="notNull";
                } else if (checkedId == R.id.radioButton23){
                    score2 = 2;
                    radio2="notNull";
                } else if(checkedId == R.id.radioButton24){
                    score2 = 3;
                    radio2="notNull";
                }
            }
        });
        group3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton31) {
                    score3 = 0;
                    radio3="notNull";
                } else if (checkedId == R.id.radioButton32) {
                    score3 = 1;
                    radio3="notNull";
                } else if (checkedId == R.id.radioButton33){
                    score3 = 2;
                    radio3="notNull";
                } else if(checkedId == R.id.radioButton34){
                    score3 = 3;
                    radio3="notNull";
                }
            }
        });
        group4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton41) {
                    score4 = 0;
                    radio4="notNull";
                } else if (checkedId == R.id.radioButton42) {
                    score4 = 1;
                    radio4="notNull";
                } else if (checkedId == R.id.radioButton43){
                    score4 = 2;
                    radio4="notNull";
                } else if(checkedId == R.id.radioButton44){
                    score4 = 3;
                    radio4="notNull";
                }
            }
        });
        group5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton51) {
                    score5 = 0;
                    radio5="notNull";
                } else if (checkedId == R.id.radioButton52) {
                    score5 = 1;
                    radio5="notNull";
                } else if (checkedId == R.id.radioButton53){
                    score5 = 2;
                    radio5="notNull";
                } else if(checkedId == R.id.radioButton54){
                    score5 = 3;
                    radio5="notNull";
                }
            }
        });
        group6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton61) {
                    score6 = 0;
                    radio6="notNull";
                } else if (checkedId == R.id.radioButton62) {
                    score6 = 1;
                    radio6="notNull";
                } else if (checkedId == R.id.radioButton63){
                    score6 = 2;
                    radio6="notNull";
                } else if(checkedId == R.id.radioButton64){
                    score6 = 3;
                    radio6="notNull";
                }
            }
        });
        group7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton71) {
                    score7 = 0;
                    radio7="notNull";
                } else if (checkedId == R.id.radioButton72) {
                    score7 = 1;
                    radio7="notNull";
                } else if (checkedId == R.id.radioButton73){
                    score7 = 2;
                    radio7="notNull";
                } else if(checkedId == R.id.radioButton74){
                    score7 = 3;
                    radio7="notNull";
                }
            }
        });








        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to force the user to complete the GAD questionairre
              if(radio1.matches("")|| radio2.matches("")||radio3.matches("")||radio4.matches("")||
                        radio5.matches("")|| radio6.matches("")||radio7.matches("")){
                Toast.makeText(getActivity(), "Please complete the questionairre",
                          Toast.LENGTH_SHORT).show();

                   return;}

                finalScore = score1 + score2 + score3 + score4 + score5 + score6 + score7;
                GAD7Scalescore = String.valueOf(finalScore);

                Bundle bundle = new Bundle();
                bundle.putString("fullName", fullName);
                bundle.putString("email",email);
                bundle.putString("password",password);
                bundle.putString("GAD7Scalescore", GAD7Scalescore);
                Fragment fragmentOne = new Sociodemo();
                fragmentOne.setArguments(bundle);

                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.layout, fragmentOne);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });




        return view;
    }


}
