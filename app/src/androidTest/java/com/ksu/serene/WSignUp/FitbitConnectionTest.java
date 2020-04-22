package com.ksu.serene.WSignUp;

import com.ksu.serene.R;
import com.ksu.serene.controller.signup.FitbitConnection;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)

public class FitbitConnectionTest {

    @Rule
    public ActivityTestRule<FitbitConnection> activityTestRule = new ActivityTestRule<FitbitConnection>(FitbitConnection.class);
    private FitbitConnection fitbitConnection = null;

    @Before
    public void setUp() throws Exception {
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
    public void backButton () {
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backButton)).check(matches(isClickable()));
        //perform click on back button
        onView(withId(R.id.backButton)).perform(click());
        //check the activity is finish
        assertTrue(activityTestRule.getActivity().isFinishing());
        //check the gad will display
        //onView(withId(R.id.fragmentGad)).check(matches(isDisplayed()));
    }

    @Test
    public void NextButtonStAsDef () {
        //check the status as default
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
        //click the next button
        onView(withId(R.id.nextBtn)).perform(click());
        //check what happen
        //check the activity still display
        assertFalse(activityTestRule.getActivity().isFinishing());
        onView(withId(R.id.FitbitConnection)).check(matches(isDisplayed()));
    }

    @Test
    public void NextButtonStNotConnect () {
        //first connect to fit bit
        onView(withId(R.id.connectFitbit)).perform(click());
        //Don't Enter Any thing just when the Web site open close it
        //check the status as default
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
        //click the next button
        onView(withId(R.id.nextBtn)).perform(click());
        //check what happen
        //check the activity still display
        assertFalse(activityTestRule.getActivity().isFinishing());
        onView(withId(R.id.FitbitConnection)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        fitbitConnection = null;
    }



}