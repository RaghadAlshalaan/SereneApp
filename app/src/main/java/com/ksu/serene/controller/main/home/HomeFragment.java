package com.ksu.serene.controller.main.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.ksu.serene.controller.liveChart.ChartView;
import com.ksu.serene.controller.liveChart.draw.data.InputData;
import com.ksu.serene.controller.main.calendar.PatientAppointmentDetailPage;
import com.ksu.serene.model.TherapySession;
import com.ksu.serene.model.dbSetUp;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {


    static FirebaseStorage storage;
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final int PERMISSION_INTERNET = 1;
    private static final int PERMISSION_ACCESS_NETWORK_STATE = 2;

    // Next Appointment
    private TextView nextAppointment,improvement;
    private String Name="", DTime="", id="";
    private Date DDate;
    private View card3;


    // Quote
    private ImageView AL_graph, quote;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        init (root);

        getDailyReport();

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

        Timer timer = new Timer ();
        TimerTask t = new TimerTask () {
            @Override
            public void run () {
                // to excute  the
                executeApi(mAuth.getUid());

            }
        };

        timer.schedule (t, 0l, 1000*60*60*24);

        return root;
    }


    private void init(View root) {

        nextAppointment = root.findViewById(R.id.noUpcoming);
        card3 = root.findViewById(R.id.card3);
        //AL_graph = root.findViewById(R.id.AL_graph);
        improvement = root.findViewById(R.id.improvement_num);
        quote = root.findViewById(R.id.picQ);
        setQuoteImage();

        // parse Preference file
        SharedPreferences sp = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);

        // Live Chart
        ChartView chartView = root.findViewById(R.id.charView);
        List<InputData> dataList = createChartData();
        chartView.setData(dataList);

    }

    // Live chart
    @NonNull
    private List<InputData> createChartData() {

        // TODO : GET CHART DATA FROM FLAST
        List<InputData> dataList = new ArrayList<>();
        dataList.add(new InputData(2.5));
        dataList.add(new InputData(2));
        dataList.add(new InputData(1.4));
        dataList.add(new InputData(2));
        dataList.add(new InputData(1));
        dataList.add(new InputData(0.4));
        dataList.add(new InputData(1.3));
        dataList.add(new InputData(2.8));
        dataList.add(new InputData(2.1));

        long currMillis = System.currentTimeMillis();
        currMillis -= currMillis % TimeUnit.DAYS.toMillis(1);

        for (int i = 0; i < dataList.size(); i++) {
            long position = dataList.size() - 1 - i;
            long offsetMillis = TimeUnit.DAYS.toMillis(position);

            long millis = currMillis - offsetMillis;
            dataList.get(i).setMillis(millis);
        }

        return dataList;
    }

    private void getDailyReport() {

        String docID = "daily" + mAuth.getUid();

        db.collection("DailyReport").document("dailyUqTdL3T7MteuQHBe1aNfSE9u0Na2")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();

                            String quote_image_url = document.get("AL_graph").toString();

                            //Glide.with(getContext()).load(quote_image_url).into(AL_graph);

                            String improvementPercentage = document.get("improvement").toString();
                            improvement.setText(improvementPercentage);
                        }
                    }
                });

    }

    private void setQuoteImage() {

        db.collection("Quotes").document("quote")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot document = task.getResult();

                            String quote_image_url = document.get("image_url").toString();

                            Glide.with(getContext()).load(quote_image_url).into(quote);

                        }
                    }
                });

    }

    public void getAppointment2() {

        Query nextApp = db.collection("PatientSessions")
                .orderBy("dateTimestamp", Query.Direction.ASCENDING);

        nextApp.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(!queryDocumentSnapshots.isEmpty()) {

                            boolean found = false;
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                //check for appointment date if it's within selected duration

                                if ( document.get("patinetID").equals(mAuth.getUid())){

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

                                        break;

                                    }}
                            }

                            if(!found){
                                nextAppointment.setText("No Upcoming Appointments");
                                nextAppointment.setTextColor(getResources().getColor(R.color.Grey));
                            }

                        }
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        getAppointment2();
    }

    private void requestPermission(String permission, int requestId) {
        if (ContextCompat.checkSelfPermission(getContext(),
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{permission},
                    requestId);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_INTERNET: {
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.INTERNET, PERMISSION_INTERNET);
                }
                return;
            }
            case PERMISSION_ACCESS_NETWORK_STATE: {
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    requestPermission(Manifest.permission.ACCESS_NETWORK_STATE, PERMISSION_ACCESS_NETWORK_STATE);
                }
                return;
            }
        }
    }

    private void executeApi(String id){
        requestPermission(Manifest.permission.INTERNET, PERMISSION_INTERNET);
        requestPermission(Manifest.permission.ACCESS_NETWORK_STATE, PERMISSION_ACCESS_NETWORK_STATE);
        //todo: change API
        String url = "http://8db4c79b.ngrok.io/daily_report/"+id;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("LOG", "success: " + response.toString());
                        Toast.makeText(getContext() ,"Daily report updated successfully" , Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText( getContext(), "Failed to update Daily report: "+error.toString() , Toast.LENGTH_LONG).show();
                        Log.e("LOG","ERROR: "+error.toString() );

                    }
                }

        );

        objectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 100000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 100000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(objectRequest);
        //todo: retrieve data
    }


}