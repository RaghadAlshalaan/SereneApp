package com.ksu.serene.controller.main.calendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;
//import com.ksu.serene.MainActivity;
import com.google.firebase.firestore.SetOptions;
import com.ksu.serene.controller.Reminder.AlarmScheduler;
import com.ksu.serene.model.Medicine;
import com.ksu.serene.R;
import com.ksu.serene.model.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static android.R.layout.simple_spinner_dropdown_item;

public class Add_Medicine_Page extends AppCompatActivity {

    private EditText MedicineName;
    private Button FromDay;
    private Button Time;
    private EditText Dose;
    private Button Confirm;
    private Calendar calendar;
    private EditText repeatIntervalET;
    private EditText repeatNOET;
    private Spinner repeatTypeSpinner;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private Timestamp FDTS;
    private Timestamp LDTS;
    ImageView back;
    private String date;
    private int repeatInterval, repeatNO;
    private Long repeatTime;//we will save the final calculated repeat interval (for setting alarm) here
    private String repeatType;
    private String medDocumentID;
    private long selectedTimestamp;


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
            //TillDay.setText(DateFormat.format(calendar.getTime()));
        }
    };
    //for time
    private final TimePickerDialog.OnTimeSetListener Mtime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minutes);

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

        back = findViewById(R.id.backButton);
        MedicineName = findViewById(R.id.nameET);
        FromDay = findViewById(R.id.MFromDays);
        Time = findViewById(R.id.MTime);
        Dose = findViewById(R.id.MedicineDose);
        //repeat inputs
        repeatIntervalET =(EditText) findViewById(R.id.repeatInterval);
        repeatTypeSpinner = (Spinner)findViewById(R.id.repeatType);//string array MedicineRepeatType
        repeatNOET = (EditText) findViewById(R.id.repeatNO);

        //init repeatTypeSpinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.MedicineRepeatType, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(simple_spinner_dropdown_item);
        repeatTypeSpinner.setAdapter(adapter);
        repeatTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                repeatType = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //default type
                //repeatType = "day(s)";
            }
        });

        Confirm = findViewById(R.id.button);
        calendar = Calendar.getInstance();
        date = getIntent().getStringExtra("date");
        if (date != null){
            FromDay.setText(date);
        }
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
                if (checkFields(MedicineName.getText().toString(), Dose.getText().toString(), repeatIntervalET.getText().toString(),repeatNOET.getText().toString())) {
                    //check the day and time in future or the day now but time in future
                    if (checkDayandTime(FromDay.getText().toString() , Time.getText().toString())) {
                        repeatInterval = Integer.parseInt(repeatIntervalET.getText().toString());//get repeat interval
                        repeatNO = Integer.parseInt(repeatNOET.getText().toString());//get repeat number (how many times reminder repeats)

                        // if all checked successfully save medicine in firestore with user id
                        SaveNewMed (MedicineName.getText().toString(),FromDay.getText().toString() ,Time.getText().toString() , Integer.parseInt(Dose.getText().toString()));


                    } //checkdayandtime if

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private boolean checkFields (String MName , String MDose, String interval, String repeatNo) {
        if ( !(TextUtils.isEmpty(MName)) && !(TextUtils.isEmpty(MDose)) && !(TextUtils.isEmpty(interval)) && !(TextUtils.isEmpty(repeatNo)) && !(Time.getText().toString().equals("Set Time")) && !(FromDay.getText().toString().equals("Start"))){
            return true;
        }
        Toast.makeText(Add_Medicine_Page.this,R.string.EmptyFields, Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean checkDayandTime (String SDate, String time) {
        SimpleDateFormat TimeFormat = new SimpleDateFormat ("hh : mm");
        SimpleDateFormat DateFormat = new SimpleDateFormat ("dd/MM/yy");
        Date CurrentDate = new Date();
        //convert string to date to used in compare
        try {
            StartD = DateFormat.parse(SDate);
            /*FinishD = DateFormat.parse(EDate);*/
            MTime = TimeFormat.parse(time);
            CuttentTime = TimeFormat.parse(new SimpleDateFormat("HH : mm").format(CurrentDate));
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //when all after current date no constraint return true
        if ( (StartD.after(CurrentDate))){
            return true;
        }
        //check if the start date is the current date the time is after or same current time, if not return false after display meaningful message
        if (StartD.before(CurrentDate) || StartD.compareTo(CurrentDate) ==0  ) {
            if (MTime.before(CuttentTime) || (MTime.compareTo(CuttentTime) == 0)){
                Toast.makeText(Add_Medicine_Page.this,R.string.CurrentTime, Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }
        return false;
    }

    private void SaveNewMed (String MName , String FDay,String Time, int MD ) {
        // Medicine(String id, String name, Date day, Time time, int doze, int period)
        //the period is the (TD - FD0+1
        SimpleDateFormat TimeFormat = new SimpleDateFormat("hh : mm");
        //convert string to date to used in compare
        try {
            StartD = DateFormat.parse(FDay);
            FDTS = new Timestamp(StartD);
            //FinishD = DateFormat.parse(EDay);
            MTime = TimeFormat.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //long period = Long.parseLong(((FinishD.getTime() - StartD.getTime()) + 1) + "");

        long milMinute = 60000L;
        long milHour = 3600000L;
        long milDay = 86400000L;
        long milWeek = 604800000L;

        // Check repeat type & set repeatTime to send to AlarmScheduler
        switch (repeatType) {
            case "minute(s)":
                repeatTime = repeatInterval * milMinute;
                break;
            case "hour(s)":
                repeatTime = repeatInterval * milHour;
                break;
            case "day(s)":
                repeatTime = repeatInterval * milDay;
                break;
            case "week(s)":
                repeatTime = repeatInterval * milWeek;
                break;
        }

        long periodInMS = repeatTime * repeatNO; //the amount of time between first day and last day in ms
        long period = periodInMS/milDay;

        //time for first day reminder
        Calendar firstReminder = Calendar.getInstance();
        /*String FirstDay = newMedicine.getDay();
        String Time = newMedicine.getTime();*/

        int startYear = Integer.parseInt(FDay.substring(6));
        int startMonth = Integer.parseInt(FDay.substring(3,5))-1; //01/34/56
        int startDay = Integer.parseInt(FDay.substring(0,2));

        int hours = Integer.parseInt(Time.substring(0,2)); //hh : mm
        int minutes = Integer.parseInt(Time.substring(5));


        firstReminder.set(startYear, startMonth, startDay, hours, minutes,0);
        selectedTimestamp =  firstReminder.getTimeInMillis();
        long lastDayinMillisecond = (selectedTimestamp+periodInMS);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastDayinMillisecond);

        int lastDayYear = calendar.get(Calendar.YEAR);
        int lastDayMonth = calendar.get(Calendar.MONTH)+1;
        int lastDayDay = calendar.get(Calendar.DAY_OF_MONTH);

        String lastDay = lastDayDay+"/"+lastDayMonth+"/"+lastDayYear;

        try {
            FinishD = DateFormat.parse(lastDay);
            LDTS = new Timestamp(FinishD);//may produce exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        String patientID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String medID = db.collection("PatientMedicin").document().getId();
        medDocumentID = getRandomID();
        final Medicine newMedicine = new Medicine(medID, MName, FDay, lastDay, Time, MD, period, repeatInterval, repeatType);
        //store the newMed obj in firestore

        Map<String, Object> med = new HashMap<>();
        med.put("Fday", newMedicine.getDay());
        med.put("Lday", newMedicine.getLastDay());
        med.put("doze", newMedicine.getDoze()+"");
        med.put("name", newMedicine.getName());
        med.put("patinetID", patientID);
        med.put("documentID", medDocumentID);
        med.put("period", newMedicine.getPeriod() + "");
        med.put("time", newMedicine.getTime().toString());
        med.put("FirstDayTS", FDTS);
        med.put("LastDayTS", LDTS);
        med.put("reminderInterval", newMedicine.getReminderInterval());
        med.put("reminderType", newMedicine.getReminderType());


// Add a new document with a generated ID

        db.collection("PatientMedicin").document(medDocumentID)
                .set(med)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                           setReminder(newMedicine);
                           Toast.makeText(Add_Medicine_Page.this, R.string.MedSavedSuccess, Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                           Toast.makeText(Add_Medicine_Page.this, R.string.MedSavedFialed, Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void setReminder(Medicine newMedicine){
        //add medicine reminder

        //return content URI for new reminder
        ContentValues values = new ContentValues();
        values.put(Reminder.ReminderEntry.KEY_NAME, newMedicine.getName());//name
        values.put(Reminder.ReminderEntry.KEY_DOCUMENT_ID, medDocumentID);
        values.put(Reminder.ReminderEntry.KEY_DATE, newMedicine.getDay());//date
        values.put(Reminder.ReminderEntry.KEY_TIME, newMedicine.getTime().toString());//time
        values.put(Reminder.ReminderEntry.KEY_DOSE, newMedicine.getDoze()+"");//dose
        values.put(Reminder.ReminderEntry.KEY_REPEAT_TIME, repeatTime);//interval between repeating notifications, aka 4 hours (in milliseconds)
        values.put(Reminder.ReminderEntry.KEY_REPEAT_TYPE, repeatType);//repeat type
        values.put(Reminder.ReminderEntry.KEY_REPEAT_NO, repeatNO-1);//number of repeats, same mechanism as period, -1 to make it accurate
        //values.put(Reminder.ReminderEntry.KEY_PERIOD, newMedicine.getPeriod()); //no longer using period, instead number of repeats



        //calculate last day

        // This is a NEW reminder, so insert a new reminder into the provider,
        // returning the content URI for the new reminder.
        Uri newUri = getContentResolver().insert(Reminder.ReminderEntry.CONTENT_URI, values);
        String newUriPath = newUri.toString();

        // Show a toast message depending on whether or not the insertion was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            //Toast.makeText(this, "insert_reminder_failed",Toast.LENGTH_SHORT).show();
            Log.d("Reminder", "insert_reminder_failed");
        } else {
            // Otherwise, the insertion was successful and we can display a toast.

            //add URI field to firebase
            Map<String, Object> URIpathAndLastDay = new HashMap<>();
            URIpathAndLastDay.put("URI_path", newUriPath);

            db.collection("PatientMedicin").document(medDocumentID)
                    .set(URIpathAndLastDay, SetOptions.merge());

            //create new notification
            new AlarmScheduler().setRepeatAlarm(getApplicationContext(),selectedTimestamp, newUri, repeatTime);//
            //new AlarmScheduler().setAlarm(getApplicationContext(), selectedTimestamp, newUri);
           // Toast.makeText(this, "Alarm time is at " + new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date(selectedTimestamp)),Toast.LENGTH_LONG).show();
            Log.d("Alarm time is at ", new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date(selectedTimestamp)));

            //Toast.makeText(this, "Reminder was successfully scheduled",Toast.LENGTH_SHORT).show();
            Log.d("Reminder", "Reminder was successfully scheduled");

        }
    }

    private String getRandomID(){
        return UUID.randomUUID().toString();
    }
}
