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
import com.ksu.serene.model.TherapySession;

import java.util.ArrayList;
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
        empty = root.findViewById(R.id.highestday_date);
        recyclerView = root.findViewById(R.id.recycleView);


/*       retrieve = root.findViewById(R.id.fitbitRetrieve);
        data = root.findViewById(R.id.data);

        storage = FirebaseStorage.getInstance();*/


        // parse Preference file
        SharedPreferences sp = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);

        // get values from Map
        final String access_token = sp.getString("FITBIT_ACCESS_TOKEN", "");


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



}