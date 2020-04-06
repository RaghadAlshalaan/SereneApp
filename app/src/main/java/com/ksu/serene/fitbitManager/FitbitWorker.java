package com.ksu.serene.fitbitManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

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

import static com.firebase.ui.auth.ui.phone.VerifyPhoneNumberFragment.TAG;

public class FitbitWorker extends Worker {

    // The result key:
    private String WORK_RESULT = "result";

    // FITBIT API
    private static String urlString; // string to pass in url
    private static String accessToken; // string to pass in access token
    private static String requestMethod; // string to pass in GET or POST
    private static String authHeader; // string to pass in authorization header first word
    private static Boolean isRevoke = false; // boolean to check if action is revoking access token
    private static String clientId;
    private static String clientSecret;

    StorageReference storageRef = storage.getReference();
    StorageReference spaceRef;
    private String image_id="";
    private int img_id;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    static FirebaseStorage storage = FirebaseStorage.getInstance();


    public FitbitWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    /**
     * This will be called whenever work manager run the work.
     */
    @NonNull
    @Override
    public Result doWork() {

        // STEP 0 : UPDATE QUOTE OF THE DAY

        //get current quote ID
        db.collection("Quotes").document("quote")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        image_id = document.get("id").toString();

                        if(image_id.equals("6"))
                            img_id = 1;
                        else
                            img_id = Integer.parseInt(image_id) + 1;


                        image_id = "q"+img_id;
                        spaceRef = storageRef.child("quotes_images/"+image_id+".png");
                        spaceRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {

                                // upload to Firebase storage
                                db.collection("Quotes")
                                        .document("quote")
                                        .update("id",img_id,
                                                "image_url",uri.toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.e("DONE: ", uri + " IMAGE UPLOADED SUCCESSFULLY");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //Toast.makeText(PatientTextDraftDetailPage.this, "Did Not Update, Try Again", Toast.LENGTH_LONG).show();
                                            }
                                        });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        /**------------------------------------**/


        // Step 1 : GET USER ACCESS TOKEN FROM DB
        final String userEmail = user.getEmail();

        db.collection("Patient")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.exists()) {

                                        accessToken = document.getString("fitbit_access_token");

                                        // Step 2 : GET DATE OF YESTERDAY in format ( YYYY-MM-DD )
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
                                                        "https://api.fitbit.com/1/user/-/activities/heart/date/" + date + "/1d/1min.json",
                                                        "https://api.fitbit.com/1/user/-/activities/date/" + date + ".json",
                                                        "https://api.fitbit.com/1.2/user/-/sleep/date/" + date + ".json"
                                                };

                                        for (int i = 0; i < 3; i++) {

                                            // Request the data from Fitbit API
                                            String dataRetrieved = getData(uriRequests[i], accessToken);

                                            // Generate file name
                                            final String filename = date + "-" + category[i] + ".json";
                                            String fileContents = dataRetrieved;

                                            // Create JSON file to be uploaded
                                            FileOutputStream outputStream;

                                            try {

                                                outputStream = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
                                                outputStream.write(fileContents.getBytes());
                                                outputStream.close();

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            File file = new File(getApplicationContext().getFilesDir(), date + "-" + category[i] + ".json");

                                            // Upload file to Firebase storage inside the usedId folder
                                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                            StorageReference storageRef = storage.getReference();

                                            StorageReference filePath = storageRef.child(userId).child("fitbitData").child(date).child(date + "-" + category[i] + ".json");

                                            Uri uri = Uri.fromFile(file);

                                            UploadTask uploadTask = filePath.putFile(uri);


                                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                    Log.e("DONE: ", filename + " FILE UPLOADED SUCCESSFULLY");

                                                }
                                            });

                                        }
                                    }
                                }
                            }

                        } else {

                        }
                    }
                });


        return null;
    }


    // This method retrieves data from api and returns resulting json string
    public static String getData(String url, String aToken) {

        urlString = url;
        accessToken = aToken;
        requestMethod = "GET";
        authHeader = "Bearer";
        isRevoke = false;

        try {
            return new FitbitWorker.RetrieveDataFromApi().execute().get();
        } catch (InterruptedException e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        } catch (ExecutionException e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    // This method extracts information from json string and stores them in a json object
    public static JSONObject convertStringToJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject;
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }


    // this method revokes current access token
    public static void revokeToken(String aToken, String id, String secret) {
        urlString = "https://api.fitbit.com/oauth2/revoke";
        accessToken = aToken;
        requestMethod = "POST";
        authHeader = "Basic";
        isRevoke = true;
        clientId = id;
        clientSecret = secret;

        try {
            new FitbitWorker.RetrieveDataFromApi().execute().get();
        } catch (InterruptedException e) {
            Log.e("ERROR", e.getMessage(), e);
        } catch (ExecutionException e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }


    // INNER CLASS
    // Asynctask to get fitbit information from web api
    static class RetrieveDataFromApi extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... urls) {


            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(requestMethod);
                if (isRevoke) {
                    urlConnection.setRequestProperty("Authorization", authHeader + " " + Base64.encodeToString((clientId + ":" + clientSecret).getBytes("UTF-8"), Base64.DEFAULT));
                    urlConnection.addRequestProperty("token", accessToken);
                } else {
                    urlConnection.setRequestProperty("Authorization", authHeader + " " + accessToken);
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

                    Log.i("AppInfo", "jsonString = " + jsonString);

                    return jsonString;

                } finally {
                    urlConnection.disconnect();
                }
            } catch (SocketTimeoutException e) {
                Log.e("ERROR", e.getMessage(), e);
                return "THE CONNECTION HAS TIMED OUT";
            } catch (MalformedURLException e) {
                Log.e("ERROR", e.getMessage(), e);
                return "INCORRECT URL";
            } catch (IOException e) {
                Log.e("ERROR", e.getMessage(), e);
                return new IOException().toString();
            }

        }
    }

}
