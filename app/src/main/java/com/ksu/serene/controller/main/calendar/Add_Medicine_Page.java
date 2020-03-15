package com.ksu.serene.controller.main.calendar;

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
import com.google.firebase.Timestamp;
import com.ksu.serene.MainActivity;
import com.ksu.serene.model.Medicine;
import com.ksu.serene.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Add_Medicine_Page extends AppCompatActivity {

    private EditText MedicineName;
    private Button FromDay;
    private Button TillDay;
    private Button Time;
    private EditText Dose;
    private Button Confirm;
    private Calendar calendar;
    private SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private Timestamp FDTS;
    private Timestamp LDTS;


    //for from day
    private final DatePickerDialog.OnDateSetListener FromDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            FromDay.setText(DateFormat.format(calendar.getTime()));
        }
    };
    //for end day
    private final DatePickerDialog.OnDateSetListener TillDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            TillDay.setText(DateFormat.format(calendar.getTime()));
        }
    };
    //for time
    private final TimePickerDialog.OnTimeSetListener Mtime = new TimePickerDialog.OnTimeSetListener() {
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
    private Date StartD;
    private Date FinishD;
    private Date CuttentTime;
    private Date MTime;
    private boolean added = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine_page);
        getSupportActionBar().hide();

        MedicineName = findViewById(R.id.MedicineName);
        FromDay = findViewById(R.id.MedicineDaysFrom);
        TillDay = findViewById(R.id.MedicineDaysTill);
        Time = findViewById(R.id.MedicineTime);
        Dose = findViewById(R.id.MedicineDose);
        Confirm = findViewById(R.id.ConfirmAddedMedicine);
        calendar = Calendar.getInstance();
        //when day edit text click show calender view to choose the start and end days
        //the start day when click
        FromDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialogStartDate = new DatePickerDialog(Add_Medicine_Page.this, FromDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogStartDate.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialogStartDate.show();
            }
        });
        //the finish day when click
        TillDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialogFinishDate = new DatePickerDialog(Add_Medicine_Page.this, TillDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogFinishDate.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialogFinishDate.show();
            }
        });

        //when time text click show the watch view to choose the time of taken
        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(Add_Medicine_Page.this, Mtime, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chack all filed filled
                if (checkFields(MedicineName.getText().toString(), Dose.getText().toString())) {
                    //check the day and time in future or the day now but time in future
                    if (checkDayandTime(FromDay.getText().toString() , TillDay.getText().toString() , Time.getText().toString())) {
                        // if all checked successfully save medicine in firestore with user id
                        if (SaveNewMed (MedicineName.getText().toString(),FromDay.getText().toString() , TillDay.getText().toString() , Time.getText().toString() , Integer.parseInt(Dose.getText().toString()))) {

                        }
                    }

                }
            }
        });
    }

    private boolean checkFields (String MName , String MDose ) {
        if ( !(TextUtils.isEmpty(MName)) && !(TextUtils.isEmpty(MDose)) && !(Time.getText().toString().equals("")) && !(FromDay.getText().toString().equals("")) && !(TillDay.getText().toString().equals("")) ){
            return true;
        }
        Toast.makeText(Add_Medicine_Page.this,"All Fields Required", Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean checkDayandTime (String SDate, String EDate , String time) {
        SimpleDateFormat TimeFormat = new SimpleDateFormat ("hh : mm");
        Date CurrentDate = new Date();
        //convert string to date to used in compare
        try {
            StartD = DateFormat.parse(SDate);
            FinishD = DateFormat.parse(EDate);
            MTime = TimeFormat.parse(time);
            CuttentTime = TimeFormat.parse(new SimpleDateFormat("HH : mm").format(CurrentDate));
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // if the start day and finish day both after current date no need for check for time, just need check for both days
        if ( (StartD.after(CurrentDate)) && (FinishD.after(CurrentDate)) ){
            //check if the end date after or same start date, if not return false after display meaningful message
            if (FinishD.before(StartD) && (FinishD.compareTo(StartD) != 0)) {
                Toast.makeText(Add_Medicine_Page.this,"The End Day must be after or same as Start Day", Toast.LENGTH_LONG).show();
                return false;
            }
            else {
                return true;
            }
        }
        //check if the start date is the current date the time is after or same current time, if not return false after display meaningful message
        if ( (StartD.compareTo(CurrentDate) == 0) ) {
            //check if the end date after or same start date, if not return false after display meaningful message
            if (FinishD.before(StartD) && (FinishD.compareTo(StartD) != 0)) {
                Toast.makeText(Add_Medicine_Page.this,"The End Day must be after or same as Start Day", Toast.LENGTH_LONG).show();
                return false;
            }
            if (MTime.before(CuttentTime) && (MTime.compareTo(CuttentTime) != 0)){
                Toast.makeText(Add_Medicine_Page.this,"The chosen time must be now or after now ", Toast.LENGTH_LONG).show();
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

    private boolean SaveNewMed (String MName , String FDay, String EDay, String Time, int MD ) {
        // Medicine(String id, String name, Date day, Time time, int doze, int period)
        //the perios is the (TD - FD0+1
        SimpleDateFormat TimeFormat = new SimpleDateFormat("hh : mm");
        //convert string to date to used in compare
        try {
            StartD = DateFormat.parse(FDay);
            FDTS = new Timestamp(StartD);
            FinishD = DateFormat.parse(EDay);
            LDTS = new Timestamp(FinishD);
            MTime = TimeFormat.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long period = Long.parseLong(((FinishD.getTime() - StartD.getTime()) + 1) + "");
        String patientID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String medID = db.collection("PatientMedicin").document().getId();

        Medicine newMedicine = new Medicine(medID, MName, FDay, EDay, Time, MD, period);
        //store the newMed obj in firestore

        Map<String, Object> med = new HashMap<>();
        med.put("Fday", newMedicine.getDay());
        med.put("Lday", newMedicine.getLastDay());
        med.put("doze", newMedicine.getDoze()+"");
        med.put("name", newMedicine.getName());
        med.put("patinetID", patientID);
        med.put("period", newMedicine.getPeriod() + "");
        med.put("time", newMedicine.getTime().toString());
        med.put("FirstDayTS", FDTS);
        med.put("LastDayTS", LDTS);


// Add a new document with a generated ID
        db.collection("PatientMedicin").document()
                .set(med)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                           Toast.makeText(Add_Medicine_Page.this, "The Med added successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Add_Medicine_Page.this , MainActivity.class);
                            startActivity(intent);
                            added = true;
                        } else {
                           Toast.makeText(Add_Medicine_Page.this, "The Med did not add", Toast.LENGTH_LONG).show();
                            added = false;
                        }
                    }
                });

        return added;

    }
}
