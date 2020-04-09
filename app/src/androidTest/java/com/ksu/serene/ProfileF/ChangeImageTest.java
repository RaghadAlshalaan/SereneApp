package com.ksu.serene.ProfileF;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.ToastMatcher;
import com.ksu.serene.controller.main.profile.Editprofile;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.ksu.serene.ImageViewMatcher.hasDrawable;
import static org.hamcrest.Matchers.not;
@RunWith(AndroidJUnit4.class)
public class ChangeImageTest {

    @Rule
    public IntentsTestRule intentsTestRule = new IntentsTestRule(Editprofile.class);
   // public ActivityTestRule<Editprofile> activityTestRule = new ActivityTestRule<Editprofile>(Editprofile.class);
    public GrantPermissionRule permissionRead = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);
    public GrantPermissionRule permissionWrite = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Before
    public void setUp() throws Exception {
        //Intents.init();
        //intentsTestRule.getActivity();
        //intentsTestRule.launchActivity(new Intent());
    }

    //Test fine but there is a little issue when clicked save the activity hide so i can not until now test the toast message and test the moving activity
    @Test
    public void changeImage () {
        onView(ViewMatchers.withId(R.id.buttonImage)).perform(click());
        Bundle bundle = new Bundle();
        ArrayList<Parcelable> parcels = new ArrayList<>();
        Intent resultData = new Intent();
        Uri uri1 = Uri.parse("file://mnt/sdcard/img02.jpg");
        Parcelable parcelable1 = (Parcelable) uri1;
        parcels.add(parcelable1);
        bundle.putParcelableArrayList(Intent.ACTION_GET_CONTENT, parcels);
        // Create the Intent that will include the bundle.
        resultData.putExtras(bundle);
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData));
        //tester choose image
        // check toast visibility
        onView(withText(R.string.image))
                .inRoot(new ToastMatcher())
                .check(matches(withText(R.string.image)));
        //check image added to image view
        onView(withId(R.id.imageView)).check(matches(hasDrawable()));
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(35000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(35000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(35000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //check text view
            onView(withId(R.id.ImageSavedStorage)).check(matches(withText("DocumentSnapshot successfully updated!")));
            //click save button
            onView(withId(R.id.save)).perform(click());
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }

    }

    @After
    public void tearDown() throws Exception {
        //Intents.release();
        //intentsTestRule.finishActivity();
    }
}