package com.ksu.serene.controller.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ksu.serene.controller.fitbitDataWorker.FitbitWorker;
import com.ksu.serene.model.FitbitAuthentication;
import com.ksu.serene.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class FitbitConnection extends AppCompatActivity implements View.OnClickListener {


    Context context = this;

    private Button connectFitbit;
    private TextView statusTV;
    private Button next, back;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String access_token ;


    // FITBIT API
    private static String urlString; // string to pass in url
    private static String accessToken; // string to pass in access token
    private static String requestMethod; // string to pass in GET or POST
    private static String authHeader; // string to pass in authorization header first word
    private static Boolean isRevoke = false; // boolean to check if action is revoking access token
    private static String clientId;
    private static String clientSecret;


    static FirebaseStorage storage = FirebaseStorage.getInstance();


    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitbit_connection);

                getSupportActionBar().hide();

            // Change status bar color
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));


                next = findViewById(R.id.nextBtn);
                back = findViewById(R.id.backBtn);


                // go to next page
                next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent i = new Intent(FitbitConnection.this, GoogleCalendarConnection.class);
                startActivity(i);
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

                default: break;

                }//end switch
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

        Uri uri = intent.getData();

        access_token = uri.toString();

        access_token = access_token.substring(access_token.indexOf("=")+1, access_token.indexOf("&"));


        // check if user granted or denied
        if (!access_token.equals("The+user+denied+the+request.")) {
        statusTV.setText("Status : Connected!");


            // SAVE USER's FITBIT ACCESS TOKEN IN Database
            addAccessToken();

            // TODO : START FETCH USER DATA FOR THE FIRST TIME AND HERE I WANT TO START THE BACKGROUND SERVICE !
            //uploadFitbit();

            // CREATE fitbit upload work manager
            WorkManager fitbitWorkManager = WorkManager.getInstance();

            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            PeriodicWorkRequest fitbitUpload = new PeriodicWorkRequest
                    .Builder(FitbitWorker.class,    20, TimeUnit.MINUTES).setConstraints(constraints).build();
            fitbitWorkManager.enqueue(fitbitUpload);


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

    private void uploadFitbit() {


        // GET DATE OF YESTERDAY in format ( YYYY-MM-DD )
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE, -1);
        String date = dateFormat.format(cal.getTime());

        final String[] category =
                {
                        "heartrate",
                        "activity",
                        "sleep"
                };
        String[] uriRequests =
                {
                        "https://api.fitbit.com/1/user/-/activities/heart/date/"+date+"/1d/1min.json",
                        "https://api.fitbit.com/1/user/-/activities/steps/date/"+date+"/1d/1min.json",
                        "https://api.fitbit.com/1.2/user/-/sleep/date/"+date+".json"
                };

        for ( int i = 0 ; i < 3 ; i ++ ){

            // Request the data from Fitbit API
            String dataRetrieved = getData(uriRequests[i] ,access_token);


            final String filename = date+"-"+category[i]+".json";
            String fileContents = dataRetrieved;

            // Create JSON file to be uploaded
            FileOutputStream outputStream;

            try{

                outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(fileContents.getBytes());
                outputStream.close();

            }catch(IOException e){
                e.printStackTrace();
            }

            File file = new File( context.getFilesDir(), date+"-"+category[i]+".json");


            // Upload file to Firebase storage inside the usedId folder
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            StorageReference storageRef = storage.getReference();

            StorageReference filePath = storageRef.child(userId).child("fitbitData").child(date).child(date+"-"+category[i]+".json");

            Uri uri = Uri.fromFile(file);

            UploadTask uploadTask = filePath.putFile(uri);


            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Log.e("DONE",  filename +" FILE UPLOADED SUCCESSFULLY");

                }
            });

        }

    }

    // This method retrieves data from api and returns resulting json string
    public static String getData(String url, String aToken){

        urlString = url;
        accessToken = aToken;
        requestMethod = "GET";
        authHeader = "Bearer";
        isRevoke = false;

        try {
            return new FitbitConnection.RetrieveDataFromApi().execute().get();
        }
        catch(InterruptedException e){
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
        catch(ExecutionException e){
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }



    // INNER CLASS
    // Asynctask to get fitbit information from web api
    static class RetrieveDataFromApi extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... urls){


            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(requestMethod);
                if (isRevoke){
                    urlConnection.setRequestProperty("Authorization", authHeader+" "+ Base64.encodeToString((clientId+":"+clientSecret).getBytes("UTF-8"), Base64.DEFAULT));
                    urlConnection.addRequestProperty("token", accessToken);
                }
                else{
                    urlConnection.setRequestProperty("Authorization", authHeader+" "+accessToken);
                }
                urlConnection.connect();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // getInputStream connects to url and gets stream
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    String jsonString = stringBuilder.toString();

                    return jsonString;

                } finally {
                    urlConnection.disconnect();
                }
            }
            catch (SocketTimeoutException e) {
                Log.e("ERROR", e.getMessage(), e);
                return "THE CONNECTION HAS TIMED OUT";
            }
            catch (MalformedURLException e){
                Log.e("ERROR", e.getMessage(), e);
                return "INCORRECT URL";
            }
            catch (IOException e){
                Log.e("ERROR", e.getMessage(), e);
                return new IOException().toString();
            }

        }
    }


}
