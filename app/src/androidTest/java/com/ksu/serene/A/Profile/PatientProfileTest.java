package com.ksu.serene.A.Profile;

import static org.junit.Assert.*;
import android.content.Intent;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.profile.PatientProfile;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.ksu.serene.ImageViewMatcher.hasDrawable;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)

public class PatientProfileTest {
    @Rule
    public ActivityTestRule<PatientProfile> activityTestRule = new ActivityTestRule<PatientProfile>(PatientProfile.class);
    private PatientProfile patientProfile = null;

    @Before
    public void setUp() throws Exception {
        patientProfile = activityTestRule.getActivity();
        //launch activity
        //activityTestRule.launchActivity(new Intent());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the activity is visible
            onView(withId(R.id.PatientProfile)).check(matches(isDisplayed()));
            //check the buttons visible
            //edit profile button
            onView(withId(R.id.edit_profile_btn)).check(matches(isDisplayed()));
            //edit socio button
            onView(withId(R.id.go_to1)).check(matches(isDisplayed()));
            //add doctor button
            onView(withId(R.id.go_to2)).check(matches(isDisplayed()));
            //log out button
            onView(withId(R.id.log_out_btn)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    //check setting the name and email
    @Test
    public void CheckSetting () {
        //check the name
        onView(withId(R.id.full_name)).check(matches(isDisplayed()));
        //check the image
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView)).check(matches(hasDrawable()));
    }

    @Test
    public void backButton () {
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backButton)).check(matches(isClickable()));
        //perform click on back button
        onView(withId(R.id.backButton)).perform(click());
        //check the home page appear
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the main activity showen
            onView(withId(R.id.MainActivity)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
        //assertTrue(activityTestRule.getActivity().isFinishing());
    }

    //check when click the edit profile will go to edit profile page
    @Test
    public void EditProfile () {
        //click the button
        onView(withId(R.id.edit_profile_btn)).perform(click());
        //check the edit profile page apear
        onView(withId(R.id.Editprofile)).check(matches(isDisplayed()));
    }

    //check when click the edit socio will go to edit profile page
    @Test
    public void EditSocio () {
        //click the button
        onView(withId(R.id.go_to1)).perform(click());
        //check the edit socio page appear
        onView(withId(R.id.EditSocio)).check(matches(isDisplayed()));
    }

    //check when click the doctor and user have doctor go to doctor page details
    @Test
    public void ViewDoctor () {
        //check the doctor name will viewd
        //onView(withId(R.id.doctor_text2)).check(matches(withText("")));
        onView(withId(R.id.doctor_text2)).check(matches(isDisplayed()));
        //click the button
        onView(withId(R.id.go_to2)).perform(click());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the doc info page apear
            onView(withId(R.id.MyDoctor)).check(matches(isDisplayed()));
            onView(withId(R.id.nameET)).check(matches(withText("Ahmed")));
            onView(withId(R.id.emailET)).check(matches(withText("lama449@gmail.com")));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    //check when click to log out the Dialog appear
    @Test
    public void LogOutCancel () {
        //click the log out
        onView(withId(R.id.log_out_btn)).perform(click());
        //check the dialog appear
        onView(withText(R.string.LogOutMsg))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click the cancel button
        onView(withText(R.string.LogOutCancle)).perform(click());
        //nothing happened
        assertFalse(activityTestRule.getActivity().isFinishing());
        onView(withId(R.id.PatientProfile)).check(matches(isDisplayed()));
    }

    @Test
    public void googleCalendarConnectionP () {
        //check calendar disonncect
        onView(withId(R.id.connection_status)).check(matches(withText(R.string.disabled)));
        //click connect to calendar
        onView(withId(R.id.go_to3)).perform(click());
        //check the connect page display
        onView(withId(R.id.GoogleCalendarConnection)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        patientProfile = null;
    }
}