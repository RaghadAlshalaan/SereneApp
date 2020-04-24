package com.ksu.serene.ReportF;

import android.content.Context;
import android.content.Intent;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.controller.main.report.PatientReport;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.Root;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.ksu.serene.ImageViewMatcher.hasDrawable;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)

public class PatientReportTest {

    @Rule
    public ActivityTestRule<PatientReport> activityTestRule = new ActivityTestRule<PatientReport>(PatientReport.class){
        @Override
        protected Intent getActivityIntent() {
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(context, PatientReport.class);
            intent.putExtra(Constants.Keys.DURATION, "2week");
            return intent;
        }
    };
    private PatientReport report = null;
    UiDevice uiDevice;

    @Before
    public void setUp() throws Exception {
        uiDevice =UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        report = activityTestRule.getActivity();
        //add timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(6000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(6000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the activity display
            onView(withId(R.id.PatientReport)).check(matches(isDisplayed()));
            //check the enixiy graph has img
            onView(withId(R.id.graphIcon)).check(matches(hasDrawable()));
            //check the show map button visible
            onView(withId(R.id.heatmap)).check(matches(isDisplayed()));
            //check the menue button visible
            onView(withId(R.id.more)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void backButton () {
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backButton)).check(matches(isClickable()));
        //perform click on back button
        onView(withId(R.id.backButton)).perform(click());
        //check the activity  finish
        assertTrue(activityTestRule.getActivity().isFinishing());
    }

    //@Test
    public void clickMap () {
        //click the button
        onView(withId(R.id.heatmap)).perform(click());
        //chech the heat map is showen
        onView(withId(R.id.map)).check(matches(isDisplayed()));
        //click the back button
        uiDevice.pressBack();
        //check the patient report display
        onView(withId(R.id.PatientReport)).check(matches(isDisplayed()));
    }

    @Test
    public void clickMore () {
        //click more
        onView(withId(R.id.more)).perform(click());
        //will show 2 buttons
        //print button
        onView(withText(R.string.print_report)).check(matches(isDisplayed()));
        //share button
        onView(withText(R.string.share_report)).check(matches(isDisplayed()));
    }

    //@Test
    public void clickMorePrint () {
        //click more
        onView(withId(R.id.more)).perform(click());
        //will show 2 buttons
        //print button
        onView(withText(R.string.print_report)).check(matches(isDisplayed()));
        //click print
        onView(withText(R.string.print_report)).perform(click());
    }

    @Test
    public void clickMoreShare () throws UiObjectNotFoundException {
        //click more
        onView(withId(R.id.more)).perform(click());
        //will show 2 buttons
        //share button
        onView(withText(R.string.share_report)).check(matches(isDisplayed()));
        //click share
        onView(withText(R.string.share_report)).perform(click());
        //click gmail button
        UiObject mGmail = uiDevice.findObject(new UiSelector().text("Gmail"));
        mGmail.click();
    }



    /*public static Matcher<Root> isPopupWindow() {
        return isPlatformPopup();
    }*/
}