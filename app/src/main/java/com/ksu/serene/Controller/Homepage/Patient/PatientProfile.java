package com.ksu.serene.Controller.Homepage.Patient;

import android.os.Bundle;
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

import com.ksu.serene.Controller.Signup.Sociodemo;
import com.squareup.picasso.Picasso;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.ksu.serene.Model.MySharedPreference;
import com.ksu.serene.R;

public class PatientProfile extends Fragment {

    private ImageView image, SocioArrow;
    private TextView name, email;
    private String nameDb, emailDb, imageDb;
    private FirebaseAuth mAuth;
    private Button editProfile;


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

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentOne = new Editprofile();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.layout, fragmentOne);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

    });
        SocioArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentOne = new EditSocio();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.layout, fragmentOne);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



    //display current user name and email



        return view;
    }

   /* public void displayName(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            nameDb = user.getDisplayName();
            emailDb = user.getEmail();
            imageDb = user.getPhotoUrl().toString();
            // If the above were null, iterate the provider data
            // and set with the first non null data
            for (UserInfo userInfo : user.getProviderData()) {
                if (nameDb == null && userInfo.getDisplayName() != null && emailDb == null && userInfo.getEmail() != null
                        && imageDb == null && userInfo.getPhotoUrl().toString() != null) {
                    nameDb = userInfo.getDisplayName();
                    emailDb = userInfo.getEmail();
                    imageDb = userInfo.getPhotoUrl().toString();
                    MySharedPreference.putString(getContext(), "name", nameDb);
                    MySharedPreference.putString(getContext(), "email", emailDb);
                    MySharedPreference.putString(getContext(), "image", imageDb);

                }
            }
            name.setText(nameDb);
            email.setText(emailDb);
            Picasso.get().load(imageDb).into(image);


        }//end if
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!MySharedPreference.getString(getContext(), "name", "").equals("")) {
            name.setText(MySharedPreference.getString(getContext(), "name", ""));
        } else if (!MySharedPreference.getString(getContext(), "email", "").equals("")) {
            email.setText(MySharedPreference.getString(getContext(), "email", ""));
        } else if (!MySharedPreference.getString(getContext(), "image", "").equals("")) {
            imageDb = MySharedPreference.getString(getContext(), "image", "");
            Picasso.get().load(imageDb).into(image);
        } else {

            displayName();
        }
    }*/
}
