package com.ksu.serene.ProfileF;

import android.content.Intent;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.profile.PatientProfile;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import androidx.fragment.app.FragmentTransaction;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.ksu.serene.ImageViewMatcher.hasDrawable;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PatientProfileTest {

    //TODO check for change language after added to class
    @Rule
    public ActivityTestRule<PatientProfile> activityTestRule = new ActivityTestRule<PatientProfile>(PatientProfile.class);

    @Before
    public void setUp() throws Exception {
        //launch activity
        activityTestRule.launchActivity(new Intent());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the activity is visible
            onView(withId(R.id.PatientProfile)).check(matches(isDisplayed()));
            //check the buttons visible
            //edit profile button
            onView(withId(R.id.edit_profile_btn)).check(matches(isDisplayed()));
            //edit socio button
            onView(withId(R.id.go_to1)).check(matches(isDisplayed()));
            //add doctor button
            onView(withId(R.id.go_to2)).check(matches(isDisplayed()));
            //log out button
            onView(withId(R.id.log_out_btn)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    //check setting the name and email
    @Test
    public void CheckSetting () {
        //check the name
        onView(withId(R.id.full_name)).check(matches(isDisplayed()));//.check(matches(withText("userSerene")));
        //check the email
        onView(withId(R.id.emailET)).check(matches(withText("lama-almarshad@hotmail.com")));
        //TODO check for image that exactly as apear
        //check the image
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView)).check(matches(hasDrawable()));
    }

    @Test
    public void backButton () {
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backButton)).check(matches(isClickable()));
        //perform click on back button
        onView(withId(R.id.backButton)).perform(click());
    }

    //check the alert dialog appear when email not verified
    //@Test
    public void AlertENVSuccess () {
        //check the alert dialog
        onView(withId(R.id.alert)).check(matches(isDisplayed()));
        //check the message
        onView(withId(R.id.alarmMsg)).check(matches(withText(R.string.VervEmail)));
        //TODO CHECK WHEN CLICK ON THE DIALOG EMAIL WILL BE SENT FOR VERVICATION AND TOAST APEAR INDICATE IF EMAIL SENT OR NOT
        //click dialog to sent email verfication
        onView(withId(R.id.alert)).perform(click());
        // check toast visibility
        onView(withText(R.string.VervEmailSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.VervEmailSuccess)));

    }

    /*@Test
    public void AlertENVFail () {
        //check the alert dialog
        onView(withId(R.id.alert)).check(matches(isDisplayed()));
        //check the message
        onView(withId(R.id.alarmMsg)).check(matches(withText("To help ensure your account's security please verify your email.")));
        //TODO CHECK WHEN CLICK ON THE DIALOG EMAIL WILL BE SENT FOR VERVICATION AND TOAST APEAR INDICATE IF EMAIL SENT OR NOT
        //click dialog to sent email verfication
        onView(withId(R.id.alert)).perform(click());
        // check toast visibility
        onView(withText("Email Did Not Sent, Try Again"))
                .inRoot(new ToastMatcher())
                .check(matches(withText("Email Did Not Sent, Try Again")));

    }*/

    //check when click the edit profile will go to edit profile page
    @Test
    public void EditProfile () {
        //click the button
        onView(withId(R.id.edit_profile_btn)).perform(click());
        //check the edit profile page apear
        onView(withId(R.id.Editprofile)).check(matches(isDisplayed()));
    }

    //check when click the edit socio will go to edit profile page
    @Test
    public void EditSocio () {
        //click the button
        onView(withId(R.id.go_to1)).perform(click());
        //check the edit socio page appear
        onView(withId(R.id.EditSocio)).check(matches(isDisplayed()));
    }

    //check when click the doctor and user doesn't have doctor go to add doctor page
    //@Test
    public void AddDoctor () {
        //click the button
        onView(withId(R.id.go_to2)).perform(click());
        //check the add doctor page apear
        //onView(withId(R.id.AddDoctor)).check(matches(isDisplayed()));
    }

    //check when click the doctor and user have doctor go to doctor page details
    //@Test
    public void ViewDoctor () {
        //check the doctor name will viewd
        //onView(withId(R.id.doctor_text2)).check(matches(withText("")));
        onView(withId(R.id.doctor_text2)).check(matches(isDisplayed()));
        //click the button
        onView(withId(R.id.go_to2)).perform(click());
        //check the edit profile page apear
        onView(withId(R.id.MyDoctor)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
    }
}