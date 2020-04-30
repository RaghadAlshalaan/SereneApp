package com.ksu.serene.B.B_First;

import static org.junit.Assert.*;
import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.controller.signup.FitbitConnection;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)

public class FitbitConnectionSuccessTest {
    @Rule
    public ActivityTestRule<FitbitConnection> activityTestRule = new ActivityTestRule<FitbitConnection>(FitbitConnection.class);
    private FitbitConnection fitbitConnection = null;
    UiDevice uiDevice;

    @Before
    public void setUp() throws Exception {
        uiDevice =UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        fitbitConnection = activityTestRule.getActivity();
        onView(withId(R.id.FitbitConnection)).check(matches(isDisplayed()));
        //check buttons visible
        //connect button
        onView(withId(R.id.connectFitbit)).check(matches(isDisplayed()));
        //back button
        onView(withId(R.id.backBtn)).check(matches(isDisplayed()));
        //next buttob
        onView(withId(R.id.nextBtn)).check(matches(isDisplayed()));
        //check the status at default not connected
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
    }

    @Test
    public void NextButtonStConnect ()  {
        //first connect to fit bit
        onView(withId(R.id.connectFitbit)).perform(click());
        //Enter The fit bit account info sign in
        UiObject mAcc = uiDevice.findObject(new UiSelector().text("JUST ONCE"));
        try {
            mAcc.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        //check the status as default
        onView(withId(R.id.status)).check(matches(withText(R.string.status_connect)));
        //click the next button
        onView(withId(R.id.nextBtn)).perform(click());
        //add timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check what happen
            onView(withId(R.id.GoogleCalendarConnection)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @After
    public void tearDown() throws Exception {
        fitbitConnection = null;
    }

}