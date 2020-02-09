package com.ksu.serene.Controller.Homepage.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.Controller.Signup.Signup;
import com.ksu.serene.Controller.Signup.Sociodemo;
import com.ksu.serene.Model.Token;
import com.ksu.serene.Patient;
import com.ksu.serene.WelcomePage;
import com.squareup.picasso.Picasso;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.ksu.serene.Model.MySharedPreference;
import com.ksu.serene.R;

import java.util.HashMap;
import java.util.Map;

public class PatientProfile extends Fragment {

    private ImageView image, SocioArrow;
    private TextView name, email;
    private String nameDb, emailDb, imageDb;
    private FirebaseAuth mAuth;
    private Button editProfile, logOut;
    private String TAG = PatientProfile.class.getSimpleName();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_page, container, false);

        image = view.findViewById(R.id.imageView);
        email = view.findViewById(R.id.email);
        name = view.findViewById(R.id.full_name);
        mAuth = FirebaseAuth.getInstance();
        editProfile = view.findViewById(R.id.edit_profile_btn);
        SocioArrow = view.findViewById(R.id.go_to1);
        logOut = view.findViewById(R.id.log_out_btn);

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
      logOut.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              signOut();
          }
      });


    //display current user name and email



        return view;
    }

    //TODO get image from database

    public void displayName(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            nameDb = user.getDisplayName();
            emailDb = user.getEmail();
         //   imageDb = user.getPhotoUrl().toString();
            // If the above were null, iterate the provider data
            // and set with the first non null data
            for (UserInfo userInfo : user.getProviderData()) {
                if (nameDb == null && userInfo.getDisplayName() != null && emailDb == null && userInfo.getEmail() != null
                         ) {
                    nameDb = userInfo.getDisplayName();
                    emailDb = userInfo.getEmail();
                  //  imageDb = userInfo.getPhotoUrl().toString();
                    MySharedPreference.putString(getContext(), "name", nameDb);
                    MySharedPreference.putString(getContext(), "email", emailDb);
                    MySharedPreference.putString(getContext(), "image", imageDb);

                }
            }
            name.setText(nameDb);
            email.setText(emailDb);
          //  if(imageDb !=null)
           // Picasso.get().load(imageDb).into(image);


        }//end if
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!MySharedPreference.getString(getContext(), "name", "").equals("")) {
            name.setText(MySharedPreference.getString(getContext(), "name", ""));
        } else if (!MySharedPreference.getString(getContext(), "email", "").equals("")) {
            email.setText(MySharedPreference.getString(getContext(), "email", ""));
        } //else if (!MySharedPreference.getString(getContext(), "image", "").equals("")) {
          //  imageDb = MySharedPreference.getString(getContext(), "image", "");
          //  if(imageDb !=null)
          //  Picasso.get().load(imageDb).into(image);
       // }
        else {

            displayName();
        }
    }

    public void signOut() {
        // to delete the token after logout to not send him notification if he is logout
        DocumentReference userTokenDR = FirebaseFirestore.getInstance().collection("Tokens").document(mAuth.getUid());
        Token mToken = new Token("");
        final Map<String, Object> tokenh = new HashMap<>();
        tokenh.put("token",mToken);
// Set the "isCapital" field of the city 'DC'
        userTokenDR
                .update("token", "")
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
}
