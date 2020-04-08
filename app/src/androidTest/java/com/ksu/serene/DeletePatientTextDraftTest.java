package com.ksu.serene;

import com.ksu.serene.controller.main.drafts.PatientTextDraftDetailPage;

import org.junit.After;
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

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
@RunWith(AndroidJUnit4.class)
public class DeletePatientTextDraftTest {

    @Rule
    public ActivityTestRule<PatientTextDraftDetailPage> activityTestRule = new ActivityTestRule<PatientTextDraftDetailPage>(PatientTextDraftDetailPage.class);
    private PatientTextDraftDetailPage textDraftDetailPage = null;

    @Before
    public void setUp() throws Exception {
        //add Timer for all tests
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            textDraftDetailPage = activityTestRule.getActivity();
            //check activity visible
            onView(withId(R.id.PatientTextDraftDetailPage)).check(matches(isDisplayed()));
            //check the button visible
            onView(withId(R.id.delete)).check(matches(isDisplayed()));
            onView(withId(R.id.SaveChanges)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
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
    }

    @After
    public void tearDown() throws Exception {
        textDraftDetailPage = null;
    }
}