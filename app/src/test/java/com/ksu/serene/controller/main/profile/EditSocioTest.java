package com.ksu.serene.controller.main.profile;

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

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseFirestore.class, FirebaseAuth.class, ArrayAdapter.class})
public class EditSocioTest {

    private EditSocio editSocioDemo;
    String age,height,weight,MI,CD,EmpS,MartS,CigS;
    //TODO test retrieve data from DB
    //TODO test update Socio in DB

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
        //mock arrayAdapter
        ArrayAdapter mockArrayAdapter= Mockito.mock(ArrayAdapter.class);
        PowerMockito.mockStatic(ArrayAdapter.class);
        //when(ArrayAdapter.class).thenReturn(mockArrayAdapter);
        editSocioDemo = new EditSocio();
    }

    @Test
    public void EmptyFields () {
        age = "";
        height = "";
        weight = "";
        MI = "";
        CD = "";
        EmpS = "";
        MartS = "";
        CigS = "";
        boolean isEmptyFields = editSocioDemo.checkSocioFields(age,height,weight,MI,CD
                ,EmpS,MartS,CigS);
        assertFalse(isEmptyFields);
    }

    @Test
    public void ageEmptyField () {
        age = "";
        height = "150";
        weight = "50";
        MI = "90";
        CD = "No";
        EmpS = "Student";
        MartS = "No";
        CigS = "No";
        boolean isEmptyFields = editSocioDemo.checkSocioFields(age,height,weight,MI,CD
                ,EmpS,MartS,CigS);
        assertFalse(isEmptyFields);
    }

    @Test
    public void heightEmptyField () {
        age = "20";
        height = "";
        weight = "50";
        MI = "90";
        CD = "No";
        EmpS = "Student";
        MartS = "No";
        CigS = "No";
        boolean isEmptyFields = editSocioDemo.checkSocioFields(age,height,weight,MI,CD
                ,EmpS,MartS,CigS);
        assertFalse(isEmptyFields);
    }

    @Test
    public void weightEmptyField () {
        age = "20";
        height = "150";
        weight = "";
        MI = "90";
        CD = "No";
        EmpS = "Student";
        MartS = "No";
        CigS = "No";
        boolean isEmptyFields = editSocioDemo.checkSocioFields(age,height,weight,MI,CD
                ,EmpS,MartS,CigS);
        assertFalse(isEmptyFields);
    }

    @Test
    public void MIEmptyField () {
        age = "20";
        height = "150";
        weight = "50";
        MI = "";
        CD = "No";
        EmpS = "Student";
        MartS = "No";
        CigS = "No";
        boolean isEmptyFields = editSocioDemo.checkSocioFields(age,height,weight,MI,CD
                ,EmpS,MartS,CigS);
        assertFalse(isEmptyFields);
    }

    @Test
    public void CDEmptyField () {
        age = "20";
        height = "150";
        weight = "50";
        MI = "90";
        CD = "";
        EmpS = "Student";
        MartS = "No";
        CigS = "No";
        boolean isEmptyFields = editSocioDemo.checkSocioFields(age,height,weight,MI,CD
                ,EmpS,MartS,CigS);
        assertFalse(isEmptyFields);
    }

    @Test
    public void fillField () {
        age = "20";
        height = "150";
        weight = "50";
        MI = "90";
        CD = "No";
        EmpS = "Student";
        MartS = "No";
        CigS = "No";
        boolean isFill = editSocioDemo.checkSocioFields(age,height,weight,MI,CD
                ,EmpS,MartS,CigS);
        assertTrue(isFill);
    }

    @Test
    public void notValidAge() {
        //below
        double age = 3;
        boolean notValidMin = editSocioDemo.isValidAge(age);
        assertFalse(notValidMin);
        //above
        age = 115;
        boolean notValidMax = editSocioDemo.isValidAge(age);
        assertFalse(notValidMax);
    }

    @Test
    public void ValidAge() {
        double age = 20;
        boolean isValid = editSocioDemo.isValidAge(age);
        assertTrue(isValid);
    }

    @Test
    public void notValidHeight() {
        //below
        double height = 15;
        boolean notValidMin = editSocioDemo.isValidHeight(height);
        assertFalse(notValidMin);
        //above
        height = 305;
        boolean notValidMax = editSocioDemo.isValidHeight(height);
        assertFalse(notValidMax);
    }

    @Test
    public void ValidHeight() {
        double height = 120;
        boolean isValid = editSocioDemo.isValidHeight(height);
        assertTrue(isValid);
    }

    @Test
    public void notValidWeight() {
        //below
        double weight = 15;
        boolean notValidMin = editSocioDemo.isValidWeight(weight);
        assertFalse(notValidMin);
        //above
        weight = 305;
        boolean notValidMax = editSocioDemo.isValidWeight(weight);
        assertFalse(notValidMax);
    }

    @Test
    public void ValidWeight() {
        double weight = 120;
        boolean isValid = editSocioDemo.isValidWeight(weight);
        assertTrue(isValid);
    }

    @Test
    public void notValidMI() {
        //below
        double MI = -1;
        boolean notValidMin = editSocioDemo.isValidMonthlyIncome(MI);
        assertFalse(notValidMin);
        //above
        MI = 5100000;
        boolean notValidMax = editSocioDemo.isValidMonthlyIncome(MI);
        assertFalse(notValidMax);
    }

    @Test
    public void ValidMI() {
        double MI = 120;
        boolean isValid = editSocioDemo.isValidMonthlyIncome(MI);
        assertTrue(isValid);
    }

    @Test
    public void notValidCD() {
        CD = "199";
        boolean notValid = editSocioDemo.isValidChronicDisease(CD);
        assertFalse(notValid);
    }

    @Test
    public void isValidCD() {
        CD = "No";
        boolean isValid = editSocioDemo.isValidChronicDisease(CD);
        assertTrue(isValid);
    }
}