package com.ksu.serene.Controller.Homepage.Calendar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.Model.TherapySession;
import com.ksu.serene.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PatientAppointmentDetail extends AppCompatActivity {

    private TextView AppointmentName;
    private TextView Date;
    private TextView Time;
    private Button Delete;
    private TherapySession session;
    private String AppID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_detail);
        AppID = getIntent().getStringExtra("AppointmentID");
        AppointmentName = (TextView) findViewById(R.id.AppName);
        Date = (TextView) findViewById(R.id.AppDate);
        Time = (TextView) findViewById(R.id.AppTime);
        Delete = (Button) findViewById(R.id.DeleteApp);

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
                Toast.makeText(PatientAppointmentDetail.this, "Fails to get data", Toast.LENGTH_LONG);
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show window dialog with 2 button yes and no
                new AlertDialog.Builder(PatientAppointmentDetail.this)
                        .setTitle("Delete Appointment Reminder")
                        .setMessage("Are you sure that you want delete the" + AppointmentName.getText().toString())
                        .setPositiveButton("Yes, I'm sur", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.collection("PatientSessions")
                                        .document(AppID)
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(PatientAppointmentDetail.this, "The Appointment reminder deleted successfully", Toast.LENGTH_LONG);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(PatientAppointmentDetail.this, "The Appointment reminder did not delete", Toast.LENGTH_LONG);
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No, cancel", null)
                        .show();
            }
        });

    }
}
