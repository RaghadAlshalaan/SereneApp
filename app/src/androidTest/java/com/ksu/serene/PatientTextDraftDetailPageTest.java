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
@RunWith(AndroidJUnit4.class)
public class PatientTextDraftDetailPageTest {

    @Rule
    public ActivityTestRule<PatientTextDraftDetailPage> activityTestRule = new ActivityTestRule<PatientTextDraftDetailPage>(PatientTextDraftDetailPage.class);
    private PatientTextDraftDetailPage textDraftDetailPage = null;

    @Before
    public void setUp() throws Exception {
        /*Intent intent = new Intent();
        intent.putExtra("TextDraftID", "first_draft408156");
        activityTestRule.launchActivity(intent);*/
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
    public void EditDraftSuccess () {
        //enter title
        onView(withId(R.id.TitleTextD)).perform(replaceText("New Title"));
        //close keyboard
        closeSoftKeyboard();
        ///leave subj as it is
        //press button
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(withId(R.id.SaveChanges)).perform(click());
            // check toast visibility
            onView(withText(R.string.TDUpdatedSuccess))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.TDUpdatedSuccess)));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void EditDraftEmptyFields () {
        //leave all fields empty
        onView(withId(R.id.TitleTextD)).perform(replaceText(""));
        onView(withId(R.id.SubjtextD)).perform(replaceText(""));
        //press button
        onView(withId(R.id.SaveChanges)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
    }

    @Test
    public void EditDraftOneEmptyField () {
        //enter title
        onView(withId(R.id.TitleTextD)).perform(replaceText("Edit Draft"));
        //close keyboard
        closeSoftKeyboard();
        //leave draft empty
        onView(withId(R.id.SubjtextD)).perform(replaceText(""));
        //press button
        onView(withId(R.id.SaveChanges)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
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
    }

    @After
    public void tearDown() throws Exception {
        textDraftDetailPage = null;
    }

}