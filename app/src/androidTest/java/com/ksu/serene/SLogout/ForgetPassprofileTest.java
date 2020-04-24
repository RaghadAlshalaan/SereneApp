package com.ksu.serene.SLogout;

import android.Manifest;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.PermissionGranter;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.profile.Editprofile;

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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
@RunWith(AndroidJUnit4.class)

public class ForgetPassprofileTest {

    @Rule
    public ActivityTestRule<Editprofile> activityTestRule = new ActivityTestRule<Editprofile>(Editprofile.class);
    private Editprofile editprofile = null;

    @Before
    public void setUp() throws Exception {
        editprofile = activityTestRule.getActivity();
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //activityTestRule.launchActivity(new Intent());
            //check activity visible
            onView(ViewMatchers.withId(R.id.Editprofile)).check(matches(isDisplayed()));
            //check button visible
            onView(withId(R.id.forgetPassword)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void ForgetPass () {
        //click to forget passord text
        onView(withId(R.id.forgetPassword)).perform(click());
        //check the toast apear
        // check toast visibility
        onView(withText(R.string.ForgetPassSuccess))
                .inRoot(new ToastMatcher()).check(matches(withText(R.string.ForgetPassSuccess)));
        //check the welcome page appear
        onView(withId(R.id.WelcomePage)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
        login ();
        editprofile = null;
    }

    private void login () {
        //click to log in
        onView(withId(R.id.login)).perform(click());
        //check if log in page appear
        onView(withId(R.id.LogIn)).check(matches(isDisplayed()));
        //log in success
        //enter registered email
        onView(withId(R.id.Email)).perform(typeText("lama-almarshad@hotmail.com"));
        //close keyboard
        closeSoftKeyboard();
        //enter correct password
        onView(withId(R.id.Password)).perform(typeText("passw99"));
        //close keyboard
        closeSoftKeyboard();
        onView(withId(R.id.loginBtn)).perform(click());
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(5000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource1 = new ElapsedTimeIdlingResource(5000);
        try {
            IdlingRegistry.getInstance().register(idlingResource1);
            //grantedd permission
            PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.ACCESS_COARSE_LOCATION);
            PermissionGranter.allowPermissionsIfNeeded(Manifest.permission.ACCESS_FINE_LOCATION);
            onView(withId(R.id.MainActivity)).check(matches(isDisplayed()));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource1);
        }
    }

}