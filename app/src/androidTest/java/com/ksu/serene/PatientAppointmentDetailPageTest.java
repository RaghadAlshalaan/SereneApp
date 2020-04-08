package com.ksu.serene;

import android.content.Intent;

import com.ksu.serene.controller.main.calendar.PatientAppointmentDetailPage;

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
@RunWith(AndroidJUnit4.class)
public class PatientAppointmentDetailPageTest {

    @Rule
    public ActivityTestRule<PatientAppointmentDetailPage> activityTestRule = new ActivityTestRule<PatientAppointmentDetailPage>(PatientAppointmentDetailPage.class);
    private PatientAppointmentDetailPage appointmentDetailPage = null;

    @Before
    public void setUp() throws Exception {
        //add timer for all tests
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            activityTestRule.launchActivity(new Intent());
            //appointmentDetailPage = activityTestRule.getActivity();
            onView(withId(R.id.PatientAppointmentDetailPage)).check(matches(isDisplayed()));
            //check button visible
            onView(withId(R.id.DeleteApp)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void DeleteCancle () {
        //TODO update APP info
        //check for app name
        //onView(withId(R.id.nameET)).check(matches(withText("First App")));
        onView(withId(R.id.nameET)).check(matches(isDisplayed()));
        //check for app date
        //onView(withId(R.id.MTillDays)).check(matches(withText("23/11/2021")));
        onView(withId(R.id.MTillDays)).check(matches(isDisplayed()));
        //check for app time
        //onView(withId(R.id.MTime)).check(matches(withText("14 : 03")));
        onView(withId(R.id.MTime)).check(matches(isDisplayed()));
        //check when click button
        onView(withId(R.id.DeleteApp)).perform(click());
        //check the dialog appear
        onView(withText(R.string.DeleteMessageApp))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click the cancel button
        onView(withText(R.string.DeleteCancleApp)).perform(click());
        //nothing happened
    }

    @Test
    public void DeleteOK () {
        //check for app name
        onView(withId(R.id.nameET)).check(matches(isDisplayed()));
        //check for app date
        onView(withId(R.id.MTillDays)).check(matches(isDisplayed()));
        //check for app time
        onView(withId(R.id.MTime)).check(matches(isDisplayed()));
        //check when click button
        onView(withId(R.id.DeleteApp)).perform(click());
        //check the dialog appear
        onView(withText(R.string.DeleteMessageApp))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click the delete button
        onView(withText(R.string.DeleteOKApp)).perform(click());
            // check toast visibility
            onView(withText(R.string.AppDeletedSuccess))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.AppDeletedSuccess)));
    }

    @After
    public void tearDown() throws Exception {
        appointmentDetailPage = null;
    }
}