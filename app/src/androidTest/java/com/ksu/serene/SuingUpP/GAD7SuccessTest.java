package com.ksu.serene.SuingUpP;

import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.signup.GAD7;
import com.ksu.serene.controller.signup.Questionnairs;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class GAD7SuccessTest {

    @Rule
    public ActivityTestRule<Questionnairs> activityTestRule = new ActivityTestRule<Questionnairs>(Questionnairs.class);

    @Before
    public void setUp() throws Exception {
        FragmentTransaction fragmentTransaction = activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.Questionnairs, new GAD7());
        fragmentTransaction.commit();
        getInstrumentation().waitForIdleSync();
    }


    @Test
    public void gadTestSuccess () {
        //all radio buttons groups checked
        onView(withId(R.id.radioButton11)).perform(click());
        onView(withId(R.id.radioButton11)).check(matches(isChecked()));//radio 1
        onView(withId(R.id.radioButton12)).check(matches(isNotChecked()));//radio 2
        onView(withId(R.id.radioButton13)).check(matches(isNotChecked()));//radio 3
        onView(withId(R.id.radioButton14)).check(matches(isNotChecked()));//radio 4
        //Group2
        onView(withId(R.id.radioButton21)).perform(click());
        onView(withId(R.id.radioButton21)).check(matches(isChecked()));//radio 1
        onView(withId(R.id.radioButton22)).check(matches(isNotChecked()));//radio 2
        onView(withId(R.id.radioButton23)).check(matches(isNotChecked()));//radio 3
        onView(withId(R.id.radioButton24)).check(matches(isNotChecked()));//radio 4
        //Group3
        onView(withId(R.id.radioButton31)).perform(click());//radio 1
        onView(withId(R.id.radioButton31)).check(matches(isChecked()));//radio 1
        onView(withId(R.id.radioButton32)).check(matches(isNotChecked()));//radio 2
        onView(withId(R.id.radioButton33)).check(matches(isNotChecked()));//radio 3
        onView(withId(R.id.radioButton34)).check(matches(isNotChecked()));//radio 4
        //Group4
        onView(withId(R.id.radioButton41)).perform(click());//radio 1
        onView(withId(R.id.radioButton41)).check(matches(isChecked()));//radio 1
        onView(withId(R.id.radioButton42)).check(matches(isNotChecked()));//radio 2
        onView(withId(R.id.radioButton43)).check(matches(isNotChecked()));//radio 3
        onView(withId(R.id.radioButton44)).check(matches(isNotChecked()));//radio 4
        //Group5
        onView(withId(R.id.radioButton51)).perform(click());//radio 1
        onView(withId(R.id.radioButton51)).check(matches(isChecked()));//radio 1
        onView(withId(R.id.radioButton52)).check(matches(isNotChecked()));//radio 2
        onView(withId(R.id.radioButton53)).check(matches(isNotChecked()));//radio 3
        onView(withId(R.id.radioButton54)).check(matches(isNotChecked()));//radio 4
        //Group6
        onView(withId(R.id.radioButton61)).perform(click());//radio 1
        onView(withId(R.id.radioButton61)).check(matches(isChecked()));//radio 1
        onView(withId(R.id.radioButton62)).check(matches(isNotChecked()));//radio 2
        onView(withId(R.id.radioButton63)).check(matches(isNotChecked()));//radio 3
        onView(withId(R.id.radioButton64)).check(matches(isNotChecked()));//radio 4
        //Group7
        onView(withId(R.id.radioButton71)).perform(click());//radio 1
        onView(withId(R.id.radioButton71)).check(matches(isChecked()));//radio 1
        onView(withId(R.id.radioButton72)).check(matches(isNotChecked()));//radio 2
        onView(withId(R.id.radioButton73)).check(matches(isNotChecked()));//radio 3
        onView(withId(R.id.radioButton74)).check(matches(isNotChecked()));//radio 4
        //click button
        onView(allOf(withId(R.id.nextBtn), isDisplayed())).perform(click());
        // check toast visibility
        onView(withText(R.string.GADSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.GADSuccess)));
        onView(withId(R.id.FitbitConnection)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
    }
}