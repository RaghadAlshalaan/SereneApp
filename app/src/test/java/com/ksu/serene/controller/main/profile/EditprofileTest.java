package com.ksu.serene.controller.main.profile;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

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
@PrepareForTest({ FirebaseFirestore.class, FirebaseAuth.class, FirebaseStorage.class})
public class EditprofileTest {
    private Editprofile editProfile;
    String name,oldPass,newPass,confirmNewPass;

    @Before
    public void setUp() throws Exception {
        FirebaseFirestore mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
        FirebaseAuth mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockFirebaseAuth);
        FirebaseStorage mockFirebaseStorage = Mockito.mock(FirebaseStorage.class);
        PowerMockito.mockStatic(FirebaseStorage.class);
        when(FirebaseStorage.getInstance()).thenReturn(mockFirebaseStorage);
        editProfile = new Editprofile();//activityTestRule.getActivity();
    }

    @Test
    public void nameEmptyField () {
        name= "";
        boolean isNameEmpty = editProfile.CheckNameField(name);
        assertFalse(isNameEmpty);
    }

    @Test
    public void fillField () {
        name = "user";
        boolean isFill = editProfile.CheckNameField(name);
        assertTrue(isFill);
    }

    @Test
    public void notValidName() {
        name = "199";
        boolean notValid = editProfile.isValidName(name);
        assertFalse(notValid);
    }

    @Test
    public void ValidName() {
        name = "Lama";
        boolean isValid = editProfile.isValidName(name);
        assertTrue(isValid);
    }

    @Test
    public void ResetPasswordEmptyFiels () {
        oldPass = "";
        newPass = "";
        confirmNewPass = "";
        boolean isEmpty = editProfile.CheckPassField(oldPass,newPass,confirmNewPass);
        assertFalse(isEmpty);
    }

    @Test
    public void ResetPasswordFilledFiels () {
        oldPass = "pass00";
        newPass = "pass99";
        confirmNewPass = "pass99";
        boolean isFilled = editProfile.CheckPassField(oldPass,newPass,confirmNewPass);
        assertTrue(isFilled);
    }

    @Test
    public void newPassEqualToOldPass() {
        oldPass = "pass199";
        newPass = "pass199";
        confirmNewPass = "pass99";
        boolean samePassword = editProfile.sameOldPassword(oldPass,newPass);
        assertTrue(samePassword);
    }

    @Test
    public void isNewPassword() {
        oldPass = "pass199";
        newPass = "pass99";
        confirmNewPass = "pass99";
        boolean notSamePass = editProfile.sameOldPassword(oldPass,newPass);
        assertFalse(notSamePass);
    }

    @Test
    public void oldPassShort () {
        oldPass = "pass";
        newPass = "pass99";
        confirmNewPass = "pass99";
        boolean isOldPassShort = editProfile.checkResetPassLength(oldPass,newPass,confirmNewPass);
        assertFalse(isOldPassShort);
    }

    @Test
    public void newPassShort () {
        oldPass = "pass199";
        newPass = "pass";
        confirmNewPass = "pass99";
        boolean isNewPassShort = editProfile.checkResetPassLength(oldPass,newPass,confirmNewPass);
        assertFalse(isNewPassShort);
    }

    @Test
    public void newConfirmPassShort () {
        oldPass = "pass199";
        newPass = "pass00";
        confirmNewPass = "pass";
        boolean isConfirmNewPassShort = editProfile.checkResetPassLength(oldPass,newPass,confirmNewPass);
        assertFalse(isConfirmNewPassShort);
    }

    @Test
    public void resetPassLengthFine () {
        oldPass = "pass199";
        newPass = "pass00";
        confirmNewPass = "pass00";
        boolean resetPassFine = editProfile.checkResetPassLength(oldPass,newPass,confirmNewPass);
        assertTrue(resetPassFine);
    }

    @Test
    public void PassNotMatch () {
        newPass = "pass00";
        confirmNewPass = "pass99";
        boolean isNotMatch = editProfile.isPasswordMatch(newPass,confirmNewPass);
        assertFalse(isNotMatch);
    }

    @Test
    public void PassisMatch () {
        newPass = "pass99";
        confirmNewPass = "pass99";
        boolean isMatch = editProfile.isPasswordMatch(newPass,confirmNewPass);
        assertTrue(isMatch);
    }
}