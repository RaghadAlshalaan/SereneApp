package com.ksu.serene.controller.main.report;

import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
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

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseFirestore.class, FirebaseAuth.class})
public class ReportFragmentTest {
    private ReportFragment fragment;
    String startDate;
    String endDate;

    @Before
    public void setUp() throws Exception {
        //mock firebaseFirestore
        FirebaseFirestore mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
        //mock fireAuth
        FirebaseAuth mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockFirebaseAuth);
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
        isCurrent.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-1);
        boolean isEqual = fragment.isStartDEqualToCurrent(isCurrent);
        assertTrue(isEqual);
    }

    @Test
    public void isDatesNotChoosen() {
        int allDatesNotSet = fragment.isDatesChosen(startDate, endDate);
        assertThat(allDatesNotSet, is(1));
    }

    @Test
    public void isEDateNotChoosen() {
        startDate = "13/4/2020";
        int endDateNotSet = fragment.isDatesChosen(startDate, endDate);
        assertThat(endDateNotSet, is(-1));
    }

    @Test
    public void isDatesChoosen() {
        startDate = "13/4/2020";
        endDate = "13/4/2020";
        int allDatesSet = fragment.isDatesChosen(startDate, endDate);
        assertThat(allDatesSet, is(0));
    }

}