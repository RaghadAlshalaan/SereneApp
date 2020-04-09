package com.ksu.serene;

import android.Manifest;
import android.content.Intent;
import android.util.Log;

import com.ksu.serene.controller.main.drafts.AddVoiceDraftPage;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import timber.log.Timber;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class AddVoiceDraftPageEspressoTest {

    @Rule
    //public IntentsTestRule intentsTestRule = new IntentsTestRule(AddVoiceDraftPage.class);
    public ActivityTestRule<AddVoiceDraftPage> activityTestRule = new ActivityTestRule<AddVoiceDraftPage>(AddVoiceDraftPage.class);
    public GrantPermissionRule permissionWrite = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    public GrantPermissionRule permissionRead = GrantPermissionRule.grant(Manifest.permission.RECORD_AUDIO);

    @Before
    public void setUp() throws Exception {
        //intentsTestRule.getActivity();
        activityTestRule.launchActivity(new Intent());
        //granted permission
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.RECORD_AUDIO);
    }

    @Test
    public void ReadyToRecord () {
        //check activity visible
        onView(withId(R.id.StartRecording)).check(matches(isDisplayed()));
        //check  button visible
        onView(withId(R.id.start)).check(matches(isDisplayed()));
        //click the button
        onView(withId(R.id.start)).perform(click());
        //onView(withText("ALLOW")).perform(click());
        //check the record activity is showen
        onView(withId(R.id.content)).check(matches(isDisplayed()));
    }
    @After
    public void tearDown() throws Exception {
    }
}