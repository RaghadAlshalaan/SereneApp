package com.ksu.serene;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.Model.Medicine;

import androidx.appcompat.app.AppCompatActivity;

public class PatientMedicineDetail extends AppCompatActivity {

    TextView MedicineName;
    TextView StartDay;
    TextView EndDay;
    TextView Time;
    TextView Dose;
    TextView Delete;
    Medicine medicine;
    String MedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medicine_detail);
        MedID = getIntent().getStringExtra("MedicineID");
        MedicineName = (TextView) findViewById(R.id.MName);
        StartDay = (TextView) findViewById(R.id.MFromDays);
        EndDay = (TextView) findViewById(R.id.MTillDays);
        Time = (TextView) findViewById(R.id.MTime);
        Dose = (TextView) findViewById(R.id.MDose);
        Delete = (TextView) findViewById(R.id.DeleteMedicine);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("PatientMedicin")
    }
}
