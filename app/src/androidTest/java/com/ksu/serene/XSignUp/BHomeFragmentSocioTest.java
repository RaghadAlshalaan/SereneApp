package com.ksu.serene.XSignUp;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;

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
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class BHomeFragmentSocioTest {

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
            onView(withId(R.id.socio)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void socioPage () {
        onView(withId(R.id.socio)).check(matches(isDisplayed()));
    }

    @Test
    public void socioTestSuccess () {
        //enter valid age
        onView(withId(R.id.a1)).perform(typeText("30"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter valid height
        onView(withId(R.id.a2)).perform(typeText("140"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter valid weight
        onView(withId(R.id.a3)).perform(typeText("60"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.a4)).perform(typeText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.a8)).perform(typeText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //click button
        onView(withId(R.id.nextBtn)).perform(click());
        // check toast visibility
        onView(withText(R.string.SocioSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.SocioSuccess)));
        onView(withId(R.id.fragmentGad)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {

    }

}