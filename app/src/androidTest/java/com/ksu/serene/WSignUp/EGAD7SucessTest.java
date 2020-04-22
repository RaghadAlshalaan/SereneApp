package com.ksu.serene.WSignUp;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.signup.GAD7;
import com.ksu.serene.controller.signup.Questionnairs;

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
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)

public class EGAD7SucessTest {

    @Rule
    public ActivityTestRule<Questionnairs> activityTestRule = new ActivityTestRule<Questionnairs>(Questionnairs.class);

    @Before
    public void setUp() throws Exception {
        FragmentTransaction fragmentTransaction = activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.Questionnairs, new GAD7());
        fragmentTransaction.commit();
        getInstrumentation().waitForIdleSync();
        //add timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check the gad activity showen
            onView(withId(R.id.fragmentGad)).check(matches(isDisplayed()));
            //check the groups of radio button available
            //g1
            onView(withId(R.id.radio1)).check(matches(isDisplayed()));
            //g2
            onView(withId(R.id.radio2)).check(matches(isDisplayed()));
            //g3
            onView(withId(R.id.radio3)).check(matches(isDisplayed()));
            //g4
            onView(withId(R.id.radio4)).check(matches(isDisplayed()));
            //g5
            onView(withId(R.id.radio5)).check(matches(isDisplayed()));
            //g6
            onView(withId(R.id.radio6)).check(matches(isDisplayed()));
            //g7
            onView(withId(R.id.radio7)).check(matches(isDisplayed()));
            //check the next button visible
            onView(allOf(withId(R.id.nextBtn), isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void AllFieldFill () {
        //group 1 all not checked
        onView(withId(R.id.radioButton11)).perform(click());
        onView(withId(R.id.radioButton11)).check(matches(isChecked()));//radio 1
        onView(withId(R.id.radioButton12)).check(matches(isNotChecked()));//radio 2
        onView(withId(R.id.radioButton13)).check(matches(isNotChecked()));//radio 3
        onView(withId(R.id.radioButton14)).check(matches(isNotChecked()));//radio 4
        //check for other groups from 2-7
        //Group2
        onView(withId(R.id.radioButton21)).perform(click());
        onView(withId(R.id.radioButton21)).check(matches(isChecked()));//radio 1
        onView(withId(R.id.radioButton22)).check(matches(isNotChecked()));//radio 2
        onView(withId(R.id.radioButton23)).check(matches(isNotChecked()));//radio 3
        onView(withId(R.id.radioButton24)).check(matches(isNotChecked()));//radio 4
        //Group3
        onView(withId(R.id.radioButton31)).perform(click());//radio 1
        onView(withId(R.id.radioButton31)).check(matches(isChecked()));//radio 1
        onView(withId(R.id.radioButton32)).check(matches(isNotChecked()));//radio 2
        onView(withId(R.id.radioButton33)).check(matches(isNotChecked()));//radio 3
        onView(withId(R.id.radioButton34)).check(matches(isNotChecked()));//radio 4
        //Group4
        onView(withId(R.id.radioButton41)).perform(click());//radio 1
        onView(withId(R.id.radioButton41)).check(matches(isChecked()));//radio 1
        onView(withId(R.id.radioButton42)).check(matches(isNotChecked()));//radio 2
        onView(withId(R.id.radioButton43)).check(matches(isNotChecked()));//radio 3
        onView(withId(R.id.radioButton44)).check(matches(isNotChecked()));//radio 4
        //Group5
        onView(withId(R.id.radioButton51)).perform(click());//radio 1
        onView(withId(R.id.radioButton51)).check(matches(isChecked()));//radio 1
        onView(withId(R.id.radioButton52)).check(matches(isNotChecked()));//radio 2
        onView(withId(R.id.radioButton53)).check(matches(isNotChecked()));//radio 3
        onView(withId(R.id.radioButton54)).check(matches(isNotChecked()));//radio 4
        //Group6
        onView(withId(R.id.radioButton61)).perform(click());//radio 1
        onView(withId(R.id.radioButton61)).check(matches(isChecked()));//radio 1
        onView(withId(R.id.radioButton62)).check(matches(isNotChecked()));//radio 2
        onView(withId(R.id.radioButton63)).check(matches(isNotChecked()));//radio 3
        onView(withId(R.id.radioButton64)).check(matches(isNotChecked()));//radio 4
        //Group7
        onView(withId(R.id.radioButton71)).perform(click());//radio 1
        onView(withId(R.id.radioButton71)).check(matches(isChecked()));//radio 1
        onView(withId(R.id.radioButton72)).check(matches(isNotChecked()));//radio 2
        onView(withId(R.id.radioButton73)).check(matches(isNotChecked()));//radio 3
        onView(withId(R.id.radioButton74)).check(matches(isNotChecked()));//radio 4
        //click button
        onView(allOf(withId(R.id.nextBtn), isDisplayed())).perform(click());
        // check toast visibility
        onView(withText(R.string.GADSuccess))
                .inRoot(new ToastMatcher()).check(matches(withText(R.string.GADSuccess)));
        onView(withId(R.id.FitbitConnection)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
    }
}