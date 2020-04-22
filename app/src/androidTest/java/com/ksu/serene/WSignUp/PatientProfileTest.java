package com.ksu.serene.WSignUp;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.profile.PatientProfile;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)

public class PatientProfileTest {

    @Rule
    public ActivityTestRule<PatientProfile> activityTestRule = new ActivityTestRule<PatientProfile>(PatientProfile.class);
    private PatientProfile patientProfile = null;

    @Before
    public void setUp() throws Exception {
        patientProfile = activityTestRule.getActivity();
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the activity is visible
            onView(withId(R.id.PatientProfile)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    //check the alert dialog appear when email not verified
    @Test
    public void AlertENVSuccess () {
        //check the alert dialog
        onView(withId(R.id.alert)).check(matches(isDisplayed()));
        //check the message
        onView(withId(R.id.alarmMsg)).check(matches(withText(R.string.VervEmail)));
        //click dialog to sent email verfication
        onView(withId(R.id.alert)).perform(click());
        // check toast visibility
        onView(withText(R.string.VervEmailSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.VervEmailSuccess)));
    }

    @Test
    public void LogOutOK () {
        //click the log out
        onView(withId(R.id.log_out_btn)).perform(click());
        //check the dialog appear
        onView(withText(R.string.LogOutMsg))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click the ok button
        onView(withText(R.string.LogOutOK)).perform(click());
        // check toast visibility
        onView(withText(R.string.LogOutSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.LogOutSuccess)));
        //check the welcome activity appear
        onView(withId(R.id.WelcomePage)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        patientProfile = null;
    }
}