package com.ksu.serene.A.Profile;

import static org.junit.Assert.*;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static org.junit.Assert.*;
import android.content.Intent;
import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.profile.MyDoctor;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.concurrent.TimeUnit;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)

public class QMyDoctorTest {
    @Rule
    public ActivityTestRule<MyDoctor> activityTestRule = new ActivityTestRule<MyDoctor>(MyDoctor.class);
    private MyDoctor myDoctor = null;

    @Before
    public void setUp() throws Exception {
        myDoctor = activityTestRule.getActivity();
        //add timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //activityTestRule.launchActivity(new Intent());
            //check the activity visible
            onView(ViewMatchers.withId(R.id.MyDoctor)).check(matches(isDisplayed()));
            //check buttons visibility
            //delete Doctor button
            onView(withId(R.id.delete)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void BackButton () {
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backButton)).check(matches(isClickable()));
        //perform click on back button
        onView(withId(R.id.backButton)).perform(click());
        assertTrue(activityTestRule.getActivity().isFinishing());
    }

    //check when delete doctor clicked the dialog appears
    @Test
    public void DeleteDoctorCancel () {
        //click the delete out
        onView(withId(R.id.delete)).perform(click());
        //check the dialog appear
        onView(withText(R.string.DeleteMessageDoc))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click the cancel button
        onView(withText(R.string.DeleteCancleDoc)).perform(click());
        //nothing happened
        assertFalse(activityTestRule.getActivity().isFinishing());
        onView(ViewMatchers.withId(R.id.MyDoctor)).check(matches(isDisplayed()));
    }

    //check when delete doctor clicked the dialog appears
    @Test
    public void DeleteDoctorOK () {
        //click the delete out
        onView(withId(R.id.delete)).perform(click());
        //check the dialog appear
        onView(withText(R.string.DeleteMessageDoc))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click the ok button
        onView(withText(R.string.DeleteOKDoc)).perform(click());
        // check toast visibility
        onView(withText(R.string.DocDeletedSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.DocDeletedSuccess)));
        //check the patient profile showen
        onView(withId(R.id.PatientProfile)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        myDoctor = null;
    }

}