package com.ksu.serene.XSignUp;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.MainActivity;
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
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class DHomeFragmentFitBitTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        //check activity visible
        onView(withId(R.id.Home)).check(matches(isDisplayed()));
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(3000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(3000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(3000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check will redirect to socio qestioneres
            onView(withId(R.id.FitbitConnection)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void NextButtonStConnect () {
        //first connect to fit bit
        onView(withId(R.id.connectFitbit)).perform(click());
        //Enter The fit bit account info sign in
        //check the status as default
        onView(withId(R.id.status)).check(matches(withText(R.string.status_connect)));
        //click the next button
        onView(withId(R.id.nextBtn)).perform(click());
        //check what happen
        onView(withId(R.id.MainActivity)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {

    }
}