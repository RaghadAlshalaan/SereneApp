package com.ksu.serene.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ksu.serene.Controller.Homepage.Home.HomeFragment;
import com.ksu.serene.LogInPage;

import com.ksu.serene.R;


public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView loginTV;

    private EditText nameET, emailET, passwordET, confirmPasswordET;
    private Button signUpBtn;
    private String fullName, password, email, confirmPassword;
    private String TAG = Signup.class.getSimpleName();
    private boolean isNewUser;
    //image view for sign in with google
    private ImageView signInWithGoogle ;

    private Task<SignInMethodQueryResult> result;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //create googleAPClient object
    private GoogleApiClient mGoogleApiClient;//
    private GoogleSignInClient mGoogleSignInClient;
    private boolean foundEmail = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
       // initToolBar();
        mAuth = FirebaseAuth.getInstance();
        loginTV = findViewById(R.id.description2);
        nameET = findViewById(R.id.username);
        emailET = findViewById(R.id.emailInput);
        passwordET = findViewById(R.id.passwordInput);
        confirmPasswordET = findViewById(R.id.CpasswordInput);
        signUpBtn = findViewById(R.id.reg_btn);
        isNewUser = true;
        signInWithGoogle = findViewById(R.id.signup_withgoogle);

        //for sign in with google need to create GoogleSigninOption object
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(Signup.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed (ConnectionResult result){
                        Log.d(TAG , "OnConnectionFailed" + result);
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();//

        mGoogleSignInClient = GoogleSignIn.getClient(this , gso);
        //when click to sign up with google will show sign up with google page
        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                //Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);//
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 9001);
             }
         }
        );

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this,LogInPage.class);
                startActivity(intent);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    fullName = nameET.getText().toString();
                    email = emailET.getText().toString();
                    password = passwordET.getText().toString();
                    confirmPassword = confirmPasswordET.getText().toString();


             if(email !=null) {

                 mAuth.fetchSignInMethodsForEmail(email)
                         .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                             @Override
                             public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                 isNewUser = task.getResult().getSignInMethods().isEmpty();

                                 if (isNewUser) {
                                     //

                                 } else {
                                     isNewUser = false;
                                     Toast.makeText(Signup.this, "Email already exist, please go back and enter new email",
                                             Toast.LENGTH_SHORT).show();
                                     emailET.setText("");

                                 }

                             }
                         });
             }

                    //empty field validation
                  else if (fullName.matches("") || password.matches("") || confirmPassword.matches("") || email.matches("")) {
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




        //loginTV.setOnClickListener(new View.OnClickListener() {
         //   @Override
           // public void onClick(View view) {
           //     Intent intent = new Intent(SignUp.this, LoginActivity.class);
           //     startActivity(intent);
          //      finish();
        //    }
     //   });

    }//end onCreate()


    //for sign up with google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9001){
            //GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);//
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null){
                    //make request with firebase
                    firebaseAuthWithGoogle(account);
                }
            }
            catch (ApiException e){
                e.printStackTrace();
            }

        }
    }
    private void handleSignInResult (GoogleSignInResult result){
        Log.d(TAG , "handleSignInResult" + result.isSuccess());
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
        }
        else {

        }
    }//*/

    private void firebaseAuthWithGoogle (GoogleSignInAccount account){
        Log.d("TAG" , "firebaseAuthWithGoogle: " + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken() , null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            final String name = user.getDisplayName();
                            final String email = user.getEmail();

                            //if it first time go to Que either go to Home
                            //search in firebase for same emil
                            CollectionReference reference = FirebaseFirestore.getInstance().collection("Patient");
                            final Query query = reference.whereEqualTo("email",email);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            //so here the email founded so the user sign up before go to Home page
                                            Intent intent = new Intent(Signup.this, HomeFragment.class);
                                            intent.putExtra("Name" , name);
                                            intent.putExtra("Email" , email);
                                            startActivity(intent);
                                            foundEmail = true;
                                        }
                                    }
                                    else {
                                        Log.d("TAG", "Query Failed");
                                    }
                                    if (!foundEmail){
                                        // here the email not founded so go to next step of register and then save the name and email in firebase
                                        Bundle bundle = new Bundle();
                                        bundle.putString("fullName", name);
                                        bundle.putString("email", email);
                                        bundle.putString("password", "password");
                                        Fragment fragmentOne = new GAD7();
                                        fragmentOne.setArguments(bundle);
                                        FragmentManager fm = getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                        fragmentTransaction.replace(R.id.layout, fragmentOne);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    }
                                }
                            });
                        } else {
                            Log.w("TAG", "signInWithCredential:failure", task.getException());

                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
