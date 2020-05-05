package com.ksu.serene.B.B_First;

import static org.junit.Assert.*;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.R;
import com.ksu.serene.controller.signup.Questionnairs;

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

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)


public class DSociodemoSuccessTest {

    @Rule
    public ActivityTestRule<Questionnairs> activityTestRule = new ActivityTestRule<Questionnairs>(Questionnairs.class);

    @Before
    public void setUp() throws Exception {
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        //check the activity visible
        onView(withId(R.id.socio)).check(matches(isDisplayed()));
        onView(withId(R.id.a1)).check(matches(isDisplayed()));//age
        onView(withId(R.id.a2)).check(matches(isDisplayed()));//height
        onView(withId(R.id.a3)).check(matches(isDisplayed()));//weight
        onView(withId(R.id.a4)).check(matches(isDisplayed()));//monthly income
        onView(withId(R.id.a8)).check(matches(isDisplayed()));//chronic disease
        onView(withId(R.id.a9)).check(matches(isDisplayed()));//gender
        onView(withId(R.id.a5)).check(matches(isDisplayed()));//martial
        onView(withId(R.id.a6)).check(matches(isDisplayed()));//employment
        onView(withId(R.id.a7)).check(matches(isDisplayed()));//smoking
        //check the next button visible
        onView(withId(R.id.nextBtn)).check(matches(isDisplayed()));
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
        closeSoftKeyboard();
        //GENDER
        onView(withId(R.id.a9)).perform(click());
        //female
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.a9)).check(matches(withSpinnerText(containsString("Female"))));
        //martial
        onView(withId(R.id.a5)).perform(click());
        //single
        onData(anything()).atPosition(0).perform(click());
        onView(withId(R.id.a5)).check(matches(withSpinnerText(containsString("Single"))));
        //employment
        onView(withId(R.id.a6)).perform(click());
        //student
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.a6)).check(matches(withSpinnerText(containsString("Student"))));
        //smoking
        onView(withId(R.id.a7)).perform(click());
        //no
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.a7)).check(matches(withSpinnerText(containsString("No"))));
        //close keyboard
        closeSoftKeyboard();
        //click button
        onView(withId(R.id.nextBtn)).perform(click());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(withId(R.id.fragmentGad)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @After
    public void tearDown() throws Exception {

    }

}