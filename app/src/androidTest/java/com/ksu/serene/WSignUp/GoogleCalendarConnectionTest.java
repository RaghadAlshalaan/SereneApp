package com.ksu.serene.WSignUp;

import android.Manifest;
import android.widget.EditText;

import com.ksu.serene.PermissionGranter;
import com.ksu.serene.R;
import com.ksu.serene.controller.signup.GoogleCalendarConnection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.click;

import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)

public class GoogleCalendarConnectionTest {

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

    //@Test
    public void A_AnotherAccount () throws UiObjectNotFoundException {
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
        //check the connect button visible
        onView(withId(R.id.connectCalendar)).perform(click());
        //perform click
        onView(withId(R.id.connectCalendar)).perform(click());
        UiObject mAcc = uiDevice.findObject(new UiSelector().index(0));
        mAcc.click();
        //click cancle button
        UiObject mOk = uiDevice.findObject(new UiSelector().text("OK"));
        mOk.click();
        //enter email
        UiObject mEmail = uiDevice.findObject(new UiSelector().textContains("Email"));
        mEmail.setText("sereneapp0@gmail.com");
        //click next
        UiObject mNext = uiDevice.findObject(new UiSelector().text("Next"));
        mNext.click();
        //enter pass
        UiObject mPass = uiDevice.findObject(new UiSelector().textContains("password"));
        mPass.setText("sereneApp00-");
        //click next
        mNext.click();
        //connect
        UiObject mDeny = uiDevice.findObject(new UiSelector().text("Deny"));
        mDeny.click();
        //check the status from default not connected
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
    }

    @Test
    public void B_AnotherAccount () throws UiObjectNotFoundException {
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
        //check the connect button visible
        onView(withId(R.id.connectCalendar)).check(matches(isDisplayed()));
        //perform click
        onView(withId(R.id.connectCalendar)).perform(click());
        UiObject mAcc = uiDevice.findObject(new UiSelector().textContains("@gmail.com"));
        mAcc.click();
        /*UiObject mAcc = uiDevice.findObject(new UiSelector().textContains("Add"));
        mAcc.click();
        //click cancle button
        UiObject mOk = uiDevice.findObject(new UiSelector().text("OK"));
        mOk.click();
        //enter email
        UiObject mEmail = uiDevice.findObject(new UiSelector().className(EditText.class));
        mEmail.setText("sereneapp0@gmail.com");
        //click next
        UiObject mNext = uiDevice.findObject(new UiSelector().text("Next"));
        mNext.click();
        //enter pass
        UiObject mPass = uiDevice.findObject(new UiSelector().className(EditText.class));
        mPass.setText("sereneApp00-");
        //click next*/

        UiObject mNextPass = uiDevice.findObject(new UiSelector().text("Next"));
        mNextPass.click();
        //connect
        UiObject mAllow = uiDevice.findObject(new UiSelector().text("Allow"));
        mAllow.click();
        //check the status from default not connected
        onView(withId(R.id.status)).check(matches(withText("Connected")));
        //click finish will go to main activity home
        onView(withId(R.id.finishBtn)).perform(click());
        //first granted the permission
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.ACCESS_COARSE_LOCATION);
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.ACCESS_FINE_LOCATION);
        onView(withId(R.id.MainActivity)).check(matches(isDisplayed()));
    }
}