package com.ksu.serene.controller.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.LogInPage;

import com.ksu.serene.fitbitManager.Util;
import com.ksu.serene.model.Patient;
import com.ksu.serene.model.Token;
import com.ksu.serene.R;
import com.ksu.serene.MyOnCompleteListener;

import java.util.HashMap;
import java.util.Map;


public class Signup extends AppCompatActivity {

    private TextView loginTV, Error;
    private EditText nameET, emailET, passwordET, confirmPasswordET;
    private Button signupBtn;
    private String TAG = Signup.class.getSimpleName();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean createAcc = false;
    private  Patient user;

    /*public Signup(FirebaseAuth mockMAuth) {
        this.mAuth = mockMAuth;
    }*/

    public void setmAuth(FirebaseAuth mockMAuth) {
        this.mAuth = mockMAuth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        SharedPreferences sp = getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
        String preferred_lng = sp.getString("PREFERRED_LANGUAGE", "en");
        Util.setLocale(preferred_lng, this);

        getSupportActionBar().hide();

        // Change status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.darkAccent));


        init();

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validName(nameET.getText().toString())) {
                    nameET.setText("");
                    Error.setText(R.string.NotValidName);
                    return;
                }
                if (!passMatch(passwordET.getText().toString(), confirmPasswordET.getText().toString())) {
                    passwordET.setText("");
                    confirmPasswordET.setText("");
                    Error.setText(R.string.NotMatchPass);
                    return;
                }
                if (createUserAccount(emailET.getText().toString(), passwordET.getText().toString(), confirmPasswordET.getText().toString(), nameET.getText().toString()) != null) { //;
                   /*Intent i = new Intent( Signup.this, Questionnairs.class );
                   startActivity(i);
                   finish();*/
                }
            }
        });

        nameET.addTextChangedListener(signUpTextWatcher);
        emailET.addTextChangedListener(signUpTextWatcher);
        passwordET.addTextChangedListener(signUpTextWatcher);
        confirmPasswordET.addTextChangedListener(signUpTextWatcher);

    }//end onCreate()


    private void init() {

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

            if (CheckFields(nameInput, emailInput, passwordInput, confirmPasswordInput)) {

                signupBtn.setBackground(getResources().getDrawable(R.drawable.main_button));

                signupBtn.setEnabled(true);

            } else {

                signupBtn.setBackground(getResources().getDrawable(R.drawable.off_button));

                signupBtn.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    public Patient createUserAccount(final String email, String password, String confirmPassword, final String name) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new MyOnCompleteListener<AuthResult>(){//new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = new Patient(name,email);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

                            updateProfile (profileUpdates);

                            updateEmail (email);

                            // Create a new user with a first and last name
                            saveUserinDB (name,email);

                            /*Map<String, Object> user = new HashMap<>();
                            user.put("email", email);
                            user.put("name", name);

                            // Add a new document with a generated ID
                            db.collection("Patient")
                                    .document(mAuth.getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot added with");
                                            //add token
                                            Token mToken = new Token("");

                                            // Add a new document with a generated ID
                                            db.collection("Tokens")
                                                    .document(mAuth.getUid())
                                                    .set(mToken)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "DocumentSnapshot added with");
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
                                    });*/

                            updateToken("");
                            SharedPreferences sp = getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("CURRENT_USERID", mAuth.getCurrentUser().getUid());
                            editor.apply();

                            //Toast.makeText(Signup.this, R.string.SignUpSuccess, Toast.LENGTH_LONG).show();
                            createAcc = true;
                            Intent i = new Intent(Signup.this, Questionnairs.class);
                            startActivity(i);
                            sendVerificationEmail();
                            finish();

                        } else {

                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Error.setText(R.string.ExistUser);
                                createAcc = false;
                                //user = null;

                            } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                Error.setText(R.string.PasswordShort);
                                createAcc = false;
                                //user = null;

                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Error.setText(R.string.NotValidEmail);
                                createAcc = false;
                                //user = null;

                            } else {
                                // If sign in fails, display a message to the user.
                                Error.setText(R.string.SignUpFialed);
                                createAcc = false;
                                //user = null;
                            }

                        }
                    }
                });
        return user;
    }// end create user

    public void updateProfile (UserProfileChangeRequest profileUpdates) {
        FirebaseUser userf = mAuth.getCurrentUser();
        userf.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    public void updateEmail (String email) {
        FirebaseUser userf = mAuth.getCurrentUser();
        userf.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });
    }

    public void saveUserinDB (String name, String email) {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("name", name);
        user.put("first_fitbit","");
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
    }

    public void updateToken(String token) {

        DocumentReference userTokenDR = FirebaseFirestore.getInstance().collection("Tokens").document(mAuth.getUid());
        Token mToken = new Token(token);
        final Map<String, Object> tokenU = new HashMap<>();
        tokenU.put("token", mToken.getToken());

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

    public void sendVerificationEmail() {

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

    public boolean CheckFields(String name, String email, String password, String confirmPassword) {
        if (!name.equals("") && name != null && !email.equals("") && email != null
                && !password.equals("") && password != null && !confirmPassword.equals("") && confirmPassword != null) {
            return true;
        }
        return false;
    }

    public boolean validName(String name) {
        if (!name.matches("^[ A-Za-z]+$")) {
            return false;
        }
        return true;
    }

    //TODO add method to check for email validation
    public boolean isValidEmail(String email) {
        if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            return false;
        }
        return true;
    }

    //TODO add Method for check for pass length
    public boolean isShortPass(String password) {
        if (password.length() < 6) {
            return false;
        }
        return true;
    }


    public boolean passMatch(String pass, String confirmPass) {
        if (!(pass.equals(confirmPass))) {
            return false;
        }
        return true;
    }

    private void login() {

        //Login button
        Intent i = new Intent(this, LogInPage.class);
        startActivity(i);
        finish();

    }


}
