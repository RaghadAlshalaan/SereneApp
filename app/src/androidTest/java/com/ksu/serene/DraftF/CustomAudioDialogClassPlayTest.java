package com.ksu.serene.DraftF;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;
import com.ksu.serene.controller.main.drafts.draftsFragment;

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
import static com.ksu.serene.TestUtils.withRecyclerView;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class CustomAudioDialogClassPlayTest {

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

    @Test
    public void PlayAudio () {
        //click on the play button
        onView(withId(R.id.pause)).perform(click());
        //TODO check the text time changed
    }

    @Test
    public void PlayAudioBack () {
        //click on the play button
        onView(withId(R.id.pause)).perform(click());
        //TODO check the text time changed
            //click the backward button
            onView(withId(R.id.backward)).perform(click());
            //TODO check the text time changed
    }

    @Test
    public void PlayAudioFor () {
        //click on the play button
        onView(withId(R.id.pause)).perform(click());
        //TODO check the text time changed
        //click the forward button
        onView(withId(R.id.forward)).perform(click());
        //TODO check the text time changed
    }

    @Test
    public void PlayAudioSpeed () {
        //click on the play button
        onView(withId(R.id.pause)).perform(click());
        //TODO check the text time changed
        //click the speed button
        onView(withId(R.id.speed)).perform(click());
        //TODO check the text time changed
    }


}