package com.ksu.serene.fitbitManager;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ksu.serene.R;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.firebase.ui.auth.ui.phone.VerifyPhoneNumberFragment.TAG;

public class DailyWorker extends Worker {

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
    private static final int PERMISSION_INTERNET = 1;
    private static final int PERMISSION_ACCESS_NETWORK_STATE = 2;
    private String day14;
    private String doctorID;

    StorageReference storageRef = storage.getReference();
    StorageReference spaceRef;
    private String image_id = "";
    private int img_id;
    Context context;
    DocumentReference patientDoc;
    String newDate;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();




    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    static FirebaseStorage storage = FirebaseStorage.getInstance();


    public DailyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    /**
     * This will be called whenever work manager run the work (24-hour).
     */
    @NonNull
    @Override
    public Result doWork() {

        /**------------------DAILY REPORT------------------**/

        //to do execute
        executeDailyReportApi(user.getUid());
        /**------------------DOCTOR REPORT------------------**/
        Toast.makeText(getApplicationContext() ,"start doctor", Toast.LENGTH_SHORT).show();
        isTwoWeeksPassed();



        /**--------------------QUOTE--------------------**/

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

                        if (image_id.equals("6"))
                            img_id = 1;
                        else
                            img_id = Integer.parseInt(image_id) + 1;


                        image_id = "q" + img_id;
                        spaceRef = storageRef.child("quotes_images/" + image_id + ".png");
                        spaceRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {

                                // upload to Firebase storage
                                db.collection("Quotes")
                                        .document("quote")
                                        .update("id", img_id,
                                                "image_url", uri.toString())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.e("Quote:", " IMAGE UPLOADED SUCCESSFULLY");
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


        /**-------------------FITBIT-------------------**/


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
                                for (final QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.exists()) {

                                        accessToken = document.getString("fitbit_access_token");

                                        // Step 2 : GET DATE OF YESTERDAY in format ( YYYY-MM-DD )
                                        Calendar cal = Calendar.getInstance();
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                                        DateFormat fitbitDate = new SimpleDateFormat("dd/MM/yyyy");

                                        cal.add(Calendar.DATE, -1);
                                        String date = dateFormat.format(cal.getTime());
                                        newDate = fitbitDate.format(cal.getTime());


                                        // upload first and last day of fitbit
                                        patientDoc = db.collection("Patient").document(user.getUid());

                                        patientDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();

                                                    if(document.get("first_fitbit").toString().equals("")){
                                                       update(2);
                                                    }else{
                                                        update(1);
                                                    }
                                                }

                                            }
                                        });

                                        final String[] category =
                                                {
                                                        "heartrate",
                                                        "activity",
                                                        "sleep"
                                                };
                                        String[] uriRequests =
                                                {
                                                        "https://api.fitbit.com/1/user/-/activities/heart/date/" + date + "/1d/15min.json",
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

    private void update(int i) {
        if(i == 2){
            patientDoc.update("first_fitbit", newDate);
            patientDoc.update("last_fitbit", newDate);
        }else{
            patientDoc.update("last_fitbit", newDate);
        }
    }


    // This method retrieves data from api and returns resulting json string
    public static String getData(String url, String aToken) {

        urlString = url;
        accessToken = aToken;
        requestMethod = "GET";
        authHeader = "Bearer";
        isRevoke = false;

        try {
            return new DailyWorker.RetrieveDataFromApi().execute().get();
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
            new DailyWorker.RetrieveDataFromApi().execute().get();
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


    private void executeDailyReportApi(String id) {

        String url = "https://e8a76a2c.ngrok.io/daily_report/" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("APII", "Success: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText( getApplicationContext(), context.getResources().getText(R.string.api_daily_error_msg) , Toast.LENGTH_SHORT).show();
                        Log.e("APII", "ERROR: " + error.toString());

                    }
                }
        );

        objectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 100000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 100000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        requestQueue.add(objectRequest);
    }

    //1. is 2w has passed?
    public String isTwoWeeksPassed() {
        //1. get last generated doctor report date
        final DocumentReference userRev = FirebaseFirestore.getInstance().collection("Patient").document(mAuth.getUid() + "");
        userRev.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                if (documentSnapshot.get("reportTime").toString().equals("")) {
                    Timestamp lastReportTimestamp = (Timestamp) documentSnapshot.get("reportTime");
                    String lastReportDate = getDateFormat(lastReportTimestamp, false);

                    //2. get day 14
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar c = Calendar.getInstance();
                    try {
                        c.setTime(formatter.parse(lastReportDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    c.add(Calendar.DATE, 15);  // number of days to add
                    day14 = formatter.format(c.getTime());

                    //3. get today date
                    Timestamp todayTimestamp = Timestamp.now();
                    String todayDate = getDateFormat(todayTimestamp, false);

                    //4. compare the two dates
                    // if (todayDate.equals(day14))
                    // Check if doctor is validated
                    Toast.makeText( getApplicationContext(), "two", Toast.LENGTH_LONG).show();

                    isDoctorValidated();
                    Toast.makeText(getApplicationContext() ,"passed", Toast.LENGTH_SHORT).show();
                }//big if

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return "";
    }//isTwoWeeksPassed


    //2. is doctor validated
    public void isDoctorValidated(){
        final Query userRev = FirebaseFirestore.getInstance().collection("Doctor").whereEqualTo("patientID", mAuth.getUid());
        userRev.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                boolean isVerified = (boolean) queryDocumentSnapshots.getDocuments().get(0).get("isVerified");
                doctorID = queryDocumentSnapshots.getDocuments().get(0).get("id").toString();
                if (isVerified){
                    Toast.makeText( getApplicationContext(), "doctor is validated", Toast.LENGTH_LONG).show();
                    isThereEnoughDate();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }//isDoctorValidated

    //3. is there enough data in storage
    public void isThereEnoughDate() {
        final DocumentReference userRev = FirebaseFirestore.getInstance().collection("Patient").document(mAuth.getUid());
        userRev.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final String firstFit_bit = documentSnapshot.get("first_fitbit").toString();

                if(!firstFit_bit.equals("")|| firstFit_bit==null){
                    //Finally!, send Doctor report
                    Toast.makeText( getApplicationContext(), "there are enough data", Toast.LENGTH_LONG).show();
                    executeDoctorReportApi(user.getUid(), doctorID);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        //2. get last generated report date


    }//isThereEnoughDate

    private String getDateFormat(Timestamp timeStamp, boolean isReverse) {


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp.getSeconds() * 1000);
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (isReverse)
            return mYear + "/" + mMonth + "/" + mDay;
        else
            return mDay + "/" + mMonth + "/" + mYear;
    }// getDateFormat


    private void executeDoctorReportApi(String id, String doc) {

        String url = "https://e8a76a2c.ngrok.io/doctor_report/" + id + "/" + doc;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("APII", "Success: " + response.toString());
                        Toast.makeText( getApplicationContext(), "DOCTOR SEND", Toast.LENGTH_LONG).show();

                        storeDoctorReport();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("APII", "ERROR: " + error.toString());
                        Toast.makeText( getApplicationContext(), "Doctor report send Successfully!", Toast.LENGTH_LONG).show();
                        Log.e("APII", "ERROR: " + error.toString());

                    }
                }
        );

        objectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 100000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 100000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        requestQueue.add(objectRequest);
        //todo: retrieve data
    }
    public void storeDoctorReport(){
        final Map<String, Object> user = new HashMap<>();
        user.put("reportTime", FieldValue.serverTimestamp());


        db.collection("Patient")
                .document(mAuth.getUid())
                .set(user , SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "init doctor report added successfully");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }//storeDoctorReport

}
