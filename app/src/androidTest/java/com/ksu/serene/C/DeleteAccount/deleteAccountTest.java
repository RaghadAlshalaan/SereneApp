package com.ksu.serene.C.DeleteAccount;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
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
public class deleteAccountTest {
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
            onView(withId(R.id.delete)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void DeleteAccountOK () {
        //check the display name as expected
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        //click the delete button
        onView(withId(R.id.delete)).perform(click());
        //this line is to check when button clicked this dialog will apear
        onView(withText(R.string.DeleteMessageAcc))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click ok button
        onView(withText(R.string.DeleteOKAcc)).perform(click());
        //check toast
        // check toast visibility
        onView(withText(R.string.AccDeletedSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.AccDeletedSuccess)));
    }

    @After
    public void tearDown() throws Exception {
        editprofile = null;
    }
}