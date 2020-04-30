package com.ksu.serene.A.Profile;

import static org.junit.Assert.*;
import android.view.View;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.controller.main.profile.Editprofile;

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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)

public class ChangeLanagauge {

    @Rule
    public ActivityTestRule<Editprofile> activityTestRule = new ActivityTestRule<Editprofile>(Editprofile.class);
    private Editprofile editprofile = null;

    @Before
    public void setUp() throws Exception {
        editprofile = activityTestRule.getActivity();
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //activityTestRule.launchActivity(new Intent());
            //check activity visible
            onView(withId(R.id.Editprofile)).check(matches(isDisplayed()));
            //check button visible
            onView(withId(R.id.English)).check(matches(isDisplayed()));
            onView(withId(R.id.Arabic)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void changeToAr () {
        //click to arabic
        onView(withId(R.id.Arabic)).perform(click());
        //check what happend
        //the ar button will be invisible
        assertEquals(editprofile.findViewById(R.id.Arabic).getVisibility(), View.INVISIBLE);
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
    }

    @Test
    public void changeToEng () {
        //click to arabic
        onView(withId(R.id.English)).perform(click());
        //check what happend
        //the ar button will be invisible
        assertEquals(editprofile.findViewById(R.id.English).getVisibility(), View.INVISIBLE);
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
    }


    @Test
    public void DeleteAccountCancel () {
        //check the display name as expected
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        //click the delete button
        onView(withId(R.id.delete)).perform(click());
        //this line is to check when button clicked this dialog will apear
        onView(withText(R.string.DeleteMessageAcc))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click cancel button
        onView(withText(R.string.DeleteCancleAcc)).perform(click());
        //nothing will be happen
        //check the activity still showen
        onView(withId(R.id.Editprofile)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        editprofile = null;
    }


}