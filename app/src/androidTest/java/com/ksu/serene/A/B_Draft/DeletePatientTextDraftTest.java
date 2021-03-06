package com.ksu.serene.A.B_Draft;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.drafts.PatientTextDraftDetailPage;
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

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.ksu.serene.TestUtils.withRecyclerView;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DeletePatientTextDraftTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        onView(withId(R.id.navigation_drafts)).perform(click());
        //add tiemr
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(withId(R.id.allDraft)).check(matches(isDisplayed()));
            onView(withRecyclerView(R.id.Recyclerview_All_DraftText).atPosition(0)).perform(click());
            //add Timer for all tests
            //Mack sure Espresso does not time out
            IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
            IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
            //Now we waite
            IdlingResource idlingResource1 = new ElapsedTimeIdlingResource(5000);
            try {
                IdlingRegistry.getInstance().register(idlingResource1);
                //textDraftDetailPage = activityTestRule.getActivity();
                //check activity visible
                onView(withId(R.id.PatientTextDraftDetailPage)).check(matches(isDisplayed()));
                //check the button visible
                onView(withId(R.id.delete)).check(matches(isDisplayed()));
                onView(withId(R.id.SaveChanges)).check(matches(isDisplayed()));
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
    public void DeleteCancle () {
        //click button
        onView(withId(R.id.delete)).perform(click());
        //delete dialog will appear
        onView(withText(R.string.DeleteMessageTD))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //press the cancel button
        onView(withText(R.string.DeleteCancleTD)).perform(click());
        //check nothing changes
        onView(withId(R.id.TitleTextD)).check(matches(isDisplayed()));
        onView(withId(R.id.SubjtextD)).check(matches(isDisplayed()));
        //check the activity still showen
        onView(withId(R.id.PatientTextDraftDetailPage)).check(matches(isDisplayed()));
    }

    @Test
    public void DeleteOK () {
        //click button
        onView(withId(R.id.delete)).perform(click());
        //delete dialog will appear
        onView(withText(R.string.DeleteMessageTD))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click delete button
        onView(withText(R.string.DeleteOKTD)).perform(click());
        // check toast visibility
       onView(withText(R.string.TDDeletedSuccess))
               .inRoot(new ToastMatcher())
               .check(matches(withText(R.string.TDDeletedSuccess)));
        //  check the activity is finish
        onView(withId(R.id.allDraft)).check(matches(isDisplayed()));
    }

    //@Test
    public void DeleteOKFailer () {
        //click button
        onView(withId(R.id.delete)).perform(click());
        //delete dialog will appear
        onView(withText(R.string.DeleteMessageTD))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //add timer to disconnect internet connection form simulater
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //click delete button
            onView(withText(R.string.DeleteOKTD)).perform(click());
            // check toast visibility
            onView(withText(R.string.TDDeletedFialed))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.TDDeletedFialed)));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @After
    public void tearDown() throws Exception {
        //textDraftDetailPage = null;
    }
}