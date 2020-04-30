package com.ksu.serene.A.A_First;

import android.view.View;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.WelcomePage;

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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)

public class AWelcomePageTest {

    @Rule
    public ActivityTestRule<WelcomePage> activityTestRule = new ActivityTestRule<WelcomePage>(WelcomePage.class);
    WelcomePage welcomePage = null;

    @Before
    public void setUp() throws Exception {
        welcomePage = activityTestRule.getActivity();

        //check the welcom view is visible
        onView(ViewMatchers.withId(R.id.WelcomePage)).check(matches(isDisplayed()));
        //check the log in button is visible
        onView(withId(R.id.login)).check(matches(isDisplayed()));
        //check the sign up button is visible
        onView(withId(R.id.register)).check(matches(isDisplayed()));
        //lang buttons
        onView(withId(R.id.Arabic)).check(matches(isDisplayed()));
        onView(withId(R.id.English)).check(matches(isDisplayed()));

    }

    @Test
    public void checkLoginButton () throws Exception {
        //click button
        onView(withId(R.id.login)).perform(click());
        //check if log in page appear
        onView(withId(R.id.LogIn)).check(matches(isDisplayed()));
    }

    @Test
    public void checkSignupButton () throws Exception {
        //check that when sign up button click will go to sign up page
        onView(withId(R.id.register)).perform(click());
        //check if sing up page appear
        onView(withId(R.id.signup_page)).check(matches(isDisplayed()));
    }

    @Test
    public void changeToAr () {
        //click to arabic
        onView(withId(R.id.Arabic)).perform(click());
    }

    @Test
    public void changeToEng () {
        //click to arabic
        onView(withId(R.id.English)).perform(click());
    }

    @After
    public void tearDown() throws Exception {
        welcomePage = null;
    }


}