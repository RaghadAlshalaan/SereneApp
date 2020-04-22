package com.ksu.serene.XSignUp;

import android.Manifest;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.LogInPage;
import com.ksu.serene.PermissionGranter;
import com.ksu.serene.R;

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

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)

public class ALogInPageTest {

    @Rule
    public ActivityTestRule<LogInPage> activityTestRule = new ActivityTestRule<LogInPage>(LogInPage.class);
    private LogInPage logInPage = null;

    @Before
    public void setUp() throws Exception {
        logInPage = activityTestRule.getActivity();
        //activityTestRule.launchActivity(new Intent());
        onView(ViewMatchers.withId(R.id.LogIn)).check(matches(isDisplayed()));
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
        onView(withId(R.id.Email)).perform(typeText("lama449@gmail.com"));
        //close keyboard
        closeSoftKeyboard();
        //enter correct password
        onView(withId(R.id.Password)).perform(typeText("serene00"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.loginBtn)).perform(click());
            //grantedd permission
            PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.ACCESS_COARSE_LOCATION);
            PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.ACCESS_FINE_LOCATION);
            onView(withId(R.id.MainActivity)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        logInPage = null;
    }
}