package com.ksu.serene.B.C_Profile;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.profile.PatientProfile;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class VerficationEmailTest {
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
            //elert msg
            onView(withId(R.id.alert)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void verficationEmail () {
        //click
        onView(withId(R.id.alert)).perform(click());
        //check the taost
        // check toast visibility
        onView(withText(R.string.VervEmailSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.VervEmailSuccess)));
    }
}