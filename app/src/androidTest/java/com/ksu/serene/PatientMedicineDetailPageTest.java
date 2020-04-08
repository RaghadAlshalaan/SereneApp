package com.ksu.serene;

import android.content.Intent;

import com.ksu.serene.controller.main.calendar.PatientMedicineDetailPage;

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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
@RunWith(AndroidJUnit4.class)
public class PatientMedicineDetailPageTest {

    @Rule
    public ActivityTestRule<PatientMedicineDetailPage> activityTestRule = new ActivityTestRule<PatientMedicineDetailPage>(PatientMedicineDetailPage.class);
    private PatientMedicineDetailPage medicineDetailPage = null;

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
            //medicineDetailPage = activityTestRule.getActivity();
            activityTestRule.launchActivity(new Intent());
            onView(withId(R.id.PatientMedicineDetailPage)).check(matches(isDisplayed()));
            //check button visible
            onView(withId(R.id.DeleteMedicine)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void DeleteCancle () {
        //TODO update the med info
        //check for med name
        //onView(withId(R.id.nameET)).check(matches(withText("First Med")));
        onView(withId(R.id.nameET)).check(matches(isDisplayed()));
        //check for med dates
        //onView(withId(R.id.MFromDays)).check(matches(withText("23/11/2021")));
        onView(withId(R.id.MFromDays)).check(matches(isDisplayed()));
        //onView(withId(R.id.MTillDays)).check(matches(withText("25/11/2021")));
        onView(withId(R.id.MTillDays)).check(matches(isDisplayed()));
        //check for med time
        //onView(withId(R.id.MTime)).check(matches(withText("14 : 03")));
        onView(withId(R.id.MTime)).check(matches(isDisplayed()));
        //check for med dose
        //onView(withId(R.id.MDose)).check(matches(withText("2")));
        onView(withId(R.id.MDose)).check(matches(isDisplayed()));
        //check when click button
        onView(withId(R.id.DeleteMedicine)).perform(click());
        //check the dialog appear
        onView(withText(R.string.DeleteMessageMed))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click the cancel button
        onView(withText(R.string.DeleteCancleMed)).perform(click());
        //nothing happened
    }

    @Test
    public void DeleteOK () {
        //TODO update the med info
        //check for med name
        //onView(withId(R.id.nameET)).check(matches(withText("First Med")));
        onView(withId(R.id.nameET)).check(matches(isDisplayed()));
        //check for med dates
        //onView(withId(R.id.MFromDays)).check(matches(withText("23/11/2021")));
        onView(withId(R.id.MFromDays)).check(matches(isDisplayed()));
        //onView(withId(R.id.MTillDays)).check(matches(withText("25/11/2021")));
        onView(withId(R.id.MTillDays)).check(matches(isDisplayed()));
        //check for med time
        //onView(withId(R.id.MTime)).check(matches(withText("14 : 03")));
        onView(withId(R.id.MTime)).check(matches(isDisplayed()));
        //check for med dose
        //onView(withId(R.id.MDose)).check(matches(withText("2")));
        onView(withId(R.id.MDose)).check(matches(isDisplayed()));
        //check when click button
        onView(withId(R.id.DeleteMedicine)).perform(click());
        //check the dialog appear
        onView(withText(R.string.DeleteMessageMed))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click the delete button
        onView(withText(R.string.DeleteOKMed)).perform(click());
        // check toast visibility
        onView(withText(R.string.MedDeletedSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.MedDeletedSuccess)));
        //onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        medicineDetailPage = null;
    }

}