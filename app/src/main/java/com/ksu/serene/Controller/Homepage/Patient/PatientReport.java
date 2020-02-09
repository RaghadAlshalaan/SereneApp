package com.ksu.serene.Controller.Homepage.Patient;

//import all widget types we're going to write into

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.firebase.auth.FirebaseAuth;

import com.ksu.serene.Controller.Constants;
import com.ksu.serene.R;

public class PatientReport extends AppCompatActivity {
    //attributes that we will write into

    //anxiety level graph in the report
    //private View AL_view;

    //improvement_num : calculated improvement throughout AL graph, highestday_date= date of highest day of AL in graph
    private TextView improvement_num, highestday_date;
    private String duration;


    private String nameDb, emailDb, imageDb; //database info..?
    private FirebaseAuth mAuth;
    private Button generate_report_btn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_report);
        getExtras();
        Toast.makeText(getBaseContext() , duration ,Toast.LENGTH_SHORT).show();



    }//onCreate

    public void getExtras() {
        Intent intent = getIntent();
        duration = intent.getExtras().getString(Constants.Keys.DURATION);

    }
}//end of class
