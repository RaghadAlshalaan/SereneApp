package com.ksu.serene;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseFirestore.class, FirebaseAuth.class, FirebaseUser.class})
public class LogInPageTest {
    private LogInPage logIn;
    String email,password;
    @Mock
    FirebaseAuth mockFirebaseAuth;
    FirebaseUser mockFirebaseUser;

    //TODO test for update token
    //TODO test for check user state
    //TODO test for check sign up setting

    @Before
    public void setUp() throws Exception {
        //make mock obg for firbaseAuth
        mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockFirebaseAuth);
        //make mock obj for firebase user
        mockFirebaseUser = Mockito.mock(FirebaseUser.class);
        PowerMockito.mockStatic(FirebaseUser.class);
        when(FirebaseAuth.getInstance().getCurrentUser()).thenReturn(mockFirebaseUser);
        logIn = new LogInPage();//activityTestRule.getActivity();
        logIn.setmAuth(mockFirebaseAuth);
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

    @Test
    public void loginAuthTest () {
        email = "user@hotmail.com";
        password = "pass00";
        //call log in  from mock firebase
        Task taskMock = Mockito.mock(Task.class);
        when(mockFirebaseAuth.signInWithEmailAndPassword(email,password)).thenReturn(taskMock);
        //here call the log in method
        logIn.login(email,password);
        //
        verify(mockFirebaseAuth, times(1)).signInWithEmailAndPassword(email,password);
        verify(taskMock, times(1)).addOnCompleteListener(any(MyOnCompleteListener.class));

        //check if email verified
        when(mockFirebaseUser.isEmailVerified()).thenReturn(false);
        //call check email verfication from log i  page
        logIn.checkIfEmailVerified();
        verify(mockFirebaseUser, times(1)).isEmailVerified();
    }

    @Test
    public void ResetPassAuthTest () {
        email = "user@hotmail.com";
        //call reset Password from mock firebase
        Task taskMock = Mockito.mock(Task.class);
        when(mockFirebaseAuth.sendPasswordResetEmail(email)).thenReturn(taskMock);
        //here call the reset password method
        logIn.resetPassword(email);
        //
        verify(mockFirebaseAuth, times(1)).sendPasswordResetEmail(email);
        verify(taskMock, times(1)).addOnCompleteListener(any(OnCompleteListener.class));
    }
}