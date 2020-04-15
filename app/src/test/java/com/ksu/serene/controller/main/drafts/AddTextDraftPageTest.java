package com.ksu.serene.controller.main.drafts;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.model.TextDraft;

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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseFirestore.class})
public class AddTextDraftPageTest {
    FirebaseFirestore mockFirebaseFirestore;
    private AddTextDraftPage textDraft;
    String title, msg;
    @Mock Task<Void> mockVoidTask;
    private ArgumentCaptor<OnCompleteListener> onComplete;


    @Before
    public void setUp() throws Exception {
        mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
        textDraft = new AddTextDraftPage();//activityTestRule.getActivity();
        onComplete = ArgumentCaptor.forClass(OnCompleteListener.class);
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
        title = "New Draft";
        msg = "This Test For Check that Field not Empty";
        boolean isFillFields = textDraft.CheckFields(title,msg);
        assertTrue(isFillFields);
    }

    @Test(expected = NullPointerException.class)
    public void generateIDNull () {
        assertNull(textDraft.getDraftID(title));
    }

    @Test
    public void generateID () {
        title = "Title";
        assertNotNull(textDraft.getDraftID(title));
    }

    @Test
    public void SaveTextDraft() {
        //TestObserver obs = TestObserver.create();
        mockSuccessfulResultForTask(mockVoidTask);
        title = "Title";
        msg = "This Test For Check that Field not Empty";
        String patientID = "123458965lkjhjfjk";
        String id = textDraft.getDraftID(title);
        Map<String, Object> draft = new HashMap<>();
        draft.put("text", msg);
        //draft.put("timestamp", FieldValue.serverTimestamp());
        draft.put("title", title);
        draft.put("patinetID", patientID);
        when(mockFirebaseFirestore.getInstance()
                .collection("TextDraft")
                .document("123")
                .set(draft))
                .thenReturn(mockVoidTask);
        //TextDraft textDraftSaved = textDraft.SaveTextDraft(title,msg,patientID);
        //assertNotNull(textDraftSaved);
        //assertTrue(title.equals(textDraftSaved.getTitle()));
        //assertTrue(msg.equals(textDraftSaved.getMessage()));
    }

    private void mockSuccessfulResultForTask(Task task) {
        when(task.isSuccessful()).thenReturn(true);
        //noinspection unchecked
        when(task.addOnCompleteListener(onComplete.capture())).thenReturn(task);
    }

}