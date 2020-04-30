package com.ksu.serene.B.C_Profile;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.provider.MediaStore;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.profile.Editprofile;

import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.ksu.serene.ImageViewMatcher.hasDrawable;

import org.hamcrest.CustomMatcher;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import androidx.test.espresso.matcher.ViewMatchers;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import androidx.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class EditprofileTestChangeImage {

    @Rule
    public IntentsTestRule intentsTestRule = new IntentsTestRule(Editprofile.class);


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void pickImage () {
        //check image button displayed
        onView(ViewMatchers.withId(R.id.buttonImage)).check(matches(isDisplayed()));
        //click the button
        onView(ViewMatchers.withId(R.id.buttonImage)).perform(click());
        Intent resultData = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK,resultData));
        //add timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the image has drawable
            onView(withId(R.id.imageView)).check(matches(hasDrawable()));
            //click save button
            onView(withId(R.id.save)).perform(click());
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
        //check the patient profile showen
        onView(withId(R.id.PatientProfile)).check(matches(isDisplayed()));
        //add timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(30000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(30000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResourceImage = new ElapsedTimeIdlingResource(30000);
        try {
            IdlingRegistry.getInstance().register(idlingResourceImage);
            //check the image has drawable
            onView(withId(R.id.EditImg)).check(matches(withText("Congrats Your Image Updated")));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResourceImage);
        }

    }
}