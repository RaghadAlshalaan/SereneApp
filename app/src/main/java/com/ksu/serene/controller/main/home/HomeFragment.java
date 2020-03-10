package com.ksu.serene.controller.main.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.ksu.serene.R;

public class HomeFragment extends Fragment {


    private TextView data;
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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        init (root);


        return root;
    }

    private void init(View root) {
/*        retrieve = root.findViewById(R.id.fitbitRetrieve);
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