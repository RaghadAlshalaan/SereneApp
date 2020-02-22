package com.ksu.serene.Controller.Homepage.Calendar;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.Model.Medicine;
import com.ksu.serene.Model.TherapySession;
import com.ksu.serene.R;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.text.ParseException;
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
    private int MPeriod;
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
        recyclerViewSession = (RecyclerView) root.findViewById(R.id.RecyclerviewSession);
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

        final Query queryPatientSession = referenceSession.whereEqualTo("patientID",patientId);
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
        recyclerViewMedicine = (RecyclerView) root.findViewById(R.id.RecyclerViewMedicine);
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
                        MPeriod = Integer.parseInt(document.get("period").toString());
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
                    }
                    adapterMedicines.notifyDataSetChanged();
                }
            }
        });
        recyclerViewMedicine.setAdapter(adapterMedicines);
    }

}
