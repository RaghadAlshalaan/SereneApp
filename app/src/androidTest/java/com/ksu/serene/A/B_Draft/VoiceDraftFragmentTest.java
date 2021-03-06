package com.ksu.serene.A.B_Draft;

import android.Manifest;
import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;
import com.ksu.serene.controller.main.drafts.VoiceDraftFragment;
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
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.ksu.serene.TestUtils.withRecyclerView;
import static org.hamcrest.core.AllOf.allOf;

import com.ksu.serene.PermissionGranter;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class VoiceDraftFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
            onView(withId(R.id.navigation_drafts)).perform(click());
            onView(withText(R.string.VOICE)).perform(click());
            //add tiemr
            //Mack sure Espresso does not time out
            IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
            IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
            //Now we waite
            IdlingResource idlingResource1 = new ElapsedTimeIdlingResource(6000);
            try {
                IdlingRegistry.getInstance().register(idlingResource1);
                //check the activity is visible
                onView(withId(R.id.VoiceDraftFragment)).check(matches(isDisplayed()));
                //check the button visible
                onView(allOf(withId(R.id.button_expandable_110_250))).check(matches(isDisplayed()));
                //check recycler voice visible
                onView(allOf(withId(R.id.Recyclerview_Voice_Draft))).check(matches(isDisplayed()));
            }
            //clean upp
            finally {
                IdlingRegistry.getInstance().unregister(idlingResource1);
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

    @Test
    public void testItemClickVoiceRecycler() {
        onView(withRecyclerView(R.id.Recyclerview_Voice_Draft).atPosition(0)).perform(click());
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

    @After
    public void tearDown() throws Exception {
    }
}