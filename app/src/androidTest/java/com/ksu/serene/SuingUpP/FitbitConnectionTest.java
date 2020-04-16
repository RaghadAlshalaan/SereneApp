package com.ksu.serene.SuingUpP;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.ksu.serene.ElapsedTimeIdlingResource;
import com.ksu.serene.R;
import com.ksu.serene.controller.signup.FitbitConnection;
import com.ksu.serene.model.FitbitAuthentication;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.test.espresso.IdlingPolicies;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class FitbitConnectionTest {

    @Rule
    public ActivityTestRule<FitbitConnection> activityTestRule = new ActivityTestRule<FitbitConnection>(FitbitConnection.class);
    private FitbitConnection fitbitConnection = null;

    @Before
    public void setUp() throws Exception {
        //activityTestRule.launchActivity(new Intent());
            fitbitConnection = activityTestRule.getActivity();
            //check activiy visible
            onView(withId(R.id.FitbitConnection)).check(matches(isDisplayed()));
            //check buttons visible
            //connect button
            onView(withId(R.id.connectFitbit)).check(matches(isDisplayed()));
            //back button
            onView(withId(R.id.backBtn)).check(matches(isDisplayed()));
            //next buttob
            onView(withId(R.id.nextBtn)).check(matches(isDisplayed()));
            //check the status at default not connected
            onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
    }

    @Test
    public void BackButton () {
        //click the back button
        onView(withId(R.id.backBtn)).perform(click());
        //check what happen
        //check the GAD activity appears
        onView(withId(R.id.fragmentGad)).check(matches(isDisplayed()));
    }

    @Test
    public void NextButtonStAsDef () {
        //check the status as default
        onView(withId(R.id.status)).check(matches(withText(R.string.status_not_connected)));
        //click the next button
        onView(withId(R.id.nextBtn)).perform(click());
        //check what happen
    }

    @Test
    public void NextButtonStConnect () {
        //first connect to fit bit
        onView(withId(R.id.connectFitbit)).perform(click());
        //TODO add bundle that go to fitbit site
        /*Bundle bundle = new Bundle();
        ArrayList<Parcelable> arrayList = new ArrayList<>();
        Intent resultData = new Intent();
        FitbitAuthentication FA = new FitbitAuthentication();
        StringBuilder oauthUrl = new StringBuilder().append(FA.getUrl())
                .append("&client_id=").append(FA.getClientId()) // the client id from the api console registration
                .append("&redirect_uri=").append(FA.getRedirect_uri())
                .append("&scope=").append(FA.getScope()) // scope is the api permissions we are requesting
                .append("&expires_in=").append(FA.getExpires_in());
        Uri uri = Uri.parse(oauthUrl.toString());
        Parcelable parcelable = (Parcelable) uri;
        arrayList.add(parcelable);
        bundle.putParcelableArrayList(Intent.ACTION_VIEW,arrayList);
        resultData.putExtras(bundle);
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData));*/
        /*
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        Uri uri = Uri.parse(oauthUrl.toString());
        customTabsIntent.launchUrl(activityTestRule.getActivity(),uri);*/
        //check the status as default
        onView(withId(R.id.status)).check(matches(isDisplayed()));
        //click the next button
        onView(withId(R.id.nextBtn)).perform(click());
        //check what happen
    }

    @After
    public void tearDown() throws Exception {
        fitbitConnection = null;
    }
}