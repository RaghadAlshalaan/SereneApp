package com.ksu.serene;

import android.content.Intent;

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
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class LogInPageSuccessTest {

    @Rule
    public ActivityTestRule<LogInPage> activityTestRule = new ActivityTestRule<LogInPage>(LogInPage.class);
    private LogInPage logInPage = null;

    @Before
    public void setUp() throws Exception {
        //logInPage = activityTestRule.getActivity();
        activityTestRule.launchActivity(new Intent());
        onView(withId(R.id.LogIn)).check(matches(isDisplayed()));

        //check the log in button is visible
        onView(withId(R.id.loginBtn)).check(matches(isDisplayed()));
        //check the sign up button is visible
        onView(withId(R.id.registerTV)).check(matches(isDisplayed()));
        //check the forget pass button is visible
        onView(withId(R.id.forgetPassword)).check(matches(isDisplayed()));
    }

    @Test
    public void loginSuccess () {
        //enter registered email
        onView(withId(R.id.Email)).perform(typeText("patientSerene@hotmail.com"));
        //close keyboard
        closeSoftKeyboard();
        //enter correct password
        onView(withId(R.id.Password)).perform(typeText("serene00"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.loginBtn)).perform(click());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource1 = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource1);
            onView(withId(R.id.MainActivity)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource1);
        }
    }

    @After
    public void tearDown() throws Exception {
        logInPage = null;
    }
}