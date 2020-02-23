package com.ksu.serene.Controller.Homepage.Report;

//import all widget types we're going to write into

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.Controller.Constants;
import com.ksu.serene.Model.Location;
import com.ksu.serene.R;

public class PatientReport extends AppCompatActivity {

    //improvement_num : calculated improvement throughout AL graph, highestday_date= date of highest day of AL in graph
    private TextView improvement_num, highestday_date;
    private String duration;

    private String nameDb, emailDb, imageDb; //database info..?
    private FirebaseAuth mAuth;
    private Button generate_report_btn;
    private String startDate;
    private String endDate;
    private String userId = "6I5l8TvCxjWG3Jwxxpb4FcuDsGA2"; // TODO : REMOVE THIS
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ImageView ALGraph ;
    private TextView improvementNum, Highestday_date, location_name, numOfDays, location_AL;
    private RecyclerView recyclerView;
    private Adapter adapter;
    ArrayList<Location> locations;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_report);

        init();
        getExtras();
        if (duration.equals("custom")){
        Toast.makeText(getBaseContext(), startDate, Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(getBaseContext(), duration, Toast.LENGTH_SHORT).show();



        lastGeneratedPatientReport();
        location();

    }//onCreate

    private void init (){
        ALGraph = findViewById(R.id.AL_graph);
        improvement_num = findViewById(R.id.improvement_num);
        highestday_date = findViewById(R.id.highestday_date);
        //location_name = findViewById(R.id.location_name);
        //location_AL = findViewById(R.id.location_AL);
        recyclerView = findViewById(R.id.recycleView);
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

                                /*String date = doc.get(0).get("highestDay").toString();
                                highestday_date.setText(date);*/

                                String date = ((Timestamp)doc.get(0).get("highestDay")).toDate().toString();
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



        locations = new ArrayList<Location>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Task<QuerySnapshot> docRef = firebaseFirestore.collection("PatientLocations")
                .whereEqualTo("patientID", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {


                        if(task.isSuccessful()){
                            boolean locationFound = false;
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                List<DocumentSnapshot> doc = task.getResult().getDocuments();

                                //check for location date if it's within selected duration

                                Date loc_date = ((Timestamp)doc.get(i).get("arrivalTime")).toDate();//date received
                                Date today = new Date();//today
                                switch(duration){
                                    case "2week":
                                        if (daysBetween(loc_date,today)<15) {
                                            locationFound = true;
                                            break; //get out of switch and proceed to save other attributes
                                        }
                                        else{
                                            locationFound = false;
                                            break;
                                        }

                                    case "month":
                                        if (daysBetween(loc_date,today)<31){
                                            locationFound = true;
                                            break;
                                        }
                                        else{
                                            locationFound = false;
                                            break;
                                        }

                                    case "custom":
                                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                        Date startDate1 = null, endDate1 = null;
                                        try {
                                            startDate1 = formatter.parse(startDate);
                                            endDate1 = formatter.parse(endDate);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        if (startDate1.compareTo(loc_date)*loc_date.compareTo(endDate1)>=0){//if date is between startdate and enddate
                                            locationFound = true;
                                            break;
                                        }
                                        else{
                                            locationFound = false;
                                            break;
                                        }

                                }//end of switch

                                if (locationFound) {

                                    String locationName = doc.get(i).get("name").toString();
                                    String loc_AL = doc.get(i).get("anxietyLevel").toString();
                                    locations.add(new Location(locationName, loc_AL, daysBetween(loc_date, today)));
                                }

                                i++;


                            }// for every location belonging to this patient (for loop)


                            recyclerView.setHasFixedSize(true);
                            adapter = new Adapter(PatientReport.this, locations);
                            recyclerView.setAdapter(adapter);



                        }//if
                    }// onComplete
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });//addOnCompleteListener

    }//location

    private static long daysBetween(Date one, Date two) {
        long difference =  (one.getTime()-two.getTime())/86400000;
        return Math.abs(difference);
    }//end of daysbetween method

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