package com.ksu.serene.SuingUpP;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.signup.Questionnairs;

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
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class SociodemoTest {

    @Rule
    public ActivityTestRule<Questionnairs> activityTestRule = new ActivityTestRule<Questionnairs>(Questionnairs.class);

    @Before
    public void setUp() throws Exception {
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        //add timer
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check edit texts visible
            onView(ViewMatchers.withId(R.id.a1)).check(matches(isDisplayed()));//age
            onView(withId(R.id.a2)).check(matches(isDisplayed()));//height
            onView(withId(R.id.a3)).check(matches(isDisplayed()));//weight
            onView(withId(R.id.a4)).check(matches(isDisplayed()));//monthly income
            onView(withId(R.id.a8)).check(matches(isDisplayed()));//chronic disease
            //check the next button visible
            onView(withId(R.id.nextBtn)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    //TODO should i test also for martial state smoking, and state menus when there is a default value
    //TODO should i test for valid format for age, height, weight and monthly income when the edit text is only for numbers.

    @Test
    public void EmptyFields () {
        //all edit text empty
        onView(withId(R.id.a1)).perform(typeText(""));//age
        onView(withId(R.id.a2)).perform(typeText(""));//height
        onView(withId(R.id.a3)).perform(typeText(""));//weight
        onView(withId(R.id.a4)).perform(typeText(""));//monthly income
        onView(withId(R.id.a8)).perform(typeText(""));//chronic disease
        //click the button
        onView(withId(R.id.nextBtn)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
    }

    @Test
    public void notValidAgeLess () {
        //enter unvalid age
        onView(withId(R.id.a1)).perform(typeText("3"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter valid height
        onView(withId(R.id.a2)).perform(typeText("150"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter valid weight
        onView(withId(R.id.a3)).perform(typeText("50"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.a4)).perform(typeText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.a8)).perform(typeText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //click button
        onView(withId(R.id.nextBtn)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotValidAge))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotValidAge)));
    }

    @Test
    public void notValidAgeMore () {
        //enter unvalid age
        onView(withId(R.id.a1)).perform(typeText("115"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter valid height
        onView(withId(R.id.a2)).perform(typeText("150"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter valid weight
        onView(withId(R.id.a3)).perform(typeText("50"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.a4)).perform(typeText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.a8)).perform(typeText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //click button
        onView(withId(R.id.nextBtn)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotValidAge))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotValidAge)));
    }

    @Test
    public void notValidHeightLess () {
        //enter valid age
        onView(withId(R.id.a1)).perform(typeText("30"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter unvalid height
        onView(withId(R.id.a2)).perform(typeText("40"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter valid weight
        onView(withId(R.id.a3)).perform(typeText("50"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.a4)).perform(typeText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.a8)).perform(typeText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //click button
        onView(withId(R.id.nextBtn)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotValidHeight))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotValidHeight)));
    }

    @Test
    public void notValidHeightMore () {
        //enter valid age
        onView(withId(R.id.a1)).perform(typeText("30"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter unvalid height
        onView(withId(R.id.a2)).perform(typeText("350"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter valid weight
        onView(withId(R.id.a3)).perform(typeText("50"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.a4)).perform(typeText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.a8)).perform(typeText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //click button
        onView(withId(R.id.nextBtn)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotValidHeight))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotValidHeight)));
    }

    @Test
    public void notValidWeightLess () {
        //enter valid age
        onView(withId(R.id.a1)).perform(typeText("30"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter valid height
        onView(withId(R.id.a2)).perform(typeText("140"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter unvalid weight
        onView(withId(R.id.a3)).perform(typeText("10"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.a4)).perform(typeText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.a8)).perform(typeText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //click button
        onView(withId(R.id.nextBtn)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotValidWeight))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotValidWeight)));
    }

    @Test
    public void notValidWeightMore () {
        //enter valid age
        onView(withId(R.id.a1)).perform(typeText("30"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter valid height
        onView(withId(R.id.a2)).perform(typeText("140"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter unvalid weight
        onView(withId(R.id.a3)).perform(typeText("350"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.a4)).perform(typeText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.a8)).perform(typeText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //click button
        onView(withId(R.id.nextBtn)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotValidWeight))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotValidWeight)));
    }

    @Test
    public void notValidMIMore () {
        //enter valid age
        onView(withId(R.id.a1)).perform(typeText("30"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter valid height
        onView(withId(R.id.a2)).perform(typeText("140"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter unvalid weight
        onView(withId(R.id.a3)).perform(typeText("60"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter unvalid monthly income
        onView(withId(R.id.a4)).perform(typeText("100000000"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.a8)).perform(typeText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //click button
        onView(withId(R.id.nextBtn)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotValidMI))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotValidMI)));
    }

    @Test
    public void notValidCDNumber () {
        //enter valid age
        onView(withId(R.id.a1)).perform(typeText("30"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter valid height
        onView(withId(R.id.a2)).perform(typeText("140"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter valid weight
        onView(withId(R.id.a3)).perform(typeText("60"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.a4)).perform(typeText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //reenter unvalid chronic disease
        onView(withId(R.id.a8)).perform(typeText("123"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.nextBtn)).perform(click());
        // check toast visibility
        onView(withText( R.string.NotValidCD))
                .inRoot(new ToastMatcher())
                .check(matches(withText( R.string.NotValidCD)));
    }

    @Test
    public void notValidCDSC () {
        //enter valid age
        onView(withId(R.id.a1)).perform(typeText("30"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter valid height
        onView(withId(R.id.a2)).perform(typeText("140"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter valid weight
        onView(withId(R.id.a3)).perform(typeText("60"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.a4)).perform(typeText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //reenter unvalid chronic disease
        onView(withId(R.id.a8)).perform(typeText("*High pressure**"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.nextBtn)).perform(click());
        // check toast visibility
        onView(withText( R.string.NotValidCD))
                .inRoot(new ToastMatcher())
                .check(matches(withText( R.string.NotValidCD)));
    }
/*
    @Test
    public void socioTestFail () {
        onView(withId(R.id.a1)).perform(typeText("30"));//age
        onView(withId(R.id.a2)).perform(typeText("140"));//height
        onView(withId(R.id.a3)).perform(typeText("60"));//weight
        onView(withId(R.id.a4)).perform(typeText("500"));//monthly income
        onView(withId(R.id.a8)).perform(typeText("No"));//chronic disease
        //TODO should i test also for martial state smoking, and state menus when there is a default value
        onView(withId(R.id.nextBtn)).perform(click());
        // check toast visibility
        onView(withText("Error getting documents: "))
                .inRoot(withDecorView((Matcher<View>) not((Validator) activityTestRule.getActivity().getWindow()
                        .getDecorView()))).check(matches(isDisplayed()));
    }*/


    @After
    public void tearDown() throws Exception {
    }

}