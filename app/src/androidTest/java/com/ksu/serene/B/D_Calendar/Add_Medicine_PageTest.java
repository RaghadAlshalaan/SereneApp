package com.ksu.serene.B.D_Calendar;

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

public class Add_Medicine_PageTest {

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
    public void addMedSuccessToday () {
        try {
            AT = TimeFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //enter med name
        onView(withId(R.id.nameET)).perform(typeText("Test Med Reminder"));
        //close keyboard
        closeSoftKeyboard();
        // check when click the date and time button
        onView(withId(R.id.MFromDays)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), (currentMonth+1), current.get(Calendar.DAY_OF_MONTH)));
        //click ok dialog button
        onView(withText("OK")).perform(click());
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose tiem
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime((AT.getHours()+2) , (AT.getMinutes()+2) ));
        //click ok button
        onView(withText("OK")).perform(click());
        //dose
        onView(withId(R.id.MedicineDose)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //notivecation info
        //type the interval
        onView(withId(R.id.repeatInterval)).perform(typeText("1"));
        //close keyboard
        closeSoftKeyboard();
        //first click the spinner
        onView(withId(R.id.repeatType)).perform(click());
        //click the position wanted
        onData(anything()).atPosition(0).perform(click());
        //check the selected option like what is expected
        onView(withId(R.id.repeatType)).check(matches(withSpinnerText(containsString("minute(s)"))));
        //then type the times
        onView(withId(R.id.repeatNO)).perform(typeText("1"));
        //close keyboard
        closeSoftKeyboard();
            //click confirm button
            onView(withId(R.id.button)).perform(click());
            // check toast visibility
         onView(withText(R.string.MedSavedSuccess)).inRoot(new ToastMatcher()).check(matches(withText(R.string.MedSavedSuccess)));
        //check the activity  finish
        assertTrue(activityTestRule.getActivity().isFinishing());
    }

    @Test
    public void addMedSuccessFuture () {
        //enter med name
        onView(withId(R.id.nameET)).perform(typeText("Test Med Reminder"));
        //close keyboard
        closeSoftKeyboard();
        // check when click the date and time button
        onView(withId(R.id.MFromDays)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(current.get(Calendar.YEAR), (currentMonth+1), tomorrow ));
        //click ok dialog button
        onView(withText("OK")).perform(click());
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose tiem
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(current.get(Calendar.HOUR) , current.get(Calendar.MINUTE)));
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
        onView(withText(R.string.MedSavedSuccess))
                .inRoot(new ToastMatcher()).check(matches(withText(R.string.MedSavedSuccess)));
        //check the activity  finish
        assertTrue(activityTestRule.getActivity().isFinishing());
    }
    
    //Days
    @Test
    public void addMedSuccessTodayCurrentHour () {
        try {
            AT = TimeFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //enter med name
        onView(withId(R.id.nameET)).perform(typeText("Test Med Reminder"));
        //close keyboard
        closeSoftKeyboard();
        // check when click the date and time button
        onView(withId(R.id.MFromDays)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), (currentMonth+1), current.get(Calendar.DAY_OF_MONTH)));
        //click ok dialog button
        onView(withText("OK")).perform(click());
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose tiem
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime((AT.getHours()) , (AT.getMinutes()+2) ));
        //click ok button
        onView(withText("OK")).perform(click());
        //dose
        onView(withId(R.id.MedicineDose)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //notivecation info
        //type the interval
        onView(withId(R.id.repeatInterval)).perform(typeText("1"));
        //close keyboard
        closeSoftKeyboard();
        //first click the spinner
        onView(withId(R.id.repeatType)).perform(click());
        //click the position wanted
        onData(anything()).atPosition(2).perform(click());//days
        //check the selected option like what is expected
        onView(withId(R.id.repeatType)).check(matches(withSpinnerText(containsString("day(s)"))));
        //then type the times
        onView(withId(R.id.repeatNO)).perform(typeText("1"));
        //close keyboard
        closeSoftKeyboard();
        //click confirm button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.MedSavedSuccess)).inRoot(new ToastMatcher()).check(matches(withText(R.string.MedSavedSuccess)));
        //check the activity  finish
        assertTrue(activityTestRule.getActivity().isFinishing());
    }

    //Week
    @Test
    public void addMedSuccessFutureWeek () {
        //enter med name
        onView(withId(R.id.nameET)).perform(typeText("Test Med Reminder"));
        //close keyboard
        closeSoftKeyboard();
        // check when click the date and time button
        onView(withId(R.id.MFromDays)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), (currentMonth+1), tomorrow ));
        //click ok dialog button
        onView(withText("OK")).perform(click());
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose tiem
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(current.get(Calendar.HOUR) , current.get(Calendar.MINUTE)));
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
        onData(anything()).atPosition(3).perform(click());
        //check the selected option like what is expected
        onView(withId(R.id.repeatType)).check(matches(withSpinnerText(containsString("week(s)"))));
        //then type the times
        onView(withId(R.id.repeatNO)).perform(typeText("2"));
        //close keyboard
        closeSoftKeyboard();
        //click confirm button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.MedSavedSuccess)).inRoot(new ToastMatcher()).check(matches(withText(R.string.MedSavedSuccess)));
        //check the activity  finish
        assertTrue(activityTestRule.getActivity().isFinishing());
    }


        @After
    public void tearDown() throws Exception {
        addMedicinePage = null;
    }

}