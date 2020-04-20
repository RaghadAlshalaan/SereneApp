package com.ksu.serene.ProfileF;

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

import java.util.concurrent.TimeUnit;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class LogOutTest {

    @Rule
    public ActivityTestRule<PatientProfile> activityTestRule = new ActivityTestRule<PatientProfile>(PatientProfile.class);

    @Before
    public void setUp() throws Exception {
        //launch activity
        activityTestRule.launchActivity(new Intent());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            /*FragmentTransaction fragmentTransaction = activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.nav_host_fragment, new PatientProfile());
            fragmentTransaction.commit();
            getInstrumentation().waitForIdleSync();*/
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
    }

   /*@Test
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
    }*/

    @After
    public void tearDown() throws Exception {
    }
}