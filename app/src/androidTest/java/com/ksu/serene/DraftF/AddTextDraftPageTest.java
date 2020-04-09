package com.ksu.serene.DraftF;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.drafts.AddTextDraftPage;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
@RunWith(AndroidJUnit4.class)
public class AddTextDraftPageTest {

    @Rule
    public ActivityTestRule<AddTextDraftPage> activityTestRule = new ActivityTestRule<AddTextDraftPage>(AddTextDraftPage.class);
    private AddTextDraftPage addTextDraftPage = null;

    @Before
    public void setUp() throws Exception {
        //add timer for all tests
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            addTextDraftPage = activityTestRule.getActivity();
            //check the view is visible
            onView(ViewMatchers.withId(R.id.AddTextDraftPage)).check(matches(isDisplayed()));
            //check edit texts visible
            onView(withId(R.id.TitleTextD)).check(matches(isDisplayed()));
            onView(withId(R.id.SubjtextD)).check(matches(isDisplayed()));
            //check the button visible
            onView(withId(R.id.ConfirmTextDraft)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

    @Test
    public void EmptyFields() {
        //leave all fields empty
        onView(withId(R.id.TitleTextD)).perform(typeText(""));
        onView(withId(R.id.SubjtextD)).perform(typeText(""));
        //click button
        onView(withId(R.id.ConfirmTextDraft)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
    }

    @Test
    public void OneEmptyField() {
        //enter title
        onView(withId(R.id.TitleTextD)).perform(typeText("First Draft"));
        //close keyboard
        closeSoftKeyboard();
        //leave subj empty
        onView(withId(R.id.SubjtextD)).perform(typeText(""));
        //click button
        onView(withId(R.id.ConfirmTextDraft)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
    }

   /* @Test
    public void addTextFail() {
        onView(withId(R.id.TitleTextD)).perform(typeText("First Draft"));
        onView(withId(R.id.SubjtextD)).perform(typeText("Test The First Draft"));
        onView(withId(R.id.ConfirmTextDraft)).perform(click());
        // check toast visibility
        onView(withText("Not Saved"))
                .inRoot(new ToastMatcher())
                    .check(matches(isDisplayed()));
    }*/

    @Test
    public void addTextSuccess() {
        //enter title
        onView(withId(R.id.TitleTextD)).perform(typeText("Test Text Note"));
        //close keyboard
        closeSoftKeyboard();
        //enter subject
        onView(withId(R.id.SubjtextD)).perform(typeText("Some content for testing purposes"));
        //close keyboard
        closeSoftKeyboard();
        //add timer for all tests
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //click button
            onView(withId(R.id.ConfirmTextDraft)).perform(click());
            // check toast visibility
            onView(withText(R.string.TDSavedSuccess))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.TDSavedSuccess)));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }

    @After
    public void tearDown() throws Exception {
        addTextDraftPage = null;
    }
}