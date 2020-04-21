package com.ksu.serene.controller.main.profile;

import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseFirestore.class, FirebaseAuth.class, FirebaseStorage.class, FirebaseUser.class, TextUtils.class})//
public class EditprofileTest {
    private Editprofile editProfile;
    String name,oldPass,newPass,confirmNewPass;
    FirebaseAuth mockFirebaseAuth;
    FirebaseUser mockFirebaseUser;

    //TODO test retrieve name form DB
    //TODO test update Token in DB
    //TODO test chang password DB && Auth
    //TODO test change image

    @Before
    public void setUp() throws Exception {
        //make mock obj for firestore
        FirebaseFirestore mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
        //make mock obj for Auth
        mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockFirebaseAuth);
        // make mock obj for storage
        FirebaseStorage mockFirebaseStorage = Mockito.mock(FirebaseStorage.class);
        PowerMockito.mockStatic(FirebaseStorage.class);
        when(FirebaseStorage.getInstance()).thenReturn(mockFirebaseStorage);
        //make mock obj for firebase user
        mockFirebaseUser = Mockito.mock(FirebaseUser.class);
        PowerMockito.mockStatic(FirebaseUser.class);
        when(FirebaseAuth.getInstance().getCurrentUser()).thenReturn(mockFirebaseUser);
        //mock for TextUtil used in UserProfile
        TextUtils mockTextUtil = Mockito.mock(TextUtils.class);
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.when(TextUtils.isEmpty(any(CharSequence.class))).thenReturn(true);
        //.isEmpty(any(String.class))).thenReturn(mockTextUtil );

        editProfile = new Editprofile();//activityTestRule.getActivity();
        editProfile.setmAuth(mockFirebaseAuth);
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

    @Test
    public void ResetPassAuthTest () {
        String email = "user@hotmail.com";
        //call reset Password from mock firebase
        Task taskMock = Mockito.mock(Task.class);
        when(mockFirebaseAuth.sendPasswordResetEmail(email)).thenReturn(taskMock);
        //here call the reset password method
        editProfile.resetPassword(email);
        //
        verify(mockFirebaseAuth, times(1)).sendPasswordResetEmail(email);
        verify(taskMock, times(1)).addOnCompleteListener(any(OnCompleteListener.class));
    }

    @Test
    public void deleteAccount () {
        //call delete account from mock firebase
        Task taskMock = Mockito.mock(Task.class);
        when(mockFirebaseUser.delete()).thenReturn(taskMock);
        //here call the delete account method
        editProfile.deleteAccount();
        //
        verify(mockFirebaseUser, times(1)).delete();
        verify(taskMock, times(1)).addOnCompleteListener(any(OnCompleteListener.class));
    }

    //TODO test update name in DB
    @Test
    public void updateName () {
        name = "newName";
        //call delete account from mock firebase
        Task taskMock = Mockito.mock(Task.class);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
        when(mockFirebaseUser.updateProfile(profileUpdates)).thenReturn(taskMock);
        //here call the delete account method
        editProfile.changeNameFirebase(name,profileUpdates);
        //
        verify(mockFirebaseUser, times(1)).updateProfile(profileUpdates);
        verify(taskMock, times(1)).addOnCompleteListener(any(OnCompleteListener.class));
    }
}