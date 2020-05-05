package com.ksu.serene.controller.main.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.ksu.serene.R;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.controller.main.calendar.PatientAppointmentDetailPage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import static com.firebase.ui.auth.ui.phone.VerifyPhoneNumberFragment.TAG;
import static java.lang.Float.NaN;


public class HomeFragment extends Fragment {


    static FirebaseStorage storage;
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    // Next Appointment
    private TextView nextAppointment,improvement, percent, noResult;
    private String Name="", DTime="", id="";
    private Date DDate;

    private View card3, card0;
    private BarChart chart1;
    private RelativeLayout relativeProgressHomeChart;

    // Quote
    private ImageView  quote;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        init(root);

        executeDailyReport();

        getDailyReport();

        getAppointment();

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

    private void executeDailyReport() {

        DocumentReference docRef = db.collection("DailyReport").document("daily"+mAuth.getUid());

        if( docRef != null ) {

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


                            // GET DATE OF today in format ( YYYY-MM-DD )
                            Calendar cal = Calendar.getInstance();
                            String Today = dateFormat.format(cal.getTime());
                            Date dateOfToday = null;
                            try {
                                dateOfToday = dateFormat.parse(Today);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            // Date of last daily report update
                            String LastUpdate = document.get("date").toString();
                            Date dateOfLastUpdate = null;
                            try {
                                dateOfLastUpdate = dateFormat.parse(LastUpdate);
                            } catch (ParseException e) {
                                dateOfLastUpdate = dateOfToday;
                            }



                            if (!dateOfLastUpdate.equals(dateOfToday) || dateOfLastUpdate.before(dateOfToday)) {
                                Log.d("Daily not updated", "API is called");
                                executeDailyReportApi(mAuth.getUid());
                            } else {
                                Log.d("Daily Already updated", "API did not called");

                                try {
                                    setChartInfo();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        else{
            Log.d("Daily not found", "new API is called");
            executeDailyReportApi(mAuth.getUid());
        }

    }

    JSONObject jsonObjectDate , jsonObjectAnxiety ;
    // This method extracts information from json string and stores them in a json object
    public void convertStringToJson(String date, String anxiety) {
        try {
            jsonObjectDate = new JSONObject(date);
            jsonObjectAnxiety = new JSONObject(anxiety);

        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    private void executeDailyReportApi(String id) {

        String url = "https://e8a76a2c.ngrok.io/daily_report/" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("DailyAPII", "Success: " + response.toString());

                        try {

                            // Save to SP
                            SharedPreferences preferences = getActivity().getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("dailyJSONd", response.getJSONObject("date").toString() );
                            editor.putString("dailyJSONa", response.getJSONObject("Anxiety").toString() );
                            editor.apply();

                            setChartInfo();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //relativeProgressHomeChart.setVisibility(View.GONE);
                            Log.d("LOG_TAG", "JSONException = " + e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText( getApplicationContext(), context.getResources().getText(R.string.api_daily_error_msg) , Toast.LENGTH_SHORT).show();
                        Log.e("DailyAPII", "ERROR: " + error.toString());

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
    }

    private void setChartInfo() throws JSONException {
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
        String JSONd = preferences.getString("dailyJSONd"," ");
        String JSONa = preferences.getString("dailyJSONa"," ");
        convertStringToJson(JSONd, JSONa);

        JSONObject Dates = jsonObjectDate;
        JSONObject AnxietyLevel = jsonObjectAnxiety;

        List<BarEntry> entriesBar = new ArrayList<>();
        entriesBar.add(new BarEntry(1f, (float) AnxietyLevel.getDouble("0")));
        entriesBar.add(new BarEntry(2f, (float) AnxietyLevel.getDouble("1")));
        entriesBar.add(new BarEntry(3f, (float) AnxietyLevel.getDouble("2")));
        entriesBar.add(new BarEntry(4f, (float) AnxietyLevel.getDouble("3")));
        entriesBar.add(new BarEntry(5f, (float) AnxietyLevel.getDouble("4")));
        entriesBar.add(new BarEntry(6f, (float) AnxietyLevel.getDouble("5")));
        entriesBar.add(new BarEntry(7f, (float) AnxietyLevel.getDouble("6")));

        List<String> xAxisValues = new ArrayList<>();
        xAxisValues.add("");
        xAxisValues.add(Dates.getString("0"));
        xAxisValues.add(Dates.getString("1"));
        xAxisValues.add(Dates.getString("2"));
        xAxisValues.add(Dates.getString("3"));
        xAxisValues.add(Dates.getString("4"));
        xAxisValues.add(Dates.getString("5"));
        xAxisValues.add(Dates.getString("6"));

        relativeProgressHomeChart.setVisibility(View.GONE);
        chart1.setVisibility(View.VISIBLE);

        setChartData(entriesBar, xAxisValues);
    }

    private void setChartData(List<BarEntry> entriesBar, List<String> xAxisValues) {

        BarDataSet set = new BarDataSet(entriesBar, "Anxiety Level");
        set.setColors(generateChartColorLine(entriesBar));
        BarData data = new BarData(set);
        data.setBarWidth(0.1f); // set custom bar width

        //to hide right Y and top X border
        YAxis rightYAxis = chart1.getAxisRight();
        rightYAxis.setEnabled(false);
        XAxis topXAxis = chart1.getXAxis();
        topXAxis.setEnabled(false);

        //set bottom labels params
        XAxis xAxis = chart1.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(290);


        YAxis yAxis = chart1.getAxisLeft();
        yAxis.setAxisMinimum(0f); // start at zero
        yAxis.setAxisMaximum(3f); // the axis maximum is 100

        chart1.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        chart1.getDescription().setEnabled(false);

        chart1.setData(data);

        // off all controls
        chart1.setTouchEnabled(false);
        chart1.setDragEnabled(true);
        chart1.setScaleEnabled(false);
        chart1.setPinchZoom(false);
        chart1.setDrawGridBackground(false);

        //hide background lines
        chart1.getXAxis().setDrawGridLines(false);
        chart1.getAxisLeft().setDrawGridLines(false);
        chart1.getAxisRight().setDrawGridLines(false);
        chart1.setFitBars(true); // make the x-axis fit exactly all bars

        //settings legend color, count labels, space
        Legend leg = chart1.getLegend();
        List <LegendEntry> entrylist = new ArrayList<>();
        entrylist.add(new LegendEntry("Low", Legend.LegendForm.DEFAULT, NaN, NaN, null, getResources().getColor(R.color.low_chart_color)));
        entrylist.add(new LegendEntry("Med", Legend.LegendForm.DEFAULT, NaN, NaN, null, getResources().getColor(R.color.med_chart_color)));
        entrylist.add(new LegendEntry("High", Legend.LegendForm.DEFAULT, NaN, NaN, null, getResources().getColor(R.color.high_chart_color)));
        leg.setXEntrySpace(25);
        leg.setCustom(entrylist);
        chart1.animateY(500);
        chart1.invalidate(); // refresh
    }

    private List<Integer> generateChartColorLine(List<BarEntry> entriesBar){
        List<Integer> colorsList = new ArrayList<>();

        for (int i = 0; i < entriesBar.size(); i++) {
            Log.d("LOG_TAG", "Y value = " + entriesBar.get(i).getY());
            if (entriesBar.get(i).getY() >= 0 && entriesBar.get(i).getY() <= 1) {
                colorsList.add(getResources().getColor(R.color.low_chart_color));
            }
            if (entriesBar.get(i).getY() > 1 && entriesBar.get(i).getY() <= 2) {
                colorsList.add(getResources().getColor(R.color.med_chart_color));
            }
            if (entriesBar.get(i).getY() > 2 && entriesBar.get(i).getY() <= 3) {
                colorsList.add(getResources().getColor(R.color.high_chart_color));
            }
        }

        return colorsList;
    }


    private void init(View root) {

        nextAppointment = root.findViewById(R.id.noUpcoming);
        card3 = root.findViewById(R.id.card3);
        improvement = root.findViewById(R.id.improvement_num);
        quote = root.findViewById(R.id.picQ);
        percent = root.findViewById(R.id.improvement_per);
//        noResult = root.findViewById(R.id.noResult);
//        noResult.setVisibility(View.VISIBLE);
        setQuoteImage();

        // live chart
        chart1 = root.findViewById(R.id.chart1);
        relativeProgressHomeChart = root.findViewById(R.id.relativeProgressHomeChart);

    }


    private void getDailyReport() {

        String docID = "daily" + mAuth.getUid();

        db.collection("DailyReport").document(docID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {

                                String improvementPercentage = document.get("improvement").toString();
                                setImprovement(improvementPercentage);


                            }else{
                                //noResult.setVisibility(View.VISIBLE);

                                improvement.setText("--");
                            }
                        }//task successful
                    }
                });

    }

    private void setImprovement(String improvementPercentage) {
        improvement.setText(improvementPercentage);

        if (Double.parseDouble(improvementPercentage) >= 0) {
            improvement.setTextColor(getActivity().getResources().getColor(R.color.green));
            percent.setTextColor(getActivity().getResources().getColor(R.color.green));
        } else {
            improvement.setTextColor(getActivity().getResources().getColor(R.color.Error));
            percent.setTextColor(getActivity().getResources().getColor(R.color.Error));
        }
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

    public void getAppointment() {

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
                                        nextAppointment.setTextColor(getActivity().getResources().getColor(R.color.darkAccent));

                                        break;

                                    }}
                            }

                            if(!found){
                                nextAppointment.setText(R.string.no_appoiintments);
                                nextAppointment.setTextColor(getActivity().getResources().getColor(R.color.grey));
                            }

                        }
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        getAppointment();
    }


}