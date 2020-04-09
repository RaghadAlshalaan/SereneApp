package com.ksu.serene;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.ksu.serene.ImageViewMatcher.hasDrawable;
import static org.hamcrest.Matchers.not;
@RunWith(AndroidJUnit4.class)
public class ChangeImageTest {

    @Rule
    public IntentsTestRule intentsTestRule = new IntentsTestRule(Editprofile.class);
    //public ActivityTestRule<Editprofile> activityTestRule = new ActivityTestRule<Editprofile>(Editprofile.class);
    public GrantPermissionRule permissionRead = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);
    public GrantPermissionRule permissionWrite = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Before
    public void setUp() throws Exception {
        //activityTestRule.launchActivity(new Intent());
        //Intents.init();
        intentsTestRule.getActivity();
    }

    //Test fine but there is a little issue when clicked save the activity hide so i can not until now test the toast message and test the moving activity
    @Test
    public void changeImage () {
        onView(withId(R.id.buttonImage)).perform(click());
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
        //TODO add timer give to user 5 seconds
        //check image added to image view
        onView(withId(R.id.imageView)).check(matches(hasDrawable()));
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(10000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource = new ElapsedTimeIdlingResource(10000);
        try {
            IdlingRegistry.getInstance().register(idlingResource);
            //click save button
            onView(withId(R.id.save)).perform(click());
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
        //Mack sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(30000 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(30000 * 2, TimeUnit.MILLISECONDS);
        //Now we waite
        IdlingResource idlingResource1 = new ElapsedTimeIdlingResource(30000);
        try {
            IdlingRegistry.getInstance().register(idlingResource1);
            onView(withText("DocumentSnapshot successfully updated!"))
                    .inRoot(new ToastMatcher())
                    .check(matches(withText("DocumentSnapshot successfully updated!")));
        }
        //clean upp
        finally {
            IdlingRegistry.getInstance().unregister(idlingResource1);
        }
    }


    @After
    public void tearDown() throws Exception {
        //Intents.release();
    }
}