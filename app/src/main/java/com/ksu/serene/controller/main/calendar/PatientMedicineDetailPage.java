package com.ksu.serene.controller.main.calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.ksu.serene.controller.Reminder.AlarmManagerProvider;
import com.ksu.serene.controller.Reminder.ReminderAlarmService;
import com.ksu.serene.model.Medicine;
import com.ksu.serene.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class PatientMedicineDetailPage extends AppCompatActivity {

    private TextView MedicineName;
    private TextView StartDay;
    private TextView EndDay;
    private TextView Time;
    private TextView Dose;
    private Button Delete;
    private Medicine medicine;
    private String MedID, URI_path;
    ImageView backButton ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_patient_medicine_detail_page);
        MedID = getIntent().getStringExtra("MedicineID");
        MedicineName = findViewById(R.id.nameET);
        StartDay = findViewById(R.id.MFromDays);
        EndDay = findViewById(R.id.MTillDays);
        Time = findViewById(R.id.MTime);
        Dose = findViewById(R.id.MDose);
        Delete = findViewById(R.id.DeleteMedicine);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db.collection("PatientMedicin")
                .document(MedID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MedicineName.setText(documentSnapshot.get("name").toString());
                StartDay.setText(documentSnapshot.get("Fday").toString());
                EndDay.setText(documentSnapshot.get("Lday").toString());
                Time.setText(documentSnapshot.get("time").toString()+" (every "+documentSnapshot.get("reminderInterval").toString()+" "+documentSnapshot.get("reminderType").toString()+")");
                Dose.setText(documentSnapshot.get("doze").toString());
                URI_path = documentSnapshot.get("URI_path").toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PatientMedicineDetailPage.this, "Fails to get data", Toast.LENGTH_LONG).show();
            }
        });

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show window dialog with 2 button yes and no
                new AlertDialog.Builder(PatientMedicineDetailPage.this)
                        .setTitle(R.string.DeleteMed)
                        .setMessage(R.string.DeleteMessageMed)
                        .setPositiveButton(R.string.DeleteOKMed, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.collection("PatientMedicin")
                                        .document(MedID)
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Uri tempURI = Uri.parse(URI_path);//get path

                                                //check if URI path actually contains a reminder
                                                if (tempURI != null) {
                                                    // Call the ContentResolver to delete the reminder at the given content URI.
                                                    // Pass in null for the selection and selection args because the mCurrentreminderUri
                                                    // content URI already identifies the reminder that we want.
                                                    AlarmManager alarmmanager = AlarmManagerProvider.getAlarmManager(getApplicationContext());

                                                    PendingIntent operation =
                                                            ReminderAlarmService.getReminderPendingIntent(getApplicationContext(), tempURI);

                                                    alarmmanager.cancel(operation);//cancels reminder alarm

                                                    int rowsDeleted = getContentResolver().delete(tempURI, null, null);

                                                    // Show a toast message depending on whether or not the delete was successful.
                                                    if (rowsDeleted == 0) {
                                                        // If no rows were deleted, then there was an error with the delete.
                                                        //Toast.makeText(PatientMedicineDetailPage.this, "The Med reminder was not found", Toast.LENGTH_LONG);
                                                        Log.d("Reminder","The Med reminder was not found");
                                                    } else {
                                                        // Otherwise, the delete was successful and we delete alarm and display a toast.


                                                        //Toast.makeText(PatientMedicineDetailPage.this, "The Med reminder was successfully cancelled", Toast.LENGTH_LONG);
                                                        Log.d("Reminder","The Med reminder was successfully cancelled");
                                                    }
                                                }

                                                Toast.makeText(PatientMedicineDetailPage.this, R.string.MedDeletedSuccess, Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(PatientMedicineDetailPage.this, R.string.MedDeletedFialed, Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton(R.string.DeleteCancleMed, null)
                        .show();
            }
        });
    }
}

