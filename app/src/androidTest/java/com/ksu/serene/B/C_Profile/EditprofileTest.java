package com.ksu.serene.B.C_Profile;

import android.content.Intent;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.profile.Editprofile;

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

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class EditprofileTest {

    @Rule
    public ActivityTestRule<Editprofile> activityTestRule = new ActivityTestRule<Editprofile>(Editprofile.class);
    private Editprofile editprofile = null;

    @Before
    public void setUp() throws Throwable {
        editprofile = activityTestRule.getActivity();
        //add timer so each toast for each test
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //activityTestRule.launchActivity(new Intent());
            onView(ViewMatchers.withId(R.id.Editprofile)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void updateNameSuccess () {
        //check the display name as expected
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        //check the old pass empty
        onView(withId(R.id.oldPassword)).check(matches(withText("")));
        //check the new pass empty
        onView(withId(R.id.newPassword)).check(matches(withText("")));
        //check the confirm new pass empty
        onView(withId(R.id.reNewPassword)).check(matches(withText("")));
        //change name to new valid name
        onView(withId(R.id.username)).perform(replaceText("SerenePatient"));
        //click the button
        onView(withId(R.id.save)).perform(click());
        // check toast visibility
        onView(withText(R.string.UpdateNameSuccess))
                .inRoot(new ToastMatcher()).check(matches(withText(R.string.UpdateNameSuccess)));
        onView(withId(R.id.PatientProfile)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        editprofile = null;
    }

}