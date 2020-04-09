package com.ksu.serene;

import com.ksu.serene.controller.main.drafts.draftsFragment;

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
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.ksu.serene.TestUtils.withRecyclerView;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class CustomAudioDialogClassTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        activityTestRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //set fragment
                Fragment draftFragment = new draftsFragment();
                FragmentTransaction fragmentTransaction = activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
                //fragmentTransaction.replace(R.id.Home, CalendarF);
                fragmentTransaction.add(R.id.MainActivity, draftFragment);
                fragmentTransaction.commit();

            }
        });
        //add tiemr
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the activity is visible
            onView(withId(R.id.allDraft)).check(matches(isDisplayed()));
            onView(withRecyclerView(R.id.Recyclerview_All_DraftVoice).atPosition(0)).perform(click());
            //add tiemr
            //Mack sure Espresso does not time out
            IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
            IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
            //Now we waite
            IdlingResource idlingResource1 = new ElapsedTimeIdlingResource(5000);
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
                IdlingRegistry.getInstance().unregister(idlingResource1);
            }
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    //Test When cancle button click
    @Test
    public void CancleButton () {
        //cancel button click
        onView(withId(R.id.cancel)).perform(click());
        //check the activity is shown
        onView(withId(R.id.allDraft)).check(matches(isDisplayed()));
    }

    //Test When delete button click
    @Test
    public void DeleteCancle () {
        //click button
        onView(withId(R.id.delete)).perform(click());
        //delete dialog will appear
        onView(withText(R.string.DeleteMessageAudio))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click cancle button
        onView(withText(R.string.no)).perform(click());
        //nothing will be happen
    }

    @Test
    public void DeleteOK () {
        //click button
        onView(withId(R.id.delete)).perform(click());
        //delete dialog will appear
        onView(withText(R.string.DeleteMessageAudio))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click delete button
        onView(withText(R.string.yes)).perform(click());
        // check toast visibility
        onView(withText(R.string.AudioDeletedSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.AudioDeletedSuccess)));
        //check the activity is shown
        onView(withId(R.id.allDraft)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
    }
}