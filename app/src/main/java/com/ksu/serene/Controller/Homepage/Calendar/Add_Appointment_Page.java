package com.ksu.serene.Controller.Homepage.Calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.MainActivity;
import com.ksu.serene.Model.TherapySession;
import com.ksu.serene.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Add_Appointment_Page extends AppCompatActivity {

    private EditText AppName;
    private EditText Date;
    private EditText Time;
    private Button Confirm;
    private Calendar calendar ;
    private SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);

    //for  day
    private final DatePickerDialog.OnDateSetListener AppDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date.setText(DateFormat.format(calendar.getTime()));
        }
    };
    //for time
    private final TimePickerDialog.OnTimeSetListener AppTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minutes);
            String AmPm;
            if (hours >=12){
                AmPm = "PM";
            } else {
                AmPm = "AM";
            }
            Time.setText(String.format("%02d : %02d", hours ,minutes));
        }
    };
    // var's used for check day and time
    private java.util.Date AD;
    private Date CuttentTime;
    private Date AT;
    private boolean added = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment_page);

        calendar = Calendar.getInstance();
        AppName = findViewById(R.id.AppName);
        Date = findViewById(R.id.AppDate);
        Time = findViewById(R.id.AppTime);
        Confirm = findViewById(R.id.ConfirmAddApp);

        //when day edit text click show calender view to choose the start and end days
        //the start day when click
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialogStartDate = new DatePickerDialog(Add_Appointment_Page.this, AppDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogStartDate.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialogStartDate.show();
            }
        });
        //when time text click show the watch view to choose the time of taken
        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Add_Appointment_Page.this, AppTime, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check all filed filled
                if (checkFields(AppName.getText().toString())) {
                    //check the day and time in future or the day now but time in future
                    if (checkDayandTime(Date.getText().toString(), Time.getText().toString())) {
                        // if all checked successfully save medicine in firestore with user id
                        //if (SaveNewMed (AppName.getText().toString(),Date.getText().toString() ,  Time.getText().toString() , Integer.parseInt(Dose.getText().toString()))) {
                        //update calender view color From Start to finish days
                        //search about it
                        if (SaveNewApp (AppName.getText().toString() , Date.getText().toString(), Time.getText().toString())){
                            Toast.makeText(Add_Appointment_Page.this, "The Appointment Reminder added Successfully to your Calender", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(Add_Appointment_Page.this , MainActivity.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(Add_Appointment_Page.this, "The Appointment Reminder did not add ", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
    }
    //The method for check if all field empty or not
    private boolean checkFields (String AName ) {
        if ( !(TextUtils.isEmpty(AName)) && !(Time.getText().toString().equals("")) && !(Date.getText().toString().equals(""))  ){
            return true;
        }
        Toast.makeText(Add_Appointment_Page.this,"All Fields Required", Toast.LENGTH_LONG).show();
        return false;
    }

    //check if time after current time when the Date is current date
    private boolean checkDayandTime (String date, String time) {
        SimpleDateFormat TimeFormat = new SimpleDateFormat ("hh : mm");
        Date CurrentDate = new Date();
        //convert string to date to used in compare
        try {
            AD = DateFormat.parse(date);
            AT = TimeFormat.parse(time);
            CuttentTime = TimeFormat.parse(new SimpleDateFormat("HH : mm").format(CurrentDate));
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // if the  day after current date no need for check for time
        if ( (AD.after(CurrentDate))  ){
            return true;
        }
        //check if the  date is the current date the time is after or same current time, if not return false after display meaningful message
        if ( (AD.compareTo(CurrentDate) == 0) ) {
            //check for time, if it before current time return false with meaningful message
            if (AT.before(CuttentTime) && (AT.compareTo(CuttentTime) != 0)){
                Toast.makeText(Add_Appointment_Page.this,"The chosen time must be now or after now ", Toast.LENGTH_LONG).show();
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }
    }

    //save new appointment in firestore
    private boolean SaveNewApp (String AppName , String date , String time) {
        SimpleDateFormat TimeFormat = new SimpleDateFormat ("hh : mm");
        Date CurrentDate = new Date();
        //convert string to date to used in make TherapySession obj
        try {
            AD = DateFormat.parse(date);
            AT = TimeFormat.parse(time);
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String patientID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //store the newApp obj in firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String AppID = db.collection("PatientSessions").document().getId();
        TherapySession newApp = new TherapySession(AppID, AppName, date , time);
        Map<String, Object> App = new HashMap<>();
        App.put("date", newApp.getDay().toString());
        App.put("name", newApp.getName());
        App.put("patinetID", patientID);
        App.put("time", newApp.getTime().toString());

// Add a new document with a generated ID
        db.collection("PatientSessions").document()
                .set(App)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(Add_Appointment_Page.this, "The App added successfully", Toast.LENGTH_LONG);
                            added = true;
                        } else {
                            //Toast.makeText(Add_Appointment_Page.this, "The App did not add", Toast.LENGTH_LONG);
                            added = false;
                        }
                    }
                });
        return added;
    }

}

