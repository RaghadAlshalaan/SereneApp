package com.ksu.serene.WSignUp;
import com.ksu.serene.R;
import com.ksu.serene.controller.signup.Questionnairs;
import com.ksu.serene.ToastMatcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)

public class CSociodemoFailedTest {

    @Rule
    public ActivityTestRule<Questionnairs> activityTestRule = new ActivityTestRule<Questionnairs>(Questionnairs.class);

    @Before
    public void setUp() throws Exception {
        activityTestRule.getActivity().getSupportFragmentManager().beginTransaction();
        //check the activity visible
        onView(withId(R.id.socio)).check(matches(isDisplayed()));
        onView(withId(R.id.a1)).check(matches(isDisplayed()));//age
        onView(withId(R.id.a2)).check(matches(isDisplayed()));//height
        onView(withId(R.id.a3)).check(matches(isDisplayed()));//weight
        onView(withId(R.id.a4)).check(matches(isDisplayed()));//monthly income
        onView(withId(R.id.a8)).check(matches(isDisplayed()));//chronic disease
        //check the next button visible
        onView(withId(R.id.nextBtn)).check(matches(isDisplayed()));
    }

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
                .inRoot(new ToastMatcher()).check(matches(withText(R.string.EmptyFields)));
       //check the activity still showen
        onView(withId(R.id.socio)).check(matches(isDisplayed()));
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
                .inRoot(new ToastMatcher()).check(matches(withText(R.string.NotValidAge)));
        //check the activity still showen
        onView(withId(R.id.socio)).check(matches(isDisplayed()));
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
                .inRoot(new ToastMatcher()).check(matches(withText(R.string.NotValidAge)));
        //check the activity still showen
        onView(withId(R.id.socio)).check(matches(isDisplayed()));
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
                .inRoot(new ToastMatcher()).check(matches(withText(R.string.NotValidHeight)));
        //check the activity still showen
        onView(withId(R.id.socio)).check(matches(isDisplayed()));
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
                .inRoot(new ToastMatcher()).check(matches(withText(R.string.NotValidHeight)));
        //check the activity still showen
        onView(withId(R.id.socio)).check(matches(isDisplayed()));
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
        //check the activity still showen
        onView(withId(R.id.socio)).check(matches(isDisplayed()));
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
        //check the activity still showen
        onView(withId(R.id.socio)).check(matches(isDisplayed()));
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
        //check the activity still showen
        onView(withId(R.id.socio)).check(matches(isDisplayed()));
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
        //check the activity still showen
        onView(withId(R.id.socio)).check(matches(isDisplayed()));
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
        //check the activity still showen
        onView(withId(R.id.socio)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {

    }

    /*@Test
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


}