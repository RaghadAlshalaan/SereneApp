package com.ksu.serene.controller.main.calendar;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class CalendarFragmentTest {

    private CalendarFragment calendarFragment;
    private Calendar current = Calendar.getInstance();
    String date;

    @Before
    public void setUp() throws Exception {
        calendarFragment = new CalendarFragment();
    }

    @Test
    public void testCalendarDateNull () {
        assertNull(calendarFragment.checkCalendarDate
                (12,4,2019));
    }

    @Test
    public void tesCalendarDateCurrent () {
        date = (calendarFragment.checkCalendarDate
                (current.get(Calendar.DAY_OF_MONTH)
                ,(current.get(Calendar.MONTH)+1)
                        ,(current.get(Calendar.YEAR))));
        assertNotNull(date);
    }

    @Test
    public void tesCalendarDateFuture () {
        date = (calendarFragment.checkCalendarDate
                (5,4,2021));
        assertNotNull(date);
        assertEquals(date, "0"+5+"/"+"0"+4+"/"+2021);
    }

    @Test
    public void tesCalendarDateFuture2 () {
        date = (calendarFragment.checkCalendarDate
                (15,4,2021));
        assertNotNull(date);
        assertEquals(date, 15+"/"+"0"+4+"/"+2021);
    }

    @Test
    public void tesCalendarDateFuture3 () {
        date = (calendarFragment.checkCalendarDate
                (5,10,2021));
        assertNotNull(date);
        assertEquals(date, "0"+5+"/"+10+"/"+2021);
    }

    @Test
    public void tesCalendarDateFuture4 () {
        date = (calendarFragment.checkCalendarDate
                (15,10,2021));
        assertNotNull(date);
        assertEquals(date, 15+"/"+10+"/"+2021);
    }


}