package com.ksu.serene.controller.main.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.ksu.serene.R;
import com.ksu.serene.controller.main.calendar.PatientAppointmentDetailPage;
import com.ksu.serene.model.TherapySession;
import com.ksu.serene.model.dbSetUp;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {


    static FirebaseStorage storage;
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


    // Next Appointment
    private TextView nextAppointment;
    private String Name="", DTime="", id="";
    private Date DDate;
    private View card3;

    // Quote
    private ImageView quote;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        init (root);

        getAppointment2();

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appointmentDetail = new Intent(getContext(), PatientAppointmentDetailPage.class);
                appointmentDetail.putExtra("AppointmentID", id);
                startActivity(appointmentDetail);
            }
        });
        card3.setEnabled(false);

        return root;
    }

    private void init(View root) {

        nextAppointment = root.findViewById(R.id.noUpcoming);
        card3 = root.findViewById(R.id.card3);

        quote = root.findViewById(R.id.picQ);
        setQuoteImage();

        // parse Preference file
        SharedPreferences sp = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);

    }

    private void setQuoteImage() {

        db.collection("Quotes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String quote_image_url = document.get("image_url").toString();

                                Glide.with(getContext()).load(quote_image_url).into(quote);

                            }
                        }
                    }
                });


    }

//    public void getAppointment(){
//
//        db.collection("PatientSessions")
//                .whereGreaterThanOrEqualTo("dateTimestamp", new Date())
//                .limit(50)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            if(!task.getResult().isEmpty()) {
//
//                                boolean found = false;
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    //check for appointment date if it's within selected duration
//
//                                    if(mAuth.getUid().equals(document.get("patientID").toString())){
//
//                                    DTime = document.get("time").toString();
//                                    int hours = Integer.parseInt(DTime.substring(0, 2)); //hh : mm
//                                    int minutes = Integer.parseInt(DTime.substring(5));
//
//                                    Calendar rightNow = Calendar.getInstance();
//                                    int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
//                                    int currentMin = rightNow.get(Calendar.MINUTE);
//
//                                    if(hours > currentHour){
//                                        if (minutes > currentMin){
//
//                                    Name = document.get("name").toString();
//                                    DTime = document.get("time").toString();
//                                    DDate = ((Timestamp) document.get("dateTimestamp")).toDate();
//                                    id = document.getId();
//
//                                    found = true;
//                                    break;
//                                    }}}
//
//                                }
//
//                                if(found) {
//                                    card3.setEnabled(true);
//
//                                    DateFormat dateFormat = new SimpleDateFormat("E d MMM");
//                                    String D = dateFormat.format(DDate);
//
//                                    int hours = Integer.parseInt(DTime.substring(0, 2)); //hh : mm
//                                    int minutes = Integer.parseInt(DTime.substring(5));
//                                    String amPM;
//
//                                    if (hours > 12) {
//                                        amPM = "PM";
//                                        hours = hours - 12;
//                                    } else {
//                                        amPM = "AM";
//                                    }
//
//                                    DTime = hours + ":" + minutes + " " + amPM;
//
//                                    String appointment;
//                                    appointment = Name + " | " + D + " | " + DTime;
//                                    nextAppointment.setText(appointment);
//                                    nextAppointment.setTextColor(getResources().getColor(R.color.black));
//
//                                }else {
//                                    nextAppointment.setText("No Upcoming Appointments");
//                                    nextAppointment.setTextColor(getResources().getColor(R.color.Grey));
//                                }
//                            }else {
//                                nextAppointment.setText("No Upcoming Appointments");
//                                nextAppointment.setTextColor(getResources().getColor(R.color.Grey));
//                            }
//                        }//if
//                    }
//                    }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//
//    }

    public void getAppointment2(){

        Query nextApp = db.collection("PatientSessions")
                .whereEqualTo("patinetID",mAuth.getUid())
                .orderBy("dateTimestamp", Query.Direction.ASCENDING);

        nextApp.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                    boolean found = false;
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        //check for appointment date if it's within selected duration


                            DDate = ((Timestamp) document.get("dateTimestamp")).toDate();

                            DTime = document.get("time").toString();
                            int hours = Integer.parseInt(DTime.substring(0, 2)); //hh : mm
                            int minutes = Integer.parseInt(DTime.substring(5));


                            DDate.setHours(hours);
                            DDate.setMinutes(minutes);
                            long theday = DDate.getTime();


                            Calendar rightNow = Calendar.getInstance();
                            long currentDate = rightNow.getTime().getTime();


                            if (currentDate <= theday ) {//Check day ( today or after )

                                Name = document.get("name").toString();
                                DTime = document.get("time").toString();
                                DDate = ((Timestamp) document.get("dateTimestamp")).toDate();
                                id = document.getId();

                                found = true;
                                break;
                            }


                        if (found) {
                            card3.setEnabled(true);

                            DateFormat dateFormat = new SimpleDateFormat("E d MMM");
                            String D = dateFormat.format(DDate);

                            String amPM;

                            if (hours > 12) {
                                amPM = "PM";
                                hours = hours - 12;
                            } else {
                                amPM = "AM";
                            }

                            DTime = hours + ":" + minutes + " " + amPM;

                            String appointment;
                            appointment = Name + " | " + D + " | " + DTime;
                            nextAppointment.setText(appointment);
                            nextAppointment.setTextColor(getResources().getColor(R.color.black));

                        }
                    }

                    if(!found){
                        nextAppointment.setText("No Upcoming Appointments");
                        nextAppointment.setTextColor(getResources().getColor(R.color.Grey));
                    }

                }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        //getAppointment2();
    }
}