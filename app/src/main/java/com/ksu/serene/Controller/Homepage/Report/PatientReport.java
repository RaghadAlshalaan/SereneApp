package com.ksu.serene.Controller.Homepage.Report;

//import all widget types we're going to write into

import java.util.Date;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.Controller.Constants;
import com.ksu.serene.R;

import java.util.List;

public class PatientReport extends AppCompatActivity {

    //improvement_num : calculated improvement throughout AL graph, highestday_date= date of highest day of AL in graph
    private TextView improvement_num, highestday_date;
    private String duration;

    private String nameDb, emailDb, imageDb; //database info..?
    private FirebaseAuth mAuth;
    private Button generate_report_btn;
    private String startDate;
    private String endDate;
    private String userId = "6I5l8TvCxjWG3Jwxxpb4FcuDsGA2";
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ImageView ALGraph ;
    private TextView improvementNum;
    private TextView Highestday_date;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_report);
        init();
        getExtras();
        Toast.makeText(getBaseContext(), duration, Toast.LENGTH_SHORT).show();

        lastGeneratedPatientReport();


    }//onCreate
private void init (){
    ALGraph = findViewById(R.id.AL_graph);
    improvement_num = (TextView)findViewById(R.id.improvement_num);
    highestday_date = (TextView)findViewById(R.id.highestday_date);


}

    private void lastGeneratedPatientReport() {
        Task<QuerySnapshot> docRef = firebaseFirestore.collection("LastGeneratePatientReport")
                .whereEqualTo("patientID", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            // assume that the patient has only one Doc contains his info
                            int counter = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                counter++;
                            }

                            if (counter == 1){

                                List<DocumentSnapshot> doc = task.getResult().getDocuments();
                                //improvement
                                String improvement = doc.get(0).get("improvement").toString();
                                improvement_num.setText(improvement + " %");

                                //graph
                                String img  = doc.get(0).get("anxietyLevelGraph").toString();
                                Glide.with(PatientReport.this)
                                        .load(img + "")
                                        .into(ALGraph);
                                //highest day
                                String date = doc.get(0).get("highestDay").toString();
                                highestday_date.setText(date);



                            }//if


                        }//if
                    }// onComplete
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });//addOnCompleteListener




    }//lastGeneratedPatientReport

    private void recommendation() {

    }//recommendation

    private void location() {

    }//location

    private void events(){

    }//events


    public void getExtras() {
        Intent intent = getIntent();
        duration = intent.getExtras().getString(Constants.Keys.DURATION);
        if (duration.equals("custom")) {
            startDate = intent.getExtras().getString(Constants.Keys.START_DATE);
            endDate = intent.getExtras().getString(Constants.Keys.END_DATE);
        }//if
    }//getExtras

}//end of class
