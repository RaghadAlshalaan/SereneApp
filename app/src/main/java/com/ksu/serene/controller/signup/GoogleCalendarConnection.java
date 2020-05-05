package com.ksu.serene.controller.signup;
import static com.ksu.serene.model.MySharedPreference.getInstance;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.controller.liveChart.utils.Utils;
import com.ksu.serene.controller.main.profile.PatientProfile;
import com.ksu.serene.model.MySharedPreference;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.Collections;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class GoogleCalendarConnection extends AppCompatActivity {


    private Button finish, back, connect;
    private ImageView backBtn, Success, Fail;
    private LinearLayout nav;
    GoogleAccountCredential credential;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    final HttpTransport transport = new com.google.api.client.http.javanet.NetHttpTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
    static final int REQUEST_AUTHORIZATION = 1;
    static final int REQUEST_ACCOUNT_PICKER = 2;
    com.google.api.services.calendar.Calendar client;

    TextView textViewStatus;
    Boolean fromProfile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_calendar_connection);

        textViewStatus = findViewById(R.id.status);

        SharedPreferences sp = getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
        String preferred_lng = sp.getString("PREFERRED_LANGUAGE", "en");
        Utils.setLocale(preferred_lng, this);

        getSupportActionBar().hide();

        // Change status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.babyAccent
        ));

        getExtra();

        nav = findViewById(R.id.nav);
        connect = findViewById(R.id.connectCalendar);
        finish = findViewById(R.id.finishBtn);
        back = findViewById(R.id.backBtn);
        Success = findViewById(R.id.check);
        Fail = findViewById(R.id.cancel);
        Fail.setVisibility(View.VISIBLE);

        backBtn = findViewById(R.id.backButton);
        backBtn.setVisibility(GONE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromProfile ){
                    if (textViewStatus.getText().toString().equals(R.string.status_connected)) {
                        SharedPreferences preferences = GoogleCalendarConnection.this.getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("Connect",true);
                        editor.apply();
                        Success.setVisibility(VISIBLE);
                        Fail.setVisibility(GONE);
                    }
                    Intent intent = new Intent(GoogleCalendarConnection.this, PatientProfile.class);
                    startActivity(intent);
                }
                finish();
            }
        });

        if(fromProfile){
            nav.setVisibility(GONE);
            backBtn.setVisibility(VISIBLE);
        }

        credential =
                GoogleAccountCredential.usingOAuth2(GoogleCalendarConnection.this, Collections.singleton(CalendarScopes.CALENDAR));

        client = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential).setApplicationName("SERENE")
                .build();


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] PERMISSIONS = {
                        Manifest.permission.ACCESS_FINE_LOCATION
                };

                Permissions.check(GoogleCalendarConnection.this/*context*/,
                        PERMISSIONS, null/*rationale*/, null/*options*/, new PermissionHandler() {
                            @Override
                            public void onGranted() {
                                // do your task.

                                Intent i = new Intent(GoogleCalendarConnection.this, MainActivity.class);
                                i.putExtra("first","1");
                                startActivity(i);
                                finish();

                            }
                        });
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleCalendarConnection.super.onBackPressed();
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //haveGooglePlayServices();
                startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == Activity.RESULT_OK) {
                    haveGooglePlayServices();
                } else {
                    checkGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == Activity.RESULT_OK) {

                    textViewStatus.setText(R.string.status_connected);
                    connect.setVisibility(GONE);
                    Success.setVisibility(VISIBLE);
                    Fail.setVisibility(GONE);
                } else {
                    chooseAccount();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);

                        Prefs.putString(PREF_ACCOUNT_NAME, accountName);

                        new LoadCalendarAsyncTask().execute();

                    }
                }
                break;

        }
    }

    static final String FIELDS = "id,summary";
    static final String FEED_FIELDS = "items(" + FIELDS + ")";



    private class LoadCalendarAsyncTask extends AsyncTask<Void, Void, Void> {


        LoadCalendarAsyncTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... args) {
            try {
                CalendarList feed = client.calendarList().list().setFields(FEED_FIELDS).execute();
                Log.i("AppInfo", "number of items: " + feed.getItems().size());


            } catch (UserRecoverableAuthIOException ex) {
                startActivityForResult(ex.getIntent(), REQUEST_AUTHORIZATION);

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void appts) {
            super.onPostExecute(appts);
            textViewStatus.setText(R.string.status_connected);
            connect.setVisibility(GONE);
            Success.setVisibility(VISIBLE);
            Fail.setVisibility(GONE);
        }
    }


    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode, GoogleCalendarConnection.this, REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     */
    private boolean checkGooglePlayServicesAvailable() {
        final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        }
        return true;
    }

    private void haveGooglePlayServices() {
        // check if there is already an account selected
        if (credential.getSelectedAccountName() == null) {
            // ask user to choose account
            chooseAccount();
        } else {
            // load calendars
            //AsyncLoadCalendars.run(this);

        }
    }

    private void chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    private void getExtra() {

        if (getIntent().getExtras() != null) {
            fromProfile = getIntent().getExtras().getBoolean("fromProfile");
        }
    }

}
