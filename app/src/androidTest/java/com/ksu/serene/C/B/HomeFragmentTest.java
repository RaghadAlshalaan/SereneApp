package com.ksu.serene.C.B;

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
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)

public class HomeFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check activity visible
            onView(withId(R.id.Home)).check(matches(isDisplayed()));
            //check the improvement text visible
            // onView(ViewMatchers.withId(R.id.improvement_num)).check(matches(isDisplayed()));
            //check upcoming appointment visible
            onView(withId(R.id.noUpcoming)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void NotifButton () {
        //check the notification icon button visible
        onView(allOf(withId(R.id.bell_icon), isDisplayed())).check(matches(isDisplayed()));
        //click the button
        onView(allOf(withId(R.id.bell_icon), isDisplayed())).perform(click());
        //check the name, and date, msg showne
        onView(allOf(withId(R.id.reminder_name), isDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.reminder_message), isDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.reminder_time), isDisplayed())).check(matches(isDisplayed()));
        //TODO click outside notification to disappear
        onView(allOf(withId(R.id.outside_notification_box), isDisplayed())).perform(click());
    }

    @Test
    public void NotiMedDetails () {
        //check the notification icon button visible
        onView(allOf(withId(R.id.bell_icon), isDisplayed())).check(matches(isDisplayed()));
        //click the button
        onView(allOf(withId(R.id.bell_icon), isDisplayed())).perform(click());
        //check the name, and date, msg showne
        onView(allOf(withId(R.id.reminder_name), isDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.reminder_message), isDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.reminder_time), isDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.notification_card), isDisplayed())).perform(click());
        //add timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the med page appear
            onView(withId(R.id.PatientMedicineDetailPage)).check(matches(isDisplayed()));
            //check med info
            onView(withId(R.id.nameET)).check(matches(isDisplayed()));
            onView(withId(R.id.MFromDays)).check(matches(isDisplayed()));
            onView(withId(R.id.MTillDays)).check(matches(isDisplayed()));
            onView(withId(R.id.MTime)).check(matches(isDisplayed()));
            onView(withId(R.id.MDose)).check(matches(isDisplayed()));
            //check button visible
            onView(withId(R.id.DeleteMedicine)).check(matches(isDisplayed()));
        }
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @After
    public void tearDown() throws Exception {
    }
}