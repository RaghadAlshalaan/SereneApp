package com.ksu.serene;

import android.content.Intent;

import com.ksu.serene.controller.main.profile.MyDoctor;

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
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MyDoctorTest {

    @Rule
    public ActivityTestRule<MyDoctor> activityTestRule = new ActivityTestRule<MyDoctor>(MyDoctor.class);
    private MyDoctor myDoctor = null;

    @Before
    public void setUp() throws Exception {
        //add timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            activityTestRule.launchActivity(new Intent());
            //check the activity visible
            onView(withId(R.id.MyDoctor)).check(matches(isDisplayed()));
            //check buttons visibility
            //delete Doctor button
            onView(withId(R.id.delete)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    //check when delete doctor clicked the dialog appears
    @Test
    public void DeleteDoctorCancel () {
        //click the delete out
        onView(withId(R.id.delete)).perform(click());
        //check the dialog appear
        onView(withText(R.string.DeleteMessageDoc))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click the cancel button
        onView(withText(R.string.DeleteCancleDoc)).perform(click());
        //nothing happened
    }

    //check when delete doctor clicked the dialog appears
    @Test
    public void DeleteDoctorOK () {
        //click the delete out
        onView(withId(R.id.delete)).perform(click());
        //check the dialog appear
        onView(withText(R.string.DeleteMessageDoc))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click the ok button
        onView(withText(R.string.DeleteOKDoc)).perform(click());
        // check toast visibility
        onView(withText(R.string.DocDeletedSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.DocDeletedSuccess)));
    }

    @After
    public void tearDown() throws Exception {
        myDoctor = null;
    }
}