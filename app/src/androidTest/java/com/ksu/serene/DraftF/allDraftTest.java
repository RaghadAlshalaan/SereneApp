package com.ksu.serene.DraftF;

import android.Manifest;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.MainActivity;
import com.ksu.serene.PermissionGranter;
import com.ksu.serene.R;
import com.ksu.serene.controller.main.drafts.allDraft;
import com.ksu.serene.controller.main.drafts.draftsFragment;

import org.hamcrest.CoreMatchers;
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
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.ksu.serene.TestUtils.withRecyclerView;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class allDraftTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        onView(withId(R.id.navigation_drafts)).perform(click());
        //add tiemr
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the activity is visible
            onView(withId(R.id.allDraft)).check(matches(isDisplayed()));
            //check the button visible
            onView(allOf(withId(R.id.button_expandable_110_250))).check(matches(isDisplayed()));
            //check recycler Text visible
            onView(allOf(withId(R.id.Recyclerview_All_DraftText))).check(matches(isDisplayed()));
            //check recycler voice visible
            onView(allOf(withId(R.id.Recyclerview_All_DraftVoice))).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void TestAddButton () {
        //click the button
        onView(allOf(withId(R.id.button_expandable_110_250))).perform(click());
        //check the two buttons will appears
        //record audio
        onView(allOf(withId(R.id.AddVoiceButton))).check(matches(isDisplayed()));
        //add text
        onView(allOf(withId(R.id.AddTextButton))).check(matches(isDisplayed()));
    }

    @Test
    public void AddText () {
        //click the button
        onView(allOf(withId(R.id.button_expandable_110_250))).perform(click());
        //click add text button
        onView(allOf(withId(R.id.AddTextButton))).perform(click());
        //check the add text activity appears
        onView(withId(R.id.AddTextDraftPage)).check(matches(isDisplayed()));
    }

    @Test
    public void AddAudio () {
        //click the button
        onView(allOf(withId(R.id.button_expandable_110_250))).perform(click());
        //click add audio button
        onView(allOf(withId(R.id.AddVoiceButton))).perform(click());
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.RECORD_AUDIO);
        //check the add audio activity appears
        onView(withId(R.id.StartRecording)).check(matches(isDisplayed()));
    }

    //TODO check for voice recyclers view
    @Test
    public void testItemClickVoiceRecycler() {
        onView(withRecyclerView(R.id.Recyclerview_All_DraftVoice).atPosition(0)).perform(click());
        //add tiemr
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //activity
            onView(withId(R.id.your_dialog_root_element)).check(matches(isDisplayed()));
            //title
            onView(withId(R.id.title)).check(matches(isDisplayed()));
            //currentTime
            onView(withId(R.id.currentTime)).check(matches(isDisplayed()));
            //remaining Time
            onView(withId(R.id.remainingTime)).check(matches(isDisplayed()));
            //pause
            onView(withId(R.id.pause)).check(matches(isDisplayed()));
            //back button
            onView(withId(R.id.backward)).check(matches(isDisplayed()));
            //forward button
            onView(withId(R.id.forward)).check(matches(isDisplayed()));
            //speed
            onView(withId(R.id.speed)).check(matches(isDisplayed()));
            //delete button
            onView(withId(R.id.delete)).check(matches(isDisplayed()));
            //cancel button
            onView(withId(R.id.cancel)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void testItemClickTxtRecycler() {
        onView(withRecyclerView(R.id.Recyclerview_All_DraftText).atPosition(0)).perform(click());
        //add tiemr
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //activity
            onView(withId(R.id.PatientTextDraftDetailPage)).check(matches(isDisplayed()));
            //title
            onView(withId(R.id.TitleTextD)).check(matches(isDisplayed()));
            //subj
            onView(withId(R.id.SubjtextD)).check(matches(isDisplayed()));
            //delete button
            onView(withId(R.id.delete)).check(matches(isDisplayed()));
            //edit button
            onView(withId(R.id.SaveChanges)).check(matches(isDisplayed()));
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