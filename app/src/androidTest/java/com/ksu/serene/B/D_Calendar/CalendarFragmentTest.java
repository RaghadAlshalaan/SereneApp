package com.ksu.serene.B.D_Calendar;

import android.widget.CalendarView;

import static com.ksu.serene.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.*;
import com.fangxu.allangleexpandablebutton.ButtonData;
import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;
import com.ksu.serene.controller.main.calendar.CalendarFragment;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
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
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
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
    private CalendarFragment calendarFragment = new CalendarFragment ();

    @Before
    public void setUp() throws Exception {
        onView(withId(R.id.navigation_calendar)).perform(click());
        //add tiemr
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try { IdlingRegistry.getInstance().register(idlingResource);
            //check the activity is visible
            onView(withId(R.id.CalendarFragment)).check(matches(isDisplayed()));
            //check the button visible
            onView(allOf(withId(R.id.button_expandable_110_250))).check(matches(isDisplayed()));
            onView(withClassName(Matchers.equalTo(CalendarView.class.getName()))).check(matches(isDisplayed()));
        }//clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

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

    // check for App recyclers view
    @Test
    public void testItemClickAppRecycler() {
        onView(withRecyclerView(R.id.RecyclerviewSession).atPosition(0)).perform(click());
        //add tiemr
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //activity
            onView(withId(R.id.PatientAppointmentDetailPage)).check(matches(isDisplayed()));
            //check for app name
            onView(withId(R.id.nameET)).check(matches(isDisplayed()));
            //check for app date
            onView(withId(R.id.MTillDays)).check(matches(isDisplayed()));
            //check for app time
            onView(withId(R.id.MTime)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    // check for Med recyclers view
    @Test
    public void testItemClickMedRecycler() {
        onView(withId(R.id.RecyclerViewMedicine))
                .perform(ViewActions.scrollTo())
                .check(ViewAssertions.matches(isDisplayed()));
        onView(withRecyclerView(R.id.RecyclerViewMedicine).atPosition(0)).perform(click());
        //add tiemr
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //activity
            onView(withId(R.id.PatientMedicineDetailPage)).check(matches(isDisplayed()));
            //check for med name
            onView(withId(R.id.nameET)).check(matches(isDisplayed()));
            //check for med date
            onView(withId(R.id.MFromDays)).check(matches(isDisplayed()));
            onView(withId(R.id.MTillDays)).check(matches(isDisplayed()));
            //check for med time
            onView(withId(R.id.MTime)).check(matches(isDisplayed()));
            //check dose med
            onView(withId(R.id.MDose)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    //test event recycler
   // @Test
    public void testItemClickEventRecycler() {
        onView(withId(R.id.RecyclerViewEvents))
                .perform(ViewActions.scrollTo())
                .check(ViewAssertions.matches(isDisplayed()));
        //add tiemr
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check for event name
            onView(withId(R.id.text_view_appointment)).check(matches(isDisplayed()));
            //check for event date
            onView(withId(R.id.text_view_start_time)).check(matches(isDisplayed()));
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