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
public class SociodemoTest {

    private Sociodemo socioDemo;
    private String age,height,weight,MI,CD;

    ///TODO test for save the socio in DB

    @Before
    public void setUp() throws Exception {
        FirebaseFirestore mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
        socioDemo = new Sociodemo();
    }

    @Test
    public void EmptyFields () {
        age =("");
        height =("");
        weight =("");
        MI =("");
        CD =("");
        boolean isEmptyFields = socioDemo.checkSocioFields(age,height,weight,MI,CD);
        assertFalse(isEmptyFields);
    }

    @Test
    public void ageEmptyField () {
        age =("");
        height = ("150");
        weight = ("50");
        MI = ("90");
        CD = ("No");
        boolean isEmptyFields = socioDemo.checkSocioFields(age,height,weight,MI,CD);
        assertFalse(isEmptyFields);
    }

    @Test
    public void heightEmptyField () {
        age = ("20");
        height=("");
        weight=("50");
        MI=("90");
        CD=("No");
        boolean isEmptyFields = socioDemo.checkSocioFields(age,height,weight,MI,CD);
        assertFalse(isEmptyFields);
    }

    @Test
    public void weightEmptyField () {
        age=("20");
        height=("150");
        weight=("");
        MI=("90");
        CD=("No");
        boolean isEmptyFields =socioDemo.checkSocioFields(age,height,weight,MI,CD);
        assertFalse(isEmptyFields);
    }

    @Test
    public void MIEmptyField () {
        age=("20");
        height=("150");
        weight=("50");
        MI=("");
        CD=("No");
        boolean isEmptyFields = socioDemo.checkSocioFields(age,height,weight,MI,CD);
        assertFalse(isEmptyFields);
    }

    @Test
    public void CDEmptyField () {
        age=("20");
        height=("150");
        weight=("50");
        MI=("90");
        CD=("");
        boolean isEmptyFields = socioDemo.checkSocioFields(age,height,weight,MI,CD);
        assertFalse(isEmptyFields);
    }

    @Test
    public void fillField () {
        age=("20");
        height=("150");
        weight=("50");
        MI=("90");
        CD=("No");
        boolean isFill =socioDemo.checkSocioFields(age,height,weight,MI,CD);
        assertTrue(isFill);
    }

    @Test
    public void notValidAge() {
        //below
        age=("3");
        double ageMin = Double.parseDouble(age.toString());
        boolean notValidMin = socioDemo.isValidAge(ageMin);
        assertFalse(notValidMin);
        //above
        age=("115");
        double ageMax = Double.parseDouble(age.toString());
        boolean notValidMax = socioDemo.isValidAge(ageMax);
        assertFalse(notValidMax);
    }

    @Test
    public void ValidAge() {
        age=("30");
        double ageValid = Double.parseDouble(age.toString());
        boolean isValid = socioDemo.isValidAge(ageValid);
        assertTrue(isValid);
    }

    @Test
    public void notValidHeight() {
        //below
        height=("15");
        double heightMin = Double.parseDouble(height.toString());
        boolean notValidMin = socioDemo.isValidHeight(heightMin);
        assertFalse(notValidMin);
        //above
        height=("305");
        double heightMax = Double.parseDouble(height.toString());
        boolean notValidMax = socioDemo.isValidHeight(heightMax);
        assertFalse(notValidMax);
    }

    @Test
    public void ValidHeight() {
        height=("110");
        double heightValid = Double.parseDouble(height.toString());
        boolean isValid = socioDemo.isValidHeight(heightValid);
        assertTrue(isValid);
    }

    @Test
    public void notValidWeight() {
        //below
        weight=("15");
        double weightMin = Double.parseDouble(weight.toString());
        boolean notValidMin = socioDemo.isValidWeight(weightMin);
        assertFalse(notValidMin);
        //above
        weight=("305");
        double weightMax = Double.parseDouble(weight.toString());
        boolean notValidMax = socioDemo.isValidWeight(weightMax);
        assertFalse(notValidMax);
    }

    @Test
    public void ValidWeight() {
        weight=("50");
        double weightValid = Double.parseDouble(weight.toString());
        boolean isValid = socioDemo.isValidWeight(weightValid);
        assertTrue(isValid);
    }

    @Test
    public void notValidMI() {
        //above
        MI=("51000000");
        double MIMax = Double.parseDouble(MI.toString());
        boolean notValidMax = socioDemo.isValidMonthlyIncome(MIMax);
        assertFalse(notValidMax);
    }

    @Test
    public void ValidMI() {
        MI=("120");
        double MIValid = Double.parseDouble(MI.toString());
        boolean isValid = socioDemo.isValidMonthlyIncome(MIValid);
        assertTrue(isValid);
    }

    @Test
    public void notValidCD() {
        CD=("199");
        //String notValidCD = CD.getText().toString();
        boolean notValid = socioDemo.isValidChronicDisease(CD);
        assertFalse(notValid);
    }

    @Test
    public void isValidCD() {
        CD=("No");
        //String isValidCD = CD.getText().toString();
        boolean isValid = socioDemo.isValidChronicDisease(CD);
        assertTrue(isValid);
    }
}