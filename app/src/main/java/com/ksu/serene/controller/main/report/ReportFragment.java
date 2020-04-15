package com.ksu.serene.controller.main.report;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.ksu.serene.controller.Constants;
import com.ksu.serene.R;
import com.ksu.serene.controller.main.drafts.AddTextDraftPage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import www.sanju.motiontoast.MotionToast;

public class ReportFragment extends Fragment {

    private Button generate_report;
    private RadioGroup radioGroup;
    private View root;
    private String duration;
    private ImageView durationBtn;
    private Button start;
    private Button end;
    private CustomDialogClass dateDialog;
    private TextView startDateTxt;
    private TextView endDateTxt;
    private LinearLayout datePicker;
    private String startDate;
    private String endDate;
    private int startDay;
    private int startMonth;
    private int startYear;
    private Resources res;
    private Calendar myCalendar = Calendar.getInstance();
    private Calendar myCalendarEnd = Calendar.getInstance();
    private SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_report, container, false);
        res = getResources();
        init();
        // by default
        datePicker.setVisibility(LinearLayout.GONE);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDate = myCalendar.get(Calendar.DAY_OF_MONTH) + "/" + myCalendar.get(Calendar.MONTH)  + "/" + myCalendar.get(Calendar.YEAR);
                startDateTxt.setText(DateFormat.format(myCalendar.getTime()));
            }

        };

        final DatePickerDialog.OnDateSetListener Enddate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendarEnd.set(Calendar.YEAR,year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDate = myCalendarEnd.get(Calendar.DAY_OF_MONTH) + "/" + myCalendarEnd.get(Calendar.MONTH)  + "/" + myCalendarEnd.get(Calendar.YEAR);
                endDateTxt.setText(DateFormat.format(myCalendarEnd.getTime()));
            }

        };
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                myCalendar.set(Calendar.YEAR,(Calendar.getInstance().get(Calendar.YEAR)));
                myCalendar.set(Calendar.MONTH,(Calendar.getInstance().get(Calendar.MONTH))-6);//
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());//System.currentTimeMillis());
                datePickerDialog.show();
                //show dialog
                /*myCalendar.set(Calendar.YEAR,(Calendar.getInstance().get(Calendar.YEAR)));
                myCalendar.set(Calendar.MONTH, (Calendar.getInstance().get(Calendar.MONTH))-6);
                myCalendar.set(Calendar.DAY_OF_MONTH, (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
                startDateTxt.setText(myCalendar.getTimeInMillis()+"");//DateFormat.format(myCalendar.getTime()));
               // dateDialog.datePicker.setMinDate(myCalendar.getTimeInMillis());

                dateDialog.datePicker.setMaxDate(System.currentTimeMillis());
                dateDialog.show();
                //dateDialog.datePicker.setMaxDate(System.currentTimeMillis());
                dateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // validate user choice (end> start , not null)
                        if (dateDialog.getDay() != null && dateDialog.getMonth() != null && dateDialog.getYear() != null) {
                            startDate = dateDialog.getDay() + "/" + (Integer.parseInt(dateDialog.getMonth())+1) + "/" + dateDialog.getYear();
                            startDay = Integer.parseInt(dateDialog.getDay());
                            startMonth = Integer.parseInt(dateDialog.getMonth());
                            startYear = Integer.parseInt(dateDialog.getYear());
                            startDateTxt.setText(startDate);

                        }

                    }
                });*/

            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStartDateSet (startDate)){//(startDate != null) {
                    //check the start date not equal to current date
                    if (!isStartDEqualToCurrent(myCalendar)) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), Enddate, myCalendarEnd
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendarEnd.get(Calendar.DAY_OF_MONTH));
                        //myCalendarEnd.set(Calendar.YEAR,(Calendar.getInstance().get(Calendar.YEAR)));
                        //myCalendarEnd.set(Calendar.MONTH,(Calendar.getInstance().get(Calendar.MONTH))-6);//
                        datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());//System.currentTimeMillis());
                        datePickerDialog.show();
                    }
                    //when that date shoosen for start is equal to current time no nned to show the dialog take the same date
                    else {
                        endDate = myCalendar.get(Calendar.DAY_OF_MONTH) + "/" + myCalendar.get(Calendar.MONTH)  + "/" + myCalendar.get(Calendar.YEAR);
                        endDateTxt.setText(DateFormat.format(myCalendar.getTime()));
                    }
                    /*Calendar calen = Calendar.getInstance();
                    calen.set(Calendar.MONTH, startMonth);
                    calen.set(Calendar.DAY_OF_MONTH, startDay);
                    calen.set(Calendar.YEAR, startYear);
                    //show dialog

                    // enable showing the dialog if the patient has chosen start date
                    // disable choosing date older then the start date
                    dateDialog.datePicker.setMinDate(calen.getTimeInMillis());
                    dateDialog.show();
                    dateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (dateDialog.getDay() != null && dateDialog.getMonth() != null && dateDialog.getYear() != null) {

                                endDate = dateDialog.getDay() + "/" +(Integer.parseInt(dateDialog.getMonth())+1) + "/" + dateDialog.getYear();
                                endDateTxt.setText(endDate);
                            }

                        }//if
                    });*/

                } else {

                    String text = String.format(res.getString(R.string.date_picker));

                    MotionToast.Companion.darkToast(
                            getActivity(),
                            text,
                            MotionToast.Companion.getTOAST_WARNING(),
                            MotionToast.Companion.getGRAVITY_BOTTOM(),
                            MotionToast.Companion.getSHORT_DURATION(),
                            ResourcesCompat.getFont( getActivity().getApplicationContext(), R.font.montserrat));
                }//else


            }//onClick
        });// listener


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        datePicker.setVisibility(LinearLayout.GONE);
                        duration = "2week";
                        break;
                    case R.id.radioButton2:
                        datePicker.setVisibility(LinearLayout.GONE);
                        duration = "month";
                        break;
                    case R.id.radioButton3:
                        duration = "custom";
                        datePicker.setVisibility(LinearLayout.VISIBLE);
                        break;
                }// switch


            }// onChecked
        });//listener

        generate_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // error dialog if no selection is made
                // intent to next activity
                final Intent intent = new Intent(getContext(), PatientReport.class);

                intent.putExtra(Constants.Keys.DURATION, duration);

                if (duration.equals("custom")) {
                    if (isDatesChoosen (startDate, endDate) == 0){//(startDate != null && endDate != null) {
                        intent.putExtra(Constants.Keys.START_DATE, startDate);
                        intent.putExtra(Constants.Keys.END_DATE, endDate);
                        startActivity(intent);

                    } else {
                        switch (isDatesChoosen (startDate,endDate)) {
                            //here the enly missing is the start date
                            case 1 : {
                                String text = String.format(res.getString(R.string.date_pickerrr));
                                dialog(text);
                                break;}
                            //here the enly missing is the end date
                            case -1 : {
                                String text = String.format(res.getString(R.string.date_pickerrrr));
                                dialog(text);
                            }
                            //here the missing are the tow dates
                            case 2: {
                                String text = String.format(res.getString(R.string.date_pickerr));
                                dialog(text);
                                break;}

                        }
                        // error dialog null input
                        /*if (startDate == null && endDate == null) {
                            String text = String.format(res.getString(R.string.date_pickerr));

                            dialog(text);

                        } else if (startDate == null) {
                            String text = String.format(res.getString(R.string.date_pickerrr));

                            dialog(text);

                        } else if (endDate == null) {
                            String text = String.format(res.getString(R.string.date_pickerrrr));

                            dialog(text);

                        }//else*/

                    }//if
                }//bigger if
                else {
                    startActivity(intent);
                }
            }// onClick


        });


        return root;
    }// onCreate

    private void init() {
        generate_report = root.findViewById(R.id.generate_report_btn);
        radioGroup = root.findViewById(R.id.radio_group);
        end = root.findViewById(R.id.end);
        start = root.findViewById(R.id.start);
        dateDialog = new CustomDialogClass(getActivity());
        startDateTxt = root.findViewById(R.id.start_date);
        endDateTxt = root.findViewById(R.id.end_date);
        datePicker = root.findViewById(R.id.data_picker);
        duration = "2week";// default value

    }//init

    public void dialog(String text) {

        MotionToast.Companion.darkToast(
                getActivity(),
                text,
                MotionToast.Companion.getTOAST_WARNING(),
                MotionToast.Companion.getGRAVITY_BOTTOM(),
                MotionToast.Companion.getSHORT_DURATION(),
                ResourcesCompat.getFont( getActivity().getApplicationContext(), R.font.montserrat));
    }

    public boolean isStartDateSet (String startDate){
        if (startDate == null){
            return false;
        }
        return true;
    }

    public boolean isStartDEqualToCurrent (Calendar calenderMillis) {
        Calendar currentTime = Calendar.getInstance();
        if ( calenderMillis.get(Calendar.YEAR) == currentTime.get(Calendar.YEAR)
                && calenderMillis.get(Calendar.MONTH) == currentTime.get(Calendar.MONTH)
                && calenderMillis.get(Calendar.DAY_OF_MONTH) == currentTime.get(Calendar.DAY_OF_MONTH) ){
            return true;
        }
            return false;
    }

    public int isDatesChoosen (String startDate, String endDate){
        if (startDate == null && endDate == null){
            return 2;
        }
        if (startDate == null && endDate != null){
            return 1;
        }
        if (startDate != null && endDate == null){
            return -1;
        }
        return 0;
    }
}// class