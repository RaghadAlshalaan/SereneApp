//test the date and time button from this source
//https://stackoverflow.com/questions/43149728/select-date-from-calendar-in-android-espresso?rq=1

package com.ksu.serene.B.D_Calendar;

import android.content.Intent;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.calendar.Add_Appointment_Page;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Rule;
import org.junit.Before;
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
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)

public class Add_Appointment_PageTest {

    @Rule
    public ActivityTestRule<Add_Appointment_Page> activityTestRule = new ActivityTestRule<Add_Appointment_Page>(Add_Appointment_Page.class);
    private Add_Appointment_Page addAppointmentPage = null;
    private Calendar current = Calendar.getInstance();
    private Date AT = current.getTime();
    private SimpleDateFormat TimeFormat = new SimpleDateFormat ("HH : mm",Locale.UK);
    private String time = TimeFormat.format(AT);
    private int tomorrow = current.get(Calendar.DAY_OF_MONTH)+1;
    private int currentMonth = current.get(Calendar.MONTH);
    //private int minutes = current.get(Calendar.MINUTE)+20;
    private int hours = current.get(Calendar.HOUR)-1;
    private int currentHour = current.get(Calendar.HOUR);

    @Before
    public void setUp() throws Exception {
       // activityTestRule.launchActivity(new Intent());
        addAppointmentPage = activityTestRule.getActivity();
        //add timer for all tests
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check activity visible
            onView(ViewMatchers.withId(R.id.Add_Appointment_Page)).check(matches(isDisplayed()));
            //check name edit text visible
            onView(withId(R.id.nameET)).check(matches(isDisplayed()));
            //check for all buttons is visible
            //date
            onView(withId(R.id.MTillDays)).check(matches(isDisplayed()));
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
    public void addAppSuccessFuture () {
        onView(withId(R.id.nameET)).perform(typeText("Test Add App"));
        //close keyboard
        closeSoftKeyboard();
        // check when click the date and time button
        onView(withId(R.id.MTillDays)).perform(click());
        //choose date after current date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(current.get(Calendar.YEAR), (currentMonth+1), tomorrow));
        onView(withText("OK")).perform(click());
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose time
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(13 , 00));
        //click ok button
        onView(withText("OK")).perform(click());
            //click confirm button
        onView(withId(R.id.button)).perform(click());
            // check toast visibility
         onView(withText(R.string.AppSavedSuccess))
                 .inRoot(new ToastMatcher())
                 .check(matches(withText(R.string.AppSavedSuccess)));
            //check the activity is finished
        assertTrue(activityTestRule.getActivity().isFinishing());
    }

    @Test
    public void addAppSuccessToday () {
        try {
            AT = TimeFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.nameET)).perform(typeText("Test Add App"));
        //close keyboard
        closeSoftKeyboard();
        // check when click the date and time button
        onView(withId(R.id.MTillDays)).perform(click());
        //choose date after current date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(current.get(Calendar.YEAR), (currentMonth+1), current.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose time
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime((AT.getHours()+1) , AT.getMinutes() ));
        //click ok button
        onView(withText("OK")).perform(click());
        //click confirm button
        onView(withId(R.id.button)).perform(click());
            // check toast visibility
       onView(withText(R.string.AppSavedSuccess))
               .inRoot(new ToastMatcher())
               .check(matches(withText(R.string.AppSavedSuccess)));
        //check the activity is finished
        assertTrue(activityTestRule.getActivity().isFinishing());
    }

      
    @After
    public void tearDown() throws Exception {
        addAppointmentPage = null;
    }
}
