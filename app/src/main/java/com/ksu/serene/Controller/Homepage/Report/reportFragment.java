package com.ksu.serene.Controller.Homepage.Report;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.ksu.serene.Controller.Constants;
import com.ksu.serene.Controller.Homepage.Patient.PatientReport;
import com.ksu.serene.R;

public class reportFragment extends Fragment {

    private Button generate_report;
    private RadioGroup radioGroup;
    private View root;
    private String duration;
    private ImageView durationBtn;
    private Button start;
    private Button end;
    private CustomDialogClass dateDialog;
    private TextView startDate;
    private TextView endDate;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_report, container, false);

        init();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog
                dateDialog.show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog
                dateDialog.show();
                dateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        endDate.setText(dateDialog.getDay()+"/"+dateDialog.getMonth()+"/"+dateDialog.getYear());


                    }
                });


            }


        });
        generate_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // error dialog if no selection is made
                // intent to next activity
                final Intent intent = new Intent(getContext(), PatientReport.class);
                duration = "2week";// default value


                durationBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "isclicked", Toast.LENGTH_SHORT).show();
                        showDatePickerDialog();
                    }
                });


                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.radioButton1:
                                duration = "2week";
                                break;
                            case R.id.radioButton2:
                                duration = "month";
                                break;
                            case R.id.radioButton3:
                                duration = "custom";
                                showDatePickerDialog();
                                // a date picker dialog and get the data
                                break;
                        }// switch


                    }// onChecked
                });//listener

                intent.putExtra(Constants.Keys.DURATION, duration);
                startActivity(intent);
            }// onClick


        });


        return root;
    }// onCreate

    private void init() {
        generate_report = root.findViewById(R.id.generate_report_btn);
        radioGroup = root.findViewById(R.id.radio_group);
        durationBtn = root.findViewById(R.id.go_to3);
        end = root.findViewById(R.id.end);
        start = root.findViewById(R.id.start);
        dateDialog = new CustomDialogClass(getActivity());
        startDate = root.findViewById(R.id.start_date);
        endDate = root.findViewById(R.id.end_date);

    }//init

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
}// class