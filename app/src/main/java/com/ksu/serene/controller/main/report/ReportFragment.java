package com.ksu.serene.controller.main.report;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.R;
import com.ksu.serene.controller.main.calendar.PatientEventAdapter;
import com.ksu.serene.model.Event;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import www.sanju.motiontoast.MotionToast;

import static android.view.View.GONE;

public class ReportFragment extends Fragment {

    private Button generate_report;
    private RadioGroup radioGroup;
    private View root;
    private String duration;
    private Button start;
    private Button end;
    private LinearLayout datePicker;
    private String startDate;
    private String endDate;
    private Resources res;
    private Calendar myCalendarStart = Calendar.getInstance();
    private Calendar myCalendarEnd = Calendar.getInstance();
    private SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    // Google Calendar Events
    public boolean GoogleCalendar = false;
    GoogleAccountCredential credential;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    final HttpTransport transport = new com.google.api.client.http.javanet.NetHttpTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    static final int REQUEST_AUTHORIZATION = 1;
    static final int REQUEST_ACCOUNT_PICKER = 2;
    com.google.api.services.calendar.Calendar client;

    static final String FIELDS = "id,summary";
    static final String FEED_FIELDS = "items(" + FIELDS + ")";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_report, container, false);
        res = getResources();

        init();
        // Initialize Google API
        initGoogleApi();

        // by default
        datePicker.setVisibility(LinearLayout.GONE);

        final DatePickerDialog.OnDateSetListener StartDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                startDate = myCalendarStart.get(Calendar.DAY_OF_MONTH) + "/" + myCalendarStart.get(Calendar.MONTH) + "/" + myCalendarStart.get(Calendar.YEAR);
                start.setText(DateFormat.format(myCalendarStart.getTime()));
            }

        };

        final DatePickerDialog.OnDateSetListener EndDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, monthOfYear);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDate = myCalendarEnd.get(Calendar.DAY_OF_MONTH) + "/" + myCalendarEnd.get(Calendar.MONTH) + "/" + myCalendarEnd.get(Calendar.YEAR);
                end.setText(DateFormat.format(myCalendarEnd.getTime()));
            }

        };

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartDate(StartDate);
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEndDate(EndDate);
        }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setRadioButton(checkedId);
        }// onChecked
        });

        generate_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateReport();
        }// onClick
        });


        return root;
    }// onCreate

    private void generateReport() {
        // error dialog if no selection is made
        // intent to next activity
        final Intent intent = new Intent(getContext(), PatientReport.class);
        intent.putExtra(Constants.Keys.DURATION, duration);

        if (duration.equals("custom")) {
            if (isDatesChoosen(startDate, endDate) == 0) {
                intent.putExtra(Constants.Keys.START_DATE, startDate);
                intent.putExtra(Constants.Keys.END_DATE, endDate);

                startActivity(intent);
            } else {

                switch (isDatesChoosen(startDate, endDate)) {
                    //here the only missing is the start StartDate
                    case 1: {
                        String text = String.format(res.getString(R.string.date_pickerrr));
                        dialog(text);
                        break;
                    }
                    //here the only missing is the end StartDate
                    case -1: {
                        String text = String.format(res.getString(R.string.date_pickerrrr));
                        dialog(text);
                    }
                    //here the missing are the tow dates
                    case 2: {
                        String text = String.format(res.getString(R.string.date_pickerr));
                        dialog(text);
                        break;
                    }

                }

            }//if
        }//bigger if
        else {
            startActivity(intent);
        }
    }

    private void setRadioButton(int checkedId) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

        switch (checkedId) {
            case R.id.radioButton1:
                datePicker.setVisibility(LinearLayout.GONE);
                duration = "2week";
                Calendar calw = Calendar.getInstance();
                calw.add(Calendar.DATE, -1);
                endDate = dateFormat.format(calw.getTime());

                Calendar cals = Calendar.getInstance();
                cals.add(Calendar.DATE, -15);
                startDate = dateFormat.format(cals.getTime());

                break;

            case R.id.radioButton2:
                datePicker.setVisibility(LinearLayout.GONE);
                duration = "month";
                Calendar calm = Calendar.getInstance();
                calm.add(Calendar.DATE, -1);
                endDate = dateFormat.format(calm.getTime());
                Calendar calms = Calendar.getInstance();
                calms.add(Calendar.MONTH, -1);
                startDate = dateFormat.format(calms.getTime());

                break;

            case R.id.radioButton3:
                duration = "custom";
                datePicker.setVisibility(LinearLayout.VISIBLE);
                break;
        }// switch
    }

    private void setEndDate(DatePickerDialog.OnDateSetListener EndDate) {
        if (isStartDateSet(startDate)) {
            //check the start StartDate not equal to current StartDate
            if (!isStartDEqualToCurrent(myCalendarStart)) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), EndDate, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(myCalendarStart.getTimeInMillis());
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);
                datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                datePickerDialog.show();
            }

            //when that StartDate chosen for start is equal to current time no need to show the dialog take the same StartDate
            else {
                endDate = myCalendarStart.get(Calendar.DAY_OF_MONTH) + "/" + myCalendarStart.get(Calendar.MONTH) + "/" + myCalendarStart.get(Calendar.YEAR);
                end.setText(DateFormat.format(myCalendarStart.getTime()));
            }


        } else {

            String text = String.format(res.getString(R.string.date_picker));

            MotionToast.Companion.darkToast(
                    getActivity(),
                    text,
                    MotionToast.Companion.getTOAST_WARNING(),
                    MotionToast.Companion.getGRAVITY_BOTTOM(),
                    MotionToast.Companion.getSHORT_DURATION(),
                    ResourcesCompat.getFont(getActivity().getApplicationContext(), R.font.montserrat));

        }//else
    }

    private void setStartDate(DatePickerDialog.OnDateSetListener startDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), startDate, myCalendarStart
                .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                myCalendarStart.get(Calendar.DAY_OF_MONTH));

        myCalendarStart.set(Calendar.YEAR, (Calendar.getInstance().get(Calendar.YEAR)));
        myCalendarStart.set(Calendar.MONTH, (Calendar.getInstance().get(Calendar.MONTH)) - 6);//
        myCalendarStart.set(Calendar.DAY_OF_MONTH, (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));

        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.MONTH, -4);
        datePickerDialog.getDatePicker().setMinDate(cal2.getTimeInMillis());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        datePickerDialog.show();
    }

    private void init() {
        generate_report = root.findViewById(R.id.generate_report_btn);
        radioGroup = root.findViewById(R.id.radio_group);
        end = root.findViewById(R.id.end);
        start = root.findViewById(R.id.start);
        datePicker = root.findViewById(R.id.data_picker);
        duration = "2week";// default value
    }//init

    private void initGoogleApi() {

        credential =
                GoogleAccountCredential.usingOAuth2(getActivity(), Collections.singleton(CalendarScopes.CALENDAR));

        String accountName = Prefs.getString(PREF_ACCOUNT_NAME, "");
        if (!accountName.isEmpty()) {
            credential.setSelectedAccountName(accountName);

            client = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential).setApplicationName("SERENE")
                    .build();

            GoogleCalendar = true;

        } else {
            GoogleCalendar = false;
        }

    }

    public void dialog(String text) {

        MotionToast.Companion.darkToast(
                getActivity(),
                text,
                MotionToast.Companion.getTOAST_WARNING(),
                MotionToast.Companion.getGRAVITY_BOTTOM(),
                MotionToast.Companion.getSHORT_DURATION(),
                ResourcesCompat.getFont(getActivity().getApplicationContext(), R.font.montserrat));

    }

    public boolean isStartDateSet(String startDate) {
        if (startDate == null) {
            return false;
        }
        return true;
    }

    public boolean isStartDEqualToCurrent(Calendar calenderMillis) {
        Calendar currentTime = Calendar.getInstance();
        if (calenderMillis.get(Calendar.YEAR) == currentTime.get(Calendar.YEAR)
                && calenderMillis.get(Calendar.MONTH) == currentTime.get(Calendar.MONTH)
                && calenderMillis.get(Calendar.DAY_OF_MONTH) == currentTime.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }

    public int isDatesChoosen(String startDate, String endDate) {
        if (startDate == null && endDate == null) {
            return 2;
        }
        if (startDate == null && endDate != null) {
            return 1;
        }
        if (startDate != null && endDate == null) {
            return -1;
        }
        return 0;
    }
}// class