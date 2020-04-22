package com.ksu.serene.AFirst;

import static org.junit.Assert.*;
import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.LogInPage;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.concurrent.TimeUnit;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)

public class ForgetPasswordTest {

    @Rule
    public ActivityTestRule<LogInPage> activityTestRule = new ActivityTestRule<LogInPage>(LogInPage.class);
    private LogInPage logInPage = null;

    @Before
    public void setUp() throws Exception {
        logInPage = activityTestRule.getActivity();
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(withId(R.id.LogIn)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void checkForgetPassButton () throws Exception {
        //then check if reset password click the dialog will apear?
        onView(withId(R.id.forgetPassword)).perform(click());
        onView(withText(R.string.ForgetPassMessage))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkForgetPassButtonCancel () {
        onView(withId(R.id.forgetPassword)).perform(click());
        //then check if reset password click the dialog will apear?
        onView(withText(R.string.ForgetPassMessage))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //check that the negative button appear && clicked
        onView(withText(R.string.ForgetPassCancle)).perform(click());
        // nothing happened
        onView(withId(R.id.LogIn)).check(matches(isDisplayed()));
    }

    @Test
    public void checkForgetPassButtonOKEmpty () {
        onView(withId(R.id.forgetPassword)).perform(click());
        //then check if reset password click the dialog will apear?
        onView(withText(R.string.ForgetPassMessage))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //check when email field empty
        onView(withId(1)).perform(typeText(""));
        //check that the positive button appear && clicked
        onView(withText(R.string.ForgetPassOK)).perform(click());
        // check toast visibility with specific text
        onView(withText(R.string.EmptyEmail))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyEmail)));
        //check the dialog still appears
        onView(withText(R.string.ForgetPassMessage))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkForgetPassButtonOKWF () {
        onView(withId(R.id.forgetPassword)).perform(click());
        //then check if reset password click the dialog will apear?
        onView(withText(R.string.ForgetPassMessage))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //check when email field empty
        onView(withId(1)).perform(typeText("123.com"));
        closeSoftKeyboard();
        //check that the positive button appear && clicked
        onView(withText(R.string.ForgetPassOK)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotCorrectEmail))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotCorrectEmail)));
        //check the dialog still appears
        onView(withText(R.string.ForgetPassMessage))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
    }

    @Test
    public void checkForgetPassButtonOKSuccess () {
        onView(withId(R.id.forgetPassword)).perform(click());
        //then check if reset password click the dialog will apear?
        onView(withText(R.string.ForgetPassMessage))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //check when email exist
        onView(withId(1)).perform(typeText("user@hotmail.com"));
        closeSoftKeyboard();
        //check that the positive button appear && clicked
        onView(withText(R.string.ForgetPassOK)).perform(click());
        // check toast visibility
        onView(withText(R.string.ForgetPassSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.ForgetPassSuccess)));
        //going to log in
        onView(withId(R.id.LogIn)).check(matches(isDisplayed()));
    }

    @Test
    public void checkForgetPassButtonOKFail () {
        onView(withId(R.id.forgetPassword)).perform(click());
        //then check if reset password click the dialog will apear?
        onView(withText(R.string.ForgetPassMessage))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //check when email not exist
        onView(withId(1)).perform(typeText("userLamaPatient@hotmail.com"));
        closeSoftKeyboard();
        //check that the positive button appear && clicked
        onView(withText(R.string.ForgetPassOK)).perform(click());
        // check toast visibility
        onView(withText(R.string.ForgetPassFialed))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.ForgetPassFialed)));
        //going to log in
        onView(withId(R.id.LogIn)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        logInPage = null;
    }

}