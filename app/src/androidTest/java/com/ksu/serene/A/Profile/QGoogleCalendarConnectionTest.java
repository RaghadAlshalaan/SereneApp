package com.ksu.serene.A.Profile;

import android.widget.RadioButton;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.controller.main.profile.PatientProfile;

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
import androidx.test.uiautomator.UiDevice;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)

public class QGoogleCalendarConnectionTest {

    @Rule
    public ActivityTestRule<PatientProfile> activityTestRule = new ActivityTestRule<PatientProfile>(PatientProfile.class);
    private PatientProfile patientProfile = null;
    UiDevice uiDevice;

    @Before
    public void setUp() throws Exception {
        uiDevice =UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        patientProfile = activityTestRule.getActivity();
        //launch activity
        //activityTestRule.launchActivity(new Intent());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the activity is visible
            onView(withId(R.id.PatientProfile)).check(matches(isDisplayed()));
            //check the buttons visible
            //connect to google calender
            onView(withId(R.id.go_to3)).check(matches(isDisplayed()));
            //click connect to calendar
            onView(withId(R.id.go_to3)).perform(click());
            //check the ggogle calendar connection page visible
            onView(withId(R.id.GoogleCalendarConnection)).check(matches(isDisplayed()));
            //check the status from default not connected
            onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
            //check the connect button visible
            onView(withId(R.id.connectCalendar)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void A_backButton () {
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backButton)).check(matches(isClickable()));
        //perform click on back button
        onView(withId(R.id.backButton)).perform(click());
        onView(withId(R.id.PatientProfile)).check(matches(isDisplayed()));
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

   // @Test
    public void D_clickConnectCancleChooseAccount () throws UiObjectNotFoundException {
        //check the status from default not connected
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
        //check the connect button visible
        onView(withId(R.id.connectCalendar)).check(matches(isDisplayed()));
        //perform click
        onView(withId(R.id.connectCalendar)).perform(click());
        UiObject mAcc = uiDevice.findObject(new UiSelector().textContains("@gmail.com"));
        mAcc.click();
        //click cancle button
        UiObject mOk = uiDevice.findObject(new UiSelector().text("OK"));
        mOk.click();
        //connect
        UiObject mDeny = uiDevice.findObject(new UiSelector().text("Deny"));
        mDeny.click();
        //check the activity will return
        onView(withId(R.id.backButton)).perform(click());
        //check the Status Disconnect
        onView(withId(R.id.connection_status)).check(matches(withText(R.string.disabled)));
    }

    @Test
    public void E_clickConnectChooseCurrentAccount () throws UiObjectNotFoundException {
        //check the status from default not connected
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
        //check the connect button visible
        onView(withId(R.id.connectCalendar)).check(matches(isDisplayed()));
        //perform click
        onView(withId(R.id.connectCalendar)).perform(click());
        UiObject mAcc = uiDevice.findObject(new UiSelector().textContains("sereneapp0@gmail.com"));
        mAcc.click();
        //click cancle button
        UiObject mOk = uiDevice.findObject(new UiSelector().text("OK"));
        mOk.click();
        //connect
        UiObject mAllow = uiDevice.findObject(new UiSelector().text("Allow"));
        mAllow.click();
        //check the status from default  connected
        onView(withId(R.id.status)).check(matches(withText("Connected")));
    }



}