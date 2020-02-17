package com.ksu.serene.Controller.Homepage.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class calendarFragment extends Fragment {

    //set for layout.xml
    private ImageView add;
    private Button add_app;
    private Button add_med;
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        //set for layout.xml
        add = (ImageView)  root.findViewById(R.id.add_calender);
        add_app = (Button)  root.findViewById(R.id.add_Appointment);
        add_med = (Button)  root.findViewById(R.id.add_Medicine);
        add_app.setVisibility(View.INVISIBLE);
        add_med.setVisibility(View.INVISIBLE);

        //when the add image click will show all add buttons
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_app.setVisibility(View.VISIBLE);
                add_med.setVisibility(View.VISIBLE);
            }
        });

        //when the add appointment click will show add appointment page
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent (calendarFragment.class , addAppointment.class)
                //startActivity(intent);
            }
        });

        //when the add medicine click will show add medicine page
        add_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent (calendarFragment.class , addMedicine.class)
                //startActivity(intent);
            }
        });

        //retrieve the id of patient used for searching
        patientId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //retrieve Patient Session data
        recyclerViewSession = (RecyclerView) root.findViewById(R.id.RecyclerviewSession);
        listAppointements = new ArrayList<>();
        adapterSession = new PatientSessionsAdapter(listAppointements );

        //get the current date used to retrieve all appointments and medicine for the next coming
        calendar = Calendar.getInstance();
        date = calendar.getTime();

        //search in cloud firestore for patient sessions
        CollectionReference referenceSession = FirebaseFirestore.getInstance().collection("PatientSessions");
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

        //retrieve Mediciens data
        recyclerViewMedicine = (RecyclerView) root.findViewById(R.id.RecyclerviewMedicine);
        listMedicines = new ArrayList<>();
        adapterMedicines = new PatientMedicineAdapter(listMedicines, new PatientMedicineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Medicine item) {
                Intent intent = new Intent(getContext(), PatientMedicineDetail.class);
                intent.putExtra("MedicineID", item.getId());
            }
        });
        calendar = Calendar.getInstance();
        date = calendar.getTime();
        //search in firebase for patientsessions
        CollectionReference referenceMedicine = FirebaseFirestore.getInstance().collection("PatientMedicin");
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
}
