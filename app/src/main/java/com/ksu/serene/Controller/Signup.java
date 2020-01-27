package com.ksu.serene.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
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
    private String TAG = Signup.class.getSimpleName();
    //image view for sign in with google
    private ImageView signInWithGoogle ;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //create googleAPClient object
    private GoogleApiClient mGoogleApiClient;



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
                .build();
        //when click to sign up with google will show sign up with google page
        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, 9001);
             }
         }
        );


        //Fragment fragmentOne = new GAD7();
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserAccount(emailET.getText().toString(), passwordET.getText().toString(), confirmPasswordET.getText().toString(), nameET.getText().toString());
              /*  String fullName = nameET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String confirmPassword = confirmPasswordET.getText().toString();

                if (fullName == null || password == null || confirmPassword == null || email == null) {
                    Toast.makeText(Signup.this, "All fields are required",
                            Toast.LENGTH_SHORT).show();}
                //if the passwordET doesn't match show dialog otherwise create account

                else   if (!password.equals(confirmPassword)) {
                    // passwordIL.setError("the password  does't match, please try again");
                    Toast.makeText(Signup.this,"Password does not match, please try again",
                            Toast.LENGTH_SHORT).show();
                    passwordET.setText("");
                    confirmPasswordET.setText("");
                }
                Intent intent =  new Intent(Signup.this, GAD7.class);
                intent.putExtra("fullName", fullName);
                intent.putExtra("email", email);
                startActivity(intent);*/
            }
        });

     //   nameET.addTextChangedListener(signUpTextWatcher);
       /// emailET.addTextChangedListener(signUpTextWatcher);
       // passwordET.addTextChangedListener(signUpTextWatcher);
       // confirmPasswordET.addTextChangedListener(signUpTextWatcher);

        //loginTV.setOnClickListener(new View.OnClickListener() {
         //   @Override
           // public void onClick(View view) {
           //     Intent intent = new Intent(SignUp.this, LoginActivity.class);
           //     startActivity(intent);
          //      finish();
        //    }
     //   });

    }//end onCreate()

    private TextWatcher signUpTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String nameInput = nameET.getText().toString().trim();
            String emailInput = emailET.getText().toString().trim();
            String passwordInput = passwordET.getText().toString().trim();
            String confirmPasswordInput = confirmPasswordET.getText().toString().trim();

            signUpBtn.setEnabled(!nameInput.equals("") & !emailInput.equals("") & !passwordInput.equals("") & !confirmPasswordInput.equals(""));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    public void createUserAccount(final String email, final String password, String confirmPassword, final String name) {
       // if one of the fields is empty display message
        if (nameET == null || passwordET == null || confirmPasswordET == null || emailET == null) {
            Toast.makeText(Signup.this, "All fields are required",
                    Toast.LENGTH_SHORT).show();}

        //if the passwordET doesn't match show dialog otherwise create account
     else   if (!password.equals(confirmPassword)) {
           // passwordIL.setError("the password  does't match, please try again");
            Toast.makeText(Signup.this,"Password does not match, please try again",
                    Toast.LENGTH_SHORT).show();
            passwordET.setText("");
            confirmPasswordET.setText("");
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
                            final Map<String, Object> user = new HashMap<>();
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

                            sendVerificationEmail();

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(Signup.this,"User with Email id already exists",
                                        Toast.LENGTH_SHORT).show();
                              //  emailIL.setError("User with Email id already exists");
                              //  passwordIL.setError(null);
                            } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                Toast.makeText(Signup.this,"Password is weak, password must a least 6 characters",
                                        Toast.LENGTH_SHORT).show();
                                //passwordIL.setError("Password is weak, password must a least 6 characters");
                                passwordET.setText("");
                                confirmPasswordET.setText("");


                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(Signup.this,"incorrect email format",
                                        Toast.LENGTH_SHORT).show();
                               // emailIL.setError("incorrect email format ");
                              //  passwordIL.setError(null);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Signup.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }

                        // ...
                    }
                });
    }//end createUserAccount()

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            //showDialog("Successfully create account, we have send to your email verification link to active your account. ");
                        } else {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            Log.d(TAG, "there is problem the email doesn't send: ");

                        }
                    }
                });
    }

   /* public void showDialog(String msg) {
        androidx.appcompat.app.AlertDialog.Builder alertDialog = new AlertDialog.Builder(Signup.this);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // after email is sent just logout the user and finish this activity
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Signup.this, LoginActivity.class));
                finish();
            }
        });
        alertDialog.show();
    }//end showDialog() */

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }//end onSupportNavigateUp

    private void initToolBar() {
        setTitle("Create Account");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }//end initToolBar()

    //for sign up with google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9001){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        }
    }
    private void handleSignInResult (GoogleSignInResult result){
        Log.d(TAG , "handleSignInResult" + result.isSuccess());
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
        }
        else {

        }
    }


}
