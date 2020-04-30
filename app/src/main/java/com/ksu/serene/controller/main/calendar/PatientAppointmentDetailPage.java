package com.ksu.serene.controller.main.calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.controller.Reminder.AlarmManagerProvider;
import com.ksu.serene.controller.Reminder.ReminderAlarmService;
import com.ksu.serene.controller.liveChart.utils.Utils;
import com.ksu.serene.model.TherapySession;
import com.ksu.serene.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import www.sanju.motiontoast.MotionToast;


public class PatientAppointmentDetailPage extends AppCompatActivity {

    private TextView AppointmentName;
    private TextView Date;
    private TextView Time;
    private Button Delete;
    private TherapySession session;
    private String AppID, URI_path;
    ImageView backButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointment_detail_page);
        SharedPreferences sp = getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
        String preferred_lng = sp.getString("PREFERRED_LANGUAGE", "en");
        Utils.setLocale(preferred_lng, this);

        getSupportActionBar().hide();


        AppID = getIntent().getStringExtra("AppointmentID");
        AppointmentName = (TextView) findViewById(R.id.nameET);
        Date = (TextView) findViewById(R.id.MTillDays);
        Time = (TextView) findViewById(R.id.MTime);
        Delete = findViewById(R.id.DeleteApp);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*better approach, direct to calendarfragment page, because when user is directed
                to this page from a notification click, clicking on 'back' will leave the app*/
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
                URI_path = documentSnapshot.get("URI_path").toString();
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
                deleteAppointment();
            }
        });

    }

    private void deleteAppointment() {
        new AlertDialog.Builder(PatientAppointmentDetailPage.this)
                .setTitle(R.string.DeleteApp)
                .setMessage(R.string.DeleteMessageApp)
                .setPositiveButton(R.string.DeleteOKApp, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("PatientSessions")
                                .document(AppID)
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        //delete notification
                                        deleteNotification (AppID);
                                        //delete scheduled reminder
                                        //make uri with received path
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
                                                //Toast.makeText(PatientAppointmentDetailPage.this, "The App reminder was not found", Toast.LENGTH_LONG);
                                                Log.d("Reminder deleted","The App reminder was not found");
                                            } else {
                                                // Otherwise, the delete was successful and we delete alarm and display a toast.
                                               // Toast.makeText(PatientAppointmentDetailPage.this, "The App reminder was successfully cancelled", Toast.LENGTH_LONG);
                                                Log.d("Reminder deleted","The App reminder was successfully cancelled");
                                            }
                                        }
                                        //Toast.makeText(PatientAppointmentDetailPage.this, R.string.AppDeletedSuccess, Toast.LENGTH_LONG).show();
                                         Resources res = getResources();
                                        String text = String.format(res.getString(R.string.AppDeletedSuccess));

                                        MotionToast.Companion.darkToast(
                                                 PatientAppointmentDetailPage.this,
                                                 text,
                                                 MotionToast.Companion.getTOAST_SUCCESS(),
                                                 MotionToast.Companion.getGRAVITY_BOTTOM(),
                                                 MotionToast.Companion.getLONG_DURATION(),
                                                 ResourcesCompat.getFont(PatientAppointmentDetailPage.this, R.font.montserrat));

                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PatientAppointmentDetailPage.this, R.string.AppDeletedFialed, Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                })
                .setNegativeButton(R.string.DeleteCancleApp, null)
                .show();
    }

    public void deleteNotification (String appID) {
        final CollectionReference referenceMedicine = FirebaseFirestore.getInstance().collection("Notifications");
        final Query queryPatientMedicine = referenceMedicine.whereEqualTo("documentID",appID);
        queryPatientMedicine.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Delete all the document
                        referenceMedicine.document(document.getId()).delete();
                    }
                }
            }
        });
    }
}

