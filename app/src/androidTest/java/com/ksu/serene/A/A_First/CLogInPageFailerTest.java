package com.ksu.serene.A.A_First;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.LogInPage;
import com.ksu.serene.R;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static org.hamcrest.Matchers.not;
import java.util.concurrent.TimeUnit;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)

public class CLogInPageFailerTest {

    @Rule
    public ActivityTestRule<LogInPage> activityTestRule = new ActivityTestRule<LogInPage>(LogInPage.class);
    private LogInPage logInPage = null;

    @Before
    public void setUp() throws Exception {
        logInPage = activityTestRule.getActivity();
        onView(ViewMatchers.withId(R.id.LogIn)).check(matches(isDisplayed()));
        //check the log in button is visible
        onView(withId(R.id.loginBtn)).check(matches(isDisplayed()));
        //check the sign up button is visible
        onView(withId(R.id.registerTV)).check(matches(isDisplayed()));
        //check the forget pass button is visible
        onView(withId(R.id.forgetPassword)).check(matches(isDisplayed()));
    }
    //TODO check when user registred but the sign up setting not complete

    @Test
    public void EmptyFields () {
        //leave all filed empty
        onView(withId(R.id.Email)).perform(typeText(""));
        onView(withId(R.id.Password)).perform(typeText(""));
        //check the button un-clickable
        onView(withId(R.id.loginBtn)).check(matches(not(isEnabled())));
    }

    @Test
    public void checkSignupButton () throws Throwable {
        //then click if sign up clicked
        onView(withId(R.id.registerTV)).perform(click());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(withId(R.id.signup_page)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void loginEmailNotFounded () {
        //enter unregistered email
        onView(withId(R.id.Email)).perform(typeText("userSerenePatient@hotmail.com"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.Password)).perform(typeText("pass00"));
        //close keyboard
        closeSoftKeyboard();
        //start click to button
        onView(withId(R.id.loginBtn)).perform(click());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //stop & verify & check the text view
            onView(withId(R.id.Error)).check(matches(withText(R.string.NoUser)));
            //check activity still showen
            onView(ViewMatchers.withId(R.id.LogIn)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void loginWrongPass () {
        //enter registered email
        onView(withId(R.id.Email)).perform(typeText("user@hotmail.com"));
        //close keyboard
        closeSoftKeyboard();
        //enter uncorrected password
        onView(withId(R.id.Password)).perform(typeText("serene00"));
        //close keyboard
        closeSoftKeyboard();
        //click button
        onView(withId(R.id.loginBtn)).perform(click());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //stop & verify & check the text view
            onView(withId(R.id.Error)).check(matches(withText(R.string.wrongPassword)));
            //check activity still showen
            onView(ViewMatchers.withId(R.id.LogIn)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @After
    public void tearDown() throws Exception {
        logInPage = null;
    }


}