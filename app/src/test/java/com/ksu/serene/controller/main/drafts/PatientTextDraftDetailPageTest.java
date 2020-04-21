package com.ksu.serene.controller.main.drafts;

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
public class PatientTextDraftDetailPageTest {
    private PatientTextDraftDetailPage textDraft;
    String title, msg;

    //TODO test retrieve data from DB
    //TODO test update data in DB
    //TODO test delete data in DB

    @Before
    public void setUp() throws Exception {
        FirebaseFirestore mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
        textDraft = new PatientTextDraftDetailPage();//activityTestRule.getActivity();
    }

    @Test
    public void EmptyFields () {
        title = "";
        msg = "";
        boolean isEmptyFields = textDraft.CheckFields(title,msg);
        assertFalse(isEmptyFields);
    }

    @Test
    public void MsgEmptyField () {
        title = "Title";
        msg = "";
        boolean isMsgEmpty = textDraft.CheckFields(title, msg);
        assertFalse(isMsgEmpty);
    }

    @Test
    public void TitleEmptyFiled() {
        title = "";
        msg = "Msg";
        boolean isTitleEmpty = textDraft.CheckFields(title,msg);
        assertFalse(isTitleEmpty);
    }

    @Test
    public void FillFields () {
        String title = "New Draft";
        String msg = "This Test For Check that Field not Empty";
        boolean isFillFields = textDraft.CheckFields(title,msg);
        assertTrue(isFillFields);
    }

}