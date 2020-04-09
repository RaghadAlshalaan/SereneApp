package com.ksu.serene.SuingUpP;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.controller.signup.Signup;

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
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class SignupTest {

    @Rule
    public ActivityTestRule<Signup> activityTestRule = new ActivityTestRule<Signup>(Signup.class);
    private Signup signup = null;

    @Before
    public void setUp() throws Exception {
        signup = activityTestRule.getActivity();

        //check activity visible
        onView(ViewMatchers.withId(R.id.signup_page)).check(matches(isDisplayed()));
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

    //TODO check when all edit texts fields empty the sign up button unclickable
    @Test
    public void EmptyFiedls () {
        //leave all field empty
        //entr unvalid name
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
    public void loginButton () {
        onView(withId(R.id.loginBtn)).perform(click());
        onView(withId(R.id.LogIn)).check(matches(isDisplayed()));
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
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);

            //stop & verify & check the text view
            onView(withId(R.id.Error)).check(matches(withText(R.string.NotValidName)));
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
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);

            //stop & verify & check the text view
            onView(withId(R.id.Error)).check(matches(withText(R.string.NotMatchPass)));
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
        onView(withId(R.id.emailInput)).perform(typeText("lama-almarshad@hotmail.com"));
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
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(withId(R.id.Error)).check(matches(withText(R.string.ExistUser)));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    //TODO the short pass is <6 should be changed in sign up && log in to consist with reset pass from firebase and also in edit profile
    @Test
    public void passShort () {
        //enter valid name
        onView(withId(R.id.username)).perform(typeText("user"));
        //close keyboard
        closeSoftKeyboard();
        //enter valid email
        onView(withId(R.id.emailInput)).perform(typeText("user@hotmail.com"));
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
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(withId(R.id.Error)).check(matches(withText(R.string.PasswordShort)));
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
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(withId(R.id.Error)).check(matches(withText(R.string.NotValidEmail)));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

   /* @Test
    public void signupFail () {
        //no internet connection the reason not defined
        onView(withId(R.id.username)).perform(typeText("user"));
        closeSoftKeyboard();
        onView(withId(R.id.emailInput)).perform(typeText("user@hotmail.com"));
        closeSoftKeyboard();
        onView(withId(R.id.passwordInput)).perform(typeText("password00"));
        closeSoftKeyboard();
        onView(withId(R.id.CpasswordInput)).perform(typeText("password00"));
        closeSoftKeyboard();
        onView(withId(R.id.signupBtn)).perform(click());
        onView(withId(R.id.Error)).check(matches(withText("* Sign in Failed, Please try again")));
    }*/


    @After
    public void tearDown() throws Exception {
        signup = null;
    }

}