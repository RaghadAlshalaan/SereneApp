package com.ksu.serene.controller.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.R;

import java.util.HashMap;
import java.util.Map;

public class GAD7 extends Fragment {


    private String GAD7ScaleScore;
    private RadioGroup group1, group2, group3, group4, group5, group6, group7;
    private int score1, score2, score3, score4, score5, score6,score7, finalScore, step=2;
    private String radio1, radio2, radio3, radio4, radio5, radio6, radio7;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button next, back;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gad7, container, false);

        init(view);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkFields(radio1,radio2 ,radio3 ,radio4 ,radio5 ,radio6 ,radio7)){
                    Toast.makeText(getActivity(), R.string.EmptyFields,Toast.LENGTH_LONG).show();
                    return;
                }

                if(checkFields(radio1,radio2 ,radio3 ,radio4 ,radio5 ,radio6 ,radio7)) {

                    //Calculate the final score

                    finalScore = score1 + score2 + score3 + score4 + score5 + score6 + score7;
                    GAD7ScaleScore = String.valueOf(finalScore);


                    // Upload GAD score to user document in DB

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final String userEmail = user.getEmail();

                    saveGadScoreDB ( userEmail, GAD7ScaleScore);
                    /*final Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("GAD-7ScaleScore", GAD7ScaleScore);


                    db.collection("Patient")
                            .whereEqualTo("email", userEmail)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult()) {
                                            String id = document.getId();

                                            db.collection("Patient")
                                                    .document(id).update(userInfo);
                                        }

                                        //added this toast needed in test
                                        Toast.makeText(getActivity(), R.string.GADSuccess,
                                                Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getActivity(), FitbitConnection.class);
                                        getActivity().startActivity(i);

                                    } else {System.out.println("Error getting documents: ");
                                        //added this toast needed in test
                                        Toast.makeText(getActivity(), R.string.GADFialed,
                                                Toast.LENGTH_LONG).show();
                                    }

                                }
                            });*/
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });


        return view;
    }

    private void init(View view) {

        group1 = view.findViewById(R.id.radio1);
        group2 = view.findViewById(R.id.radio2);
        group3 = view.findViewById(R.id.radio3);
        group4 = view.findViewById(R.id.radio4);
        group5 = view.findViewById(R.id.radio5);
        group6 = view.findViewById(R.id.radio6);
        group7 = view.findViewById(R.id.radio7);
        next = view.findViewById(R.id.nextBtn);
        back = view.findViewById(R.id.backBtn);

        radio1 = "";
        radio2 = "";
        radio3 = "";
        radio4 = "";
        radio5 = "";
        radio6 = "";
        radio7 = "";

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
    }

    public boolean checkFields(String radio1,String radio2,String radio3,String radio4,
                                String radio5,String radio6,String radio7){

        // Force the user to answer all the questions
        if(radio1.matches("")|| radio2.matches("")||radio3.matches("")||radio4.matches("")||
                radio5.matches("")|| radio6.matches("")||radio7.matches("")){
            return false;
        }

        return true;

    }

    public void saveGadScoreDB (String userEmail,String GAD7ScaleScore) {
        final Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("GAD-7ScaleScore", GAD7ScaleScore);
        db.collection("Patient")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String id = document.getId();

                                db.collection("Patient")
                                        .document(id).update(userInfo);
                            }

                            //added this toast needed in test
                            //Toast.makeText(getActivity(), R.string.GADSuccess,Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getActivity(), FitbitConnection.class);
                            getActivity().startActivity(i);

                        } else {System.out.println("Error getting documents: ");
                            //added this toast needed in test
                            Toast.makeText(getActivity(), R.string.GADFialed,
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

}
