package com.ksu.serene;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
//import static org.mock.MockContext;

@RunWith(PowerMockRunner.class)
//@PowerMockRunnerDelegate(RobolectricTestRunner.class)  //TODO for DatePicker
@PowerMockRunnerDelegate(JUnit4.class)
@PrepareForTest({MainActivity.class, FirebaseFirestore.class,Activity.class})
public class MainActivityTestLoc {

    private MainActivity activity;
    private FirebaseFirestore mockFirebaseFirestore;
    @Mock
    LocationManager locationManagerMock = Mockito.mock(LocationManager.class);
    Activity contextMock = Mockito.mock(Activity.class);

    //MockContext

    @Before
    public void setUp() throws Exception {
        //mock firebase firestore
        mockFirebaseFirestore = Mockito.mock(FirebaseFirestore.class);
        PowerMockito.mockStatic(FirebaseFirestore.class);
        when(FirebaseFirestore.getInstance()).thenReturn(mockFirebaseFirestore);
        //mock activity that used for call getSystemService
        contextMock = Mockito.mock(Activity.class);
        PowerMockito.mockStatic(Activity.class);
        //when(Activity.getSystemService(Context.LOCATION_SERVICE)).thenReturn(locationManagerMock);
        //when(Activity.LOCATION_SERVICE);//.thenReturn(contextMock);

        activity = spy(MainActivity.class) ;//activityTestRule.getActivity();
    }

    @Test
    public void testUpdateLocation () {
        //create mock location
        Location mockLoc = mockLocation ();
        //call method
        activity.locationUpdated(mockLoc);
        assertNotNull(activity.lastLocation);
    }

    private Location mockLocation () {
        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Location location = new Location("MockLoc");
        location.setLatitude(466752243);
        location.setLongitude(246971054);
        location.setTime(System.currentTimeMillis());
        lm.setTestProviderLocation("MockLoc", location);
        return location;
    }

}