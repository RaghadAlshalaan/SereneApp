package com.ksu.serene;

import com.ksu.serene.controller.signup.Signup;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class SignupSuccessTest {

    @Rule
    public ActivityTestRule<Signup> activityTestRule = new ActivityTestRule<Signup>(Signup.class);
    private Signup signup = null;

    @Before
    public void setUp() throws Exception {
        signup = activityTestRule.getActivity();

        //check activity visible
        onView(withId(R.id.signup_page)).check(matches(isDisplayed()));
        //check edit texts visible
        //name
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        //email
        onView(withId(R.id.emailInput)).check(matches(isDisplayed()));
        //pass
        onView(withId(R.id.passwordInput)).check(matches(isDisplayed()));
        //re pass
        onView(withId(R.id.CpasswordInput)).check(matches(isDisplayed()));

        //check buttons visible
        //log in button
        onView(withId(R.id.loginBtn)).check(matches(isDisplayed()));
        //sign up button
        onView(withId(R.id.signupBtn)).check(matches(isDisplayed()));
    }


    @Test
    public void signupSuccess () {
        //enter valid name
        onView(withId(R.id.username)).perform(typeText("user"));
        //close keyboard
        closeSoftKeyboard();
        //enter valid email
        onView(withId(R.id.emailInput)).perform(typeText("user@hotmail.com"));
        //close keyboard
        closeSoftKeyboard();
        //enter valid password
        onView(withId(R.id.passwordInput)).perform(typeText("password00"));
        //close keyboard
        closeSoftKeyboard();
        //renter valid password
        onView(withId(R.id.CpasswordInput)).perform(typeText("password00"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.signupBtn)).perform(click());
        // check toast visibility
        onView(withText(R.string.SignUpSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.SignUpSuccess)));
        // check what activity display is it the parent Ques or the specific fragment which is socio
        onView(withId(R.id.socio)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        signup = null;
    }
}