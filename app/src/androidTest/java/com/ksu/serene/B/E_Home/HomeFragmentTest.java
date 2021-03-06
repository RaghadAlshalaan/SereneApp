package com.ksu.serene.B.E_Home;

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
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringContains.containsString;
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
    public void ProfilePage () {
        //check the profile icon button visible
        onView(allOf(withId(R.id.profile_icon), isDisplayed())).check(matches(isDisplayed()));
        //click the button
        onView(allOf(withId(R.id.profile_icon), isDisplayed())).perform(click());
        //check the profile page visible
        onView(withId(R.id.PatientProfile)).check(matches(isDisplayed()));
    }

    @Test
    public void checkReportNavBarNavig () {
        onView(withId(R.id.navigation_report)).perform(click());
        //check the report activity showen
        onView(withId(R.id.ReportF)).check(matches(isDisplayed()));

    }

    @Test
    public void checkDraftNavBarNavig () {
        onView(withId(R.id.navigation_drafts)).perform(click());
        //check the report activity showen
        onView(withId(R.id.drafts)).check(matches(isDisplayed()));

    }

    @Test
    public void checkCalendarNavBarNavig () {
        onView(withId(R.id.navigation_calendar)).perform(click());
        //check the report activity showen
        onView(withId(R.id.CalendarFragment)).check(matches(isDisplayed()));

    }

    @Test
    public void TestNexAppAM () {
        //check the appointment in time AM
        onView(withId(R.id.noUpcoming)).check(matches(withText(containsString("AM"))));//TODO change it to AM
        //click the card
        onView(withId(R.id.card3)).perform(click());
        //add timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the app details activity showen
            onView(withId(R.id.PatientAppointmentDetailPage)).check(matches(isDisplayed()));
            //delete this appointment
            onView(withId(R.id.DeleteApp)).perform(click());
            //click the delete button
            onView(withText(R.string.DeleteOKApp)).perform(click());
        }
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
    public void NotiAppDetails () {
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
            onView(withId(R.id.PatientAppointmentDetailPage)).check(matches(isDisplayed()));
            //check med info
            onView(withId(R.id.nameET)).check(matches(isDisplayed()));
            onView(withId(R.id.MTillDays)).check(matches(isDisplayed()));
            onView(withId(R.id.MTime)).check(matches(isDisplayed()));
            //check button visible
            onView(withId(R.id.DeleteApp)).check(matches(isDisplayed()));
        }
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @After
    public void tearDown() throws Exception {
    }
}