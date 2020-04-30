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

import java.util.Calendar;
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
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.ksu.serene.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PatientTextDraftDetailPageTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    //public ActivityTestRule<PatientTextDraftDetailPage> activityTestRule = new ActivityTestRule<PatientTextDraftDetailPage>(PatientTextDraftDetailPage.class);
    private PatientTextDraftDetailPage textDraftDetailPage = null;
    private Calendar calendar = Calendar.getInstance();

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
    public void EditDraftSuccess () {
        //enter title
        onView(withId(R.id.TitleTextD)).perform(replaceText("Update Title "
                +calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH+1)));
        //close keyboard
        closeSoftKeyboard();
        ///leave subj as it is
        //press button
            onView(withId(R.id.SaveChanges)).perform(click());
            // check toast visibility
         onView(withText(R.string.TDUpdatedSuccess))
                 .inRoot(new ToastMatcher())
                 .check(matches(withText(R.string.TDUpdatedSuccess)));
        //check all draft display
            onView(withId(R.id.allDraft)).check(matches(isDisplayed()));
    }

    @Test
    public void EditDraftEmptyFields () {
        //leave all fields empty
        onView(withId(R.id.TitleTextD)).perform(replaceText(""));
        closeSoftKeyboard();
        onView(withId(R.id.SubjtextD)).perform(replaceText(""));
        closeSoftKeyboard();
            //press button
            onView(withId(R.id.SaveChanges)).perform(click());
            // check toast visibility
            onView(withText(R.string.EmptyFields))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.EmptyFields)));
            //check activity still showen
        onView(withId(R.id.PatientTextDraftDetailPage)).check(matches(isDisplayed()));

    }

    @Test
    public void EditDraftOneEmptyField () {
        //enter title
        onView(withId(R.id.TitleTextD)).perform(replaceText("Edit Draft"));
        //close keyboard
        closeSoftKeyboard();
        //leave draft empty
        onView(withId(R.id.SubjtextD)).perform(replaceText(""));
        closeSoftKeyboard();
        //press button
        onView(withId(R.id.SaveChanges)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
        //check activity still showen
        onView(withId(R.id.PatientTextDraftDetailPage)).check(matches(isDisplayed()));
    }

    @Test
    public void backButton () {
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backButton)).check(matches(isClickable()));
        //perform click on back button
        onView(withId(R.id.backButton)).perform(click());
        //check all draft display
        onView(withId(R.id.allDraft)).check(matches(isDisplayed()));
    }

    //@Test
    public void EditDraftFailer () {
        //enter title
        onView(withId(R.id.TitleTextD)).perform(replaceText("Update Title"));
        //close keyboard
        closeSoftKeyboard();
        ///leave subj as it is
        //add timer to disconnect internet connection from simulater
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //press button
            onView(withId(R.id.SaveChanges)).perform(click());
            // check toast visibility
            onView(withText(R.string.TDUpdatedFialed))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.TDUpdatedFialed)));
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