package com.ksu.serene.A.Profile;

import static org.junit.Assert.*;
import android.content.Intent;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.profile.AddDoctor;

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
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)


public class AddDoctorTest {
    @Rule
    public ActivityTestRule<AddDoctor> activityTestRule = new ActivityTestRule<AddDoctor>(AddDoctor.class);
    private AddDoctor addDoctor = null;

    @Before
    public void setUp() throws Exception {
        addDoctor = activityTestRule.getActivity();
        //activityTestRule.launchActivity(new Intent());
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
            onView(withId(R.id.AddDoctor)).check(matches(isDisplayed()));
            //check the button visible
            onView(withId(R.id.confirm)).check(matches(isDisplayed()));
            //check edit texts visible
            //email
            onView(withId(R.id.emailET)).check(matches(isDisplayed()));
            //name
            onView(withId(R.id.nameET)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void EmailEmpty () {
        //leave the email empty
        onView(withId(R.id.emailET)).perform(typeText(""));
        //close Keyboard
        closeSoftKeyboard();
        //enter valid name
        onView(withId(R.id.nameET)).perform(typeText("Dr.Al"));
        //close Keyboard
        closeSoftKeyboard();
        //click the button
        onView(withId(R.id.confirm)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
        //check activity still showen
        assertFalse(activityTestRule.getActivity().isFinishing());
        onView(withId(R.id.AddDoctor)).check(matches(isDisplayed()));
    }

    //@Test
    public void NameEmpty () {
        //enter valid email
        onView(withId(R.id.emailET)).perform(typeText("user@hotmail.com"));
        //close Keyboard
        closeSoftKeyboard();
        //leave the name empty
        onView(withId(R.id.nameET)).perform(typeText(""));
        //close Keyboard
        closeSoftKeyboard();
        //click the button
        onView(withId(R.id.confirm)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
        //check activity still showen
        assertFalse(activityTestRule.getActivity().isFinishing());
        onView(withId(R.id.AddDoctor)).check(matches(isDisplayed()));
    }

    @Test
    public void allEmpty () {
        //leave the email empty
        onView(withId(R.id.emailET)).perform(typeText(""));
        //close Keyboard
        closeSoftKeyboard();
        //leave the name empty
        onView(withId(R.id.nameET)).perform(typeText(""));
        //close Keyboard
        closeSoftKeyboard();
        //click the button
        onView(withId(R.id.confirm)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
        //check activity still showen
        assertFalse(activityTestRule.getActivity().isFinishing());
        onView(withId(R.id.AddDoctor)).check(matches(isDisplayed()));
    }

    @Test
    public void NotValidEmail () {
        //enter unvalid name
        onView(withId(R.id.emailET)).perform(typeText("99@com"));
        //close Keyboard
        closeSoftKeyboard();
        //enter valid name
        onView(withId(R.id.nameET)).perform(typeText("Dr.Al"));
        //close Keyboard
        closeSoftKeyboard();
        //click the button
        onView(withId(R.id.confirm)).perform(click());
        // check toast visibility
        onView(withText(R.string.emailFormat))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.emailFormat)));
        //check activity still showen
        assertFalse(activityTestRule.getActivity().isFinishing());
        onView(withId(R.id.AddDoctor)).check(matches(isDisplayed()));
    }

    @Test
    public void NotValidName () {
        //enter unvalid name
        onView(withId(R.id.emailET)).perform(typeText("Ahmed@outlok.com"));
        //close Keyboard
        closeSoftKeyboard();
        //enter valid name
        onView(withId(R.id.nameET)).perform(typeText("Dr%Al"));
        //close Keyboard
        closeSoftKeyboard();
        //click the button
        onView(withId(R.id.confirm)).perform(click());
        // check toast visibility
        onView(withText(R.string.nameFormat))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.nameFormat)));
        //check activity still showen
        assertFalse(activityTestRule.getActivity().isFinishing());
        onView(withId(R.id.AddDoctor)).check(matches(isDisplayed()));
    }

    @Test
    public void UserEmail () {
        //enter valid email that same as patient email
        onView(withId(R.id.emailET)).perform(typeText("reemasadhan@gmail.com"));
        //close Keyboard
        closeSoftKeyboard();
        //enter valid name
        onView(withId(R.id.nameET)).perform(typeText("Dr.Al"));
        //close Keyboard
        closeSoftKeyboard();
        //click the button
        onView(withId(R.id.confirm)).perform(click());
        // check toast visibility
        onView(withText(R.string.PatientEmail))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.PatientEmail)));
        //check activity still showen
        assertFalse(activityTestRule.getActivity().isFinishing());
        onView(withId(R.id.AddDoctor)).check(matches(isDisplayed()));
    }

    @Test
    public void addDoctorSuccess () {
        //enter valid email
        onView(withId(R.id.emailET)).perform(typeText("lama449@gmail.com"));
        //close Keyboard
        closeSoftKeyboard();
        //enter valid name
        onView(withId(R.id.nameET)).perform(typeText("Ahmed"));
        //close Keyboard
        closeSoftKeyboard();
        //click the button
        onView(withId(R.id.confirm)).perform(click());
        // check toast visibility
        onView(withText(R.string.AddDocSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.AddDocSuccess)));
        //check the profile is displayed
        onView(withId(R.id.PatientProfile)).check(matches(isDisplayed()));
    }

    @Test
    public void backButton () {
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backButton)).check(matches(isClickable()));
        //perform click on back button
        onView(withId(R.id.backButton)).perform(click());
        assertTrue(addDoctor.isFinishing());
    }

    @After
    public void tearDown() throws Exception {
        addDoctor = null;
    }
}