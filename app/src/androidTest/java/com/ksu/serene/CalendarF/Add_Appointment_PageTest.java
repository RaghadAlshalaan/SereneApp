//test the date and time button from this source
//https://stackoverflow.com/questions/43149728/select-date-from-calendar-in-android-espresso?rq=1

package com.ksu.serene.CalendarF;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

@RunWith(AndroidJUnit4.class)
public class Add_Appointment_PageTest {

    @Rule
    public ActivityTestRule<Add_Appointment_Page> activityTestRule = new ActivityTestRule<Add_Appointment_Page>(Add_Appointment_Page.class);
    private Add_Appointment_Page addAppointmentPage = null;
    Calendar current = Calendar.getInstance();
    int tomorrow = current.get(Calendar.DAY_OF_MONTH)+1;
    int currentMonth = current.get(Calendar.MONTH)+1;
    int minutes = current.get(Calendar.MINUTE)+5;
    int hours = current.get(Calendar.HOUR)-1;

    @Before
    public void setUp() throws Exception {
        activityTestRule.launchActivity(new Intent());
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
    public void backButton () {
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backButton)).check(matches(isClickable()));
        //perform click on back button
        onView(withId(R.id.backButton)).perform(click());
    }

    @Test
    public void CurrentDateTime () {
        //enter app name
        onView(withId(R.id.nameET)).perform(typeText("First App"));
        //close keyboard
        closeSoftKeyboard();
        // check when click the date and time button
        onView(withId(R.id.MTillDays)).perform(click());
        //choose current date from calender
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), currentMonth, current.get(Calendar.DAY_OF_MONTH) ));
        //click ok
        onView(withText("OK")).perform(click());
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose current time or before the current time
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(hours , current.get(Calendar.MINUTE) ));
        //click ok button
        onView(withText("OK")).perform(click());
            //click confirm button
            onView(withId(R.id.button)).perform(click());
            // check toast visibility
            onView(withText(R.string.CurrentTime))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.CurrentTime)));
    }

    @Test
    public void EmptyFields () {
        //not enter leave it empty
        onView(withId(R.id.nameET)).perform(typeText(""));
        //close keyboard
        closeSoftKeyboard();
        // check when the date and time button text as default mean that the user not clicked this button
        onView(withId(R.id.MTillDays)).check(matches(withText(R.string.SetDayApp)));
        onView(withId(R.id.MTime)).check(matches(withText(R.string.SetTimeApp)));
        //click confirm button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
    }

    //check when the field filled and date button pressed not set date
    @Test
    public void DateClickedCancle () {
        //enter app name
        onView(withId(R.id.nameET)).perform(typeText("First App"));
        //close keyboard
        closeSoftKeyboard();
        // check when click the date button
        onView(withId(R.id.MTillDays)).perform(click());
        //choose  date from calender
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), currentMonth, current.get(Calendar.DAY_OF_MONTH) ));
        //check when date bicker dialog cancel button clicked
        onView(withText("CANCEL")).perform(click());
        //check that text not changed
        onView(withId(R.id.MTillDays)).check(matches(withText(R.string.SetDayApp)));
        //time button not clicked
        onView(withId(R.id.MTime)).check(matches(withText(R.string.SetTimeApp)));
        //click confirm button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
    }

    //check when the field filled and date button pressed with set date
    @Test
    public void DateClickedOK () {
        //enter app name
        onView(withId(R.id.nameET)).perform(typeText("First App"));
        //close keyboard
        closeSoftKeyboard();
        // check when click the date button
        onView(withId(R.id.MTillDays)).perform(click());
        //choose date after current time
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), currentMonth, current.get(Calendar.DAY_OF_MONTH) ));
        onView(withText("OK")).perform(click());
        //time button not clicked
        onView(withId(R.id.MTime)).check(matches(withText(R.string.SetTimeApp)));
            //click confirm button
            onView(withId(R.id.button)).perform(click());
            // check toast visibility
            onView(withText(R.string.EmptyFields))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.EmptyFields)));
    }

    //check when the field filled and time button pressed not set time
    @Test
    public void TimeClickedCancle () {
        //enter app name
        onView(withId(R.id.nameET)).perform(typeText("First App"));
        //close keyboard
        closeSoftKeyboard();
        //check that text of date not changed
        onView(withId(R.id.MTillDays)).check(matches(withText(R.string.SetDayApp)));
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose time
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime((current.get(Calendar.HOUR)) , current.get(Calendar.MINUTE) ));
        //click calncle button
        onView(withText("CANCEL")).perform(click());
        //check the text not changed
        onView(withId(R.id.MTime)).check(matches(withText(R.string.SetTimeApp)));
        //click confirm button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
    }

    //check when the field filled and date button pressed with set date
    @Test
    public void TimeClickedOK () {
        onView(withId(R.id.nameET)).perform(typeText("First App"));
        //close keyboard
        closeSoftKeyboard();
        //check that text of date not changed
        onView(withId(R.id.MTillDays)).check(matches(withText(R.string.SetDayApp)));
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose time
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime((current.get(Calendar.HOUR)) , current.get(Calendar.MINUTE) ));
        //click ok button
        onView(withText("OK")).perform(click());
            //click confirm button
            onView(withId(R.id.button)).perform(click());
            // check toast visibility
            onView(withText(R.string.EmptyFields))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.EmptyFields)));

    }

    @Test
    public void addAppSuccessFuture () {
        onView(withId(R.id.nameET)).perform(typeText("Test Add App"));
        //close keyboard
        closeSoftKeyboard();
        // check when click the date and time button
        onView(withId(R.id.MTillDays)).perform(click());
        //choose date after current date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), currentMonth, tomorrow));
        onView(withText("OK")).perform(click());
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose time
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(03 , 00));
        //click ok button
        onView(withText("OK")).perform(click());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //click confirm button
            onView(withId(R.id.button)).perform(click());
            //TODO write test for firebase to check the app added successfully in firebase
            //DocumentReference doc = t
            // check toast visibility
            onView(withText(R.string.AppSavedSuccess))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.AppSavedSuccess)));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void addAppSuccessToday () {
        onView(withId(R.id.nameET)).perform(typeText("Test Add App"));
        //close keyboard
        closeSoftKeyboard();
        // check when click the date and time button
        onView(withId(R.id.MTillDays)).perform(click());
        //choose date after current date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), currentMonth, current.get(Calendar.DAY_OF_MONTH) ));
        onView(withText("OK")).perform(click());
        //time button clicked
        onView(withId(R.id.MTime)).perform(click());
        //choose time
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime((current.get(Calendar.HOUR)) , minutes ));
        //click ok button
        onView(withText("OK")).perform(click());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //click confirm button
            onView(withId(R.id.button)).perform(click());
            //TODO write test for firebase to check the app added successfully in firebase
            //DocumentReference doc = t
            // check toast visibility
            onView(withText(R.string.AppSavedSuccess))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.AppSavedSuccess)));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

      @Test
      public void appAppFialed () {
          //due no problem in networl connection or undefined reason
          onView(withId(R.id.nameET)).perform(typeText("First App"));
          closeSoftKeyboard();
          // check when click the date and time button
          onView(withId(R.id.MTillDays)).perform(click());
          //choose date after current date
          onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), currentMonth, current.get(Calendar.DAY_OF_MONTH) ));
          onView(withText("OK")).perform(click());
          //time button clicked
          onView(withId(R.id.MTime)).perform(click());
          //choose time
          onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime((current.get(Calendar.HOUR)) , minutes ));
          //click ok button
          onView(withText("OK")).perform(click());
          //add timer to disconnet simulater from internet connection
          IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
          IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
          //Now we waite
          IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
          try {
              IdlingRegistry.getInstance().register(idlingResource);
              onView(withId(R.id.button)).perform(click());
              // check toast visibility
              onView(withText(R.string.AppSavedFialed))
                      .inRoot(new ToastMatcher())
                          .check(matches(withText(R.string.AppSavedFialed)));
          }
          //clean upp
          finally {
              IdlingRegistry.getInstance().unregister(idlingResource);
          }
      }

    @After
    public void tearDown() throws Exception {
        addAppointmentPage = null;
    }
}
