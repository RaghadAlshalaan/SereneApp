package com.ksu.serene;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.ksu.serene.controller.Reminder.Notification;
import com.ksu.serene.controller.main.home.NotificationAdapter;
import com.ksu.serene.locationManager.MyLocationManager;
import com.ksu.serene.locationManager.MyLocationManagerListener;
import com.ksu.serene.fitbitManager.SensorService;
import com.ksu.serene.fitbitManager.Util;
import com.ksu.serene.fitbitManager.FitbitWorker;
import com.ksu.serene.controller.main.profile.PatientProfile;
import com.ksu.serene.model.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, MyLocationManagerListener {

    public ImageView profile, bell;
    private RecyclerView notificationList;
    private RecyclerView.LayoutManager Notification_LayoutManager;
    private RelativeLayout w1, w2, notificationGroup;
    private ImageView ok1, ok2;
    private LinearLayout overbox, outside_notification_box;
    private Animation from_small, from_nothing;
    private NotificationAdapter notificationAdapter;

    MyLocationManager locationManager;
    boolean newNotificationFlag = false;
    String draftId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

/*        // Change status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));*/

        //create notification channel upon opening app for the first time
        createNotificationChannel();

        init();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_report, R.id.navigation_drafts, R.id.navigation_calendar)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        if (getExtras().equals("1")) {

            overbox.setAlpha(1);
            overbox.startAnimation(from_nothing);

            w1.setAlpha(1);
            w1.startAnimation(from_small);

        } else {
            w1.setVisibility(View.GONE);
            w2.setVisibility(View.GONE);
            overbox.setVisibility(View.GONE);
        }


        profile = findViewById(R.id.profile_icon);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PatientProfile.class);
                startActivity(i);
            }
        });

        //initiate inapp notification list here (upon opening app for first time)
        notificationList = findViewById(R.id.notification_list);
        outside_notification_box = findViewById(R.id.outside_notification_box);
        notificationGroup = findViewById(R.id.notificationGroup);


        notificationList.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter = new NotificationAdapter(this);//initiate arraylist of notifications = empty
        notificationList.setAdapter(notificationAdapter);
        todayNotifications();
        //NotificationAdapter.addNotification(new Notification("Sample appointment", "app", Calendar.getInstance().getTime(), "782ccad5-a267-4773-939b-100b376bbbd6"));

        bell = findViewById(R.id.bell_icon);
        bell.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //view list --> visibility = true
                findViewById(R.id.notification_circle).setVisibility(View.GONE);
                notificationGroup.setVisibility(View.VISIBLE);//notification list box and background of it are now visible
                notificationAdapter.updateView();
                //if clicked outside, visibility gone
            }
        });

        //if user clicks outside notification box, it will disappear
        outside_notification_box.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //set notification group visibility to gone
                notificationGroup.setVisibility(View.GONE);
            }
        });



    }// end onCreate()

    private void init() {

        w1 = findViewById(R.id.w1);
        w2 = findViewById(R.id.w2);
        overbox = findViewById(R.id.overbox);
        ok1 = findViewById(R.id.ok1);
        ok2 = findViewById(R.id.ok2);
        from_small = AnimationUtils.loadAnimation(this, R.anim.from_small);
        from_nothing = AnimationUtils.loadAnimation(this, R.anim.from_nothing);

        ok1.setOnClickListener(this);
        ok2.setOnClickListener(this);

        w1.setAlpha(0);
        w2.setAlpha(0);
        overbox.setAlpha(0);

    }

    private String getExtras() {

        if (getIntent().getExtras() != null) {

            String step = getIntent().getExtras().getString("first");
            return step;
        }

        return "0";
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ok1:
                w1.setVisibility(View.GONE);

                w2.setAlpha(1);
                w2.startAnimation(from_nothing);
                break;

            case R.id.ok2:
                w2.setVisibility(View.GONE);
                overbox.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        todayNotifications();
        profile.setVisibility(View.VISIBLE);
    }


    // -------------------------------LOCATION----------------------------------

    public static Location lastLocation = null;

    @Override
    public void locationUpdated(Location location) {

        if (lastLocation == null){
            lastLocation = location;
            startLocationUpdateRepeatingTask();
        }else {
            lastLocation = location;
        }

    }

    long LOCATION_UPDATE_PERIOD_MINUTES = 30; // SHOULD_BE 30 MIN
    long DAILY_UPDATE_PERIOD_MINUTES = 720; // SHOULD_BE 24/12 Hours

    private Handler locationUpdateHandler;

    Runnable locationUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                sendLocationToServer();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                locationUpdateHandler.postDelayed(locationUpdateRunnable, LOCATION_UPDATE_PERIOD_MINUTES * 60 * 1000);
            }
        }
    };


    void startLocationUpdateRepeatingTask() {
        if (locationUpdateHandler == null){
            locationUpdateHandler = new Handler();
        }
        locationUpdateRunnable.run();
    }

    void stopLocationUpdateRepeatingTask() {
        if (locationUpdateHandler!= null)
        locationUpdateHandler.removeCallbacks(locationUpdateRunnable);
    }


    private Handler saveDateHandler;

    Runnable saveDateRunnable = new Runnable() {
        @Override
        public void run() {
            try {

                saveData();

            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                saveDateHandler.postDelayed(saveDateRunnable, DAILY_UPDATE_PERIOD_MINUTES * 60 * 1000);
            }
        }
    };

    private void saveData() {
        Log.e("AppInfo", "[" + Util.getCurrentDateTime() + "] >>>> Saving Data");

        fitBitWorker =
                new OneTimeWorkRequest.Builder(FitbitWorker.class)
                        .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                        .build();

        WorkManager.getInstance(getApplicationContext()).enqueue(fitBitWorker);
    }


    void startSaveDataRepeatingTask() {
        if (saveDateHandler == null){
            saveDateHandler = new Handler();
        }
        saveDateRunnable.run();
    }

    void stopSaveDataRepeatingTask() {
        saveDateHandler.removeCallbacks(saveDateRunnable);
    }

    OneTimeWorkRequest fitBitWorker;


    private String getRandomID() {
        return UUID.randomUUID().toString();
    }


    private void sendLocationToServer() throws IOException {

        if (lastLocation == null) {
            Log.e("AppInfo", "lastLocation == null");
            return;
        }

        Log.e("AppInfo", "[" + Util.getCurrentDateTime() + "] >>>> Saving Location: " + lastLocation.getLatitude() + "   " + lastLocation.getLongitude());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();

        draftId = getRandomID();

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(24.731897 , 46.600362 , 5); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        // Get Neighborhood name from the address
        int i ;

        if(address.indexOf('،') != -1){
            i = address.indexOf('،')+1;
        }else {
            i = address.indexOf(',')+1;
        }
        int ii = address.indexOf(',', i);

        final Map<String, Object> userLoc = new HashMap<>();
        userLoc.put("patientID", userID);
        userLoc.put("time", FieldValue.serverTimestamp());
        userLoc.put("lat", 24.731897 );
        userLoc.put("lng", 46.600362 );
        userLoc.put("name", address.substring(i+1 , ii) + " District" );
        userLoc.put("anxietyLevel", "1" );
        findNearestLocation(24.731897,46.600362);


        DocumentReference ref = db.collection("PatientLocations").document(draftId);


        if(ref == null) {

            ref.set(userLoc)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            // YAY

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // NAY

                        }
                    });
        }else{
            ref.set(userLoc, SetOptions.merge());
        }
    }

    String[] nearbyLocation;
    private long mRequestStartTime;

    // Method to be used JSON object
    private void findNearestLocation(double lat, double lng) {

        if (lat == 0) {
            return ;
        }

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                String.valueOf(lat) + ","
                + String.valueOf(lng) +
                "&radius=100&key=AIzaSyCmW1fyqXd75fWOVKZuTYof6ihf9Yg99cE";


        nearbyLocation = new String[2];
        nearbyLocation[0] = "";
        nearbyLocation[1] = "";

        mRequestStartTime = System.currentTimeMillis(); // set the request start time just before you send the request.

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        long totalRequestTime = System.currentTimeMillis() - mRequestStartTime;

                        Log.i("AppInfo", "Response: " + response.toString());

                        try {
                            JSONArray resultsArray = response.getJSONArray("results");

                            int num = 0;

                            for (int y = 1; y < resultsArray.length() && num < 2; y++){


                                JSONObject Loc = resultsArray.getJSONObject(y);
                                JSONArray l1 = Loc.getJSONArray("types");

                                for (int i = 0; i < l1.length(); i++) {

                                    String type = String.valueOf(l1.get(i));

                                    if (type.equals("cafe")  || type.equals("mosque") ||
                                            type.equals("store") || type.equals("hospital") ||
                                            type.equals("shopping_mall") || type.equals("university") ||
                                            type.equals("gym") || type.equals("health") ||
                                            type.equals("supermarket") || type.equals("school") ||
                                            type.equals("pharmacy") || type.equals("park") ||
                                            type.equals("embassy") || type.equals("airport") ||
                                            type.equals("establishment")) {


                                        nearbyLocation[num] = Loc.getString("name");
                                        num++;
                                        break;

                                    }

                                }// types array

                            }// iterate responses array


                            // UPLOAD TO DB
                            String nearestLoc = "No Nearby Places Found";
                            if (!nearbyLocation[0].equals("")){
                                nearestLoc = nearbyLocation[0];

                                if(!nearbyLocation[1].equals(""))
                                    nearestLoc += " - " + nearbyLocation[1];
                            }


                            DocumentReference ref = db.collection("PatientLocations").document(draftId);
                            //add URI field to firebase
                            Map<String, Object> nearestLocation = new HashMap<>();
                            nearestLocation.put("nearestLoc", nearestLoc);


                            if (ref != null){

                                ref.set(nearestLocation, SetOptions.merge());

                            }else {
                                ref.set(nearestLocation)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //YAY
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // NAY
                                            }
                                        });

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        if(true);
                    }
                });

        // Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest);

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }



    Intent mServiceIntent;
    private SensorService mSensorService;

    @Override
    protected void onStart() {
        Log.i("AppInfo", "onStart()");

        super.onStart();

        WorkManager.getInstance(getApplicationContext()).cancelAllWork();

        mSensorService = new SensorService(getApplicationContext());
        mServiceIntent = new Intent(getApplicationContext(), mSensorService.getClass());

        startService(mServiceIntent);

        locationManager = new MyLocationManager(MainActivity.this);
        locationManager.startLocationUpdates();

        startSaveDataRepeatingTask();

    }

    @Override
    protected void onDestroy() {
        Log.i("AppInfo", "onDestroy()");

        super.onDestroy();

        //stopLocationUpdateRepeatingTask();
        stopSaveDataRepeatingTask();

    }

    // ------------------------------NOTIFICATION-----------------------------------


    // Notification channel creation
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Serene Reminders";
            String description = "Notification reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Serene_Notification_Channel", name, importance);
            channel.setDescription(description);
            channel.setSound(null, null);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void todayNotifications(){
        final List<Notification> notifications = new ArrayList<Notification>();
        notificationList.setLayoutManager(new LinearLayoutManager(this));
        notificationList.setAdapter(notificationAdapter);


        //get today in timestamp
        String today = new java.text.SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = user.getUid();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Task<QuerySnapshot> docRef = firebaseFirestore.collection("Notifications")
                .whereEqualTo("day", today)
                .whereEqualTo("userID",userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean unread = false;
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                List<DocumentSnapshot> doc = task.getResult().getDocuments();

                                String name = doc.get(i).get("name").toString();
                                String type = doc.get(i).get("type").toString();
                                String time = doc.get(i).get("day").toString()+" "+doc.get(0).get("time").toString();
                                String documentID = doc.get(i).get("documentID").toString();
                                boolean read = (Boolean)doc.get(i).get("read");
                                String notificationID = doc.get(i).get("notificationID").toString();

                                notifications.add(new Notification(name,type,time,documentID, notificationID, read));

                                if (!read)
                                    unread = true;

                                i++;

                            }
                            //after going through all docs
                            //display red circle if unread = true
                            if (unread==true){
                                ImageView notification_circle = findViewById(R.id.notification_circle);
                                notification_circle.setVisibility(View.VISIBLE);
                            }

                            notificationAdapter.updateList(notifications);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });//addOnCompleteListener

    }//end of method


}
