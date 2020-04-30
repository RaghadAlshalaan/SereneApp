package com.ksu.serene.A.Report;
import android.Manifest;
import android.content.Intent;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.PermissionGranter;
import com.ksu.serene.R;
import com.ksu.serene.controller.main.drafts.AddVoiceDraftPage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class AddVoiceDraftPageTest {

    @Rule
    public ActivityTestRule<AddVoiceDraftPage> activityTestRule = new ActivityTestRule<AddVoiceDraftPage>(AddVoiceDraftPage.class);
    private AddVoiceDraftPage addVoiceDraftPage = null;
    public GrantPermissionRule permissionWrite = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    public GrantPermissionRule permissionRead = GrantPermissionRule.grant(Manifest.permission.RECORD_AUDIO);
    UiDevice uiDevice;

    //in before granteed permission
    @Before
    public void setUp() throws Exception {
        uiDevice =UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        //addVoiceDraftPage = activityTestRule.getActivity();
        activityTestRule.launchActivity(new Intent());
        //granteed permission
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.RECORD_AUDIO);
        //check the add audio activity appears
        //ensure the activity showen
        onView(withId(R.id.StartRecording)).check(matches(isDisplayed()));
        onView(withId(R.id.start)).check(matches(isDisplayed()));
        onView(withId(R.id.start)).perform(click());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResourceRecord = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResourceRecord);
            //check the recording activity showen
            onView(withId(R.id.content)).check(matches(isDisplayed()));
        }
        finally {
            IdlingRegistry.getInstance().unregister(idlingResourceRecord);
        }
    }

    @Test
    public void cancleNotRecordYetOK () {
        //onView(withId(R.id.start)).perform(click());
        //check the recording activity showen
        //onView(withId(R.id.content)).check(matches(isDisplayed()));
        //check elements visibles
        //record button
        onView(withId(R.id.record)).check(matches(isDisplayed()));
        //title edit text
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        //timer text
        onView(withId(R.id.timer)).check(matches(isDisplayed()));
        //timer text match 00:00:00
        onView(withId(R.id.timer)).check(matches(withText("00:00:00")));
        //cancle button
        onView(withId(R.id.action_cancel)).check(matches(isDisplayed()));
        //press the cancle button
        onView(withId(R.id.action_cancel)).perform(click());
        //check the prepare recording showen
        onView(withId(R.id.StartRecording)).check(matches(isDisplayed()));
    }

    //Start Recordign
    @Test
    public void startRecordingNotTitile () {
        //check elements visibles
        //record button
        onView(withId(R.id.record)).check(matches(isDisplayed()));
        //title edit text
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        //timer text
        onView(withId(R.id.timer)).check(matches(isDisplayed()));
        //timer text match 00:00:00
        onView(withId(R.id.timer)).check(matches(withText("00:00:00")));
        //click record button
        onView(withId(R.id.record)).perform(click());

        // check the timer not equel 00:00:00
        onView(withId(R.id.timer)).check(matches(not(withText("00:00:00"))));
        //check tha status is recording
        onView(withId(R.id.status)).check(matches(withText(cafe.adriel.androidaudiorecorder.R.string.aar_recording)));
        //add 10 seconds timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResourceRecord = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResourceRecord);
            //click stop recording
            onView(withId(R.id.record)).perform(click());
            //check tha status is puse
            onView(withId(R.id.status)).check(matches(withText(cafe.adriel.androidaudiorecorder.R.string.aar_paused)));
            //check the save button visible
            onView(withId(R.id.action_save)).check(matches(isDisplayed()));
        }
        finally {
            IdlingRegistry.getInstance().unregister(idlingResourceRecord);
        }
        //click play button
        onView(withId(R.id.play)).perform(click());
        //check tha status is playing
        onView(withId(R.id.status)).check(matches(withText(cafe.adriel.androidaudiorecorder.R.string.aar_playing)));
        //add 5 seconds timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResourcePlay = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResourcePlay);
            //click stop playing
            onView(withId(R.id.play)).perform(click());
            //click save button
            onView(withId(R.id.action_save)).perform(click());
            //dialog with ok button will showen
            //check the dialog appear
            onView(withText(cafe.adriel.androidaudiorecorder.R.string.no_title))
                    .inRoot(isDialog()) // <---
                    .check(matches(isDisplayed()));
            //click the ok button
            onView(withText(cafe.adriel.androidaudiorecorder.R.string.OK)).perform(click());
        }
        finally {
            IdlingRegistry.getInstance().unregister(idlingResourcePlay);
        }
        //check the activity sill showen
        onView(withId(R.id.content)).check(matches(isDisplayed()));
    }

    //@Test
    public void startRecordingWithTitile () {
        //record button
        onView(withId(R.id.record)).check(matches(isDisplayed()));
        //title edit text
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        //timer text
        onView(withId(R.id.timer)).check(matches(isDisplayed()));
        //timer text match 00:00:00
        onView(withId(R.id.timer)).check(matches(withText("00:00:00")));
        //click record button
        onView(withId(R.id.record)).perform(click());
        // check the timer not equel 00:00:00
        onView(withId(R.id.timer)).check(matches(not(withText("00:00:00"))));
        //check tha status is recording
        onView(withId(R.id.status)).check(matches(withText(cafe.adriel.androidaudiorecorder.R.string.aar_recording)));
        //add 5 seconds timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(3000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(3000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResourceRecord = new ElapsedTimeIdlingResource(3000);
        try {
            IdlingRegistry.getInstance().register(idlingResourceRecord);
            //click stop recording
            onView(withId(R.id.record)).perform(click());
            //check tha status is puse
            onView(withId(R.id.status)).check(matches(withText(cafe.adriel.androidaudiorecorder.R.string.aar_paused)));
            //check the save button visible
            onView(withId(R.id.action_save)).check(matches(isDisplayed()));
            //perform title
            onView(withId(R.id.title)).perform(typeText("Ramadan Month"));
            closeSoftKeyboard();
        }
        finally {
            IdlingRegistry.getInstance().unregister(idlingResourceRecord);
        }
        //click save button button
        onView(withId(R.id.action_save)).perform(click());
        //check the progress bar display
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        //tiemr
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(20000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(20000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResourceRecordSaved = new ElapsedTimeIdlingResource(20000);
        try {
            IdlingRegistry.getInstance().register(idlingResourceRecordSaved);
            onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        }
        finally {
            IdlingRegistry.getInstance().unregister(idlingResourceRecordSaved);
        }


        //check the activity not showen
        //onView(withId(R.id.content)).check(matches(not(isDisplayed())));
    }

    @Test
    public void cancleRecordCancle () {
        //record button
        onView(withId(R.id.record)).check(matches(isDisplayed()));
        //title edit text
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        //timer text
        onView(withId(R.id.timer)).check(matches(isDisplayed()));
        //timer text match 00:00:00
        onView(withId(R.id.timer)).check(matches(withText("00:00:00")));
        //click record button
        onView(withId(R.id.record)).perform(click());
        // check the timer not equel 00:00:00
        onView(withId(R.id.timer)).check(matches(not(withText("00:00:00"))));
        //check tha status is recording
        onView(withId(R.id.status)).check(matches(withText(cafe.adriel.androidaudiorecorder.R.string.aar_recording)));
        //add 10 seconds timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResourceRecord = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResourceRecord);
            //click stop recording
            onView(withId(R.id.record)).perform(click());
            //check tha status is puse
            onView(withId(R.id.status)).check(matches(withText(cafe.adriel.androidaudiorecorder.R.string.aar_paused)));
            //check the save button visible
            onView(withId(R.id.action_save)).check(matches(isDisplayed()));
        }
        finally {
            IdlingRegistry.getInstance().unregister(idlingResourceRecord);
        }
        //click play button
        onView(withId(R.id.play)).perform(click());
        //check tha status is playing
        onView(withId(R.id.status)).check(matches(withText(cafe.adriel.androidaudiorecorder.R.string.aar_playing)));
        //add 5 seconds timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResourcePlay = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResourcePlay);
            //click cancle button
            onView(withId(R.id.action_cancel)).perform(click());
            //check the dialog appear
            onView(withText(cafe.adriel.androidaudiorecorder.R.string.cancel_recording_msg))
                    .inRoot(isDialog()) // <---
                    .check(matches(isDisplayed()));
            //click the cancle button
            onView(withText(cafe.adriel.androidaudiorecorder.R.string.cancel)).perform(click());
        }
        finally {
            IdlingRegistry.getInstance().unregister(idlingResourcePlay);
        }
        //check the activity sill showen
        onView(withId(R.id.content)).check(matches(isDisplayed()));
    }

    @Test
    public void cancleRecordOK () {
        //record button
        onView(withId(R.id.record)).check(matches(isDisplayed()));
        //title edit text
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        //timer text
        onView(withId(R.id.timer)).check(matches(isDisplayed()));
        //timer text match 00:00:00
        onView(withId(R.id.timer)).check(matches(withText("00:00:00")));
        //click record button
        onView(withId(R.id.record)).perform(click());
        // check the timer not equel 00:00:00
        onView(withId(R.id.timer)).check(matches(not(withText("00:00:00"))));
        //check tha status is recording
        onView(withId(R.id.status)).check(matches(withText(cafe.adriel.androidaudiorecorder.R.string.aar_recording)));
        //add 10 seconds timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResourceRecord = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResourceRecord);
            //click stop recording
            onView(withId(R.id.record)).perform(click());
            //check tha status is puse
            onView(withId(R.id.status)).check(matches(withText(cafe.adriel.androidaudiorecorder.R.string.aar_paused)));
            //check the save button visible
            onView(withId(R.id.action_save)).check(matches(isDisplayed()));
        }
        finally {
            IdlingRegistry.getInstance().unregister(idlingResourceRecord);
        }
        //click play button
        onView(withId(R.id.play)).perform(click());
        //check tha status is playing
        onView(withId(R.id.status)).check(matches(withText(cafe.adriel.androidaudiorecorder.R.string.aar_playing)));
        //add 5 seconds timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResourcePlay = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResourcePlay);
            //click cancle button
            onView(withId(R.id.action_cancel)).perform(click());
            //check the dialog appear
            onView(withText(cafe.adriel.androidaudiorecorder.R.string.cancel_recording_msg))
                    .inRoot(isDialog()) // <---
                    .check(matches(isDisplayed()));
            //click the ok button
            onView(withText(cafe.adriel.androidaudiorecorder.R.string.OK)).perform(click());
        }
        finally {
            IdlingRegistry.getInstance().unregister(idlingResourcePlay);
        }
        //the prepare activity will sohwen
        onView(withId(R.id.StartRecording)).check(matches(isDisplayed()));
    }

    //Restart Recording
    @Test
    public void restartRecording () {
        //record button
        onView(withId(R.id.record)).check(matches(isDisplayed()));
        //title edit text
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        //timer text
        onView(withId(R.id.timer)).check(matches(isDisplayed()));
        //timer text match 00:00:00
        onView(withId(R.id.timer)).check(matches(withText("00:00:00")));
        //click record button
        onView(withId(R.id.record)).perform(click());
        // check the timer not equel 00:00:00
        onView(withId(R.id.timer)).check(matches(not(withText("00:00:00"))));
        //check tha status is recording
        onView(withId(R.id.status)).check(matches(withText(cafe.adriel.androidaudiorecorder.R.string.aar_recording)));
        //add 10 seconds timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResourceRecord = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResourceRecord);
            //click stop recording
            onView(withId(R.id.record)).perform(click());
            //check tha status is puse
            onView(withId(R.id.status)).check(matches(withText(cafe.adriel.androidaudiorecorder.R.string.aar_paused)));
            //check the save button visible
            onView(withId(R.id.action_save)).check(matches(isDisplayed()));
        }
        finally {
            IdlingRegistry.getInstance().unregister(idlingResourceRecord);
        }
        //click restart button
        onView(withId(R.id.restart)).perform(click());
        //check the timer is
        onView(withId(R.id.timer)).check(matches(withText("00:00:00")));
        //check the activity sill showen
        onView(withId(R.id.content)).check(matches(isDisplayed()));
    }

}