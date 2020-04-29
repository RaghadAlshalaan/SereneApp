package com.ksu.serene.controller.main.calendar;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
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
import static org.mockito.Mockito.when;
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseFirestore.class})
public class Add_Medicine_PageTest {

    //TODO test saveMed in DB
    //TODO test set reminder
    private Add_Medicine_Page medicine;
    String name,time,date,dose,interval,repeatNo;
    Calendar current = Calendar.getInstance();
    SimpleDateFormat TimeFormat = new SimpleDateFormat ("HH : mm", Locale.UK);
    Date currentTime ;


    @Before
    public void setUp() throws Exception {
        FirebaseFirestore mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
        medicine = new Add_Medicine_Page();//activityTestRule.getActivity();
    }

    //(String MName , String MDose, String interval, String repeatNo, String time, String date)
    @Test
    public void EmptyFields () {
        name = "";
        time = "Set Time";
        date = "Set Date";
        dose = "";
        interval = "";
        repeatNo = "";
        boolean isEmptyFields = medicine.checkFields(name,dose,interval,repeatNo,time,date);
        assertFalse(isEmptyFields);
    }

    @Test
    public void NameEmptyField () {
        name = "";
        time = "13 : 00";
        date = "12/9/2020";
        dose = "1";
        interval = "Day/s";
        repeatNo = "2";
        boolean isEmptyFields = medicine.checkFields(name,dose,interval,repeatNo,time,date);
        assertFalse(isEmptyFields);
    }

    @Test
    public void TimeEmptyField () {
        name = "New App";
        time = "Set Time";
        date = "12/9/2020";
        dose = "1";
        interval = "Day/s";
        repeatNo = "2";
        boolean isEmptyFields = medicine.checkFields(name,dose,interval,repeatNo,time,date);
        assertFalse(isEmptyFields);
    }

    @Test
    public void DateEmptyField () {
        name = "New App";
        time = "13 : 00";
        date = "Set Date";
        dose = "1";
        interval = "Day/s";
        repeatNo = "2";
        boolean isEmptyFields = medicine.checkFields(name,dose,interval,repeatNo,time,date);
        assertFalse(isEmptyFields);
    }

    @Test
    public void DoseEmptyField () {
        name = "New App";
        time = "13 : 00";
        date = "11/4/2021";
        dose = "";
        interval = "Day/s";
        repeatNo = "2";
        boolean isEmptyFields = medicine.checkFields(name,dose,interval,repeatNo,time,date);
        assertFalse(isEmptyFields);
    }

    @Test
    public void IntervalEmptyField () {
        name = "New App";
        time = "13 : 00";
        date = "11/4/2021";
        dose = "1";
        interval = "";
        repeatNo = "2";
        boolean isEmptyFields = medicine.checkFields(name,dose,interval,repeatNo,time,date);
        assertFalse(isEmptyFields);
    }

    @Test
    public void RTEmptyField () {
        name = "New App";
        time = "13 : 00";
        date = "11/4/2021";
        dose = "1";
        interval = "Day/s";
        repeatNo = "";
        boolean isEmptyFields = medicine.checkFields(name,dose,interval,repeatNo,time,date);
        assertFalse(isEmptyFields);
    }

    @Test
    public void FillFields () {
        name = "New App";
        time = "13 : 00";
        date = "12/9/2020";
        dose = "1";
        interval = "Day/s";
        repeatNo = "2";
        boolean isFillFields = medicine.checkFields(name,dose,interval,repeatNo,time,date);
        assertTrue(isFillFields);
    }

    //TODO update this data it depend on current date
    @Test
    public void TimePastCurrentDate () {
        date = current.get(Calendar.DAY_OF_MONTH)+"/"+(current.get(Calendar.MONTH)+1)+"/"+current.get(Calendar.YEAR);
        try {
            currentTime=TimeFormat.parse(new SimpleDateFormat("HH : mm",Locale.UK).format(new Date()));
        }
        catch (Exception ex){

        }
        time = currentTime.getHours()+" : "+(currentTime.getMinutes()-20);
        boolean isPastDate = medicine.checkDayandTime(date,time);
        assertFalse(isPastDate);
    }

    @Test
    public void CurrentTimeCurrentDate () {
        date = current.get(Calendar.DAY_OF_MONTH)+"/"+(current.get(Calendar.MONTH)+1)+"/"+current.get(Calendar.YEAR);
        try {
            currentTime=TimeFormat.parse(new SimpleDateFormat("HH : mm",Locale.UK).format(new Date()));
        }
        catch (Exception ex){

        }
        time = currentTime.getHours()+" : "+(currentTime.getMinutes());
        boolean isPastDate = medicine.checkDayandTime(date,time);
        assertFalse(isPastDate);
    }

    @Test
    public void FutureTimeCurrentDate () {
        date = current.get(Calendar.DAY_OF_MONTH)+"/"+(current.get(Calendar.MONTH)+1)+"/"+current.get(Calendar.YEAR);
        try {
            currentTime=TimeFormat.parse(new SimpleDateFormat("HH : mm",Locale.UK).format(new Date()));
        }
        catch (Exception ex){

        }
        time = currentTime.getHours()+" : "+(currentTime.getMinutes()+20);
        boolean isFutureTime = medicine.checkDayandTime(date,time);
        assertTrue(isFutureTime);
    }

    @Test
    public void FutureTimeFutureDate () {
        date = (current.get(Calendar.DAY_OF_MONTH)+2)+"/"+(current.get(Calendar.MONTH)+1)+"/"+(current.get(Calendar.YEAR));
        String time = "23 : 14";
        boolean isFutureTime = medicine.checkDayandTime(date,time);
        assertTrue(isFutureTime);
    }

    @Test
    public void FutureTimeFutureYear () {
        date = (current.get(Calendar.DAY_OF_MONTH))+"/"+(current.get(Calendar.MONTH)+1)+"/"+(current.get(Calendar.YEAR)+1);
        String time = "23 : 14";
        boolean isFutureTime = medicine.checkDayandTime(date,time);
        assertTrue(isFutureTime);
    }

    @Test(expected = Exception.class)
    public void DateException () {
        date = "hjkl";
        time = "23 : 14";
        medicine.checkDayandTime(date,time);
    }

    //@Test(expected = Exception.class)
    public void TimeException () {
        date = "12/4/2021";
        time = "hjkl";
        medicine.checkDayandTime(date,time);
    }

    @Test
    public void getRandomID () {
        assertNotNull(medicine.getRandomID());
    }

}