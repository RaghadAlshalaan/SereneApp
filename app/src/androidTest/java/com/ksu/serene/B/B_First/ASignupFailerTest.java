package com.ksu.serene.B.B_First;

import static org.junit.Assert.*;
import com.ksu.serene.R;
import com.ksu.serene.controller.signup.Signup;
import com.ksu.serene.ElapsedTimeIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import java.util.concurrent.TimeUnit;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.not;

import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)

public class ASignupFailerTest {
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
    public void loginButton () {
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.LogIn)).check(matches(isDisplayed()));
    }

    @Test
    public void EmptyFiedls () {
        //leave all field empty
        //enter unvalid name
        onView(withId(R.id.username)).perform(typeText(""));
        //enter new email
        onView(withId(R.id.emailInput)).perform(typeText(""));
        //enter valid password
        onView(withId(R.id.passwordInput)).perform(typeText(""));
        //reenter valid password
        onView(withId(R.id.CpasswordInput)).perform(typeText(""));
        //check the button unable
        onView(withId(R.id.signupBtn)).check(matches(not(isEnabled())));
    }

    @Test
    public void notValidName () {
        //entr unvalid name
        onView(withId(R.id.username)).perform(typeText("998"));
        //close keyboard
        closeSoftKeyboard();
        //enter new email
        onView(withId(R.id.emailInput)).perform(typeText("userSerene@hotmail.com"));
        //close keyboard
        closeSoftKeyboard();
        //enter valid password
        onView(withId(R.id.passwordInput)).perform(typeText("password00"));
        //close keyboard
        closeSoftKeyboard();
        //reenter valid password
        onView(withId(R.id.CpasswordInput)).perform(typeText("password00"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.signupBtn)).perform(click());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //stop & verify & check the text view
            onView(withId(R.id.Error)).check(matches(withText(R.string.NotValidName)));
            //check the activity still showen
            assertFalse(activityTestRule.getActivity().isFinishing());
            onView(withId(R.id.signup_page)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void passNotMatch () {
        //enter valid name
        onView(withId(R.id.username)).perform(typeText("user"));
        //close keyboard
        closeSoftKeyboard();
        //enter valid email
        onView(withId(R.id.emailInput)).perform(typeText("userSerene@hotmail.com"));
        //close keyboard
        closeSoftKeyboard();
        //ener valid password
        onView(withId(R.id.passwordInput)).perform(typeText("password00"));
        //close keyboard
        closeSoftKeyboard();
        //enter valid password not equal to the above password
        onView(withId(R.id.CpasswordInput)).perform(typeText("password99"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.signupBtn)).perform(click());

        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //stop & verify & check the text view
            onView(withId(R.id.Error)).check(matches(withText(R.string.NotMatchPass)));
            //check the activity still showen
            assertFalse(activityTestRule.getActivity().isFinishing());
            onView(withId(R.id.signup_page)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void emailExist () {
        //enter valid name
        onView(withId(R.id.username)).perform(typeText("user"));
        //close keyboard
        closeSoftKeyboard();
        //enter registered email
        onView(withId(R.id.emailInput)).perform(typeText("lama449@gmail.com"));
        //close keyboard
        closeSoftKeyboard();
        //enter valid password
        onView(withId(R.id.passwordInput)).perform(typeText("password00"));
        //close keyboard
        closeSoftKeyboard();
        //renter valid password same to the above
        onView(withId(R.id.CpasswordInput)).perform(typeText("password00"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.signupBtn)).perform(click());

        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(withId(R.id.Error)).check(matches(withText(R.string.ExistUser)));
            //check the activity still showen
            assertFalse(activityTestRule.getActivity().isFinishing());
            onView(withId(R.id.signup_page)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void passShort () {
        //enter valid name
        onView(withId(R.id.username)).perform(typeText("user"));
        //close keyboard
        closeSoftKeyboard();
        //enter valid email
        onView(withId(R.id.emailInput)).perform(typeText("lama@hotmail.com"));
        //close keyboard
        closeSoftKeyboard();
        //enter short password
        onView(withId(R.id.passwordInput)).perform(typeText("pa00"));
        //close keyboard
        closeSoftKeyboard();
        //reenter short password
        onView(withId(R.id.CpasswordInput)).perform(typeText("pa00"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.signupBtn)).perform(click());

        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(withId(R.id.Error)).check(matches(withText(R.string.PasswordShort)));
            //check the activity still showen
            assertFalse(activityTestRule.getActivity().isFinishing());
            onView(withId(R.id.signup_page)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void emailNotCorrect () {
        //enter valid name
        onView(withId(R.id.username)).perform(typeText("user"));
        //close keyboard
        closeSoftKeyboard();
        //enter unvalid email
        onView(withId(R.id.emailInput)).perform(typeText("user@.com"));
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

        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(withId(R.id.Error)).check(matches(withText(R.string.NotValidEmail)));
            //check the activity still showen
            assertFalse(activityTestRule.getActivity().isFinishing());
            onView(withId(R.id.signup_page)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @After
    public void tearDown() throws Exception {
        signup = null;
    }

}