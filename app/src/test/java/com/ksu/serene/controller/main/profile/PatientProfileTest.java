package com.ksu.serene.controller.main.profile;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
@PrepareForTest({ FirebaseFirestore.class, FirebaseAuth.class})
public class PatientProfileTest {

    FirebaseAuth mockFirebaseAuth;
    private PatientProfile patient;
    private String name,email,pass,confirmPass;
    private ArgumentCaptor<OnCompleteListener> onComplete;

    @Before
    public void setUp() throws Exception {
        mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockFirebaseAuth);
        FirebaseFirestore mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
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
}