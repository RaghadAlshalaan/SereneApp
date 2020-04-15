package com.ksu.serene.controller.signup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({ FirebaseFirestore.class, FirebaseAuth.class})
public class SignupTest {

    FirebaseAuth mockFirebaseAuth;
    private Signup signup;
    private String name,email,pass,confirmPass;
    private ArgumentCaptor<OnCompleteListener> onComplete;
    @Mock FirebaseUser mockFirebaseUser;
    @Mock Task<AuthResult> mockAuthResultTask;
    @Mock AuthResult mockAuthResult;

    @Before
    public void setUp() throws Exception {
        mockFirebaseAuth = Mockito.mock(FirebaseAuth.class);
        PowerMockito.mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(mockFirebaseAuth);
        FirebaseFirestore mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
        signup = new Signup();//activityTestRule.getActivity();
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

    @Test public void testCreateUserWithNameEmailAndPassword() {
        when(mockFirebaseUser.getDisplayName()).thenReturn("foo");
        when(mockFirebaseUser.getEmail()).thenReturn("foo@Hotmail.com");

        mockSuccessfulAuthResult();

        when(mockFirebaseAuth.createUserWithEmailAndPassword("foo@Hotmail.com", "password")).thenReturn(
                mockAuthResultTask);

        assertNotNull(signup.createUserAccount("foo@hotmail.com", "password", "password", "foo",mockFirebaseAuth));

        //TestObserver<FirebaseUser> obs = TestObserver.create();

        /*RxFirebaseAuth.createUserWithEmailAndPassword(mockFirebaseAuth, "foo@bar.com", "password")
                .subscribe(obs);*/

        /*callOnComplete(mockAuthResultTask);
        obs.dispose();*/

        // Ensure no more values are emitted after unsubscribe
        //callOnComplete(mockAuthResultTask);

        /*obs.assertNoErrors();
        obs.assertComplete();*/

       /* obs.assertValue(new Predicate<FirebaseUser>() {
            @Override public boolean test(FirebaseUser firebaseUser) throws Exception {
                return "foo@bar.com".equals(firebaseUser.getEmail());
            }
        });*/
    }

    private void mockSuccessfulAuthResult() {
        when(mockAuthResult.getUser()).thenReturn(mockFirebaseUser);
        mockSuccessfulResultForTask(mockAuthResultTask, mockAuthResult);
    }

    private <T> void mockSuccessfulResultForTask(Task<T> task, T result) {
        when(task.getResult()).thenReturn(result);
        mockSuccessfulResultForTask(task);
    }

    private void mockSuccessfulResultForTask(Task task) {
        when(task.isSuccessful()).thenReturn(true);
        //noinspection unchecked
        when(task.addOnCompleteListener(onComplete.capture())).thenReturn(task);
    }
}