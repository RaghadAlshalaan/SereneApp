package com.ksu.serene;

import com.fangxu.allangleexpandablebutton.ButtonData;
import com.ksu.serene.controller.main.calendar.CalendarFragment;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class CalendarFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);


    @Before
    public void setUp() throws Exception {
        activityTestRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //set fragment
                Fragment CalendarF = new CalendarFragment();
                FragmentTransaction fragmentTransaction = activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.replace(R.id.Home, CalendarF);
                fragmentTransaction.add(R.id.MainActivity, CalendarF);
                fragmentTransaction.commit();

            }
        });

        getInstrumentation().waitForIdleSync();
        //add tiemr
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the activity is visible
            onView(withId(R.id.CalendarFragment)).check(matches(isDisplayed()));
            //check the calendar view visible
            //onView(allOf(withId(R.id.calendarView2))).check(matches(isDisplayed()));
            //onData(withId(R.id.calendarView2)).inAdapterView(withId(R.id.RootCalendar)).atPosition(1).check(matches(isEnabled()));
            //check the recyclers view visible
            //onView(withId(R.id.RecyclerviewSession)).check(matches(isDisplayed()));
            //onView(allOf(withId(R.id.RecyclerViewMedicine))).check(matches(isDisplayed()));
            //check the button visible
            onView(allOf(withId(R.id.button_expandable_110_250))).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    //TODO test click on date from calender then click the med recycler view at 1
    //TODO test click on date from calender then click the app recycler view at 1

    @Test
    public void TestAddButton () {
        //click the button
        onView(allOf(withId(R.id.button_expandable_110_250))).perform(click());
        //check the two buttons will appears
        //add med
        onView(withId(R.id.AddMedButton)).check(matches(isDisplayed()));
        //addApp
        onView(withId(R.id.AddAppButton)).check(matches(isDisplayed()));
    }

    @Test
    public void AddMed () {
        //click the button
        onView(allOf(withId(R.id.button_expandable_110_250))).perform(click());
        //click add med button
        onView(allOf(withId(R.id.AddMedButton))).perform(click());
        //check the add med activity appears
        onView(withId(R.id.Add_Medicine_Page)).check(matches(isDisplayed()));
    }

    @Test
    public void AddApp () {
        //click the button
        onView(allOf(withId(R.id.button_expandable_110_250))).perform(click());
        //click add app button
        onView(allOf(withId(R.id.AddAppButton))).perform(click());
        //check the add app activity appears
        onView(withId(R.id.Add_Appointment_Page)).check(matches(isDisplayed()));
    }

    //TODO check for recyclers view

    @After
    public void tearDown() throws Exception {
    }
}