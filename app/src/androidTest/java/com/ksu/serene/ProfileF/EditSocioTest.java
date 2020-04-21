package com.ksu.serene.ProfileF;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.profile.EditSocio;

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
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class EditSocioTest {

    @Rule
    public ActivityTestRule<EditSocio> activityTestRule = new ActivityTestRule<EditSocio>(EditSocio.class);
    private EditSocio editSocio = null;

    @Before
    public void setUp() throws Exception {
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            editSocio = activityTestRule.getActivity();
            onView(ViewMatchers.withId(R.id.EditSocio)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
    //TODO check when failed empty after added to class
    //TODO check for valid age, weight, height and monthly income as in register

    @Test
    public void EmptyFields () {
        //all edit text empty
        onView(withId(R.id.age)).perform(replaceText(""));//age
        onView(withId(R.id.height)).perform(replaceText(""));//height
        onView(withId(R.id.weight)).perform(replaceText(""));//weight
        onView(withId(R.id.income)).perform(replaceText(""));//monthly income
        onView(withId(R.id.chronic)).perform(replaceText(""));//chronic disease
        //TODO should i test also for martial state smoking, and state menus when there is a default value
        //click the button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.EmptyFields))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.EmptyFields)));
    }

    //TODO should i test for valid format for age, height, weight and monthly income when the edit text is only for numbers.

    @Test
    public void notValidAgeLess () {
        //enter unvalid age
        onView(withId(R.id.age)).perform(replaceText("3"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter valid height
        onView(withId(R.id.height)).perform(replaceText("150"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter valid weight
        onView(withId(R.id.weight)).perform(replaceText("50"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.income)).perform(replaceText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.chronic)).perform(replaceText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //TODO should i test also for martial state smoking, and state menus when there is a default value
        //click button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotCorrectAge))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotCorrectAge)));
    }

    @Test
    public void notValidAgeMore () {
        //enter unvalid age
        onView(withId(R.id.age)).perform(replaceText("115"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter valid height
        onView(withId(R.id.height)).perform(replaceText("150"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter valid weight
        onView(withId(R.id.weight)).perform(replaceText("50"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.income)).perform(replaceText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.chronic)).perform(replaceText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //TODO should i test also for martial state smoking, and state menus when there is a default value
        //click button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotCorrectAge))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotCorrectAge)));
    }

    @Test
    public void notValidHeightLess () {
        //enter valid age
        onView(withId(R.id.age)).perform(replaceText("30"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter unvalid height
        onView(withId(R.id.height)).perform(replaceText("40"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter valid weight
        onView(withId(R.id.weight)).perform(replaceText("50"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.income)).perform(replaceText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.chronic)).perform(replaceText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //TODO should i test also for martial state smoking, and state menus when there is a default value
        //click button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotCorrectHeight))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotCorrectHeight)));
    }

    @Test
    public void notValidHeightMore () {
        //enter valid age
        onView(withId(R.id.age)).perform(replaceText("30"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter unvalid height
        onView(withId(R.id.height)).perform(replaceText("350"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter valid weight
        onView(withId(R.id.weight)).perform(replaceText("50"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.income)).perform(replaceText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.chronic)).perform(replaceText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //TODO should i test also for martial state smoking, and state menus when there is a default value
        //click button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotCorrectHeight))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotCorrectHeight)));
    }

    @Test
    public void notValidWeightLess () {
        //enter valid age
        onView(withId(R.id.age)).perform(replaceText("30"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter valid height
        onView(withId(R.id.height)).perform(replaceText("140"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter unvalid weight
        onView(withId(R.id.weight)).perform(replaceText("10"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.income)).perform(replaceText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.chronic)).perform(replaceText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //TODO should i test also for martial state smoking, and state menus when there is a default value
        //click button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotCorrectWeight))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotCorrectWeight)));
    }

    @Test
    public void notValidWeightMore () {
        //enter valid age
        onView(withId(R.id.age)).perform(replaceText("30"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter valid height
        onView(withId(R.id.height)).perform(replaceText("140"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter unvalid weight
        onView(withId(R.id.weight)).perform(replaceText("350"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.income)).perform(replaceText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.chronic)).perform(replaceText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //TODO should i test also for martial state smoking, and state menus when there is a default value
        //click button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotCorrectWeight))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotCorrectWeight)));
    }

    @Test
    public void notValidMIMore () {
        //enter valid age
        onView(withId(R.id.age)).perform(replaceText("30"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter valid height
        onView(withId(R.id.height)).perform(replaceText("140"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter unvalid weight
        onView(withId(R.id.weight)).perform(replaceText("60"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter unvalid monthly income
        onView(withId(R.id.income)).perform(replaceText("50000000"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //enter valid chronic disease
        onView(withId(R.id.chronic)).perform(replaceText("No"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //TODO should i test also for martial state smoking, and state menus when there is a default value
        //click button
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotCorrectMI))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotCorrectMI)));
    }

    @Test
    public void notValidCD () {
        //enter valid age
        onView(withId(R.id.age)).perform(replaceText("30"));//age
        //close keyboard
        closeSoftKeyboard();
        //enter valid height
        onView(withId(R.id.height)).perform(replaceText("140"));//height
        //close keyboard
        closeSoftKeyboard();
        //enter valid weight
        onView(withId(R.id.weight)).perform(replaceText("60"));//weight
        //close keyboard
        closeSoftKeyboard();
        //enter valid monthly income
        onView(withId(R.id.income)).perform(replaceText("500"));//monthly income
        //close keyboard
        closeSoftKeyboard();
        //reenter unvalid chronic disease
        onView(withId(R.id.chronic)).perform(replaceText("123"));//chronic disease
        //close keyboard
        closeSoftKeyboard();
        //TODO should i test also for martial state smoking, and state menus when there is a default value
        onView(withId(R.id.button)).perform(click());
        // check toast visibility
        onView(withText(R.string.NotCorrectCD))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.NotCorrectCD)));
    }

    @Test
    public void updateSocioSucccess () {
        onView(withId(R.id.age)).perform(replaceText("23"));
        closeSoftKeyboard();
        onView(withId(R.id.height)).perform(replaceText("150"));
        closeSoftKeyboard();
        onView(withId(R.id.weight)).perform(replaceText("50"));
        closeSoftKeyboard();
        onView(withId(R.id.income)).perform(replaceText("500"));
        closeSoftKeyboard();
        onView(withId(R.id.chronic)).perform(replaceText("No"));//chronic disease
        closeSoftKeyboard();
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(8000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(8000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(8000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(withId(R.id.button)).perform(click());
            // check toast visibility
            onView(withText(R.string.SocioInfoUpdateSuccess))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.SocioInfoUpdateSuccess)));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void updateSocioFail () {
        onView(withId(R.id.age)).perform(typeText("20"));
        onView(withId(R.id.height)).perform(typeText("150"));
        onView(withId(R.id.weight)).perform(typeText("50"));
        onView(withId(R.id.income)).perform(typeText("500"));
        //add timer to disconnect internet connection from simulater
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            onView(withId(R.id.button)).perform(click());
            // check toast visibility
            onView(withText(R.string.SocioInfoUpdateFialed))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText(R.string.SocioInfoUpdateFialed)));
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
    }

    @After
    public void tearDown() throws Exception {
        editSocio = null;
    }

}