package com.ksu.serene.Controller.Homepage.Report;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

import com.ksu.serene.R;

public class CustomDialogClass extends Dialog {

    public Activity c;
    public Dialog d;
    public Button select;
    DatePicker datePicker ;
    private String day;
    private String month;
    private String year;


    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_date_picker);

        datePicker = findViewById(R.id.datePicker1);


        select  = findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set max and min date
                // check if the user made a selection

                // get tha selected date
                day = datePicker.getDayOfMonth()+"";
                month = datePicker.getMonth()+"";
                year = datePicker.getYear()+"";

                dismiss();


            }//onClick
        });


    }//onCreate

    public void setC(Activity c) {
        this.c = c;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }


}