package com.ksu.serene.controller.main.profile;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.MyOnCompleteListener;
import com.ksu.serene.controller.signup.Signup;

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

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseFirestore.class, FirebaseAuth.class, FirebaseUser.class})
public class PatientProfileTest {

    FirebaseAuth mockFirebaseAuth;
    FirebaseUser mockFirebaseUser;
    private PatientProfile patient;
    private String name,email,pass,confirmPass;
    private ArgumentCaptor<OnCompleteListener> onComplete;

    //TODO test for retrieve doc info from DB
    //TODO test for update token DB

    @Before
    public void setUp() throws Exception {
        //mock fireAuth
        mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockFirebaseAuth);
        //mock firestore
        FirebaseFirestore mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
        //mock fireUser
        mockFirebaseUser = Mockito.mock(FirebaseUser.class);
        PowerMockito.mockStatic(FirebaseUser.class);
        when(FirebaseAuth.getInstance().getCurrentUser()).thenReturn(mockFirebaseUser);
        patient = new PatientProfile();//activityTestRule.getActivity();
        patient.setmAuth(mockFirebaseAuth);
        /*name = signup.findViewById(R.id.username);
        email = signup.findViewById(R.id.emailInput);
        pass = signup.findViewById(R.id.passwordInput);
        confirmPass = signup.findViewById(R.id.CpasswordInput);*/
        onComplete = ArgumentCaptor.forClass(OnCompleteListener.class);
    }

    @Test
    public void testSignOut() {
        //call createuser from mock firebase
        Task taskMock = Mockito.mock(Task.class);
        //call sign out from firebase
        mockFirebaseAuth.signOut();
        //call sign out from profile
        patient.signOutFirebase();
        //verify it is sign out
        verify(mockFirebaseAuth, times(2)).signOut();
    }

    @Test
    public void sendVerficstionEmail () {
        Task taskMock = Mockito.mock(Task.class);
        when(mockFirebaseUser.sendEmailVerification()).thenReturn(taskMock);
        patient.sendVerificationEmail();
        verify(mockFirebaseUser, times(1)).sendEmailVerification();
        //verify(taskMock, times(1)).addOnCompleteListener(any(), any());
    }

    //test display name
    //@Test
    public void displayName () {
        when(mockFirebaseUser.getDisplayName()).thenReturn(name);
        patient.displayName();
        verify(mockFirebaseUser, times(1)).getDisplayName();
    }

    //test display email
    //@Test
    public void displayEmail () {
        when(mockFirebaseUser.getEmail()).thenReturn(email);
        patient.displayName();
        verify(mockFirebaseUser, times(1)).getEmail();
    }

}