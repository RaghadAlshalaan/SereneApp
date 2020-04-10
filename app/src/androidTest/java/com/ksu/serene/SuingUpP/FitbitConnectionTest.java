package com.ksu.serene.SuingUpP;

import android.content.Intent;

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
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class FitbitConnectionTest {

    @Rule
    public ActivityTestRule<FitbitConnection> activityTestRule = new ActivityTestRule<FitbitConnection>(FitbitConnection.class);

    @Before
    public void setUp() throws Exception {
        activityTestRule.launchActivity(new Intent());
        //add timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check activiy visible
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
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void BackButton () {
        //click the back button
        onView(withId(R.id.backBtn)).perform(click());
        //check what happen
        //check the GAD activity appears
        onView(withId(R.id.fragmentGad)).check(matches(isDisplayed()));
    }

    @Test
    public void NextButtonStAsDef () {
        //check the status as default
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
        //click the next button
        onView(withId(R.id.nextBtn)).perform(click());
        //check what happen
    }

    @Test
    public void NextButtonStConnect () {
        //first connect to fit bit
        onView(withId(R.id.connectFitbit)).perform(click());
        //check the status as default
        onView(withId(R.id.status)).check(matches(withText("Status : Connected!")));
        //click the next button
        onView(withId(R.id.nextBtn)).perform(click());
        //check what happen
    }

    @After
    public void tearDown() throws Exception {
    }
}