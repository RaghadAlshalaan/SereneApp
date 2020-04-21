package com.ksu.serene.controller.signup;

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

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseFirestore.class})
public class GAD7Test {
    private GAD7 gad;

    //TODO test when add gadScore to DB

    @Before
    public void setUp() throws Exception {
        FirebaseFirestore mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
        gad = new GAD7();
    }

    @Test
    public void EmptyFields () {
        String R1 = "";
        String R2 = "";
        String R3 = "";
        String R4 = "";
        String R5 = "";
        String R6 = "";
        String R7 = "";
        boolean isEmptyFields = gad.checkFields(R1,R2,R3,R4,R5,R6,R7);
        assertFalse(isEmptyFields);
    }

    @Test
    public void OneEmptyField () {
        String R1 = "";
        String R2 = "notNull";
        String R3 = "notNull";
        String R4 = "notNull";
        String R5 = "notNull";
        String R6 = "notNull";
        String R7 = "notNull";
        boolean isOneEmpty = gad.checkFields(R1,R2,R3,R4,R5,R6,R7);
        assertFalse(isOneEmpty);
    }

    @Test
    public void MorethanTwoEmptyField () {
        String R1 = "";
        String R2 = "";
        String R3 = "";
        String R4 = "notNull";
        String R5 = "notNull";
        String R6 = "notNull";
        String R7 = "notNull";
        boolean isMTTEmpty = gad.checkFields(R1,R2,R3,R4,R5,R6,R7);
        assertFalse(isMTTEmpty);
    }

    @Test
    public void HalfEmptyField () {
        String R1 = "";
        String R2 = "";
        String R3 = "";
        String R4 = "";
        String R5 = "notNull";
        String R6 = "notNull";
        String R7 = "notNull";
        boolean isHalfEmpty = gad.checkFields(R1,R2,R3,R4,R5,R6,R7);
        assertFalse(isHalfEmpty);
    }

    @Test
    public void NoEmptyField () {
        String R1 = "notNull";
        String R2 = "notNull";
        String R3 = "notNull";
        String R4 = "notNull";
        String R5 = "notNull";
        String R6 = "notNull";
        String R7 = "notNull";
        boolean isNotEmpty = gad.checkFields(R1,R2,R3,R4,R5,R6,R7);
        assertTrue(isNotEmpty);
    }
}