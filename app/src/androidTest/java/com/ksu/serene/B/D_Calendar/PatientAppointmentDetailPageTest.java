package com.ksu.serene.B.D_Calendar;
import android.content.Context;
import android.content.Intent;
import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.calendar.PatientAppointmentDetailPage;
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
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)

public class PatientAppointmentDetailPageTest {
    @Rule
    public ActivityTestRule<PatientAppointmentDetailPage> activityTestRule = new ActivityTestRule<PatientAppointmentDetailPage>(PatientAppointmentDetailPage.class){
        @Override
        protected Intent getActivityIntent() {
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(context, PatientAppointmentDetailPage.class);
            intent.putExtra("AppointmentID", "d8e25bdf-9b72-42c7-b6a7-c3d2c63860a7");
            //
            return intent;
        }
    };
    private PatientAppointmentDetailPage appointmentDetailPage = null;
    @Before
    public void setUp() throws Exception {
        //appointmentDetailPage = activityTestRule.getActivity();
        //add timer for all tests
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //activityTestRule.launchActivity(new Intent());
            onView(withId(R.id.PatientAppointmentDetailPage)).check(matches(isDisplayed()));
            //check button visible
            onView(withId(R.id.DeleteApp)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void DeleteCancle () {
        //check for app name
        onView(withId(R.id.nameET)).check(matches(isDisplayed()));
        //check for app date
        onView(withId(R.id.MTillDays)).check(matches(isDisplayed()));
        //check for app time
        onView(withId(R.id.MTime)).check(matches(isDisplayed()));
        //check when click button
        onView(withId(R.id.DeleteApp)).perform(click());
        //check the dialog appear
        onView(withText(R.string.DeleteMessageApp))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click the cancel button
        onView(withText(R.string.DeleteCancleApp)).perform(click());
        //nothing happened
        //check the activiy still showen
        assertFalse(activityTestRule.getActivity().isFinishing());
        onView(withId(R.id.PatientAppointmentDetailPage)).check(matches(isDisplayed()));
    }

    @Test
    public void DeleteOK () {
        //check for app name
        onView(withId(R.id.nameET)).check(matches(isDisplayed()));
        //check for app date      onView(withId(R.id.MTillDays)).check(matches(isDisplayed()));
        //check for app time
        onView(withId(R.id.MTime)).check(matches(isDisplayed()));
        //check when click button
        onView(withId(R.id.DeleteApp)).perform(click());
        //check the dialog appear
        onView(withText(R.string.DeleteMessageApp))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //click the delete button
        onView(withText(R.string.DeleteOKApp)).perform(click());
        // check toast visibility
        onView(withText(R.string.AppDeletedSuccess))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.AppDeletedSuccess)));
        //check the activity is finish
        assertTrue(activityTestRule.getActivity().isFinishing());
    }

    //@Test
    public void DeleteOKFailer () {
        //check for app name
        onView(withId(R.id.nameET)).check(matches(isDisplayed()));
        //check for app date      onView(withId(R.id.MTillDays)).check(matches(isDisplayed()));
        //check for app time
        onView(withId(R.id.MTime)).check(matches(isDisplayed()));
        //check when click button
        onView(withId(R.id.DeleteApp)).perform(click());
        //check the dialog appear
        onView(withText(R.string.DeleteMessageApp))
                .inRoot(isDialog()) // <---
                .check(matches(isDisplayed()));
        //add timer to disconnect from internet connection
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //click the delete button
            onView(withText(R.string.DeleteOKApp)).perform(click());
            // check toast visibility
            onView(withText(R.string.AppDeletedFialed))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.AppDeletedFialed)));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @After
    public void tearDown() throws Exception {
        appointmentDetailPage = null;
    }

}