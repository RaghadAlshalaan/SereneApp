package com.ksu.serene.A.Report;

import static org.junit.Assert.*;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static org.junit.Assert.*;
import android.widget.DatePicker;
import android.widget.TimePicker;
import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.calendar.Add_Medicine_Page;
import org.hamcrest.Matchers;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)

public class Add_Medicine_PageFailerTest {
    @Rule
    public ActivityTestRule<Add_Medicine_Page> activityTestRule = new ActivityTestRule<Add_Medicine_Page>(Add_Medicine_Page.class);
    private Add_Medicine_Page addMedicinePage = null;
    Calendar current = Calendar.getInstance();
    private Date AT = current.getTime();
    private SimpleDateFormat TimeFormat = new SimpleDateFormat ("HH : mm",Locale.UK);
    private String time = TimeFormat.format(AT);
    int tomorrow = current.get(Calendar.DAY_OF_MONTH)+1;
    int currentMonth = current.get(Calendar.MONTH);
    int minutes = current.get(Calendar.MINUTE)+5;
    int hours = current.get(Calendar.HOUR)-1;

    @Before
    public void setUp() throws Exception {
        addMedicinePage = activityTestRule.getActivity();
        //add timer for all tests
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check activity is visible
            onView(ViewMatchers.withId(R.id.Add_Medicine_Page)).check(matches(isDisplayed()));
            //check edit texts visible
            //name
            onView(withId(R.id.nameET)).check(matches(isDisplayed()));
            //dose
            onView(withId(R.id.MedicineDose)).check(matches(isDisplayed()));
            //interval
            onView(withId(R.id.repeatInterval)).check(matches(isDisplayed()));
            //times
            onView(withId(R.id.repeatNO)).check(matches(isDisplayed()));
            //check the spinner is visible
            onView(withId(R.id.repeatType)).check(matches(isDisplayed()));
            //check buttons visible
            //date
            onView(withId(R.id.MFromDays)).check(matches(isDisplayed()));
            //time
            onView(withId(R.id.MTime)).check(matches(isDisplayed()));
            //confirm
            onView(withId(R.id.button)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void backButton () {
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backButton)).check(matches(isClickable()));
        //perform click on back button
        onView(withId(R.id.backButton)).perform(click());
        //check the activity  finish
        assertTrue(activityTestRule.getActivity().isFinishing());
    }

    //check when first date is the current date and time before or same as current time
    @Test
    public void CurrentDateTime () {
        //enter name of med
        onView(withId(R.id.nameET)).perform(typeText("First Med"));
        //close the keyboard
        closeSoftKeyboard();
        // check when click the date and time button
        onView(withId(R.id.MFromDays)).perform(click());
        //choose current date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), (currentMonth+1), current.get(Calendar.DAY_OF_MONTH)));
        //click ok dialog button
        onView(withText("OK")).perform(click());
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose past time
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(hours , current.get(Calendar.MINUTE)));
        //click ok button
        onView(withText("OK")).perform(click());
        //dose
        onView(withId(R.id.MedicineDose)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //notivication info
        //type the interval
        onView(withId(R.id.repeatInterval)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //first click the spinner
        onView(withId(R.id.repeatType)).perform(click());
        //click the position wanted
        onData(anything()).atPosition(1).perform(click());
        //check the selected option like what is expected
        onView(withId(R.id.repeatType)).check(matches(withSpinnerText(containsString("hour(s)"))));
        //then type the times
        onView(withId(R.id.repeatNO)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //click confirm button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.CurrentTime))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.CurrentTime)));
        //check the activity still displayed
        onView(withId(R.id.Add_Medicine_Page)).check(matches(isDisplayed()));
        //check the activity not finish
        assertFalse(activityTestRule.getActivity().isFinishing());
        assertNotNull(addMedicinePage);
    }

    @Test
    public void EmptyFields () {
        //leave all fields empty and Date and time and type buttons not clicked
        onView(withId(R.id.nameET)).perform(typeText(""));
        onView(withId(R.id.MFromDays)).check(matches(withText(R.string.SetDayApp)));
        onView(withId(R.id.MTime)).check(matches(withText(R.string.SetTimeApp)));
        onView(withId(R.id.MedicineDose)).perform(typeText(""));
        //interval
        onView(withId(R.id.repeatInterval)).perform(typeText(""));
        //time
        onView(withId(R.id.repeatNO)).perform(typeText(""));
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
        //check the activity still displayed
        onView(withId(R.id.Add_Medicine_Page)).check(matches(isDisplayed()));
        //check the activity not finish
        assertFalse(activityTestRule.getActivity().isFinishing());
        assertNotNull(addMedicinePage);
    }

    //check when the field filled and time button pressed not set time
    @Test
    public void TimeClickedCancle () {
        //med name
        onView(withId(R.id.nameET)).perform(typeText("First Med"));
        //close keyboard
        closeSoftKeyboard();
        //click date
        onView(withId(R.id.MFromDays)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), (currentMonth+1), current.get(Calendar.DAY_OF_MONTH)));
        //click ok dialog button
        onView(withText("OK")).perform(click());
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose time
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime((current.get(Calendar.HOUR)) , current.get(Calendar.MINUTE)));
        //click calncle button
        onView(withText("CANCEL")).perform(click());
        //check the text not changed
        onView(withId(R.id.MTime)).check(matches(withText("Set Time")));
        //dose
        onView(withId(R.id.MedicineDose)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //notivecation info
        //type the interval
        onView(withId(R.id.repeatInterval)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //first click the spinner
        onView(withId(R.id.repeatType)).perform(click());
        //click the position wanted
        onData(anything()).atPosition(1).perform(click());
        //check the selected option like what is expected
        onView(withId(R.id.repeatType)).check(matches(withSpinnerText(containsString("hour(s)"))));
        //then type the times
        onView(withId(R.id.repeatNO)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //click confirm button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
        //check the activity still displayed
        onView(withId(R.id.Add_Medicine_Page)).check(matches(isDisplayed()));
        //check the activity not finish
        assertFalse(activityTestRule.getActivity().isFinishing());
        assertNotNull(addMedicinePage);
    }

    //check when the field filled and date button pressed with set date
    @Test
    public void TimeClickedOK () {
        //med name
        onView(withId(R.id.nameET)).perform(typeText("First Med"));
        //close keyboard
        closeSoftKeyboard();
        //check that text of date not changed
        onView(withId(R.id.MFromDays)).check(matches(withText(R.string.SetDayApp)));
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose time
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime((current.get(Calendar.HOUR)) , current.get(Calendar.MINUTE)));
        //click ok button
        onView(withText("OK")).perform(click());
        //dose
        onView(withId(R.id.MedicineDose)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //notivecation info
        //type the interval
        onView(withId(R.id.repeatInterval)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //first click the spinner
        onView(withId(R.id.repeatType)).perform(click());
        //click the position wanted
        onData(anything()).atPosition(1).perform(click());
        //check the selected option like what is expected
        onView(withId(R.id.repeatType)).check(matches(withSpinnerText(containsString("hour(s)"))));
        //then type the times
        onView(withId(R.id.repeatNO)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //click confirm button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
        //check the activity still displayed
        onView(withId(R.id.Add_Medicine_Page)).check(matches(isDisplayed()));
        //check the activity not finish
        assertFalse(activityTestRule.getActivity().isFinishing());
        assertNotNull(addMedicinePage);
    }
    //check when the field filled and first date button pressed not set date
    @Test
    public void FirstDateClickedCancle () {
        //med name
        onView(withId(R.id.nameET)).perform(typeText("First Med"));
        //close keyboard
        closeSoftKeyboard();
        // check when click the date button
        onView(withId(R.id.MFromDays)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), (currentMonth+1), current.get(Calendar.DAY_OF_MONTH)));
        //check when date bicker dialog cancel button clicked
        onView(withText("CANCEL")).perform(click());
        //check that text not changed for first, last date and time
        onView(withId(R.id.MFromDays)).check(matches(withText(R.string.SetDayApp)));
        //click time button
        onView(withId(R.id.MTime)).perform(click());
        //choose tiem
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime((current.get(Calendar.HOUR)-1) , current.get(Calendar.MINUTE)));
        //click ok button
        onView(withText("OK")).perform(click());
        //dose
        onView(withId(R.id.MedicineDose)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //notivecation info
        //type the interval
        onView(withId(R.id.repeatInterval)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //first click the spinner
        onView(withId(R.id.repeatType)).perform(click());
        //click the position wanted
        onData(anything()).atPosition(1).perform(click());
        //check the selected option like what is expected
        onView(withId(R.id.repeatType)).check(matches(withSpinnerText(containsString("hour(s)"))));
        //then type the times
        onView(withId(R.id.repeatNO)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //click confirm button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
        //check the activity still displayed
        onView(withId(R.id.Add_Medicine_Page)).check(matches(isDisplayed()));
        //check the activity not finish
        assertFalse(activityTestRule.getActivity().isFinishing());
        assertNotNull(addMedicinePage);
    }

    //check when the field filled and date button pressed with set date
    @Test
    public void FirstDateClickedOK () {
        //med name
        onView(withId(R.id.nameET)).perform(typeText("First Med"));
        //close keyboard
        closeSoftKeyboard();
        // check when click the date button
        onView(withId(R.id.MFromDays)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), (currentMonth+1), current.get(Calendar.DAY_OF_MONTH)));
        //click ok dialog button
        onView(withText("OK")).perform(click());
        //check the time button text not changed
        onView(withId(R.id.MTime)).check(matches(withText("Set Time")));
        //dose
        onView(withId(R.id.MedicineDose)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //notivecation info
        //type the interval
        onView(withId(R.id.repeatInterval)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //first click the spinner
        onView(withId(R.id.repeatType)).perform(click());
        //click the position wanted
        onData(anything()).atPosition(1).perform(click());
        //check the selected option like what is expected
        onView(withId(R.id.repeatType)).check(matches(withSpinnerText(containsString("hour(s)"))));
        //then type the times
        onView(withId(R.id.repeatNO)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //click confirm button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
        //check the activity still displayed
        onView(withId(R.id.Add_Medicine_Page)).check(matches(isDisplayed()));
        //check the activity not finish
        assertFalse(activityTestRule.getActivity().isFinishing());
        assertNotNull(addMedicinePage);
    }

    //Empty fields
    @Test
    public void ReminderInfoEmpty () {
        //med name
        onView(withId(R.id.nameET)).perform(typeText("First Med"));
        //close keyboard
        closeSoftKeyboard();
        // check when click the date button
        onView(withId(R.id.MFromDays)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), (currentMonth+1), current.get(Calendar.DAY_OF_MONTH)));
        //click ok dialog button
        onView(withText("OK")).perform(click());
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose time
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(current.get(Calendar.HOUR) , current.get(Calendar.MINUTE)));
        //click ok button
        onView(withText("OK")).perform(click());
        //dose
        onView(withId(R.id.MedicineDose)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //notivecation info
        //leave type the interval empty
        onView(withId(R.id.repeatInterval)).perform(typeText(""));
        //first click the spinner
        onView(withId(R.id.repeatType)).perform(click());
        //click the position wanted
        onData(anything()).atPosition(1).perform(click());
        //check the selected option like what is expected
        onView(withId(R.id.repeatType)).check(matches(withSpinnerText(containsString("hour(s)"))));
        //leave the num of times empty
        onView(withId(R.id.repeatNO)).perform(typeText(""));
        //click confirm button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
        //check the activity still displayed
        onView(withId(R.id.Add_Medicine_Page)).check(matches(isDisplayed()));
        //check the activity not finish
        assertFalse(activityTestRule.getActivity().isFinishing());
        assertNotNull(addMedicinePage);
    }

    @After
    public void tearDown() throws Exception {
        addMedicinePage = null;
    }
}