package com.ksu.serene.controller.main.calendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.MainActivity;
import com.ksu.serene.model.TherapySession;
import com.ksu.serene.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class PatientAppointmentDetailPage extends AppCompatActivity {

    private TextView AppointmentName;
    private TextView Date;
    private TextView Time;
    private Button Delete;
    private TherapySession session;
    private String AppID;
    ImageView backButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_detail_page);
        getSupportActionBar().hide();


        AppID = getIntent().getStringExtra("AppointmentID");
        AppointmentName = (TextView) findViewById(R.id.MName);
        Date = (TextView) findViewById(R.id.MTillDays);
        Time = (TextView) findViewById(R.id.MTime);
        Delete = findViewById(R.id.DeleteApp);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db.collection("PatientSessions")
                .document(AppID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                AppointmentName.setText(documentSnapshot.get("name").toString());
                Date.setText(documentSnapshot.get("date").toString());
                Time.setText(documentSnapshot.get("time").toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PatientAppointmentDetailPage.this, "Fails to get data", Toast.LENGTH_LONG).show();
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show window dialog with 2 button yes and no
                new AlertDialog.Builder(PatientAppointmentDetailPage.this)
                        .setTitle("Delete Appointment")
                        .setMessage("Are you sure you want to delete this Appointment Reminder ?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.collection("PatientSessions")
                                        .document(AppID)
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(PatientAppointmentDetailPage.this, "The Appointment reminder deleted successfully", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(PatientAppointmentDetailPage.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(PatientAppointmentDetailPage.this, "The Appointment reminder did not delete", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

    }
}

