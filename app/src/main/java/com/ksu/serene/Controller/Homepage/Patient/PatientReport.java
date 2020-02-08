package com.ksu.serene.Controller.Homepage.Patient;

//import all widget types we're going to write into
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.ksu.serene.Model.MySharedPreference;
import com.ksu.serene.R;

public class PatientReport {
    //attributes that we will write into

    //anxiety level graph in the report
    //private View AL_view;

    //improvement_num : calculated improvement throughout AL graph, highestday_date= date of highest day of AL in graph
    private TextView improvement_num, highestday_date;


    private String nameDb, emailDb, imageDb; //database info..?
    private FirebaseAuth mAuth;
    private Button generate_report_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.patient_report, container, false);

        //identify variables (one by one) above to the actual existing elements

        improvement_num = view.findViewById(R.id.improvement_num);
        highestday_date = view.findViewById(R.id.highestday_date);
        mAuth = FirebaseAuth.getInstance();
        //generate_report_btn = view.findViewById(R.id.generate_report_btn);




    }//end of onCreateView method


}//end of class
