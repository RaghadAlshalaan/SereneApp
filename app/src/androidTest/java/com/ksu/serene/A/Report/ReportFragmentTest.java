package com.ksu.serene.A.Report;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//import java.util.Calendar;
import java.util.concurrent.TimeUnit;

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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)


public class ReportFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        onView(withId(R.id.navigation_report)).perform(click());
        //check for buttons visible
        onView(withId(R.id.ReportF)).check(matches(isDisplayed()));
        //radio button
        onView(withId(R.id.radioButton1)).check(matches(isDisplayed()));
        onView(withId(R.id.radioButton2)).check(matches(isDisplayed()));
        onView(withId(R.id.radioButton3)).check(matches(isDisplayed()));
        //generate button
        onView(withId(R.id.generate_report_btn)).check(matches(isDisplayed()));
    }

    @Test
    public void lastTwoWeekSelected() {
        //click on last two option
        onView(withId(R.id.radioButton1)).perform(click());
        //check that the option selected
        onView(withId(R.id.radioButton1)).check(matches(isChecked()));
        //click the generate report button
        onView(withId(R.id.generate_report_btn)).perform(click());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the patient page appears button appears
            onView(withId(R.id.PatientReport)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void lastMonthSelected() {
        //click on last month option
        onView(withId(R.id.radioButton2)).perform(click());
        //check that the option selected
        onView(withId(R.id.radioButton2)).check(matches(isChecked()));
        //click the generate report button
        onView(withId(R.id.generate_report_btn)).perform(click());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the patient page appears button appears
            onView(withId(R.id.PatientReport)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    // check for all condition if specific date
    @Test
    public void SpecificPeriodEndClickBStart() {
        //click on specific option
        onView(withId(R.id.radioButton3)).perform(click());
        //check that the option selected
        onView(withId(R.id.radioButton3)).check(matches(isChecked()));
        //check the dates button appears
        onView(withId(R.id.start)).check(matches(isDisplayed()));
        onView(withId(R.id.end)).check(matches(isDisplayed()));
        //check the text of buttons
        onView(withId(R.id.start)).check(matches(withText(R.string.set_start)));
        onView(withId(R.id.end)).check(matches(withText(R.string.set_end)));
        //click the end button
        onView(withId(R.id.end)).perform(click());
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the generate report still showen
            onView(withId(R.id.ReportF)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }


}