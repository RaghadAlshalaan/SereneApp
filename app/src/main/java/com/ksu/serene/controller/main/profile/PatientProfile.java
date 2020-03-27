package com.ksu.serene.controller.main.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.model.Token;
import com.ksu.serene.WelcomePage;
import com.squareup.picasso.Picasso;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.ksu.serene.model.MySharedPreference;
import com.ksu.serene.R;

import java.util.HashMap;
import java.util.Map;

public class PatientProfile extends Fragment {

    private ImageView image, SocioArrow, doctorArrow, editProfile;
    private TextView name, email, doctor;
    private LinearLayout alert;
    private String nameDb, emailDb, imageDb;
    private FirebaseAuth mAuth;
    private Button  logOut;
    private String TAG = PatientProfile.class.getSimpleName();
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private String mToken;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_page, container, false);

        init(view);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Editprofile.class);
                getActivity().startActivity(intent);
                 }

        });

        SocioArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditSocio.class);
                getActivity().startActivity(intent);
            }
        });

        doctorArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkDoctorAvailability();

            }
        });


      logOut.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              logoutDialog();
          }
      });


      if(checkIfEmailVerified() == false ){
          alert.setVisibility(View.VISIBLE);
      }

        listenToUpdates();

        return view;
    }

    private void listenToUpdates() {
//
//        DocumentReference docRef = db.collection("Doctor").whereEqualTo("patientID"+mAuth.getUid().substring(0,5), mAuth.getUid()).get();
//        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot snapshot,
//                                @Nullable FirestoreException e) {
//                if (e != null) {
//                    System.err.println("Listen failed: " + e);
//                    return;
//                }
//
//                if (snapshot != null && snapshot.exists()) {
//                    System.out.println("Current data: " + snapshot.getData());
//                } else {
//                    System.out.print("Current data: null");
//                }
//            }
//        });
//

    }

    private void init(View view) {

        image = view.findViewById(R.id.imageView);
        email = view.findViewById(R.id.emailET);
        name = view.findViewById(R.id.full_name);
        mAuth = FirebaseAuth.getInstance();
        editProfile = view.findViewById(R.id.edit_profile_btn);
        SocioArrow = view.findViewById(R.id.go_to1);
        logOut = view.findViewById(R.id.log_out_btn);
        doctorArrow = view.findViewById(R.id.go_to2);
        doctor = view.findViewById(R.id.doctor_text2);
        alert = view.findViewById(R.id.alert);

    }


    private void checkDoctorAvailability() {

        db.collection("Doctor")
                .whereEqualTo("patientID"+mAuth.getUid().substring(0,5), mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.exists()) {
                                        Intent intent = new Intent(getActivity(), MyDoctor.class);
                                        getActivity().startActivity(intent);
                                    }
                                }
                            }
                            else{

                                Intent intent = new Intent(getActivity(), AddDoctor.class);
                                getActivity().startActivity(intent);
                            }
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    public void displayName(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            nameDb = user.getDisplayName();
            emailDb = user.getEmail();

            // imageDb = user.getPhotoUrl().toString();
            // If the above were null, iterate the provider data
            // and set with the first non null data

            for (UserInfo userInfo : user.getProviderData()) {
                if (nameDb == null && userInfo.getDisplayName() != null && emailDb == null && userInfo.getEmail() != null
                ) {
                    nameDb = userInfo.getDisplayName();
                    emailDb = userInfo.getEmail();
                    // imageDb = userInfo.getPhotoUrl().toString();
                    MySharedPreference.putString(getContext(), "name", nameDb);
                    MySharedPreference.putString(getContext(), "email", emailDb);
                    // MySharedPreference.putString(getContext(), "Image", imageDb);

                }
            }
            if(nameDb != null)
                name.setText(nameDb);

            if(emailDb != null)
                email.setText(emailDb);

            // if(imageDb !=null)
            // Picasso.get().load(imageDb).into(image);


        }//end if
    }

    @Override
    public void onResume() {
        super.onResume();

        db.collection("Doctor")
                .whereEqualTo("patientID"+mAuth.getUid().substring(0,5), mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.exists()) {
                                        doctor.setText(document.getString("name"));
                                    }} }
                            else{
                                doctor.setText("No Doctor");
                            }
                        }
                        else {

                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        if (!MySharedPreference.getString(getContext(), "name", "").equals("")) {
            name.setText(MySharedPreference.getString(getContext(), "name", ""));
        }

        if (!MySharedPreference.getString(getContext(), "Image", "").equals("")) {
            imageDb = MySharedPreference.getString(getContext(), "Image", "");
            if(imageDb !=null)
                Picasso.get().load(imageDb).into(image);
        }
        if (!MySharedPreference.getString(getContext(), "email", "").equals("")) {
            email.setText(MySharedPreference.getString(getContext(), "email", ""));
        }
        else {
            displayName();
        }

    }// end onResume


    public void logoutDialog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        //set dialog msg
        alertDialog.setMessage("Are you sure you want to logout ?");

        //set Yes Btn
        alertDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        logout();
                    }//end of OnClick
                }//end of OnClickListener
        );//end setPositiveButton

        //set Cancel Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }//end onClick
                }//end OnClickListener
        );//end setNegativeButton

        alertDialog.show();

    }//end logoutDialog


    public void logout() {

        // to delete the token after logout & avoid send him notification if he is logout
        DocumentReference userTokenDR = FirebaseFirestore.getInstance().collection("Tokens").document(mAuth.getUid());
        Token mToken = new Token("");

        final Map<String, Object> tokenh = new HashMap<>();
        tokenh.put("token",mToken);

        userTokenDR
                .update("token", mToken)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // [START auth_sign_out]
                        mAuth.signOut();
                        if (mAuth.getCurrentUser() == null) {
                            Intent intent = new Intent(getActivity(), WelcomePage.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        // [END auth_sign_out]
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

    }
    private boolean checkIfEmailVerified() {

        // TODO : MOVE IT TO MAIN PAGE
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getActivity(),  new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    mToken = instanceIdResult.getToken();
                    Log.e("Token",mToken);
                }
            });

            updateToken(mToken);
            SharedPreferences sp = getActivity().getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("CURRENT_USERID",mAuth.getCurrentUser().getUid());
            editor.apply();
           return true;
        }
        else
        {

            return false;

        }

    }
    public  void updateToken(String token){

        DocumentReference userTokenDR = FirebaseFirestore.getInstance().collection("Tokens").document(mAuth.getUid());
        Token mToken = new Token(token);
        final Map<String, Object> tokenh = new HashMap<>();
        tokenh.put("token",mToken.getToken());

        userTokenDR
                .update(tokenh)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
}
