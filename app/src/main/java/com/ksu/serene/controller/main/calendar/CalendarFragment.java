//Floating buttons from this video
//https://www.youtube.com/watch?v=Cys_i-6Pu-o

package com.ksu.serene.controller.main.calendar;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Events;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.model.Event;
import com.ksu.serene.model.Medicine;
import com.ksu.serene.model.Reminder;
import com.ksu.serene.model.TherapySession;
import com.ksu.serene.R;

import com.pixplicity.easyprefs.library.Prefs;


import java.io.IOException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Date;

import static android.view.View.*;

public class CalendarFragment extends Fragment {

    private Context context = this.getContext();
    private TextView dayN, day, month, noMed, noApp, noMedApp, appTV, medTV, googleEventsTV, noGoogleEvents;
    private ScrollView scrollView;
    //floating buttons
    private FloatingActionButton add, addMed, addApp;
    private Animation fabOpen, fabClose, rotateFor, rotateBac;
    private boolean isopen = false;

    //set for patient's appointments
    private String patientId;
    private RecyclerView recyclerViewSession;
    private RecyclerView.LayoutManager ApplayoutManager;
    private List<TherapySession> listAppointements;
    private PatientAppointmentAdapter adapterSession;
    private String AppID, AppName, AppDay, AppTime;
    private Date ADay;
    private Date ATime;
    private RecyclerView RecyclerViewEvents;

    //set for patient's medicines
    private RecyclerView recyclerViewMedicine;
    private RecyclerView.LayoutManager MlayoutManager;
    private List<Medicine> listMedicines;
    private PatientMedicineAdapter adapterMedicines;
    private String MID, MName, MFDay, MLDay, MTime;
    private int MDose;
    private long MPeriod;
    private Date FDay, LDay, time;
    private String date;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
    private Date currentDate = Calendar.getInstance().getTime();

    // Add Buttons
    private CalendarView calenderView;
    private Calendar calendar = Calendar.getInstance();
    private ArrayList<Reminder> reminders = new ArrayList<>();
    private View root;

    Calendar currentCalendar;
    ConstraintLayout.LayoutParams medMargins;
    ConstraintLayout.LayoutParams eventMargins;

    GoogleAccountCredential credential;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    final HttpTransport transport = new com.google.api.client.http.javanet.NetHttpTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    static final int REQUEST_AUTHORIZATION = 1;
    static final int REQUEST_ACCOUNT_PICKER = 2;
    com.google.api.services.calendar.Calendar client;
    PatientEventAdapter eventAdapter;
    List<Event> events = new ArrayList<>();

    static final String FIELDS = "id,summary";
    static final String FEED_FIELDS = "items(" + FIELDS + ")";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_calendar, container, false);

        init(root);

        // Initialize Google API
        initGoogleApi();
        updateAppointments();

        calendar.set(2019, 1, 1);

        calenderView.setMinDate(calendar.getTimeInMillis());
        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                recyclerViewSession.setVisibility(GONE);
                recyclerViewMedicine.setVisibility(GONE);
                appTV.setVisibility(VISIBLE);
                medTV.setVisibility(VISIBLE);
                noMed.setVisibility(VISIBLE);
                noApp.setVisibility(VISIBLE);
                noMedApp.setVisibility(GONE);
                medMargins.topMargin = 250;
                medTV.setLayoutParams(medMargins);
                eventMargins.topMargin = 250;
                googleEventsTV.setLayoutParams(eventMargins);


                SetAppRecyView(root, i, i1 + 1, i2);
                SetMedRecyView(root, i, i1 + 1, i2);


                //scrollView.smoothScrollTo(0,100);


                Calendar today = Calendar.getInstance();
                today.set(i, i1, i2);

                day.setText(i2 + "");
                dayN.setText(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(today.getTime()));
                month.setText(new SimpleDateFormat("MMMM", Locale.ENGLISH).format(today.getTime()));

                currentCalendar = today;

                updateAppointments();


                String simpleDateFormat = sdf.format(currentDate);
                int yearCurrent = Integer.parseInt(simpleDateFormat.substring(6, simpleDateFormat.length()));
                //check when calendar date in past
                date = checkCalendarDate (i2, (i1+1) , i);
                if ( date == null){
                    Log.d("Past Date",i2+"/"+(i1+1)+"/"+i);
                }

                /*if ((yearCurrent > i) ||
                        (yearCurrent == i && (currentDate.getMonth() + 1) > (i1 + 1))
                        || (yearCurrent == i && (currentDate.getMonth() + 1) == (i1 + 1) && currentDate.getDate() > i2)) {
                    Log.d("Past", "Calendar Time");
                    date = null;
                } else {
                    if ((i1 + 1) < 10) {
                        if (i2 > 10)
                            date = i2 + "/0" + (i1 + 1) + "/" + i;
                        else if (i2 < 10)
                            date = "0" + i2 + "/0" + (i1 + 1) + "/" + i;
                    } else {
                        if (i2 > 10)
                            date = i2 + "/" + (i1 + 1) + "/" + i;
                        else if (i2 < 10)
                            date = "0" + i2 + "/" + (i1 + 1) + "/" + i;
                    }
                }*/

            }
        });

        Date today = currentCalendar.getTime();

        String i2 = new SimpleDateFormat("d", Locale.ENGLISH).format(today.getTime());
        String i1 = new SimpleDateFormat("MM", Locale.ENGLISH).format(today.getTime());
        String i = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(today.getTime());

        day.setText(i2);
        dayN.setText(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(today.getTime()));
        month.setText(new SimpleDateFormat("MMMM", Locale.ENGLISH).format(today.getTime()));


        SetAppRecyView(root, Integer.parseInt(i), Integer.parseInt(i1), Integer.parseInt(i2));
        SetMedRecyView(root, Integer.parseInt(i), Integer.parseInt(i1), Integer.parseInt(i2));

//        if (noAppointment && noMedicine){
//            recyclerViewMedicine.setVisibility(GONE);
//            recyclerViewSession.setVisibility(GONE);
//            noMed.setVisibility(GONE);
//            noApp.setVisibility(GONE);
//            appTV.setVisibility(GONE);
//            medTV.setVisibility(GONE);
//            noMedApp.setVisibility(VISIBLE);
//        }


        return root;
    }

    private void initGoogleApi() {

        credential =
                GoogleAccountCredential.usingOAuth2(getActivity(), Collections.singleton(CalendarScopes.CALENDAR));

        String accountName = Prefs.getString(PREF_ACCOUNT_NAME, "");
        if (!accountName.isEmpty()) {
            credential.setSelectedAccountName(accountName);

            client = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential).setApplicationName("SERENE")
                    .build();

        }else{
            googleEventsTV.setVisibility(GONE);
            noGoogleEvents.setVisibility(GONE);
            RecyclerViewEvents.setVisibility(GONE);
        }

    }


    private void updateAppointments() {

        // check if there is already an account selected
        String accountName = Prefs.getString(PREF_ACCOUNT_NAME, "");
        if (credential.getSelectedAccountName() == null) {
            // ask user to choose account
            // chooseAccount();
        } else {
            // load calendars
            readCalendar();
        }

    }

    /**
     * Reads the calendar events using AsyncTask
     */
    void readCalendar() {
        new LoadCalendarAsyncTask().execute();
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
            List<Event> events1 = new ArrayList<>();
            try {
                CalendarList feed = client.calendarList().list().setFields(FEED_FIELDS).execute();
                Log.i("AppInfo", "number of items: " + feed.getItems().size());

                for (CalendarListEntry item : feed.getItems()) {
                    Log.i("AppInfo", "ID: " + item.getId() + " - Summary: " + item.getSummary());
                }

                DateTime min = new DateTime(currentCalendar.getTimeInMillis());
                DateTime max = new DateTime(currentCalendar.getTimeInMillis()+(24 * 60 * 60 * 1000));

                Log.i("AppInfo", "min: " + min.toString());
                Log.i("AppInfo", "max: " + max.toString());

                Events events = client.events().list(feed.getItems().get(0).getId())
                        .setMaxResults(10)
                        .setTimeMin(min)
                        .setTimeMax(max)
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


                        Event event1 = new Event();
                        event1.setSummary(event.getSummary());
                        Long dt = start.getValue();

                        String DATE_FORMAT_NOW = "h:mm aa";
                        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                        String s = sdf.format(dt);


                        event1.setStartTime(s);
                        events1.add(event1);
                    }
                }

            } catch (UserRecoverableAuthIOException ex) {
                startActivityForResult(ex.getIntent(), REQUEST_AUTHORIZATION);

            } catch (IOException e) {
                e.printStackTrace();
            }


            return events1;
        }


        @Override
        protected void onPostExecute(List<Event> appts) {
            super.onPostExecute(appts);

            Log.i("AppInfo", "Number of Appts: " + appts.size());

            if (appts.size() > 0) {
                Log.i("AppInfo", "Clearing appointments");
                googleEventsTV.setVisibility(VISIBLE);
                RecyclerViewEvents.setVisibility(VISIBLE);
                events.clear();
                events.addAll(appts);
                eventAdapter.notifyDataSetChanged();
                noGoogleEvents.setVisibility(GONE);
            } else {
                googleEventsTV.setVisibility(VISIBLE);
                RecyclerViewEvents.setVisibility(GONE);
                noGoogleEvents.setVisibility(VISIBLE);

            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {
                    updateAppointments();
                } else {
                    checkGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == Activity.RESULT_OK) {
                    //AsyncLoadCalendars.run(this);
                    readCalendar();
                } else {
                    chooseAccount();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);

                        Prefs.putString(PREF_ACCOUNT_NAME, accountName);
                        //AsyncLoadCalendars.run(this);
                        readCalendar();
                    }
                }
                break;

        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode, getActivity(), REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     */
    private boolean checkGooglePlayServicesAvailable() {
        final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        }
        return true;
    }


    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }


    private void init(View root) {

        //retrieve the id of patient used for searching
        patientId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //floating buttons
        add = root.findViewById(R.id.button_expandable_110_250);
        addMed = root.findViewById(R.id.AddMedButton);
        addApp = root.findViewById(R.id.AddAppButton);


        //by defual hide
        addMed.hide();
        addApp.hide();

        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotateFor = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_anticlock);
        rotateBac = AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_clock);

        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        addMed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Add_Medicine_Page.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        addApp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentM = new Intent(getContext(), Add_Appointment_Page.class);
                intentM.putExtra("date", date);
                startActivity(intentM);
            }
        });


        //set calender view
        calenderView = root.findViewById(R.id.calendarView2);

        day = root.findViewById(R.id.day);
        dayN = root.findViewById(R.id.dayN);
        month = root.findViewById(R.id.month);

        noApp = root.findViewById(R.id.noAppointment);
        noMed = root.findViewById(R.id.noMedicine);
        noMedApp = root.findViewById(R.id.noMedApp);
        noMedApp.setVisibility(GONE);

        appTV = root.findViewById(R.id.text1);
        medTV = root.findViewById(R.id.text2);

        scrollView = root.findViewById(R.id.scrollView);

        googleEventsTV = root.findViewById(R.id.text3);
        noGoogleEvents  = root.findViewById(R.id.noGoogleEvents);
        RecyclerViewEvents = root.findViewById(R.id.RecyclerViewEvents);
        eventAdapter = new PatientEventAdapter(getContext(), events);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerViewEvents.setLayoutManager(layoutManager);
        RecyclerViewEvents.setAdapter(eventAdapter);


        currentCalendar = Calendar.getInstance();



        medMargins = (ConstraintLayout.LayoutParams) medTV.getLayoutParams();
        eventMargins = (ConstraintLayout.LayoutParams) googleEventsTV.getLayoutParams();

    }


    private void SetAppRecyView(View root, final int year, final int month, final int day) {


        final SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
        final SimpleDateFormat TimeFormat = new SimpleDateFormat("hh : mm");

        //retrieve Patient Session data
        recyclerViewSession = root.findViewById(R.id.RecyclerviewSession);
        ApplayoutManager = new LinearLayoutManager(context);
        recyclerViewSession.setLayoutManager(ApplayoutManager);
        listAppointements = new ArrayList<>();
        adapterSession = new PatientAppointmentAdapter(listAppointements, new PatientAppointmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TherapySession item) {
                Intent intent = new Intent(getContext(), PatientAppointmentDetailPage.class);
                intent.putExtra("AppointmentID", item.getId());
                startActivity(intent);
            }
        });

        //search in cloud firestore for patient sessions
        CollectionReference referenceSession = FirebaseFirestore.getInstance().collection("PatientSessions");

        final Query queryPatientSession = referenceSession.whereEqualTo("patinetID", patientId);

        queryPatientSession.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        AppID = document.getId();
                        AppName = document.get("name").toString();
                        AppDay = document.get("date").toString();
                        AppTime = document.get("time").toString();

                        Timestamp DTS = (Timestamp) document.get("dateTimestamp");
                        Calendar appCalender = Calendar.getInstance();
                        appCalender.setTimeInMillis(DTS.getSeconds() * 1000);

                        //convert string to date to used in compare
                        try {
                            ADay = DateFormat.parse(AppDay);
                            ATime = TimeFormat.parse(AppTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if ((appCalender.get(Calendar.YEAR) == year &&
                                (appCalender.get(Calendar.MONTH) + 1) == month &&
                                appCalender.get(Calendar.DAY_OF_MONTH) == day)) {
                            listAppointements.add(new TherapySession(AppID, AppName, AppDay, AppTime));

                            recyclerViewSession.setVisibility(VISIBLE);
                            appTV.setVisibility(VISIBLE);
                            noApp.setVisibility(GONE);
                            noMedApp.setVisibility(GONE);
                            medMargins.topMargin = 420;
                            medTV.setLayoutParams(medMargins);

                        }
                    }
                    adapterSession.notifyDataSetChanged();
                }
            }
        });
        recyclerViewSession.setAdapter(adapterSession);


    }


    private void SetMedRecyView(View root, final int year, final int month, final int day) {


        final SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
        final SimpleDateFormat TimeFormat = new SimpleDateFormat("hh : mm");

        //retrieve Mediciens data
        recyclerViewMedicine = root.findViewById(R.id.RecyclerViewMedicine);
        MlayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMedicine.setLayoutManager(MlayoutManager);
        listMedicines = new ArrayList<>();
        adapterMedicines = new PatientMedicineAdapter(listMedicines, new PatientMedicineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Medicine item) {
                Intent intent = new Intent(getContext(), PatientMedicineDetailPage.class);
                intent.putExtra("MedicineID", item.getId());
                startActivity(intent);
            }
        });

        //search in firebase for patientsessions
        CollectionReference referenceMedicine = FirebaseFirestore.getInstance().collection("PatientMedicin");

        final Query queryPatientMedicine = referenceMedicine.whereEqualTo("patinetID", patientId);
        queryPatientMedicine.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Medicine medicine = document.toObject(Medicine.class);
                        //listMedicines.add(medicine);
                        MID = document.getId();
                        MName = document.get("name").toString();
                        MFDay = document.get("Fday").toString();
                        MLDay = document.get("Lday").toString();
                        MTime = document.get("time").toString();
                        MDose = Integer.parseInt(document.get("doze").toString());
                        MPeriod = Long.parseLong(document.get("period").toString());
                        Timestamp FDTS = (Timestamp) document.get("FirstDayTS");
                        calendar.setTimeInMillis(FDTS.getSeconds() * 1000);
                        Timestamp LDTS = (Timestamp) document.get("LastDayTS");
                        Calendar lCalender = Calendar.getInstance();
                        lCalender.setTimeInMillis(LDTS.getSeconds() * 1000);

                        //convert string to date to used in compare
                        try {
                            FDay = DateFormat.parse(MFDay);
                            LDay = DateFormat.parse(MLDay);
                            time = TimeFormat.parse(MTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //when the first day == last day or not for the first day and last day
                        if ((calendar.get(Calendar.YEAR) == year && (calendar.get(Calendar.MONTH) + 1) == month && calendar.get(Calendar.DAY_OF_MONTH) == day)
                                || (lCalender.get(Calendar.YEAR) == year && (lCalender.get(Calendar.MONTH) + 1) == month && lCalender.get(Calendar.DAY_OF_MONTH) == day)) {
                            listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                            viewMedicines();

                        }
                        //when first day not equal to last day
                        //for the between of them
                        if (FDay.compareTo(LDay) != 0) {

                            //when the year and month of first and last same
                            if (calendar.get(Calendar.YEAR) == lCalender.get(Calendar.YEAR)
                                    && year == calendar.get(Calendar.YEAR) && year == lCalender.get(Calendar.YEAR)
                                    && (calendar.get(Calendar.MONTH) + 1) == (lCalender.get(Calendar.MONTH) + 1)
                                    && month == (calendar.get(Calendar.MONTH) + 1) && month == (lCalender.get(Calendar.MONTH) + 1)) {
                                //check the calneder view day is grater than first day and less than last day
                                if (day > calendar.get(Calendar.DAY_OF_MONTH) && day < lCalender.get(Calendar.DAY_OF_MONTH)) {
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                    viewMedicines();

                                }
                            }
                            //when the year for first and last same
                            else if (calendar.get(Calendar.YEAR) == lCalender.get(Calendar.YEAR)
                                    && year == calendar.get(Calendar.YEAR) && year == lCalender.get(Calendar.YEAR)
                                    && (calendar.get(Calendar.MONTH) + 1) != (lCalender.get(Calendar.MONTH) + 1)) {
                                //check calender view month is grater  than first and less than  last
                                if (month > (calendar.get(Calendar.MONTH) + 1) && month < (lCalender.get(Calendar.MONTH) + 1)) {
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                    viewMedicines();

                                }
                                //check the day is grater than first when same month
                                else if (month == (calendar.get(Calendar.MONTH) + 1) && day > calendar.get(Calendar.DAY_OF_MONTH)) {
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                    viewMedicines();

                                }
                                //check the day is less than first when same month
                                else if (month == (lCalender.get(Calendar.MONTH) + 1) && day < lCalender.get(Calendar.DAY_OF_MONTH)) {
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                    viewMedicines();

                                }
                            }
                            //when the year not same for first and last
                            else if (calendar.get(Calendar.YEAR) != lCalender.get(Calendar.YEAR) && calendar.get(Calendar.YEAR) < lCalender.get(Calendar.YEAR)) {
                                //calender view year same as first and month same day should be grater
                                if (year == calendar.get(Calendar.YEAR) && month == (calendar.get(Calendar.MONTH) + 1) && day > calendar.get(Calendar.DAY_OF_MONTH)) {
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                    viewMedicines();

                                }
                                //if year same as first and month not same should be grater than first
                                else if (year == calendar.get(Calendar.YEAR) && month > (calendar.get(Calendar.MONTH) + 1)) {
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                    viewMedicines();

                                }
                                //calender view year same as last and month same day should be less
                                else if (year == lCalender.get(Calendar.YEAR) && month == (lCalender.get(Calendar.MONTH) + 1) && day < lCalender.get(Calendar.DAY_OF_MONTH)) {
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                    viewMedicines();

                                }
                                //if year same as last and month not same should be less than first
                                else if (year == lCalender.get(Calendar.YEAR) && month < (lCalender.get(Calendar.MONTH) + 1)) {
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                    viewMedicines();

                                }
                                // if year not same as first should be grater than first and less than last
                                else if (year < lCalender.get(Calendar.YEAR) && year > calendar.get(Calendar.YEAR)) {
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                    viewMedicines();

                                }
                            }
                        }
                    }
                    adapterMedicines.notifyDataSetChanged();
                }
            }
        });
        recyclerViewMedicine.setAdapter(adapterMedicines);

    }

    private void viewMedicines() {
        recyclerViewMedicine.setVisibility(VISIBLE);
        medTV.setVisibility(VISIBLE);
        noMed.setVisibility(GONE);
        noMedApp.setVisibility(GONE);
        eventMargins.topMargin = 580;
        googleEventsTV.setLayoutParams(eventMargins);

    }

    private void animateFab() {
        if (isopen) {
            add.startAnimation(rotateFor);
            addMed.setClickable(false);
            addApp.setClickable(false);
            addMed.hide();
            addApp.hide();
            isopen = false;
        } else {
            add.startAnimation(rotateBac);
            addMed.show();
            addApp.show();
            addMed.startAnimation(fabOpen);
            addApp.startAnimation(fabOpen);
            addMed.setClickable(true);
            addApp.setClickable(true);
            isopen = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        isopen = true;
        animateFab();
        addMed.hide();
        addApp.hide();

        /*if (year != 0 && month!=0 && day!= 0){
            listAppointements.clear();
            listMedicines.clear();
            SetAppRecyView(root, year, month, day);
            SetMedRecyView(root, year, month, day);
        }*/
    }

    public String checkCalendarDate (int day, int month, int year) {
        Calendar current = Calendar.getInstance();
        if ( ( year > current.get(Calendar.YEAR) )
                || ( year == current.get(Calendar.YEAR) && month > (current.get(Calendar.MONTH)+1) )
                || ( year == current.get(Calendar.YEAR) && month == (current.get(Calendar.MONTH)+1) && day > current.get(Calendar.DAY_OF_MONTH) )
                ||  year == current.get(Calendar.YEAR) && month == (current.get(Calendar.MONTH)+1) && day == current.get(Calendar.DAY_OF_MONTH) ) {
            if ( month < 10) {
                if (day > 10)
                    return day + "/0" + month + "/" + year;
                else if (day < 10)
                    return "0" + day + "/0" + month + "/" + year;
            }
            else {
                if (day > 10)
                    return day + "/" + month + "/" + year;
                else if (day < 10)
                    return "0" + day + "/" + month + "/" + year;
            }
        }
        return null;
    }

}
