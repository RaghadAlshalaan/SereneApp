package com.ksu.serene.controller.main.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;
@RunWith(PowerMockRunner.class)
//@PowerMockRunnerDelegate(RobolectricTestRunner.class)  //TODO for DatePicker
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({LayoutInflater.class ,Add_Appointment_Page.class,FirebaseFirestore.class})
public class Add_Appointment_PageTest {

    //TODO test saveApp in DB
    //TODO test set reminder

    //@Rule
    //public ActivityTestRule<Add_Appointment_Page> activityTestRule = new ActivityTestRule<Add_Appointment_Page>(Add_Appointment_Page.class);
    private Add_Appointment_Page therapySession;
    private String name, time, date;
    private Calendar current = Calendar.getInstance();
    private SimpleDateFormat TimeFormat = new SimpleDateFormat ("HH : mm", Locale.UK);
    private java.util.Date currentTime ;
    @Mock
    private EditText AppName;
    private Button Date, Time, Confirm;
    private LayoutInflater inflater;

    @Before
    public void setUp() throws Exception {
        FirebaseFirestore mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
        inflater = Mockito.mock(LayoutInflater.class);
        PowerMockito.mockStatic(LayoutInflater.class);
        therapySession = spy(Add_Appointment_Page.class) ;//activityTestRule.getActivity();
        //find the elements of activity
        doReturn(AppName).when(therapySession).findViewById(R.id.nameET);
        doReturn(Date).when(therapySession).findViewById(R.id.MTillDays);
//        assertTrue(Date.isClickable());
        doReturn(Time).when(therapySession).findViewById(R.id.MTime);
        doReturn(Confirm).when(therapySession).findViewById(R.id.button);
        suppress(method(Activity.class, "onCreate", Bundle.class));
        suppress(method(Activity.class, "setContentView", int.class));
    }

    //@Test
    public void shouldSetupListener() throws Exception {
        // When we call the onCreate...
        therapySession.onCreate(Mockito.mock(Bundle.class));
        //therapySession.onCreate(null);
        // Then the setOnClickListener method will be called.
        //Mockito.verify(Date).setOnClickListener((View.OnClickListener) any());
        //assertTrue(Date.isClickable());
    }

    @Test
    public void EmptyFields () {
        name = "";
        time = "Set Time";
        date = "Set Date";
        boolean isNotEmptyFields = therapySession.checkFields(name,time,date);
        assertFalse(isNotEmptyFields);
    }

    @Test
    public void NameEmptyField () {
        name = "";
        time = "13 : 00";
        date = "12/9/2020";
        boolean isEmptyFields = therapySession.checkFields(name,time,date);
        assertFalse(isEmptyFields);
    }

    @Test
    public void TimeEmptyField () {
        name = "New App";
        time = "Set Time";
        date = "12/9/2020";
        boolean isEmptyFields = therapySession.checkFields(name,time,date);
        assertFalse(isEmptyFields);
    }

    @Test
    public void DateEmptyField () {
        name = "New App";
        time = "13 : 00";
        date = "Set Day";
        boolean isEmptyFields = therapySession.checkFields(name,time,date);
        assertFalse(isEmptyFields);
    }

    @Test
    public void TimeAndDateEmptyField () {
        name = "New App";
        time = "Set Time";
        date = "Set Day";
        boolean isEmptyFields = therapySession.checkFields(name,time,date);
        assertFalse(isEmptyFields);
    }

    @Test
    public void FillFields () {
        name = "New App";
        time = "13 : 00";
        date = "12/9/2020";
        boolean isFillFields = therapySession.checkFields(name,time,date);
        assertTrue(isFillFields);
    }

    @Test
    public void TimePastCurrentDate () {
        date = current.get(Calendar.DAY_OF_MONTH)
                +"/"+(current.get(Calendar.MONTH)+1)
                +"/"+current.get(Calendar.YEAR);
        time = (current.get(Calendar.HOUR)-1)+" : "+current.get(Calendar.MINUTE);//"13 : 00";
        boolean isFutureTime = therapySession.checkDayandTime(date,time);
        assertFalse(isFutureTime);
    }

    @Test
    public void CurrentTimeCurrentDate () {
        date = current.get(Calendar.DAY_OF_MONTH)
                +"/"+(current.get(Calendar.MONTH)+1)
                +"/"+current.get(Calendar.YEAR);
        time = (current.get(Calendar.HOUR))+" : "+current.get(Calendar.MINUTE);
        boolean isFutureTime = therapySession.checkDayandTime(date,time);
        assertFalse(isFutureTime);
    }

    @Test
    public void FutureTimeCurrentDate () {
        date = current.get(Calendar.DAY_OF_MONTH)
                +"/"+(current.get(Calendar.MONTH)+1)
                +"/"+current.get(Calendar.YEAR);//"12/4/2020";
        try {
            currentTime=TimeFormat.parse(new SimpleDateFormat("HH : mm",Locale.UK)
                    .format(new Date()));
        }
        catch (Exception ex){

        }
        time = currentTime.getHours()+" : "+(currentTime.getMinutes()+20);
        boolean isFutureTime = therapySession.checkDayandTime(date,time);
        assertTrue(isFutureTime);
    }

    @Test
    public void FutureTimeFutureDate () {
        date = (current.get(Calendar.DAY_OF_MONTH)+2)
                +"/"+(current.get(Calendar.MONTH)+1)
                +"/"+(current.get(Calendar.YEAR));//"12/4/2021";
        time = "23 : 14";
        boolean isFutureTime = therapySession.checkDayandTime(date,time);
        assertTrue(isFutureTime);
    }

    @Test(expected = Exception.class)
    public void DateException () {
        date = "hjkl";
        time = "23 : 14";
        therapySession.checkDayandTime(date,time);
    }

    @Test
    public void getRandomID () {
        assertNotNull(therapySession.getRandomID());
    }
}