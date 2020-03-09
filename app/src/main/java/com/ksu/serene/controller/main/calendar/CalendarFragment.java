package com.ksu.serene.controller.main.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.model.Medicine;
import com.ksu.serene.model.Reminder;
import com.ksu.serene.model.TherapySession;
import com.ksu.serene.R;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Date;

public class CalendarFragment extends Fragment implements View.OnClickListener{

    private Context context = this.getContext();
    //set for patient's appointments
    private String patientId;
    private RecyclerView recyclerViewSession;
    private RecyclerView.LayoutManager ApplayoutManager;
    private List<TherapySession> listAppointements;
    private PatientSessionsAdapter adapterSession;
    private String AppID;
    private String AppName;
    private String AppDay;
    private String AppTime;
    private Date ADay;
    private Date ATime;
    //set for patient's medicines
    private RecyclerView recyclerViewMedicine;
    private RecyclerView.LayoutManager MlayoutManager;
    private List<Medicine> listMedicines;
    private PatientMedicineAdapter adapterMedicines;
    private String MID;
    private String MName;
    private String MFDay;
    private String MLDay;
    private String MTime;
    private int MDose;
    private long MPeriod;
    private Date FDay;
    private Date LDay;
    private Date time;
    // Add Buttons
    FloatingActionButton addButton, addMedicine, addAppointment;
    TextView TV_appointment, TV_medicine;
    private Animation fab_clock, fab_anti_clock;
    private boolean isFABOpen;
    private Button AddMed;
    private Button AddApp;
    private CalendarView calenderView;
    private Calendar calendar = Calendar.getInstance() ;
    private ArrayList<Reminder> reminders = new ArrayList<>() ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        //set for layout.xml
        addButton = root.findViewById(R.id.addButton);
        addMedicine = root.findViewById(R.id.add_Appointment);
        addAppointment= root.findViewById(R.id.add_Medicine);
        addButton.setOnClickListener(this);
        addMedicine.setOnClickListener(this);
        addAppointment.setOnClickListener(this);
        TV_appointment = root.findViewById(R.id.TV_appointment);
        TV_medicine = root.findViewById(R.id.TV_medicine);
        isFABOpen = false;
        fab_clock = AnimationUtils.loadAnimation( getContext(), R.anim.fab_rotate_clock );
        fab_anti_clock = AnimationUtils.loadAnimation( getContext(), R.anim.fab_rotate_anticlock );
        AddMed = root.findViewById(R.id.Add_Med);
        AddApp = root.findViewById(R.id.Add_App);

        //set calender view
        calenderView =  root.findViewById(R.id.calendarView2);
        calendar.set(2015,1,1);
        calenderView.setMinDate(calendar.getTimeInMillis());
        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                Log.d("calender", i+"/"+(i1+1)+"/"+i2);
                Log.d("try", calendarView.getDateTextAppearance()+"");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                LayoutInflater lf = getLayoutInflater();
                final View cardview = lf.inflate(R.layout.activity_card_calender_view, null);
                SetCelenderDialog (cardview, i, i1+1, i2);
                builder.setView(cardview);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        AddMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Add_Medicine_Page.class);
                startActivity(intent);
            }
        });

        AddApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Add_Appointment_Page.class);
                startActivity(intent);
            }
        });

        //retrieve the id of patient used for searching
        patientId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SetAppRecyView (root);
        SetMedRecyView (root);


        return root;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.addButton:
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
                break;

            case R.id.add_Appointment:
                Intent i = new Intent( getContext(), Add_Appointment_Page.class);
                startActivity(i);
                break;

            case R.id.add_Medicine:
                Intent intent = new Intent( getContext(), Add_Medicine_Page.class);
                startActivity(intent);
                break;

            default:
                break;
        }//end switch
    }//end onClick


    private void showFABMenu() {

        isFABOpen = true;

        addButton.startAnimation(fab_clock);

        addAppointment.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        addMedicine.animate().translationY(-getResources().getDimension(R.dimen.standard_105));

        TV_appointment.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        TV_medicine.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        TV_appointment.setVisibility(View.VISIBLE);
        TV_medicine.setVisibility(View.VISIBLE);

    }

    private void closeFABMenu() {

        isFABOpen = false;

        addButton.startAnimation(fab_anti_clock);

        TV_medicine.setVisibility(View.INVISIBLE);
        TV_appointment.setVisibility(View.INVISIBLE);

        addMedicine.animate().translationY(0);
        addAppointment.animate().translationY(0);
        TV_medicine.animate().translationY(0);
        TV_appointment.animate().translationY(0);

    }

    private void SetAppRecyView (View root) {

        final SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
        final SimpleDateFormat TimeFormat = new SimpleDateFormat ("hh : mm");
        //retrieve Patient Session data
        recyclerViewSession = root.findViewById(R.id.RecyclerviewSession);
        ApplayoutManager = new LinearLayoutManager(context);
        recyclerViewSession.setLayoutManager(ApplayoutManager);
        listAppointements = new ArrayList<>();
        adapterSession = new PatientSessionsAdapter(listAppointements, new PatientSessionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TherapySession item) {
                Intent intent = new Intent(getContext() , PatientAppointmentDetailPage.class);
                intent.putExtra("AppointmentID" , item.getId());
                startActivity(intent);
            }
        });
        //search in cloud firestore for patient sessions
        CollectionReference referenceSession = FirebaseFirestore.getInstance().collection("PatientSessions");

        final Query queryPatientSession = referenceSession.whereEqualTo("patinetID",patientId);
        queryPatientSession.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        AppID = document.getId().toString();
                        AppName = document.get("name").toString();
                        AppDay = document.get("date").toString();
                        AppTime = document.get("time").toString();
                        //convert string to date to used in compare
                        try {
                            ADay = DateFormat.parse(AppDay);
                            ATime = TimeFormat.parse(AppTime);
                        }
                        catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        listAppointements.add(new TherapySession(AppID, AppName, AppDay, AppTime));
                        reminders.add(new Reminder(AppID, AppName, AppDay, AppTime));

                    }
                    adapterSession.notifyDataSetChanged();
                }
            }
        });
        recyclerViewSession.setAdapter(adapterSession);
    }


    private void SetMedRecyView (View root) {
        final SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
        final SimpleDateFormat TimeFormat = new SimpleDateFormat ("hh : mm");

        //retrieve Mediciens data
        recyclerViewMedicine = root.findViewById(R.id.RecyclerViewMedicine);
        MlayoutManager = new LinearLayoutManager(context);
        recyclerViewMedicine.setLayoutManager(MlayoutManager);
        listMedicines = new ArrayList<>();
        adapterMedicines = new PatientMedicineAdapter(listMedicines, new PatientMedicineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Medicine item) {
                Intent intent = new Intent(getContext() , PatientMedicineDetailPage.class);
                intent.putExtra("MedicineID", item.getId());
                startActivity(intent);
            }
        });

        //search in firebase for patientsessions
        CollectionReference referenceMedicine = FirebaseFirestore.getInstance().collection("PatientMedicin");

        final Query queryPatientMedicine = referenceMedicine.whereEqualTo("patinetID",patientId);
        queryPatientMedicine.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Medicine medicine = document.toObject(Medicine.class);
                        //listMedicines.add(medicine);
                        MID = document.getId().toString();
                        MName = document.get("name").toString();
                        MFDay = document.get("Fday").toString();
                        MLDay = document.get("Lday").toString();
                        MTime = document.get("time").toString();
                        MDose = Integer.parseInt(document.get("doze").toString());
                        MPeriod = Long.parseLong(document.get("period").toString());
                        //convert string to date to used in compare
                        try {
                            FDay = DateFormat.parse(MFDay);
                            LDay = DateFormat.parse(MLDay);
                            time = TimeFormat.parse(MTime);
                        }
                        catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                        reminders.add(new Reminder(MID, MName, MFDay+"-"+MLDay, MTime));
                    }
                    adapterMedicines.notifyDataSetChanged();
                }
            }
        });
        recyclerViewMedicine.setAdapter(adapterMedicines);
    }

    private void SetCelenderDialog (final View dialog, final int year, final int month, final int day) {
        final SimpleDateFormat f1 = new SimpleDateFormat("dd/MM/yy", Locale.US);
        final SimpleDateFormat TimeFormat = new SimpleDateFormat ("hh : mm");

        Button addMed = dialog.findViewById(R.id.AddMedR);
        Button addApp = dialog.findViewById(R.id.AddAppR);

        addMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dialog.getContext(), Add_Medicine_Page.class);
                startActivity(intent);
            }
        });

        addApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dialog.getContext(), Add_Appointment_Page.class);
                startActivity(intent);
            }
        });

        //retrieve Mediciens data
        recyclerViewMedicine = dialog.findViewById(R.id.RecyclerViewMedicine);
        MlayoutManager = new LinearLayoutManager(dialog.getContext());
        recyclerViewMedicine.setLayoutManager(MlayoutManager);
        final List<Medicine> lMed = new ArrayList<>();
        final PatientMedicineAdapter adapterMed = new PatientMedicineAdapter(lMed, new PatientMedicineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Medicine item) {
                Intent intent = new Intent(dialog.getContext() , PatientMedicineDetailPage.class);
                intent.putExtra("MedicineID", item.getId());
                startActivity(intent);
            }
        });
        //search in firebase for patientsessions
        CollectionReference referenceMedicine = FirebaseFirestore.getInstance().collection("PatientMedicin");
        final Query queryPatientMedicine = referenceMedicine.whereEqualTo("patinetID",patientId);
        queryPatientMedicine.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Medicine medicine = document.toObject(Medicine.class);
                        //listMedicines.add(medicine);
                        MID = document.getId().toString();
                        MName = document.get("name").toString();
                        MFDay = document.get("Fday").toString();
                        MLDay = document.get("Lday").toString();
                        MTime = document.get("time").toString();
                        MDose = Integer.parseInt(document.get("doze").toString());
                        MPeriod = Long.parseLong(document.get("period").toString());
                        Timestamp FDTS = (Timestamp) document.get("FirstDayTS");
                        calendar.setTimeInMillis(FDTS.getSeconds()*1000);
                        Timestamp LDTS = (Timestamp) document.get("LastDayTS");
                        Calendar lCalender = Calendar.getInstance();
                        lCalender.setTimeInMillis(LDTS.getSeconds()*1000);
                        //convert string to date to used in compare
                        try {
                            FDay = f1.parse(MFDay);
                            LDay = f1.parse(MLDay);
                            time = TimeFormat.parse(MTime);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Log.d("calender FD y/m/d", calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DAY_OF_MONTH));
                        Log.d("calender LD y/m/d", lCalender.get(Calendar.YEAR)+"/"+(lCalender.get(Calendar.MONTH)+1)+"/"+lCalender.get(Calendar.DAY_OF_MONTH));
                        //search if patient has med reminder in that date (get it from calender view)
                        if ( (calendar.get(Calendar.YEAR) == year && (calendar.get(Calendar.MONTH)+1) == month && calendar.get(Calendar.DAY_OF_MONTH) == day)
                        || (lCalender.get(Calendar.YEAR) == year && (lCalender.get(Calendar.MONTH)+1) == month && lCalender.get(Calendar.DAY_OF_MONTH) == day) ) {
                            lMed.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                            //also add med within the periods
                        }

                        else if ( calendar.get(Calendar.YEAR) == year && (calendar.get(Calendar.MONTH)+1) == month && calendar.get(Calendar.DAY_OF_MONTH) < day) {
                            if ( calendar.get(Calendar.YEAR) == lCalender.get(Calendar.YEAR)
                            && (calendar.get(Calendar.MONTH)+1) == (lCalender.get(Calendar.MONTH)+1)
                            && lCalender.get(Calendar.DAY_OF_MONTH) > day ) {
                                lMed.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                            }
                            else if (calendar.get(Calendar.YEAR) == lCalender.get(Calendar.YEAR) &&
                                    (calendar.get(Calendar.MONTH)+1) < (lCalender.get(Calendar.MONTH)+1) ){
                                lMed.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                            }
                            else if (calendar.get(Calendar.YEAR) < lCalender.get(Calendar.YEAR) &&
                                    (calendar.get(Calendar.MONTH)+1) <= (lCalender.get(Calendar.MONTH)+1) ){
                                lMed.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                            }
                        }
                        else if ( lCalender.get(Calendar.YEAR) == year ) {
                            if (lCalender.get(Calendar.MONTH)+1 == month && lCalender.get(Calendar.DAY_OF_MONTH) > day) {
                                lMed.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));}
                            else if (lCalender.get(Calendar.MONTH)+1 > month) {
                                lMed.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                            }
                        }
                        else if ( lCalender.get(Calendar.YEAR) > year ) {
                            if (((calendar.get(Calendar.MONTH)+1) == month && calendar.get(Calendar.DAY_OF_MONTH) < day )|| (calendar.get(Calendar.MONTH)+1) < month)
                            lMed.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                        }

                    }
                    adapterMed.notifyDataSetChanged();
                }
            }
        });
        recyclerViewMedicine.setAdapter(adapterMed);

        //retrieve Mediciens data
        final RecyclerView recyclerViewApp = dialog.findViewById(R.id.ReadApp);
        final RecyclerView.LayoutManager ApplayoutManager = new LinearLayoutManager(dialog.getContext());
        recyclerViewApp.setLayoutManager(ApplayoutManager);
        final List<TherapySession> lApp = new ArrayList<>();
        final PatientSessionsAdapter adapterApp = new PatientSessionsAdapter(lApp, new PatientSessionsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TherapySession item) {
                Intent intent = new Intent(getContext() , PatientAppointmentDetailPage.class);
                intent.putExtra("AppointmentID" , item.getId());
                startActivity(intent);
            }
        });
        //search in cloud firestore for patient sessions
        CollectionReference referenceSession = FirebaseFirestore.getInstance().collection("PatientSessions");

        final Query queryPatientSession = referenceSession.whereEqualTo("patinetID",patientId);
        queryPatientSession.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        AppID = document.getId().toString();
                        AppName = document.get("name").toString();
                        AppDay = document.get("date").toString();
                        AppTime = document.get("time").toString();
                        Timestamp DTS = (Timestamp) document.get("dateTimestamp");
                        Calendar appCalender = Calendar.getInstance();
                        appCalender.setTimeInMillis(DTS.getSeconds()*1000);
                        //convert string to date to used in compare
                        try {
                            ADay = f1.parse(AppDay);
                            ATime = TimeFormat.parse(AppTime);
                        }
                        catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Log.d("calender LD y/m/d", appCalender.get(Calendar.YEAR)+"/"+(appCalender.get(Calendar.MONTH)+1)+"/"+appCalender.get(Calendar.DAY_OF_MONTH));
                        //search if patient has med reminder in that date (get it from calender view)
                        if ( (appCalender.get(Calendar.YEAR) == year && (appCalender.get(Calendar.MONTH)+1) == month && appCalender.get(Calendar.DAY_OF_MONTH) == day) ) {
                            lApp.add(new TherapySession(AppID, AppName, AppDay, AppTime));
                        }
                    }
                    adapterApp.notifyDataSetChanged();
                }
            }
        });
        recyclerViewApp.setAdapter(adapterApp);
    }

}
