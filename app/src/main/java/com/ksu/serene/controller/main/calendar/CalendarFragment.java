//Floating buttons from this video
//https://www.youtube.com/watch?v=Cys_i-6Pu-o

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

import com.fangxu.allangleexpandablebutton.AllAngleExpandableButton;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.fangxu.allangleexpandablebutton.ButtonEventListener;
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
import com.ksu.serene.controller.main.drafts.AddTextDraftPage;
import com.ksu.serene.controller.main.drafts.AddVoiceDraftPage;
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

import static android.view.View.*;

public class CalendarFragment extends Fragment{

    private Context context = this.getContext();

    //floating buttons
    private  FloatingActionButton add, addMed, addApp;
    private Animation fabOpen, fabClose, rotateFor, rotateBac;
    private boolean isopen = false;

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
    private String date ;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
    private Date currentDate = Calendar.getInstance().getTime();

    // Add Buttons
    private CalendarView calenderView;
    private Calendar calendar = Calendar.getInstance() ;
    private ArrayList<Reminder> reminders = new ArrayList<>() ;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_calendar, container, false);



        //retrieve the id of patient used for searching
        patientId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //dec floating buttons
        add = root.findViewById(R.id.button_expandable_110_250);
        addMed = root.findViewById(R.id.AddMedButton);
        //by defual hide
        addMed.hide();
        addApp = root.findViewById(R.id.AddAppButton);
        addApp.hide();
        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotateFor= AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_anticlock);
        rotateBac= AnimationUtils.loadAnimation(getContext(), R.anim.fab_rotate_clock);
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
        calendar.set(2019,1,1);

        String today = new SimpleDateFormat("dd/mm/yyyy").format(new Date());


        calenderView.setMinDate(calendar.getTimeInMillis());
        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                SetAppRecyView (root,i, i1+1, i2);
                SetMedRecyView (root, i, i1+1, i2);
                //TODO check when the date in past do nothing
                String simpleDateFormat = sdf.format(currentDate);
                int yearCurrent = Integer.parseInt(simpleDateFormat.substring(6,simpleDateFormat.length()));
                //check when clendar date in past
                if ( (yearCurrent > i) ||
                        ( yearCurrent == i && (currentDate.getMonth()+1) > (i1+1) )
                        || (yearCurrent == i && (currentDate.getMonth()+1) == (i1+1) && currentDate.getDate() > i2) ){
                    Log.d("Past", "Calendar Time");
                    date = null;
                }
                else {
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
                }
                updateView();
            }
        });


        return root;
    }

    private void updateView() {

    }

    private void SetAppRecyView (View root,  final int year, final int month, final int day) {

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
                        AppID = document.getId();
                        AppName = document.get("name").toString();
                        AppDay = document.get("date").toString();
                        AppTime = document.get("time").toString();
                        Timestamp DTS = (Timestamp) document.get("dateTimestamp");
                        Calendar appCalender = Calendar.getInstance();
                        appCalender.setTimeInMillis(DTS.getSeconds()*1000);
                        //convert string to date to used in compare
                        try {
                            ADay = DateFormat.parse(AppDay);
                            ATime = TimeFormat.parse(AppTime);
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if ( (appCalender.get(Calendar.YEAR) == year && (appCalender.get(Calendar.MONTH)+1) == month && appCalender.get(Calendar.DAY_OF_MONTH) == day) ) {
                            listAppointements.add(new TherapySession(AppID, AppName, AppDay, AppTime));
                        }
                    }
                    adapterSession.notifyDataSetChanged();
                }
            }
        });

        recyclerViewSession.setAdapter(adapterSession);
    }


    private void SetMedRecyView (View root,final int year, final int month, final int day) {
        final SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
        final SimpleDateFormat TimeFormat = new SimpleDateFormat ("hh : mm");

        //retrieve Mediciens data
        recyclerViewMedicine = root.findViewById(R.id.RecyclerViewMedicine);
        MlayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
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
                        MID = document.getId();
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
                            FDay = DateFormat.parse(MFDay);
                            LDay = DateFormat.parse(MLDay);
                            time = TimeFormat.parse(MTime);
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //when the first day == last day or not for the first day and last day
                        if ( (calendar.get(Calendar.YEAR) == year && (calendar.get(Calendar.MONTH)+1) == month && calendar.get(Calendar.DAY_OF_MONTH) == day)
                                || (lCalender.get(Calendar.YEAR) == year && (lCalender.get(Calendar.MONTH)+1) == month && lCalender.get(Calendar.DAY_OF_MONTH) == day) ) {
                            listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                        }
                        //when first day not equal to last day
                        //for the between of them
                        if (FDay.compareTo(LDay) != 0 ) {
                            //when the year and month of first and last same
                            if ( calendar.get(Calendar.YEAR) == lCalender.get(Calendar.YEAR)
                                    && year == calendar.get(Calendar.YEAR) && year == lCalender.get(Calendar.YEAR)
                                    && (calendar.get(Calendar.MONTH)+1) == (lCalender.get(Calendar.MONTH)+1)
                                    && month == (calendar.get(Calendar.MONTH)+1) && month == (lCalender.get(Calendar.MONTH)+1)){
                                //check the calneder view day is grater than first day and less than last day
                                if (day > calendar.get(Calendar.DAY_OF_MONTH) && day < lCalender.get(Calendar.DAY_OF_MONTH)) {
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                }
                            }
                            //when the year for first and last same
                            else if ( calendar.get(Calendar.YEAR) == lCalender.get(Calendar.YEAR)
                                    && year == calendar.get(Calendar.YEAR) && year == lCalender.get(Calendar.YEAR)
                                    && (calendar.get(Calendar.MONTH)+1) != (lCalender.get(Calendar.MONTH)+1)){
                                //check calender view month is grater  than first and less than  last
                                if (month > (calendar.get(Calendar.MONTH)+1)  && month < (lCalender.get(Calendar.MONTH)+1)){
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                }
                                //check the day is grater than first when same month
                                else if (month == (calendar.get(Calendar.MONTH)+1) && day > calendar.get(Calendar.DAY_OF_MONTH) ){
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                }
                                //check the day is less than first when same month
                                else if (month == (lCalender.get(Calendar.MONTH)+1) && day < lCalender.get(Calendar.DAY_OF_MONTH) ){
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                }
                            }
                            //when the year not same for first and last
                            else if ( calendar.get(Calendar.YEAR) != lCalender.get(Calendar.YEAR) && calendar.get(Calendar.YEAR) < lCalender.get(Calendar.YEAR)) {
                                //calender view year same as first and month same day should be grater
                                if (year == calendar.get(Calendar.YEAR) && month == (calendar.get(Calendar.MONTH)+1) && day > calendar.get(Calendar.DAY_OF_MONTH)) {
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                }
                                //if year same as first and month not same should be grater than first
                                else if ( year == calendar.get(Calendar.YEAR) && month > (calendar.get(Calendar.MONTH)+1)) {
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                }
                                //calender view year same as last and month same day should be less
                                else if (year == lCalender.get(Calendar.YEAR) && month == (lCalender.get(Calendar.MONTH)+1) && day < lCalender.get(Calendar.DAY_OF_MONTH) ){
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                }
                                //if year same as last and month not same should be less than first
                                else if ( year == lCalender.get(Calendar.YEAR) && month < (lCalender.get(Calendar.MONTH)+1)) {
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
                                }
                                // if year not same as first should be grater than first and less than last
                                else if (year < lCalender.get(Calendar.YEAR) && year > calendar.get(Calendar.YEAR)) {
                                    listMedicines.add(new Medicine(MID, MName, MFDay, MLDay, MTime, MDose, MPeriod));
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

    /*private void installButton110to250() {

        final AllAngleExpandableButton button = root.findViewById(R.id.button_expandable_110_250);
        final List<ButtonData> buttonDatas = new ArrayList<>();
        int[] drawable = {R.drawable.ic_add_symbol , R.drawable.add_medicine , R.drawable.add_appointment};
        int[] color = { R.color.colorAccent, R.color.colorAccent , R.color.colorAccent};

        for (int i = 0; i < 3; i++) {
            ButtonData buttonData;
            if (i == 0) {
                buttonData = ButtonData.buildIconButton(getContext(), drawable[i], 7);
                button.setButtonDatas(buttonDatas);
            }else {
                buttonData = ButtonData.buildIconButton(getContext(), drawable[i], 8);
            }
            buttonData.setBackgroundColorId(getContext(), color[i]);
            buttonDatas.add(buttonData);
        }
        //button.setButtonDatas(buttonDatas);
        setListener(button);


    }// installButton110to250

    private void setListener(final AllAngleExpandableButton button) {
        button.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {
                switch (index) {
                    case 1:
                        //add medicine
                        Intent intent = new Intent(getContext(), Add_Medicine_Page.class);
                        intent.putExtra("date", date);
                        startActivity(intent);
                        break;
                    case 2:
                        //add text
                        Intent intentM = new Intent(getContext(), Add_Appointment_Page.class);
                        intentM.putExtra("date", date);
                        startActivity(intentM);
                        break;

                }
            }

            @Override
            public void onExpand() {
            }

            @Override
            public void onCollapse() {
            }

        });
    }*/

    private void animateFab() {
        if (isopen){
            add.startAnimation(rotateFor);
            //addMed.startAnimation(fabClose);
            //addApp.startAnimation(fabClose);
            addMed.setClickable(false);
            addApp.setClickable(false);
            addMed.hide();
            addApp.hide();
            isopen = false;
        }
        else {
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
        isopen = true;
        animateFab();
        addMed.hide();
        addApp.hide();
        super.onResume();
        /*if (year != 0 && month!=0 && day!= 0){
            listAppointements.clear();
            listMedicines.clear();
            SetAppRecyView(root, year, month, day);
            SetMedRecyView(root, year, month, day);
        }*/
    }
}
