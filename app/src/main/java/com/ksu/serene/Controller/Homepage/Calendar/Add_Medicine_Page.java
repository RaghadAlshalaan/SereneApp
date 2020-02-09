package com.ksu.serene.Controller.Homepage.Calendar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ksu.serene.R;

import androidx.appcompat.app.AppCompatActivity;

public class Add_Medicine_Page extends AppCompatActivity {

    private EditText MedicineName;
    private EditText Days;
    private EditText Time;
    private EditText Dose;
    private Button Confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__medicine__page);
        MedicineName = (EditText) findViewById(R.id.MedicineName);
        Days = (EditText) findViewById(R.id.MedicineDays);
        Time = (EditText) findViewById(R.id.MedicineTime);
        Dose = (EditText) findViewById(R.id.MedicineDose);
        Confirm = (Button) findViewById(R.id.ConfirmAddedMedicine);

        //when day edit text click show calender view to choose the start and end days

        //when time text click show the watch view to choose the time of taken

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chack all filed filled
                //if( checkFields(string Name, String Day , String time , String Dose , String checkbox) ) {
                //check the day and time in future or the day now but time in future
                // if ( checkDayandTime (String Fromday , String tillday , String Time , String checkBox) ) {
                // if all checked successfully save medicine in firestore with user id
            }
        });
    }
}
