package com.ksu.serene.controller.main.profile;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.ksu.serene.MainActivity;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.controller.signup.GoogleCalendarConnection;
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

public class PatientProfile extends AppCompatActivity {

    private ImageView image, SocioArrow, doctorArrow, editProfile, back, googleCalendarArrow;
    private TextView name, email, doctor;
    private LinearLayout alert, resendL;
    private String nameDb, emailDb, imageDb;
    private FirebaseAuth mAuth;
    private Button logOut;
    private String TAG = PatientProfile.class.getSimpleName();
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private String mToken;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Nullable
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        // Inflate the layout for this fragment
        getSupportActionBar().hide();

        init();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientProfile.this, Editprofile.class);
                startActivity(intent);
            }

        });

        SocioArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientProfile.this, EditSocio.class);
                startActivity(intent);
            }
        });

        doctorArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkDoctorAvailability();

            }
        });

        googleCalendarArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientProfile.this, GoogleCalendarConnection.class);
                intent.putExtra("fromProfile", true);
                startActivity(intent);
            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });


        user.reload();
        if (!user.isEmailVerified()) {
            alert.setVisibility(View.VISIBLE);
        } else {
            alert.setVisibility(View.GONE);
        }

        listenToUpdates();

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

    private void init() {

        image = findViewById(R.id.imageView);
        email = findViewById(R.id.emailET);
        name = findViewById(R.id.full_name);
        mAuth = FirebaseAuth.getInstance();
        editProfile = findViewById(R.id.edit_profile_btn);
        SocioArrow = findViewById(R.id.go_to1);
        logOut = findViewById(R.id.log_out_btn);
        doctorArrow = findViewById(R.id.go_to2);
        googleCalendarArrow = findViewById(R.id.go_to3);
        doctor = findViewById(R.id.doctor_text2);
        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        alert = findViewById(R.id.alert);
        resendL = findViewById(R.id.resendL);
        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(PatientProfile.this, R.string.VervEmailSuccess, Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PatientProfile.this, R.string.VervEmailFialed, Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

    }


    private void checkDoctorAvailability() {

        db.collection("Doctor")
                .whereEqualTo("patientID", mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.exists()) {
                                        Intent intent = new Intent(PatientProfile.this, MyDoctor.class);
                                        startActivity(intent);
                                    }
                                }
                            } else {

                                Intent intent = new Intent(PatientProfile.this, AddDoctor.class);
                                startActivity(intent);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void displayName() {

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
                    MySharedPreference.putString(PatientProfile.this, "name", nameDb);
                    MySharedPreference.putString(PatientProfile.this, "email", emailDb);
                    // MySharedPreference.putString(getContext(), "Image", imageDb);

                }
            }
            if (nameDb != null)
                name.setText(nameDb);

            if (emailDb != null)
                email.setText(emailDb);

            // if(imageDb !=null)
            // Picasso.get().load(imageDb).into(image);


        }//end if
    }

    @Override
    public void onResume() {
        super.onResume();

        db.collection("Doctor")
                .whereEqualTo("patientID", mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.exists()) {
                                        doctor.setText(document.getString("name"));
                                    }
                                }
                            } else {
                                doctor.setText(R.string.NoDoc);
                            }
                        } else {

                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        if (!MySharedPreference.getString(PatientProfile.this, "name", "").equals("")) {
            name.setText(MySharedPreference.getString(PatientProfile.this, "name", ""));
        }

        if (!MySharedPreference.getString(PatientProfile.this, "Image", "").equals("")) {
            imageDb = MySharedPreference.getString(PatientProfile.this, "Image", "");
            if (imageDb != null)
                Picasso.get().load(imageDb).into(image);
        }
        if (!MySharedPreference.getString(PatientProfile.this, "email", "").equals("")) {
            email.setText(MySharedPreference.getString(PatientProfile.this, "email", ""));
        } else {
            displayName();
        }

        user.reload();
        if (!user.isEmailVerified()) {
            alert.setVisibility(View.VISIBLE);
        } else {
            alert.setVisibility(View.GONE);
        }

    }// end onResume


    public void logoutDialog() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PatientProfile.this);
        //set Title
        alertDialog.setTitle(R.string.LogOut);

        //set dialog msg
        alertDialog.setMessage(R.string.LogOutMsg);

        //set Yes Btn
        alertDialog.setPositiveButton(R.string.LogOutOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        logout();
                    }//end of OnClick
                }//end of OnClickListener
        );//end setPositiveButton

        //set Cancel Button
        alertDialog.setNegativeButton(R.string.LogOutCancle, new DialogInterface.OnClickListener() {
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
        tokenh.put("token", mToken);

        userTokenDR
                .update("token", mToken)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // [START auth_sign_out]
                        mAuth.signOut();
                        if (mAuth.getCurrentUser() == null) {
                            Toast.makeText(PatientProfile.this, R.string.LogOutSuccess, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PatientProfile.this, WelcomePage.class);
                            startActivity(intent);
                            finish();
                        }
                        // [END auth_sign_out]
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PatientProfile.this, R.string.LogOutFialed, Toast.LENGTH_LONG).show();
                        Log.w(TAG, "Error updating document", e);
                    }
                });

    }


    private boolean checkIfEmailVerified() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified()) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(PatientProfile.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    mToken = instanceIdResult.getToken();
                    Log.e("Token", mToken);
                }
            });

            updateToken(mToken);
            SharedPreferences sp = PatientProfile.this.getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("CURRENT_USERID", mAuth.getCurrentUser().getUid());
            editor.apply();
            return true;
        } else {

            return false;

        }

    }

    public void updateToken(String token) {

        DocumentReference userTokenDR = FirebaseFirestore.getInstance().collection("Tokens").document(mAuth.getUid());
        Token mToken = new Token(token);
        final Map<String, Object> tokenh = new HashMap<>();
        tokenh.put("token", mToken.getToken());

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
