package com.ksu.serene.controller.main.profile;

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
public class AddDoctorTest {
    private AddDoctor addDoctor;
    private String name, email;

    @Before
    public void setUp() throws Exception {
        FirebaseFirestore mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
        addDoctor = new AddDoctor();//activityTestRule.getActivity();
    }

    @Test
    public void EmptyFields () throws Exception {
        name=("");
        email=("");
        assertFalse(addDoctor.EmptyFields(name,email));
    }

    @Test
    public void NameEmptyField () throws Exception {
        name=("");
        email=("Lama-almarshad@hotmail.com");
        assertFalse(addDoctor.EmptyFields(name,email));
    }

    @Test
    public void EmailEmptyField () throws Exception {
        name=("Lama");
        email=("");
        assertFalse(addDoctor.EmptyFields(name,email));
    }

    @Test
    public void FiledFields () throws Exception {
        name=("Lama");
        email=("Lama-almarshad@hotmail.com");
        assertTrue(addDoctor.EmptyFields(name,email));
    }

    @Test
    public void notValidName () throws Exception {
        name=("DR%^*G");
        assertFalse(addDoctor.checkNameValidation(name));
    }

    @Test
    public void isValidName () throws Exception {
        name=("Lama");
        assertTrue(addDoctor.checkNameValidation(name));
    }

    @Test
    public void notValidEmail () throws Exception {
        email=("Ahmed@3456.com");
        assertFalse(addDoctor.checkEmailValidation(email));
    }

    @Test
    public void isValidEmail () throws Exception {
        email=("Ahmed@otlouk.com");
        assertTrue(addDoctor.checkEmailValidation(email));
    }

}