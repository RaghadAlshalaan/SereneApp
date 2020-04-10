package com.ksu.serene.controller.main.report;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.ksu.serene.controller.Constants;
import com.ksu.serene.R;
import com.ksu.serene.controller.main.drafts.AddTextDraftPage;

import java.util.Calendar;

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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_report, container, false);
        res = getResources();
        init();
        // by default
        datePicker.setVisibility(LinearLayout.GONE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog
                dateDialog.show();

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
                });

            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startDate != null) {
                    Calendar calen = Calendar.getInstance();
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
                    });

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
                    if (startDate != null && endDate != null) {
                        intent.putExtra(Constants.Keys.START_DATE, startDate);
                        intent.putExtra(Constants.Keys.END_DATE, endDate);
                        startActivity(intent);

                    } else {
                        // error dialog null input
                        if (startDate == null && endDate == null) {
                            String text = String.format(res.getString(R.string.date_pickerr));

                            dialog(text);

                        } else if (startDate == null) {
                            String text = String.format(res.getString(R.string.date_pickerrr));

                            dialog(text);

                        } else if (endDate == null) {
                            String text = String.format(res.getString(R.string.date_pickerrrr));

                            dialog(text);

                        }//else

                    }//if

                }//bigger if
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
}// class