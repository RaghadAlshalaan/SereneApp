package com.ksu.serene.controller.main.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.ksu.serene.R;
import com.ksu.serene.controller.main.report.Adapter;
import com.ksu.serene.controller.main.report.PatientReport;
import com.ksu.serene.model.Location;
import com.ksu.serene.model.TherapySession;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {


    private TextView data, doctorName, time, date, empty;
    private Button retrieve;
    String dataRetrieved;
    // FITBIT API
    private static String urlString; // string to pass in url
    private static String accessToken; // string to pass in access token
    private static String requestMethod; // string to pass in GET or POST
    private static String authHeader; // string to pass in authorization header first word
    private static Boolean isRevoke = false; // boolean to check if action is revoking access token
    private static String clientId;
    private static String clientSecret;

    static FirebaseStorage storage;
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private RecyclerView recyclerView;
    private AdapterA adapter;
    ArrayList<TherapySession> appointments;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        init (root);
        getAppointments();
        return root;
    }

    private void init(View root) {

        date = root.findViewById(R.id.session_date);
        time = root.findViewById(R.id.session_time);
        doctorName = root.findViewById(R.id.doctor_name);
        empty = root.findViewById(R.id.empty);
        recyclerView = root.findViewById(R.id.recycleView);


/*       retrieve = root.findViewById(R.id.fitbitRetrieve);
        data = root.findViewById(R.id.data);

        storage = FirebaseStorage.getInstance();*/


        // parse Preference file
        SharedPreferences sp = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);

        // get values from Map
        final String access_token = sp.getString("FITBIT_ACCESS_TOKEN", "");

       /* retrieve.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {

                // TODO : THIS IS THE FUNCTION TO BE REPEATED


                // GET DATE OF YESTERDAY in format ( YYYY-MM-DD )
                Calendar cal = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                cal.add(Calendar.DATE, -1);
                String date = dateFormat.format(cal.getTime());
                data.setText(date);

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
                    dataRetrieved = getData(uriRequests[i] ,access_token);


                    final String filename = date+"-"+category[i]+".json";
                    String fileContents = dataRetrieved;

                    // Create JSON file to be uploaded
                    FileOutputStream outputStream;

                    try{

                        outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(fileContents.getBytes());
                        outputStream.close();

                    }catch(IOException e){
                        e.printStackTrace();
                    }

                    File file = new File(getContext().getFilesDir(), date+"-"+category[i]+".json");


                    // Upload file to Firebase storage inside the usedId folder
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    StorageReference storageRef = storage.getReference();

                    StorageReference filePath = storageRef.child(userId).child("fitbitData").child(date).child(date+"-"+category[i]+".json");

                    Uri uri = Uri.fromFile(file);

                    UploadTask uploadTask = filePath.putFile(uri);


                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Log.e("FINALLY",  filename +" FILE UPLOADED SUCCESSFULLY");

                        }
                    });

                }


            }
        });*/
    }


    public void getAppointments(){
        appointments = new ArrayList<TherapySession>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        db.collection("PatientSessions")
                .whereEqualTo("patinetID", mAuth.getUid() )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                List<DocumentSnapshot> doc = task.getResult().getDocuments();

                                //check for appointment date if it's within selected duration

                                Date loc_date = ((com.google.firebase.Timestamp) doc.get(i).get("dateTimestamp")).toDate();//date received
                                Date today = new Date();//today

                                if (daysBetween(loc_date, today) < 7) {
                                    String doctorName = doc.get(i).get("name").toString();
                                    String DTime = doc.get(i).get("time").toString();
                                    String Ddate = doc.get(i).get("date").toString();
                                    String id = String.valueOf(i);
                                    appointments.add(new TherapySession(id, doctorName, Ddate, DTime));

                                } else {

                                }

                                i++;


                            }// end for


                            recyclerView.setHasFixedSize(true);
                            adapter = new AdapterA(getActivity(), appointments);
                            if(adapter.getItemCount() != 0){
                            recyclerView.setAdapter(adapter);}
                            else{
                                empty.setVisibility(View.VISIBLE);
                            }

                        }//if
                    }
                    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    private static long daysBetween(Date one, Date two) {
        long difference =  (one.getTime()-two.getTime())/86400000;
        return Math.abs(difference);
    }




/*
    // This method retrieves data from api and returns resulting json string
    public static String getData(String url, String aToken){

        urlString = url;
        accessToken = aToken;
        requestMethod = "GET";
        authHeader = "Bearer";
        isRevoke = false;

        try {
            return new RetrieveDataFromApi().execute().get();
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

    // This method extracts information from json string and stores them in a json object
    public static JSONObject convertStringToJson(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject;
        }
        catch (JSONException e){
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }


    // this method revokes current access token
    public static void revokeToken(String aToken, String id, String secret){
        urlString = "https://api.fitbit.com/oauth2/revoke";
        accessToken = aToken;
        requestMethod = "POST";
        authHeader = "Basic";
        isRevoke = true;
        clientId = id;
        clientSecret = secret;

        try {
            new RetrieveDataFromApi().execute().get();
        }
        catch(InterruptedException e){
            Log.e("ERROR", e.getMessage(), e);
        }
        catch(ExecutionException e){
            Log.e("ERROR", e.getMessage(), e);
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
    }*/


}