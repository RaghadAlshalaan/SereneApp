package com.ksu.serene.controller.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.ksu.serene.MainActivity;
import com.ksu.serene.fitbitManager.FitbitWorker;
import com.ksu.serene.model.FitbitAuthentication;
import com.ksu.serene.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FitbitConnection extends AppCompatActivity implements View.OnClickListener {


    private Button connectFitbit;
    private TextView statusTV;
    private Button next, back;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String access_token;
    private String main;
    private LinearLayout linearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitbit_connection);

        getSupportActionBar().hide();

        // Change status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.darkAccent));


        next = findViewById(R.id.nextBtn);
        back = findViewById(R.id.backBtn);

        linearLayout = findViewById(R.id.nav);
        main =  getIntent().getStringExtra("Not Connected");
        if (main.equals("MainActivity")) {
            linearLayout.setVisibility(View.INVISIBLE);

        }


        // go to next page
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statusTV.getText().equals(R.string.status_connect)) {
                    Intent i = new Intent(FitbitConnection.this, GoogleCalendarConnection.class);
                    startActivity(i);
                }
            }
        });

        // back to prev page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FitbitConnection.super.onBackPressed();
            }
        });

        // open fitbit for authintication
        connectFitbit = findViewById(R.id.connectFitbit);
        connectFitbit.setOnClickListener(this);

        // status of connction with the fitbit account
        statusTV = findViewById(R.id.status);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.connectFitbit:

                // open fitbit for authintication

                FitbitAuthentication FA = new FitbitAuthentication();

                StringBuilder oauthUrl = new StringBuilder().append(FA.getUrl())
                        .append("&client_id=").append(FA.getClientId()) // the client id from the api console registration
                        .append("&redirect_uri=").append(FA.getRedirect_uri())
                        .append("&scope=").append(FA.getScope()) // scope is the api permissions we are requesting
                        .append("&expires_in=").append(FA.getExpires_in());

                openFitbitCustomTab(oauthUrl.toString()); // open using chrome custom tabs
                break;

            default:
                break;

        }//end switch
    }


    private void openFitbitCustomTab(String url) {

        // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        // set toolbar color and/or setting custom actions before invoking build()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.darkAccent));

        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customTabsIntent = builder.build();

        // and launch the desired Url with CustomTabsIntent.launchUrl()
        customTabsIntent.launchUrl(this, Uri.parse(url));

    }

    //Method to handle the callback and get the access token for the user
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();

        access_token = uri.toString();

        access_token = access_token.substring(access_token.indexOf("=") + 1, access_token.indexOf("&"));


        // check if user granted or denied
        if (!access_token.equals("The+user+denied+the+request.")) {
            statusTV.setText(R.string.status_connect);
            connectFitbit.setVisibility(View.GONE);


            // SAVE USER's FITBIT ACCESS TOKEN IN Database
            addAccessToken();

            // CREATE fitbit upload work manager
            WorkManager fitbitWorkManager = WorkManager.getInstance();

            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            PeriodicWorkRequest fitbitUpload = new PeriodicWorkRequest
                    .Builder(FitbitWorker.class, 20, TimeUnit.MINUTES).setConstraints(constraints).build();
            fitbitWorkManager.enqueue(fitbitUpload);

            if (main.equals("MainActivity")) {
                Intent intentMain = new Intent(FitbitConnection.this, MainActivity.class);
                startActivity(intentMain);
            }

        } else { // IF DENIED
            statusTV.setText("Status : Not Connected, Please try again");
        }

    }

    private void addAccessToken() {

        // UPLOAD ACCESS TOKEN TO DB
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userEmail = user.getEmail();

        final Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("fitbit_access_token", access_token);


        db.collection("Patient")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String id = document.getId();

                                db.collection("Patient")
                                        .document(id).update(userInfo);
                            }

                        } else System.out.println("Error getting documents: ");

                    }
                });

    }

    @Override
    public void onBackPressed() {
        if (main.equals("MainActivity")) {
            //do nothing
        }
        //else
        else {
            super.onBackPressed();
        }
    }


}
