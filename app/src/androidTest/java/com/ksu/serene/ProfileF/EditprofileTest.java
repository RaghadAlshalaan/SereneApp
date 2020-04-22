package com.ksu.serene.ProfileF;

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
    //TODO make test for forget password

    //back button pressed
    @Test
    public void backButton () {
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backButton)).check(matches(isClickable()));
        //perform click on back button
        onView(withId(R.id.backButton)).perform(click());
        assertTrue(activityTestRule.getActivity().isFinishing());
    }

    @Test
    public void shortPass () {
        //check the display name as expected
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        //enter correct past password
        onView(withId(R.id.oldPassword)).perform(typeText("password99"));
        //close keyboard
        closeSoftKeyboard();
        //enter new short pass
        onView(withId(R.id.newPassword)).perform(typeText("pa00"));
        //close keyboard
        closeSoftKeyboard();
        //renter again the new short pass
        onView(withId(R.id.reNewPassword)).perform(typeText("pa00"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.save)).perform(click());
        // check toast visibility
        onView(withText(R.string.passwordChar))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.passwordChar)));
        //check activity still shown
        assertFalse(activityTestRule.getActivity().isFinishing());
    }

    @Test
    public void notMatchPass () {
        //check the display name as expected
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        //enter correct past password
        onView(withId(R.id.oldPassword)).perform(typeText("password99"));
        //close keyboard
        closeSoftKeyboard();
        //enter new pass
        onView(withId(R.id.newPassword)).perform(typeText("serene00"));
        //close keyboard
        closeSoftKeyboard();
        //enter diff new pass
        onView(withId(R.id.reNewPassword)).perform(typeText("passwoord77"));
        //close keyboard
        closeSoftKeyboard();
        //click the save button
        onView(withId(R.id.save)).perform(click());
        // check toast visibility
        onView(withText(R.string.passwordMatch))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.passwordMatch)));
        //check activity still shown
        assertFalse(activityTestRule.getActivity().isFinishing());
    }

    @Test
    public void samePass () {
        //check the display name as expected
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        //enter correct past password
        onView(withId(R.id.oldPassword)).perform(typeText("sereneuser"));
        //close keyboard
        closeSoftKeyboard();
        //enter new pass that same as old
        onView(withId(R.id.newPassword)).perform(typeText("sereneuser"));
        //close keyboard
        closeSoftKeyboard();
        //renter again the new pass that same as old
        onView(withId(R.id.reNewPassword)).perform(typeText("sereneuser"));
        //close keyboard
        closeSoftKeyboard();
        //click the button
        onView(withId(R.id.save)).perform(click());
        // check toast visibility
        onView(withText(R.string.passwordSame))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.passwordSame)));
        //check activity still shown
        assertFalse(activityTestRule.getActivity().isFinishing());
    }

    //old pass is wrong pass
    @Test
    public void passUpdateWrong () {
        //check the display name as expected
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        //enter wrong past password
        onView(withId(R.id.oldPassword)).perform(typeText("serene88"));
        //close keyboard
        closeSoftKeyboard();
        //enter new pass
        onView(withId(R.id.newPassword)).perform(typeText("serene99"));
        //close keyboard
        closeSoftKeyboard();
        //reenter new pass
        onView(withId(R.id.reNewPassword)).perform(typeText("serene99"));
        //close keyboard
        closeSoftKeyboard();
        //click the button
        onView(withId(R.id.save)).perform(click());
        // check toast visibility
        onView(withText(R.string.wrongPassword))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.wrongPassword)));
        //check activity still shown
        assertFalse(activityTestRule.getActivity().isFinishing());
    }



    @Test
    public void NameEmpty () {
        //check the display name as expected
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        //change name to empty
        onView(withId(R.id.username)).perform(replaceText(""));
        //click the button
        onView(withId(R.id.save)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyName))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyName)));
        //check activity still shown
        assertFalse(activityTestRule.getActivity().isFinishing());
    }

    @Test
    public void IncorrectNameFormat () {
        //check the display name as expected
        //onView(withId(R.id.username)).check(matches(withText("user")));
        onView(withId(R.id.username)).check(matches(isDisplayed()));
        //change name to wrong format
        onView(withId(R.id.username)).perform(replaceText("34&"));
        //click the button
        onView(withId(R.id.save)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotCorrectName))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotCorrectName)));
        //check activity still shown
        assertFalse(activityTestRule.getActivity().isFinishing());
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
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.UpdateNameSuccess)));
    }

    //@Test
    public void updateNameFail () {
        onView(withId(R.id.username)).check(matches(withText("user")));
        onView(withId(R.id.username)).perform(replaceText("user11"));
        //add timer to disconnect internet connection form simulater
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(withId(R.id.save)).perform(click());
            // check toast visibility
            onView(withText(R.string.UpdateNameFialed))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.UpdateNameFialed)));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @After
    public void tearDown() throws Exception {
        editprofile = null;
    }

}