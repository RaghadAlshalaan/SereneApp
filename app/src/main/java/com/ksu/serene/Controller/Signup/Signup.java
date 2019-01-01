package com.ksu.serene.Controller.Signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ksu.serene.Controller.Constants;
import com.ksu.serene.Controller.Homepage.Home.HomeFragment;
import com.ksu.serene.LogInPage;

import com.ksu.serene.Model.Token;
import com.ksu.serene.R;

import java.util.HashMap;
import java.util.Map;


public class Signup extends AppCompatActivity {

    private TextView loginTV, Error;
    private EditText nameET, emailET, passwordET, confirmPasswordET;
    private Button signupBtn;
    private String TAG = Signup.class.getSimpleName();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();

        init();

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createUserAccount(emailET.getText().toString(), passwordET.getText().toString(), confirmPasswordET.getText().toString(), nameET.getText().toString());
            }
        });

        nameET.addTextChangedListener(signUpTextWatcher);
        emailET.addTextChangedListener(signUpTextWatcher);
        passwordET.addTextChangedListener(signUpTextWatcher);
        confirmPasswordET.addTextChangedListener(signUpTextWatcher);

    }//end onCreate()


    private void init(){

        mAuth = FirebaseAuth.getInstance();

        loginTV = findViewById(R.id.loginBtn);
        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        Error = findViewById(R.id.Error);
        Error.setText("");

        nameET = findViewById(R.id.username);
        emailET = findViewById(R.id.emailInput);
        passwordET = findViewById(R.id.passwordInput);
        confirmPasswordET = findViewById(R.id.CpasswordInput);
        signupBtn = findViewById(R.id.signupBtn);

    }


    private TextWatcher signUpTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Error.setText("");

            String nameInput = nameET.getText().toString().trim();
            String emailInput = emailET.getText().toString().trim();
            String passwordInput = passwordET.getText().toString().trim();
            String confirmPasswordInput = confirmPasswordET.getText().toString().trim();

            if(!nameInput.equals("") & !emailInput.equals("") & !passwordInput.equals("") & !confirmPasswordInput.equals("")){

                signupBtn.setBackground(getResources().getDrawable(R.drawable.main_button));

                signupBtn.setEnabled(true);

            }else{

                signupBtn.setBackground(getResources().getDrawable(R.drawable.off_button));

                signupBtn.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    public void createUserAccount(final String email, String password, String confirmPassword, final String name) {

        //if the passwordET doesn't match show dialog otherwise create account
        if (!name.matches("^[ A-Za-z]+$")) {
            Error.setText("* Enter valid name.");
            nameET.setText("");
            return;
        }

        if (!password.equals(confirmPassword)) {
            Error.setText("* Password doesn't match.");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser userf = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

                            userf.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });

                            userf.updateEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User email address updated.");
                                            }
                                        }
                                    });

                            // Create a new user with a first and last name
                            Map<String, Object> user = new HashMap<>();
                            user.put("email", email);
                            user.put("name", name);

                            // Add a new document with a generated ID
                            db.collection("Patient")
                                    .document(mAuth.getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot added with" );
                                            //add token
                                            Token mToken = new Token("");

                                            // Add a new document with a generated ID
                                            db.collection("Tokens")
                                                    .document(mAuth.getUid())
                                                    .set(mToken)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "DocumentSnapshot added with" );
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Error writing document", e);
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });

                            updateToken(FirebaseInstanceId.getInstance().getToken());
                            SharedPreferences sp = getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("CURRENT_USERID",mAuth.getCurrentUser().getUid());
                            editor.apply();

                            Intent i = new Intent( Signup.this, Questionnairs.class );
                            startActivity(i);
                            sendVerificationEmail();
                            finish();

                        } else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Error.setText("* Email already used by another account.");

                            } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                Error.setText("* Password must be at least 8 characters.");

                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Error.setText("* Incorrect Email format.");

                            } else {
                                // If sign in fails, display a message to the user.
                                Error.setText("* Sign in Failed, Please try again");
                            }

                        }
                    }
                });

    }// end create user


    public  void updateToken(String token){

        DocumentReference userTokenDR = FirebaseFirestore.getInstance().collection("Tokens").document(mAuth.getUid());
        Token mToken = new Token(token);
        final Map<String, Object> tokenU = new HashMap<>();
        tokenU.put("token",mToken.getToken());

        userTokenDR
                .update(tokenU)
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

    private void sendVerificationEmail() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent

                            //showDialog("Account Created! An email has been sent to activate your account.");
                            // TODO : HOME PAGE WITH SMART WATCH INFO

                        } else {
                            // email not sent, so display message and restart the activity
                            Log.d(TAG, "There is problem the email doesn't send: ");

                        }
                    }
                });
    }


    private void login(){

        //Login button
        Intent i = new Intent(this, LogInPage.class);
        startActivity(i);
        finish();

    }


   /* private FirebaseAuth mAuth;

    private TextView loginTV;

    private EditText nameET, emailET, passwordET, confirmPasswordET;
    private Button signUpBtn;
    private String fullName, password, email, confirmPassword;
    private String TAG = Signup.class.getSimpleName();
    private boolean isNewUser;
    //image view for sign in with google


    private ImageView signInWithGoogle ;
    // TODO : MOVE SIGN UP WITH GOOGLE TO WELCOME PAGE

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
        loginTV = findViewById(R.id.loginBtn);
        nameET = findViewById(R.id.username);
        emailET = findViewById(R.id.emailInput);
        passwordET = findViewById(R.id.passwordInput);
        confirmPasswordET = findViewById(R.id.CpasswordInput);
        signUpBtn = findViewById(R.id.signupBtn);
        isNewUser = true;
        signInWithGoogle = findViewById(R.id.signup_withgoogle);

        //for sign in with google need to create GoogleSigninOption object
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
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


             if(email != null) {

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
                        Toast.makeText(Signup.this, R.string.allFields,
                                Toast.LENGTH_SHORT).show();
                        return;
                    } //name validity only characters no numbers
                    else if (!fullName.matches("^[ A-Za-z]+$")) {
                        Toast.makeText(Signup.this, R.string.nameFormat,
                                Toast.LENGTH_SHORT).show();
                        nameET.setText("");
                        return;
                    }
                    //if the passwordET doesn't match show dialog otherwise create account
                    else if (!password.equals(confirmPassword)) {
                        Toast.makeText(Signup.this, R.string.passwordMatch,
                                Toast.LENGTH_SHORT).show();
                        passwordET.setText("");
                        confirmPasswordET.setText("");
                        return;
                    } //if password is less than 8, show message
                    else if (password.length() < 8) {
                        Toast.makeText(Signup.this, R.string.passwordChar,
                                Toast.LENGTH_SHORT).show();
                        passwordET.setText("");
                        confirmPasswordET.setText("");
                        return;
                    } //email format
                    else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                        Toast.makeText(Signup.this, R.string.emailFormat,
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
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9001){
            //GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);//
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //if (account != null){
                //make request with firebase
                firebaseAuthWithGoogle(account);

            }
            catch (ApiException e){
                e.printStackTrace();
            }

        }
    }

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
*/



}
