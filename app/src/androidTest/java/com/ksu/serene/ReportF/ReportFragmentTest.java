package com.ksu.serene.ReportF;

import android.widget.DatePicker;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class ReportFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    //private Calendar current = Calendar.getInstance();

    @Before
    public void setUp() throws Exception {
        onView(withId(R.id.navigation_report)).perform(click());
        //add tiemr
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the activity is visible
            onView(withId(R.id.ReportF)).check(matches(isDisplayed()));
            //check for buttons visible
            //radio button
            onView(withId(R.id.radioButton1)).check(matches(isDisplayed()));
            onView(withId(R.id.radioButton2)).check(matches(isDisplayed()));
            onView(withId(R.id.radioButton3)).check(matches(isDisplayed()));
            //generate button
            onView(withId(R.id.generate_report_btn)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void checkSpecificPeriodShowsDateB() {
        //click on specific option
        onView(withId(R.id.radioButton3)).perform(click());
        //check that the option selected
        onView(withId(R.id.radioButton3)).check(matches(isChecked()));
        //check the dates button appears
        onView(withId(R.id.start)).check(matches(isDisplayed()));
        onView(withId(R.id.end)).check(matches(isDisplayed()));
    }

    @Test
    public void lastTwoWeekSelected() {
        //click on last two option
        onView(withId(R.id.radioButton1)).perform(click());
        //check that the option selected
        onView(withId(R.id.radioButton1)).check(matches(isChecked()));
        //click the generate report button
        onView(withId(R.id.generate_report_btn)).perform(click());
        //check the patient page appears button appears
        onView(withId(R.id.PatientReport)).check(matches(isDisplayed()));
    }

   //@Test
    public void lastMonthSelected() {
        //click on last month option
        onView(withId(R.id.radioButton2)).perform(click());
        //check that the option selected
        onView(withId(R.id.radioButton2)).check(matches(isChecked()));
        //click the generate report button
        onView(withId(R.id.generate_report_btn)).perform(click());
        //check the patient page appears button appears
        onView(withId(R.id.PatientReport)).check(matches(isDisplayed()));
    }

    //TODO check for all condition if specific date
    //TODO test the toast msg
    //@Test
    public void SpecificPeriodEndClickBStart() {
        //click on specific option
        onView(withId(R.id.radioButton3)).perform(click());
        //check that the option selected
        onView(withId(R.id.radioButton3)).check(matches(isChecked()));
        //check the dates button appears
        onView(withId(R.id.start)).check(matches(isDisplayed()));
        onView(withId(R.id.end)).check(matches(isDisplayed()));
        //click the end button
        onView(withId(R.id.end)).perform(click());
        //check the generate report still showen
        onView(withId(R.id.ReportF)).check(matches(isDisplayed()));
        //check the patient report not showen
        //onView(withId(R.id.PatientReport)).check(matches(isDisplayed()));
    }
    //TODO test the toast msg
    @Test
    public void SpecificPeriodNoDatesSet() {
        //click on specific option
        onView(withId(R.id.radioButton3)).perform(click());
        //check that the option selected
        onView(withId(R.id.radioButton3)).check(matches(isChecked()));
        //check the dates button appears
        onView(withId(R.id.start)).check(matches(isDisplayed()));
        onView(withId(R.id.end)).check(matches(isDisplayed()));
        //click the generate button button
        onView(withId(R.id.generate_report_btn)).perform(click());
        //check the generate report still showen
        onView(withId(R.id.ReportF)).check(matches(isDisplayed()));
        //check the patient report not showen
        //onView(withId(R.id.PatientReport)).check(matches(isDisplayed()));
    }
    //TODO test when select specific date and the start date click select only should no enable to go to patient report
    @Test
    public void SpecificPeriodStartDateSelect() {
        //click on specific option
        onView(withId(R.id.radioButton3)).perform(click());
        //check that the option selected
        onView(withId(R.id.radioButton3)).check(matches(isChecked()));
        //check the dates button appears
        onView(withId(R.id.start)).check(matches(isDisplayed()));
        onView(withId(R.id.end)).check(matches(isDisplayed()));
        //click the end button
        onView(withId(R.id.start)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 4, 12));
        //click ok
        onView(withText("OK")).perform(click());
        //check the start date text contains the exact date
        onView(withId(R.id.start_date)).check(matches(withText("12/04/2020")));
        //click the generate button button
        onView(withId(R.id.generate_report_btn)).perform(click());
        //check the generate report still showen
        onView(withId(R.id.ReportF)).check(matches(isDisplayed()));
        //check the patient report not showen
        //onView(withId(R.id.PatientReport)).check(matches(isDisplayed()));
    }
    //Test all condition when start date dialog click cancle
    @Test
    public void SpecificPeriodStartDateCancle() {
        //click on specific option
        onView(withId(R.id.radioButton3)).perform(click());
        //check that the option selected
        onView(withId(R.id.radioButton3)).check(matches(isChecked()));
        //check the dates button appears
        onView(withId(R.id.start)).check(matches(isDisplayed()));
        onView(withId(R.id.end)).check(matches(isDisplayed()));
        //click the end button
        onView(withId(R.id.start)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 4, 12));
        //click cancle
        onView(withText("CANCEL")).perform(click());
        //click the generate button button
        onView(withId(R.id.generate_report_btn)).perform(click());
        //check the generate report still showen
        onView(withId(R.id.ReportF)).check(matches(isDisplayed()));
        //check the patient report not showen
        //onView(withId(R.id.PatientReport)).check(matches(isDisplayed()));
    }
    @Test
    public void SpecificPeriodStartDateCancleClickEnd() {
        //click on specific option
        onView(withId(R.id.radioButton3)).perform(click());
        //check that the option selected
        onView(withId(R.id.radioButton3)).check(matches(isChecked()));
        //check the dates button appears
        onView(withId(R.id.start)).check(matches(isDisplayed()));
        onView(withId(R.id.end)).check(matches(isDisplayed()));
        //click the end button
        onView(withId(R.id.start)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 4, 12));
        //click cancle
        onView(withText("CANCEL")).perform(click());
        //click the end
        onView(withId(R.id.end)).perform(click());
        //click the generate button button
        onView(withId(R.id.generate_report_btn)).perform(click());
        //check the generate report still showen
        onView(withId(R.id.ReportF)).check(matches(isDisplayed()));
        //check the patient report not showen
        //onView(withId(R.id.PatientReport)).check(matches(isDisplayed()));
    }
    //Test all condition after select start date
    @Test
    public void SpecificPeriodStartEndDatesSelectNotCurrent() {
        //click on specific option
        onView(withId(R.id.radioButton3)).perform(click());
        //check that the option selected
        onView(withId(R.id.radioButton3)).check(matches(isChecked()));
        //check the dates button appears
        onView(withId(R.id.start)).check(matches(isDisplayed()));
        onView(withId(R.id.end)).check(matches(isDisplayed()));
        //click the end button
        onView(withId(R.id.start)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 4, 12));
        //click ok
        onView(withText("OK")).perform(click());
        //check the start date text contains the exact date
        onView(withId(R.id.start_date)).check(matches(withText("12/04/2020")));
        //click the end
        onView(withId(R.id.end)).perform(click());
        //choose same date or after no mater
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 4, 13));
        //click ok
        onView(withText("OK")).perform(click());
        //check the start date text contains the exact date
        onView(withId(R.id.end_date)).check(matches(withText("13/04/2020")));
        //click the generate button button
        //onView(withId(R.id.generate_report_btn)).perform(click());crash
        //check the generate report still showen
        //onView(withId(R.id.ReportF)).check(matches(isDisplayed()));
        //check the patient report not showen
        //onView(withId(R.id.PatientReport)).check(matches(isDisplayed()));
    }
    @Test
    public void SpecificPeriodEndDatesSelectDateCancle() {
        //click on specific option
        onView(withId(R.id.radioButton3)).perform(click());
        //check that the option selected
        onView(withId(R.id.radioButton3)).check(matches(isChecked()));
        //check the dates button appears
        onView(withId(R.id.start)).check(matches(isDisplayed()));
        onView(withId(R.id.end)).check(matches(isDisplayed()));
        //click the end button
        onView(withId(R.id.start)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 4, 12));
        //click ok
        onView(withText("OK")).perform(click());
        //check the start date text contains the exact date
        onView(withId(R.id.start_date)).check(matches(withText("12/04/2020")));
        //click the end
        onView(withId(R.id.end)).perform(click());
        //choose same date or after no mater
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 4, 13));
        //click ok
        onView(withText("CANCEL")).perform(click());
        //click the generate button button
        onView(withId(R.id.generate_report_btn)).perform(click());
        //check the generate report still showen
        onView(withId(R.id.ReportF)).check(matches(isDisplayed()));
        //check the patient report not showen
        //onView(withId(R.id.PatientReport)).check(matches(isDisplayed()));
    }
    //here check when start date choose the current date
    @Test
    public void SpecificPeriodStartEndDatesSelectCurrent() {
        //click on specific option
        onView(withId(R.id.radioButton3)).perform(click());
        //check that the option selected
        onView(withId(R.id.radioButton3)).check(matches(isChecked()));
        //check the dates button appears
        onView(withId(R.id.start)).check(matches(isDisplayed()));
        onView(withId(R.id.end)).check(matches(isDisplayed()));
        //click the end button
        onView(withId(R.id.start)).perform(click());
        //choose date
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 4, 13));
        //onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DAY_OF_MONTH)));
        //click ok
        onView(withText("OK")).perform(click());
        //check the start date text contains the exact date
        onView(withId(R.id.start_date)).check(matches(withText("13/04/2020")));
        //onView(withId(R.id.start_date)).check(matches(withText(current.get(Calendar.DAY_OF_MONTH)+"/"+current.get(Calendar.MONTH)+"/"+current.get(Calendar.YEAR))));
        //click the end
        onView(withId(R.id.end)).perform(click());
        onView(withId(R.id.end_date)).check(matches(withText("13/04/2020")));
        //check the end date text contains the exact date
        //onView(withId(R.id.end_date)).check(matches(withText(current.get(Calendar.DAY_OF_MONTH)+"/"+current.get(Calendar.MONTH)+"/"+current.get(Calendar.YEAR))));
        //click the generate button button
        //onView(withId(R.id.generate_report_btn)).perform(click());crash
        //check the generate report still showen
        //onView(withId(R.id.ReportF)).check(matches(isDisplayed()));
        //check the patient report not showen
        //onView(withId(R.id.PatientReport)).check(matches(isDisplayed()));
    }


    @After
    public void tearDown() throws Exception {
    }
}