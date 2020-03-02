package com.ksu.serene.Controller.Signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ksu.serene.Model.FitbitAuthentication;
import com.ksu.serene.R;

public class FitbitConnection extends AppCompatActivity implements View.OnClickListener {


        private Button connectFitbit;
        private TextView statusTV;
        private Button next, back;
        private String userEmail;
        private final FirebaseAuth mAuth = FirebaseAuth.getInstance();


        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitbit_connection);

                getSupportActionBar().hide();


                next = findViewById(R.id.nextBtn);
                back = findViewById(R.id.backBtn);



                next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                Intent i = new Intent(FitbitConnection.this, GoogleCalendarConnection.class);
                startActivity(i);

                }
                });

                back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        FitbitConnection.super.onBackPressed();
                }
                });

                connectFitbit = findViewById(R.id.connectFitbit);
                connectFitbit.setOnClickListener(this);

                statusTV = findViewById(R.id.status);

                //get user email (get extra)
                getExtras();


        }

        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                case R.id.connectFitbit:

                FitbitAuthentication FA = new FitbitAuthentication();

                StringBuilder oauthUrl = new StringBuilder().append(FA.getUrl())
                .append("&client_id=").append(FA.getClientId()) // the client id from the api console registration
                .append("&redirect_uri=").append(FA.getRedirect_uri())
                .append("&scope=").append(FA.getScope()) // scope is the api permissions we are requesting
                .append("&expires_in=").append(FA.getExpires_in());

                openFitbitCustomTab(oauthUrl.toString());
                break;


                default: break;

                }//end switch
        }

    private void getExtras() {

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
        if (intent.getExtras().getString("Email") != null)
        userEmail = intent.getExtras().getString("Email");
        }
        }

    private void openFitbitCustomTab(String url) {

        // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        // set toolbar color and/or setting custom actions before invoking build()
        //builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        //builder.addDefaultShareMenuItem();

        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customTabsIntent = builder.build();

        // and launch the desired Url with CustomTabsIntent.launchUrl()
        customTabsIntent.launchUrl(this, Uri.parse(url));

        }


        //Method to handle the callback and get the access token for the user
        @Override
        protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //String access_token = intent.getData().getQueryParameter("access_token");

        Uri uri = intent.getData();

        String access_token = uri.toString();

        access_token = access_token.substring(access_token.indexOf("=")+1, access_token.indexOf("&"));

        Log.w("FITBIT", "onNavigationEvent: HERE = " + access_token);



        if (!access_token.equals("The+user+denied+the+request.")) {
        statusTV.setText("Status : Connected!");


                // SAVE USER's FITBIT ACCESS TOKEN
                SharedPreferences sp = getSharedPreferences("user_details", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("FITBIT_ACCESS_TOKEN", access_token);
                editor.apply();


        } else {
        statusTV.setText("Status : Not Connected, Please try again");

        Toast.makeText(getApplicationContext(), "Fail Connecting to Fitbit, please try again!", Toast.LENGTH_SHORT).show();

        // refresh activity
        }

        }


}
