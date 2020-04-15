package com.ksu.serene;

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
@PrepareForTest({ FirebaseFirestore.class,FirebaseAuth.class})
public class LogInPageTest {
    private LogInPage logIn;
    String email,password;

    @Before
    public void setUp() throws Exception {
        FirebaseAuth mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockFirebaseAuth);
        logIn = new LogInPage();//activityTestRule.getActivity();
    }

    @Test
    public void EmptyFields () {
        email = "";
        password = "";
        boolean isEmptyFields = logIn.CheckLogInFields(email,password);
        assertFalse(isEmptyFields);
    }

    @Test
    public void emailEmptyField () {
        email = "";
        password ="pass00";
        boolean isEmailEmpty = logIn.CheckLogInFields(email,password);
        assertFalse(isEmailEmpty);
    }

    @Test
    public void passEmptyField () {
        email = "user@hotmail.com";
        password = "";
        boolean isPassEmpty =  logIn.CheckLogInFields(email,password);
        assertFalse(isPassEmpty);
    }

    @Test
    public void FillFields () {
        email = "user@hotmail.com";
        password = "pass00";
        boolean isFillFields = logIn.CheckLogInFields(email,password);
        assertTrue(isFillFields);
    }

    @Test
    public void notValidEmail() {
        email = "user@1234.user";
        boolean isValid = logIn.isValidEmail(email);
        assertFalse(isValid);
    }

    @Test
    public void emailEmptyFieldForgetPassowrd () {
        email = "";
        password ="pass00";
        boolean isEmailEmpty = logIn.CheckEmailField(email);
        assertFalse(isEmailEmpty);
    }

    @Test
    public void emailFilledFieldForgetPassowrd () {
        email = "user@hotmailcom";
        password ="pass00";
        boolean isFillEmpty = logIn.CheckEmailField(email);
        assertTrue(isFillEmpty);
    }

    @Test
    public void ValidEmail() {
        email = "user@hotmail.com";
        boolean isValid = logIn.isValidEmail(email);
        assertTrue(isValid);
    }

    @Test
    public void shortPass() {
        password = "pass";
        boolean shortPass = logIn.isShortPass(password);
        assertFalse(shortPass);
    }

    @Test
    public void lengthPassFine() {
        password = "pass00";
        boolean shortPass = logIn.isShortPass(password);
        assertTrue(shortPass);
    }
}