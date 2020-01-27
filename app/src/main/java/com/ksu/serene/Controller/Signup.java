package com.ksu.serene.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ksu.serene.Model.Token;

import com.ksu.serene.R;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;

  //  private TextView loginTV;
   // private TextInputLayout  emailIL, passwordIL;
    private EditText nameET, emailET, passwordET, confirmPasswordET;
    private Button signUpBtn;
    private String fullName, password, email, confirmPassword;
    private String TAG = Signup.class.getSimpleName();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
       // initToolBar();
        mAuth = FirebaseAuth.getInstance();

        nameET = findViewById(R.id.username);
        emailET = findViewById(R.id.emailInput);
        passwordET = findViewById(R.id.passwordInput);
        confirmPasswordET = findViewById(R.id.CpasswordInput);
        signUpBtn = findViewById(R.id.reg_btn);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    fullName = nameET.getText().toString();
                    email = emailET.getText().toString();
                    password = passwordET.getText().toString();
                    confirmPassword = confirmPasswordET.getText().toString();

                    //empty field validation
                    if (fullName.matches("") || password.matches("") || confirmPassword.matches("") || email.matches("")) {
                        Toast.makeText(Signup.this, "All fields are required",
                                Toast.LENGTH_SHORT).show();
                        return;
                    } //name validity only characters no numbers
                    else if (!fullName.matches("^[ A-Za-z]+$")) {
                        Toast.makeText(Signup.this, "Please enter full name",
                                Toast.LENGTH_SHORT).show();
                        nameET.setText("");
                        return;
                    }
                    //if the passwordET doesn't match show dialog otherwise create account
                    else if (!password.equals(confirmPassword)) {
                        Toast.makeText(Signup.this, "Password does not match, please try again",
                                Toast.LENGTH_SHORT).show();
                        passwordET.setText("");
                        confirmPasswordET.setText("");
                        return;
                    } //if password is less than 8, show message
                    else if (password.length() < 8) {
                        Toast.makeText(Signup.this, "Password is less than 8 characters, please try again",
                                Toast.LENGTH_SHORT).show();
                        passwordET.setText("");
                        confirmPasswordET.setText("");
                        return;
                    } //email format
                    else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                        Toast.makeText(Signup.this, "Please enter full name",
                                Toast.LENGTH_SHORT).show();
                        emailET.setText("");
                        return;
                    }
                    else{

                    }


                    Bundle bundle = new Bundle();
                    bundle.putString("fullName", fullName);
                    bundle.putString("email", email);
                    bundle.putString("password", password);
                    Fragment fragmentOne = new GAD7();
                    fragmentOne.setArguments(bundle);


                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.layout, fragmentOne);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();



            }
        });


      /*  public void checkEmail(String email){
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {

                @Override
                public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                    boolean check = !task.getResult().getProviders().isEmpty();

                    if(check){
                        Toast.makeText(Signup.this, "Email already exist!",
                                Toast.LENGTH_SHORT).show();
                        emailET.setText("");
                    }

                }
            });

        }*/

        //loginTV.setOnClickListener(new View.OnClickListener() {
         //   @Override
           // public void onClick(View view) {
           //     Intent intent = new Intent(SignUp.this, LoginActivity.class);
           //     startActivity(intent);
          //      finish();
        //    }
     //   });

    }//end onCreate()


}
