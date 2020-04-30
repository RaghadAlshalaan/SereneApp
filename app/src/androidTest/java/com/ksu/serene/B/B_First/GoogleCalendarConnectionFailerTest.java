package com.ksu.serene.B.B_First;

import static org.junit.Assert.*;
import com.ksu.serene.ElapsedTimeIdlingResource;

import android.Manifest;
import android.widget.EditText;

import com.ksu.serene.PermissionGranter;
import com.ksu.serene.R;
import com.ksu.serene.controller.signup.GoogleCalendarConnection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.concurrent.TimeUnit;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;

import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)

public class GoogleCalendarConnectionFailerTest {
    @Rule
    public ActivityTestRule<GoogleCalendarConnection> activityTestRule = new ActivityTestRule<GoogleCalendarConnection>(GoogleCalendarConnection.class);
    private GoogleCalendarConnection googleCalendarConnection = null;
    UiDevice uiDevice;


    @Before
    public void setUp() throws Exception {
        uiDevice =UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        googleCalendarConnection = activityTestRule.getActivity();
        //check the activity showen
        onView(withId(R.id.GoogleCalendarConnection)).check(matches(isDisplayed()));
        //check the status from default not connected
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
        //check the connect button visible
        onView(withId(R.id.connectCalendar)).check(matches(isDisplayed()));
    }

    @Test
    public void A_BackButton() {
        //Back Button
        onView(withId(R.id.backBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.backBtn)).check(matches(isClickable()));
        //perform click on back button
        onView(withId(R.id.backBtn)).perform(click());
        //check the activity is finish
        assertTrue(activityTestRule.getActivity().isFinishing());
    }
    @Test
    public void B_clickConnectNothing () throws UiObjectNotFoundException {
        //check the status from default not connected
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
        //check the connect button visible
        onView(withId(R.id.connectCalendar)).check(matches(isDisplayed()));
        //perform click
        onView(withId(R.id.connectCalendar)).perform(click());
        //click cancle button
        UiObject mCancel = uiDevice.findObject(new UiSelector().text("CANCEL"));
        mCancel.click();
        //no connect
        //check the status from default not connected
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
    }

    @Test
    public void C_clickConnectCancleChooseAcc () throws UiObjectNotFoundException {
        //check the status from default not connected
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
        //check the connect button visible
        onView(withId(R.id.connectCalendar)).check(matches(isDisplayed()));
        //perform click
        onView(withId(R.id.connectCalendar)).perform(click());
        UiObject mAcc = uiDevice.findObject(new UiSelector().index(0));
        mAcc.click();
        //click cancle button
        UiObject mCancel = uiDevice.findObject(new UiSelector().text("CANCEL"));
        mCancel.click();
        //no connect
        //check the status from default not connected
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
    }
}