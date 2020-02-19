package com.ksu.serene.Controller.Homepage.Calendar;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment implements View.OnClickListener{


    //set for patient's appointments
    private String patientId;
    private RecyclerView recyclerViewSession;
    private List<TherapySession> listAppointements;
    private PatientSessionsAdapter adapterSession;
    //retrieve the current date
    private Calendar calendar;
    private Date date;
    //set for patient's medicines
    private RecyclerView recyclerViewMedicine;
    private List<Medicine> listMedicines;
    private PatientMedicineAdapter adapterMedicines;
    // Add Buttons
    FloatingActionButton addButton, addMedicine, addAppointment;
    TextView TV_appointment, TV_medicine;
    private Animation fab_clock, fab_anti_clock;
    private boolean isFABOpen;



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


        //retrieve the id of patient used for searching
        patientId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //retrieve Patient Session data
        recyclerViewSession = (RecyclerView) root.findViewById(R.id.RecyclerviewSession);
        listAppointements = new ArrayList<>();
        adapterSession = new PatientSessionsAdapter(listAppointements);

        //get the current date used to retrieve all appointments and medicine for the next coming
        calendar = Calendar.getInstance();
        date = calendar.getTime();

        //search in cloud firestore for patient sessions
        CollectionReference referenceSession = FirebaseFirestore.getInstance().collection("patientsessions");
        final Query queryPatientSession = referenceSession.whereEqualTo("patientID",patientId);
        queryPatientSession.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    final Query queryPatientSessionAfterToday = queryPatientSession.whereGreaterThan("date" , date).orderBy("date" , Query.Direction.ASCENDING);
                    queryPatientSessionAfterToday.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                   /*if(i==2) {
                                       break;
                                   }
                                   else {*/
                                    TherapySession session = document.toObject(TherapySession.class);
                                    listAppointements.add(session);
                                    //i++;}
                                }
                                adapterSession.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });

        recyclerViewSession.setAdapter(adapterSession);

        //retrieve Medicines data
        recyclerViewMedicine = root.findViewById(R.id.RecyclerViewMedicine);
        listMedicines = new ArrayList<>();
        adapterMedicines = new PatientMedicineAdapter(listMedicines);
        calendar = Calendar.getInstance();
        date = calendar.getTime();

        //search in firebase for patient sessions
        CollectionReference referenceMedicine = FirebaseFirestore.getInstance().collection("the patientmedic");
        final Query queryPatientMedicine = referenceMedicine.whereEqualTo("patientID",patientId);
        queryPatientMedicine.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    final Query queryPatientMedicineAfterToday = queryPatientMedicine.whereGreaterThan("date" , date).orderBy("date" , Query.Direction.ASCENDING);
                    queryPatientMedicineAfterToday.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                   /*if(i==2) {
                                       break;
                                   }
                                   else {*/
                                    Medicine medicine = document.toObject(Medicine.class);
                                    listMedicines.add(medicine);
                                    //i++;}
                                }
                                adapterMedicines.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });
        recyclerViewMedicine.setAdapter(adapterMedicines);

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
}
