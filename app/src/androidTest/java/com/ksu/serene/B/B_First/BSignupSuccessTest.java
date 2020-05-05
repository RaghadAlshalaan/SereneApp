package com.ksu.serene.B.B_First;

import static org.junit.Assert.*;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.signup.Signup;

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
import static androidx.test.espresso.matcher.ViewMatchers.withText;
@RunWith(AndroidJUnit4.class)

public class BSignupSuccessTest {
    @Rule
    public ActivityTestRule<Signup> activityTestRule = new ActivityTestRule<Signup>(Signup.class);
    private Signup signup = null;

    @Before
    public void setUp() throws Exception {
        signup = activityTestRule.getActivity();
        //check activity visible
        onView(withId(R.id.signup_page)).check(matches(isDisplayed()));
    }

    @Test
    public void SignUpSuccess () {
        //enter valid name
        onView(withId(R.id.username)).perform(typeText("userSerene"));
        //close keyboard
        closeSoftKeyboard();
        //enter valid email
        onView(withId(R.id.emailInput)).perform(typeText("sereneapp0@gmail.com"));
        //close keyboard
        closeSoftKeyboard();
        //enter short password
        onView(withId(R.id.passwordInput)).perform(typeText("sereneuser"));
        //close keyboard
        closeSoftKeyboard();
        //reenter short password
        onView(withId(R.id.CpasswordInput)).perform(typeText("sereneuser"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.signupBtn)).perform(click());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check socio activity display
            onView(withId(R.id.socio)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }


}