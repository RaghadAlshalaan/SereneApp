package com.ksu.serene;

import android.content.Intent;

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

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class EditprofileTestV2 {

    @Rule
    public ActivityTestRule<Editprofile> activityTestRule = new ActivityTestRule<Editprofile>(Editprofile.class);
    private Editprofile editprofile = null;

    @Before
    public void setUp() throws Throwable {
        //editprofile = activityTestRule.getActivity();
        //add timer so each toast for each test
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            activityTestRule.launchActivity(new Intent());
            onView(withId(R.id.Editprofile)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void passUpdateSuccess () {
        //check the display name as expected
        //onView(withId(R.id.username)).check(matches(withText("user")));
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        //enter correct past password
        onView(withId(R.id.oldPassword)).perform(typeText("password99"));
        //close keyboard
        closeSoftKeyboard();
        //enter new pass
        onView(withId(R.id.newPassword)).perform(typeText("serene00"));
        //close keyboard
        closeSoftKeyboard();
        //renter new pass
        onView(withId(R.id.reNewPassword)).perform(typeText("serene00"));
        //close keyboard
        closeSoftKeyboard();
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //click the button
            onView(withId(R.id.save)).perform(click());
            // check toast visibility
            onView(withText(R.string.passwordUpdate))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.passwordUpdate)));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void updateProfileSuccess () {
        //check the display name as expected
        //onView(withId(R.id.username)).check(matches(withText("user")));
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        //change name to new valid name
        onView(withId(R.id.username)).perform(replaceText("userSerene"));
        //enter correct past password
        onView(withId(R.id.oldPassword)).perform(typeText("serene00"));
        //close keyboard
        closeSoftKeyboard();
        //enter new pass
        onView(withId(R.id.newPassword)).perform(typeText("passw99"));
        //close keyboard
        closeSoftKeyboard();
        //renter new pass
        onView(withId(R.id.reNewPassword)).perform(typeText("passw99"));
        //close keyboard
        closeSoftKeyboard();
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //click the button
            onView(withId(R.id.save)).perform(click());
            // check toast visibility
            onView(withText(R.string.ProfileInfoUpdateSuccess))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.ProfileInfoUpdateSuccess)));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }


    @After
    public void tearDown() throws Exception {
        editprofile = null;
    }
}