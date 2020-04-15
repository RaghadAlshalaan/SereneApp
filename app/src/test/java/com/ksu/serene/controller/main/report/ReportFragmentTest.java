package com.ksu.serene.controller.main.report;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ReportFragmentTest {
    private ReportFragment fragment;
    String startDate;
    String endDate;

    @Before
    public void setUp() throws Exception {
        fragment = new ReportFragment();
    }

    @Test
    public void isStartDateNotSet() {
        boolean notSet = fragment.isStartDateSet(startDate);
        assertFalse(notSet);
    }

    @Test
    public void isStartDateSet() {
        startDate = "13/4/2020";
        boolean isSet = fragment.isStartDateSet(startDate);
        assertTrue(isSet);
    }

    @Test
    public void isStartDNotEqualToCurrent() {
        Calendar notCurrent = Calendar.getInstance();
        notCurrent.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        notCurrent.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH)-1);
        notCurrent.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        boolean notEqual = fragment.isStartDEqualToCurrent(notCurrent);
        assertFalse(notEqual);
    }

    @Test
    public void isStartDEqualToCurrent() {
        Calendar isCurrent = Calendar.getInstance();
        isCurrent.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        isCurrent.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
        isCurrent.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        boolean isEqual = fragment.isStartDEqualToCurrent(isCurrent);
        assertTrue(isEqual);
    }

    @Test
    public void isDatesNotChoosen() {
        int allDatesNotSet = fragment.isDatesChoosen(startDate, endDate);
        assertThat(allDatesNotSet, is(2));
    }

    @Test
    public void isSDateNotChoosen() {
        endDate = "13/4/2020";
        int startDateNotSet = fragment.isDatesChoosen(startDate, endDate);
        assertThat(startDateNotSet, is(1));
    }

    @Test
    public void isEDateNotChoosen() {
        startDate = "13/4/2020";
        int endDateNotSet = fragment.isDatesChoosen(startDate, endDate);
        assertThat(endDateNotSet, is(-1));
    }

    @Test
    public void isDatesChoosen() {
        startDate = "13/4/2020";
        endDate = "13/4/2020";
        int allDatesSet = fragment.isDatesChoosen(startDate, endDate);
        assertThat(allDatesSet, is(0));
    }

}