package com.ksu.serene.controller.main.report;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.R;
import com.ksu.serene.controller.main.calendar.CalendarFragment;
import com.ksu.serene.model.Event;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import www.sanju.motiontoast.MotionToast;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static timber.log.Timber.tag;

public class ReportFragment extends Fragment {

    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Button generate_report;
    private RadioGroup radioGroup;
    private View root;
    private String duration;
    private Button start, end;
    private LinearLayout datePicker;
    private String startDate, endDate;
    private Resources res;
    Calendar myCalendarStart = Calendar.getInstance();
    Calendar myCalendarEnd = Calendar.getInstance();
    private SimpleDateFormat DateFormat = new SimpleDateFormat("dd/mm/yyyy", Locale.US);
    private String api_url, apiStartDate, apiEndDate;
    private ProgressBar progressBar;
    private String reportStartDate;
    private String reportEndDate;
    private TextView Info;
    private RadioButton radioBtn1, radioBtn2, radioBtn3;
    private String first_fitbit= "";
    private String last_fitbit= "";


    // Google Calendar Events
    public boolean GoogleCalendar = false;
    GoogleAccountCredential credential;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    final HttpTransport transport = new com.google.api.client.http.javanet.NetHttpTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    static final int REQUEST_AUTHORIZATION = 1;
    com.google.api.services.calendar.Calendar client;

    static final String FIELDS = "id,summary";
    static final String FEED_FIELDS = "items(" + FIELDS + ")";

    private static final int PERMISSION_INTERNET = 1;
    private static final int PERMISSION_ACCESS_NETWORK_STATE = 2;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_report, container, false);
        res = getResources();

        init();

        // Initialize Google API
        initGoogleApi();

        // by default
        datePicker.setVisibility(LinearLayout.GONE);

        final DatePickerDialog.OnDateSetListener StartDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                onSetStart(year, monthOfYear, dayOfMonth);
            }
        };

        final DatePickerDialog.OnDateSetListener EndDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                onSetEnd(year, monthOfYear, dayOfMonth);
            }

        };

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartDate(StartDate);
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEndDate(EndDate);
        }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setRadioButton(checkedId);
        }// onChecked
        });

        getFitbitduration ();

        generate_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkFitbitAvailability();
            }// onClick
        });






        return root;

    }// onCreate

    public void getFitbitduration (){

        DocumentReference patientDoc = db.collection("Patient").document(mAuth.getUid());
        patientDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    first_fitbit = document.get("first_fitbit").toString();
                    last_fitbit = document.get("last_fitbit").toString();

                    Date FF = new Date();
                    Date LF = new Date();

                    try {
                        FF = new SimpleDateFormat("dd/MM/yyyy").parse(first_fitbit);
                        LF = new SimpleDateFormat("dd/MM/yyyy").parse(last_fitbit);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long duration = LF.getTime() - FF.getTime();

                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;
                    long daysInMilli = hoursInMilli * 24;

                    long elapsedDays = duration / daysInMilli;

                    if (elapsedDays < 14){
                        radioBtn1.setEnabled(false);
                        radioBtn2.setEnabled(false);
                        radioBtn3.setChecked(true);}
                    else if (elapsedDays >= 14 && elapsedDays < 30){
                        radioBtn1.setEnabled(true);
                        radioBtn2.setEnabled(false); }
                    else if (elapsedDays >= 30 ){
                        radioBtn1.setEnabled(true);
                        radioBtn2.setEnabled(true);}


    } } }); }




    private void checkFitbitAvailability() {

        DocumentReference patientDoc = db.collection("Patient").document(mAuth.getUid());
        patientDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    first_fitbit = document.get("first_fitbit").toString();

                    Date FF = new Date();
                    Date startDate = myCalendarStart.getTime();

                    try {
                        FF = new SimpleDateFormat("dd/MM/yyyy").parse(first_fitbit);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if ((FF.before(startDate))) {
                        generateReport();
                    } else {
                        String text = String.format(res.getString(R.string.date_picker));
                        text = "You don't have enough data to analyze, please choose another duration";

                        MotionToast.Companion.darkToast(
                                getActivity(),
                                text,
                                MotionToast.Companion.getTOAST_WARNING(),
                                MotionToast.Companion.getGRAVITY_BOTTOM(),
                                MotionToast.Companion.getSHORT_DURATION(),
                                ResourcesCompat.getFont(getActivity().getApplicationContext(), R.font.montserrat));

                    }
                } }
        });

    }







    private void init() {
        generate_report = root.findViewById(R.id.generate_report_btn);
        radioGroup = root.findViewById(R.id.radio_group);
        end = root.findViewById(R.id.end);
        start = root.findViewById(R.id.start);
        datePicker = root.findViewById(R.id.data_picker);
        duration = "14";// default value
        Info = root.findViewById(R.id.info);
        progressBar =root.findViewById(R.id.progress_bar);
        radioBtn1 = root.findViewById(R.id.radioButton1);
        radioBtn2 = root.findViewById(R.id.radioButton2);
        radioBtn3 = root.findViewById(R.id.radioButton3);
        Info.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    /** Functions to handle start date and end date selection **/

    private void onSetEnd(int year, int monthOfYear, int dayOfMonth) {
        myCalendarEnd.set(Calendar.YEAR, year);
        myCalendarEnd.set(Calendar.MONTH, monthOfYear);
        myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        int month = Integer.parseInt(String.valueOf(myCalendarEnd.get(Calendar.MONTH)))+1;
        endDate = myCalendarEnd.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + myCalendarEnd.get(Calendar.YEAR);
        apiEndDate = myCalendarEnd.get(Calendar.YEAR) + "-" + month + "-" + myCalendarEnd.get(Calendar.DAY_OF_MONTH);
        end.setText(endDate);
    }

    private void onSetStart(int year, int monthOfYear, int dayOfMonth) {
        myCalendarStart.set(Calendar.YEAR, year);
        myCalendarStart.set(Calendar.MONTH, monthOfYear);
        myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        int month = Integer.parseInt(String.valueOf(myCalendarStart.get(Calendar.MONTH)))+1;
        startDate = myCalendarStart.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + myCalendarStart.get(Calendar.YEAR);
        apiStartDate = myCalendarStart.get(Calendar.YEAR) + "-" +month + "-" + myCalendarStart.get(Calendar.DAY_OF_MONTH);
        start.setText(startDate);
    }

    private void setRadioButton(int checkedId) {

        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");

        switch (checkedId) {
            case R.id.radioButton1:
                datePicker.setVisibility(LinearLayout.GONE);
                duration = "14";
                Calendar calw = Calendar.getInstance();
                calw.add(Calendar.DATE, -1);
                endDate = dateFormat.format(calw.getTime());

                Calendar cals = Calendar.getInstance();
                cals.add(Calendar.DATE, -15);
                startDate = dateFormat.format(cals.getTime());

                break;

            case R.id.radioButton2:
                datePicker.setVisibility(LinearLayout.GONE);
                duration = "30";
                Calendar calm = Calendar.getInstance();
                calm.add(Calendar.DATE, -1);
                endDate = dateFormat.format(calm.getTime());
                Calendar calms = Calendar.getInstance();
                calms.add(Calendar.MONTH, -1);
                startDate = dateFormat.format(calms.getTime());

                break;

            case R.id.radioButton3:
                duration = "custom";
                datePicker.setVisibility(LinearLayout.VISIBLE);
                break;

        }// switch
    }

    private void setEndDate(DatePickerDialog.OnDateSetListener EndDate) {
        if (isStartDateSet(startDate)) {
            //check the start StartDate not equal to current StartDate
            if (!isStartDEqualToCurrent(myCalendarStart)) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), EndDate, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(myCalendarStart.getTimeInMillis());
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                datePickerDialog.show();
            }

            //when that StartDate chosen for start is equal to current time no need to show the dialog take the same StartDate
            else {
                endDate = myCalendarStart.get(Calendar.DAY_OF_MONTH) + "/" + myCalendarStart.get(Calendar.MONTH) + "/" + myCalendarStart.get(Calendar.YEAR);
                end.setText(DateFormat.format(myCalendarStart.getTime()));
            }


        } else {

            String text = String.format(res.getString(R.string.date_picker));

            MotionToast.Companion.darkToast(
                    getActivity(),
                    text,
                    MotionToast.Companion.getTOAST_WARNING(),
                    MotionToast.Companion.getGRAVITY_BOTTOM(),
                    MotionToast.Companion.getSHORT_DURATION(),
                    ResourcesCompat.getFont(getActivity().getApplicationContext(), R.font.montserrat));

        }//else
    }

    private void setStartDate(DatePickerDialog.OnDateSetListener startDate) {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), startDate, myCalendarStart
                .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                myCalendarStart.get(Calendar.DAY_OF_MONTH));

        myCalendarStart.set(Calendar.YEAR, (Calendar.getInstance().get(Calendar.YEAR)));
        myCalendarStart.set(Calendar.MONTH, (Calendar.getInstance().get(Calendar.MONTH)) - 6);//
        myCalendarStart.set(Calendar.DAY_OF_MONTH, (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));

        DocumentReference patientDoc = db.collection("Patient").document(mAuth.getUid());
        patientDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    first_fitbit = document.get("first_fitbit").toString();
                    Date FF = new Date();
                    Date today = new Date();

                    try {
                        FF = new SimpleDateFormat("dd/MM/yyyy").parse(first_fitbit);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    long different = today.getTime() - FF.getTime();
                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;
                    long daysInMilli = hoursInMilli * 24;

                    long elapsedDays = different / daysInMilli;

                    if(elapsedDays-1 >= 90 ){
                        Calendar cal2 = Calendar.getInstance();
                        cal2.add(Calendar.MONTH, -3);
                        // TODO : or first fitbit

                        datePickerDialog.getDatePicker().setMinDate(cal2.getTimeInMillis());

                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, -1);
                        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                        datePickerDialog.show();

                    }
                    else{
                        int days = (int) elapsedDays;
                        Calendar cal2 = Calendar.getInstance();
                        cal2.add(Calendar.DATE, - days);
                        // TODO : or first fitbit

                        datePickerDialog.getDatePicker().setMinDate(cal2.getTimeInMillis());

                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, -1);
                        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                        datePickerDialog.show();
                    }

                    }
                }

            });





    }

    public boolean isStartDateSet(String startDate) {
        if (startDate == null) {
            return false;
        }
        return true;
    }

    public boolean isStartDEqualToCurrent(Calendar calenderMillis) {
        Calendar currentTime = Calendar.getInstance();
        if (calenderMillis.get(Calendar.YEAR) == currentTime.get(Calendar.YEAR)
                && calenderMillis.get(Calendar.MONTH) == currentTime.get(Calendar.MONTH)
                && calenderMillis.get(Calendar.DAY_OF_MONTH)+1 == currentTime.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }

    public int isDatesChosen(String startDate, String endDate) {
        if (startDate == null && endDate == null) {
            return 1;
        }
        //if (startDate == null && endDate != null) {return 1;}
        if ( endDate == null) {
            return -1;
        }
        return 0;
    }

    public void dialog(String text) {

        MotionToast.Companion.darkToast(
                getActivity(),
                text,
                MotionToast.Companion.getTOAST_WARNING(),
                MotionToast.Companion.getGRAVITY_BOTTOM(),
                MotionToast.Companion.getSHORT_DURATION(),
                ResourcesCompat.getFont(getActivity().getApplicationContext(), R.font.montserrat));

    }


    /** Functions to handle calling API and generating the report **/
    Intent intent;
    private void generateReport() {
        Info.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(VISIBLE);

        // error dialog if no selection is made
        // intent to next activity
        intent = new Intent(getContext(), PatientReport.class);
        intent.putExtra(Constants.Keys.DURATION, duration);

        //startActivity(intent);
        if (duration.equals("custom")) {

            if (isDatesChosen(startDate, endDate) == 0) {

                intent.putExtra(Constants.Keys.START_DATE, startDate);
                intent.putExtra(Constants.Keys.END_DATE, endDate);

                Info.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(VISIBLE);


                if (GoogleCalendar) {
                    uploadGoogleEvents();
                }else{
                    callAPI();
                }

            } else {

                switch (isDatesChosen(startDate, endDate)) {

                    //here the only missing is the end StartDate
                    case -1: {
                        String text = String.format(res.getString(R.string.date_pickerrrr));
                        dialog(text);
                    }
                    //here the missing are the tow dates
                    case 1: {
                        String text = String.format(res.getString(R.string.date_pickerr));
                        dialog(text);
                        break;
                    }

                }

            }//if

        }//bigger if
        else {
            Info.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(VISIBLE);

            setDates();

            if (GoogleCalendar) {
                uploadGoogleEvents();
            }else{
                callAPI();
            }

        }
    }//generateReport

    private void setDates() {

        Date startD, endD;
        String start = "", end;
        
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.DATE, -1);
        endD = cale.getTime();

        myCalendarEnd.set(Calendar.YEAR, 2020);
        myCalendarEnd.set(Calendar.MONTH, endD.getMonth()+1);
        myCalendarEnd.set(Calendar.DAY_OF_MONTH, endD.getDay());
        
        Calendar cal = Calendar.getInstance();

        switch (duration) {
            case "14":

                cal.add(Calendar.DATE, -14);
                startD = cal.getTime();
                myCalendarStart.set(Calendar.YEAR, 2020);
                myCalendarStart.set(Calendar.MONTH, startD.getMonth());
                myCalendarStart.set(Calendar.DAY_OF_MONTH, startD.getDay());
                break;

            case "30":

                cal.add(Calendar.MONTH, -1);
                startD = cal.getTime();
                myCalendarStart.set(Calendar.YEAR, 2020);
                myCalendarStart.set(Calendar.MONTH, startD.getMonth()+1);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, startD.getDay());
                break;

        }//end of switch

        
    }

    private void callAPI() {
        tag("AppInfo").d("callAPI");

        if(duration.equals("custom")){
            api_url = "https://e8a76a2c.ngrok.io/patient_report_custom_duration/"+mAuth.getUid()+"/"+apiStartDate+"/"+apiEndDate+"/"+GoogleCalendar;
        }else{
            api_url = "https://e8a76a2c.ngrok.io/patient_report/"+mAuth.getUid()+"/"+duration+"/"+GoogleCalendar;
        }

        executeApi();

    }

    private void executeApi(){

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, PERMISSION_INTERNET);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, PERMISSION_ACCESS_NETWORK_STATE);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                api_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("LOG", "success: " + response.toString());

                        try {
                            // Info to visualize the graph in the PatientReport Page
                            JSONArray Dates = response.getJSONArray("date");
                            JSONArray AnxietyLevel = response.getJSONArray("Anxiety");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Start new Activity which present the graph
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Processing Failed" , Toast.LENGTH_LONG).show();
                        Log.e("LOG","ERROR: "+error.toString() );
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                }

        );

        objectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 1000000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 1000000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(objectRequest);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_INTERNET: {
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, PERMISSION_INTERNET);
                }
                return;
            }
            case PERMISSION_ACCESS_NETWORK_STATE: {
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, PERMISSION_ACCESS_NETWORK_STATE);
                }
                return;
            }
        }
    }


    /** Functions to handle retrieving the events from google calendar **/

    private void initGoogleApi() {

        credential =
                GoogleAccountCredential.usingOAuth2(getActivity(), Collections.singleton(CalendarScopes.CALENDAR));

        String accountName = Prefs.getString(PREF_ACCOUNT_NAME, "");
        if (!accountName.isEmpty()) {
            credential.setSelectedAccountName(accountName);

            client = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential).setApplicationName("SERENE")
                    .build();

            GoogleCalendar = true;

        } else {
            GoogleCalendar = false;
        }

    }

    private void uploadGoogleEvents() {
        tag("AppInfo").d("uploadGoogleEvents");


        if (GoogleCalendar){

            // there is already an account selected do (Read & upload) if not do nothing
            if (credential.getSelectedAccountName() != null) {

                // load events from calendar
                readCalendar();

            }

        }// if google calendar true


    }

    void readCalendar() {
        tag("AppInfo").d("readCalendar");
        // Reads the calendar events using AsyncTask
        new ReportFragment.LoadCalendarAsyncTask().execute();
    }

    private class LoadCalendarAsyncTask extends AsyncTask<Void, Void, List<Event>> {


        LoadCalendarAsyncTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Event> doInBackground(Void... args) {
            List<Event> listOfEvents = new ArrayList<>();

            try {

                CalendarList feed = client.calendarList().list().setFields(FEED_FIELDS).execute();
                Log.i("AppInfo", "number of items: " + feed.getItems().size());

                for (CalendarListEntry item : feed.getItems()) {
                    Log.i("AppInfo", "ID: " + item.getId() + " - Summary: " + item.getSummary());
                }

                // Take the start and end date (duration of report)
                Date startDate = myCalendarStart.getTime();
                Date endDate = myCalendarEnd.getTime();

                DateTime startRange = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
                DateTime endRange = new DateTime(endDate, TimeZone.getTimeZone("UTC"));

                
                Events events = client.events().list(feed.getItems().get(0).getId())
                        .setMaxResults(100)
                        .setTimeMin(startRange)
                        .setTimeMax(endRange)
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();

                List<com.google.api.services.calendar.model.Event> items = events.getItems();

                for (com.google.api.services.calendar.model.Event event : items) {
                    if (event != null) {

                        DateTime start = event.getStart().getDateTime();
                        if (start == null) {
                            start = event.getStart().getDate();
                        }

                        Log.i("AppInfo", "ID: " + event.getId() + " - Summary: " + event.getSummary() + " - Start: " + start);


                        Event newEvent = new Event();

                        newEvent.setSummary(event.getSummary());

                        Long dt = start.getValue();
                        String DATE_FORMAT_NOW = "yyy/MM/dd hh:mm aa";
                        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                        String s = sdf.format(dt);
                        newEvent.setStartTime(s);


                        listOfEvents.add(newEvent); // list of event to be upload it to the DB
                    }
                }

                if (listOfEvents.size() > 0) {
                    Log.i("AppInfo", "Uploading new events");

                    uploadNewEvents(listOfEvents);

                }else{

                    // no new events
                        callAPI();

                }

            } catch (UserRecoverableAuthIOException ex) {
                startActivityForResult(ex.getIntent(), REQUEST_AUTHORIZATION);

            } catch (IOException e) {
                e.printStackTrace();
            }


            return listOfEvents;
        }


        @Override
        protected void onPostExecute(List<Event> newEvents) {
            super.onPostExecute(newEvents);

            Log.i("AppInfo", "Number of Events: " + newEvents.size());

        }

   }

    int i;
    boolean done = false;
    private void uploadNewEvents(final List<Event> newEvents) {

        tag("AppInfo").d("uploadNewEvents");

        for ( i = 0 ; i < newEvents.size() ; i++ ){

            Map<String, Object> event = new HashMap<>();
            event.put("patientID", mAuth.getUid());
            event.put("name", newEvents.get(i).getSummary());
            event.put("date", newEvents.get(i).getStartTime());
            event.put("anxietyLevel", "Not given");

            DocumentReference ref = db.collection("PatientEvents").document(getRandomID());

            ref.set(event)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            // Success upload event

                            if ( i == newEvents.size() && !done  ) { // last event saved successfully -> then call API here

                                callAPI();
                                done = true;
                            }

                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Resources res = getResources();
                            String text = String.format(res.getString(R.string.TDSavedSuccess));

                            MotionToast.Companion.darkToast(
                                    getActivity(),
                                    "Error loading",
                                    MotionToast.Companion.getTOAST_ERROR(),
                                    MotionToast.Companion.getGRAVITY_BOTTOM(),
                                    MotionToast.Companion.getLONG_DURATION(),
                                    ResourcesCompat.getFont( getContext(), R.font.montserrat));

                        }
                    });

        }

    }

    private String getRandomID() {
        return UUID.randomUUID().toString();
    }


}// class
