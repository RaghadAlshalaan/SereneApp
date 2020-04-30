package com.ksu.serene.A.B_Draft;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.drafts.AddTextDraftPage;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
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
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AddTextDraftPageTest {

    @Rule
    public ActivityTestRule<AddTextDraftPage> activityTestRule = new ActivityTestRule<AddTextDraftPage>(AddTextDraftPage.class);
    private AddTextDraftPage addTextDraftPage = null;
    private Calendar current = Calendar.getInstance();

    @Before
    public void setUp() throws Exception {
        addTextDraftPage = activityTestRule.getActivity();
        //add timer for all tests
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the view is visible
            onView(withId(R.id.AddTextDraftPage)).check(matches(isDisplayed()));
            //check edit texts visible
            onView(withId(R.id.TitleTextD)).check(matches(isDisplayed()));
            onView(withId(R.id.SubjtextD)).check(matches(isDisplayed()));
            //check the button visible
            onView(withId(R.id.ConfirmTextDraft)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void addTextSuccess() {
        //enter title
        onView(withId(R.id.TitleTextD)).perform(typeText("Test Text Note"));
        //close keyboard
        closeSoftKeyboard();
        //enter subject
        onView(withId(R.id.SubjtextD)).perform(typeText("Some content for testing purposes"
                +"\n"+ current.get(Calendar.DAY_OF_MONTH) + "/" + current.get(Calendar.MONTH)));
        //close keyboard
        closeSoftKeyboard();
            //click button
        onView(withId(R.id.ConfirmTextDraft)).perform(click());
        // check toast visibility
        onView(withText(R.string.TDSavedSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.TDSavedSuccess)));
            //check activity is finish
        assertTrue(activityTestRule.getActivity().isFinishing());
    }

    @After
    public void tearDown() throws Exception {
        addTextDraftPage = null;
    }
}