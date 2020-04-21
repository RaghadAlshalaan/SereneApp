package com.ksu.serene.controller.signup;

import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.MyOnCompleteListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.function.Predicate;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseFirestore.class, FirebaseAuth.class, FirebaseUser.class, TextUtils.class})
public class SignupTest {

    FirebaseAuth mockFirebaseAuth;
    private Signup signup;
    private String name,email,pass,confirmPass;
    private ArgumentCaptor<OnCompleteListener> onComplete;
    @Mock FirebaseUser mockFirebaseUser;
    @Mock Task<AuthResult> mockAuthResultTask;
    @Mock AuthResult mockAuthResult;

    //TODO test for update token
    //TODO test save user in DB

    @Before
    public void setUp() throws Exception {
        //make mock obj for fireAuth
        mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockFirebaseAuth);
        //make mock obj for fireStore
        FirebaseFirestore mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
        //make mock obj for firebase user
        mockFirebaseUser = Mockito.mock(FirebaseUser.class);
        PowerMockito.mockStatic(FirebaseUser.class);
        when(FirebaseAuth.getInstance().getCurrentUser()).thenReturn(mockFirebaseUser);
        //mock for TextUtil used in UserProfile
        TextUtils mockTextUtil = Mockito.mock(TextUtils.class);
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.when(TextUtils.isEmpty(any(CharSequence.class))).thenReturn(true);
        signup = new Signup();//activityTestRule.getActivity();
        signup.setmAuth(mockFirebaseAuth);
        /*name = signup.findViewById(R.id.username);
        email = signup.findViewById(R.id.emailInput);
        pass = signup.findViewById(R.id.passwordInput);
        confirmPass = signup.findViewById(R.id.CpasswordInput);*/
        onComplete = ArgumentCaptor.forClass(OnCompleteListener.class);
    }

    @Test
    public void EmptyFields () {
        name = ("");
        email=("");
        pass=("");
        confirmPass=("");
        boolean isEmptyFields = signup.CheckFields(name,email
                ,pass,confirmPass);
        assertFalse(isEmptyFields);
    }

    @Test
    public void nameEmptyField () {
        name=("");
        email=("user@hotmail.com");
        pass=("pass00");
        confirmPass=("pass00");
        boolean isNameEmpty = signup.CheckFields(name,email
                ,pass,confirmPass);
        assertFalse(isNameEmpty);
    }

    @Test
    public void emailEmptyField () {
        name=("user");
        email=("");
        pass=("pass00");
        confirmPass=("pass00");
        boolean isEmailEmpty = signup.CheckFields(name,email
                ,pass,confirmPass);
        assertFalse(isEmailEmpty);
    }

    @Test
    public void passEmptyField () {
        name=("user");
        email=("user@hotmail.com");
        pass=("");
        confirmPass=("pass00");
        boolean isPassEmpty = signup.CheckFields(name,email
                ,pass,confirmPass);
        assertFalse(isPassEmpty);
    }

    @Test
    public void confirmPassEmptyField () {
        name=("user");
        email=("user@hotmail.com");
        pass=("pass00");
        confirmPass=("");
        boolean isCPEmpty = signup.CheckFields(name,email
                ,pass,confirmPass);
        assertFalse(isCPEmpty);
    }

    @Test
    public void fillField () {
        name=("user");
        email=("user@hotmail.com");
        pass=("pass00");
        confirmPass=("pass00");
        boolean isFill = signup.CheckFields(name,email
                ,pass,confirmPass);
        assertTrue(isFill);
    }

    @Test
    public void notValidName() {
        name=("00User");
        boolean notValid = signup.validName(name);
        assertFalse(notValid);
    }

    @Test
    public void ValidName() {
        name=("User");
        boolean isValid = signup.validName(name);
        assertTrue(isValid);    }

    @Test
    public void notValidEmail() {
        email=("user@1234.user");
        boolean notValid = signup.isValidEmail(email);
        assertFalse(notValid);
    }

    @Test
    public void ValidEmail() {
        email=("user@hotmail.com");
        boolean isValid = signup.isValidEmail(email);
        assertTrue(isValid);
    }


    @Test
    public void passNotMatch() {
        pass=("pass00");
        confirmPass=("pass99");
        boolean notMatch = signup.passMatch(pass,confirmPass);
        assertFalse(notMatch);
    }

    @Test
    public void passMatch() {
        pass=("pass00");
        confirmPass=("pass00");
        boolean Match = signup.passMatch(pass,confirmPass);
        assertTrue(Match);
    }

    @Test
    public void shortPass() {
        pass=("pass");
        boolean shortPass = signup.isShortPass(pass);
        assertFalse(shortPass);

        confirmPass=("pass");
        boolean shortConfirmPass = signup.isShortPass(confirmPass);
        assertFalse(shortConfirmPass);
    }

    @Test
    public void passLengthFine() {
        pass=("pass00");
        boolean lengthPassFine = signup.isShortPass(pass);
        assertTrue(lengthPassFine);

        confirmPass=("pass00");
        boolean lengthConfirmPassFine = signup.isShortPass(confirmPass);
        assertTrue(lengthConfirmPassFine);
    }

    @Test
    public void testCreateUserWithNameEmailAndPassword() {
        //set the mock data for registration
        String email = "somusername@hotmail.com";
        String password = "password";
        String name = "user";
        //call createuser from mock firebase
        Task taskMock = Mockito.mock(Task.class);
        when(mockFirebaseAuth.createUserWithEmailAndPassword(email,password)).thenReturn(taskMock);
        //call method that create user in sign up activity
        //assertNotNull(signup.createUserAccount(email, password, password, name));
        signup.createUserAccount(email, password, password, name);
        verify(mockFirebaseAuth, times(1)).createUserWithEmailAndPassword(email,password);
        verify(taskMock, times(1)).addOnCompleteListener(any(Signup.class), any(MyOnCompleteListener.class));

        //after that test when the email and profile updated
        //test update profile
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
        when(mockFirebaseUser.updateProfile(profileUpdates)).thenReturn(taskMock);
        //here call the delete account method
        signup.updateProfile(profileUpdates);//,profileUpdates);
        //
        verify(mockFirebaseUser, times(1)).updateProfile(profileUpdates);
        verify(taskMock, times(1)).addOnCompleteListener(any(OnCompleteListener.class));
        //test update email
        when(mockFirebaseUser.updateEmail(email)).thenReturn(taskMock);
        //call update emial form sign up page
        signup.updateEmail(email);
        verify(mockFirebaseUser, times(1)).updateEmail(email);
        verify(taskMock, times(1)).addOnCompleteListener(any(Signup.class), any(MyOnCompleteListener.class));
    }

    @Test
    public void testCreateUserWithUnvalidEmail() {
        //set the mock data for registration
        String email = "somusername@45.com";
        String password = "password";
        String name = "user";
        //call createuser from mock firebase
        Task taskMock = Mockito.mock(Task.class);
        when(mockFirebaseAuth.createUserWithEmailAndPassword(email,password)).thenReturn(taskMock);
        //call method that create user in sign up activity
        assertNull(signup.createUserAccount(email, password, password, name));

        verify(mockFirebaseAuth, times(1)).createUserWithEmailAndPassword(email,password);
        verify(taskMock, times(1)).addOnCompleteListener(any(Signup.class), any(MyOnCompleteListener.class));
    }

    @Test
    public void testCreateUserWithShortPass() {
        //set the mock data for registration
        String email = "somusername@hotmail.com";
        String password = "pass";
        String name = "user";
        //call createuser from mock firebase
        Task taskMock = Mockito.mock(Task.class);
        when(mockFirebaseAuth.createUserWithEmailAndPassword(email,password)).thenReturn(taskMock);
        //call method that create user in sign up activity
        assertNull(signup.createUserAccount(email, password, password, name));

        verify(mockFirebaseAuth, times(1)).createUserWithEmailAndPassword(email,password);
        verify(taskMock, times(1)).addOnCompleteListener(any(Signup.class), any(MyOnCompleteListener.class));
    }

    @Test
    public void sendVerficstionEmail () {
        Task taskMock = Mockito.mock(Task.class);
        when(mockFirebaseUser.sendEmailVerification()).thenReturn(taskMock);
        signup.sendVerificationEmail();
        verify(mockFirebaseUser, times(1)).sendEmailVerification();
        //verify(taskMock, times(1)).addOnCompleteListener(any(), any());

    }

}