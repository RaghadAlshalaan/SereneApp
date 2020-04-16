package com.ksu.serene;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.controller.signup.Questionnairs;
import com.ksu.serene.controller.signup.Signup;
import com.ksu.serene.model.Token;

import java.util.HashMap;
import java.util.Map;

public class WelcomePage extends AppCompatActivity {

    private Button logIn;
    private Button register;
    private ImageView signInWithGoogle;

    private String TAG = WelcomePage.class.getSimpleName();

    //create googleAPClient object
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private GoogleSignInClient mGoogleSignInClient;
    private boolean foundEmail = false;
    private Task<SignInMethodQueryResult> result;


    // TODO : SIGN UP WITH GOOGLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        getSupportActionBar().hide();

        // Change status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.darkAccent));


        logIn = findViewById(R.id.login);
        register = findViewById(R.id.register);
        signInWithGoogle = findViewById(R.id.signup_withgoogle);


        logIn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomePage.this, LogInPage.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomePage.this, Signup.class);
                startActivity(intent);
            }
        });

        if (checkUserLogin()) {
            //TODO check if all sign up completed then go to main
            //TODO else got to the sign up settings pages
            //user is logged in
            Intent intent = new Intent(WelcomePage.this, MainActivity.class);
            startActivity(intent);
            finish();

        }

        // Configure sign-in to request the user's ID, email address, and basic
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 9001);
            }
        });


    }// End onCreate

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 9001) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account);
            //TODO check when the user already signed before using this email
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            final String name = user.getDisplayName();
                            final String email = user.getEmail();
                            final String password = "google";
                            final String id = user.getUid();
                            //search in firebase for same emil
                            checkRegisterUserGoogle (email,name, id);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    public void checkRegisterUserGoogle (final String email,final String name,final String id) {
        //search in DB if email found
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Patient");
        final Query query = reference.whereEqualTo("email",email);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        //so here the email founded so the user sign in before go to Home page

                        // TODO : LOGIN ??
                        Intent intent = new Intent(WelcomePage.this, MainActivity.class);
                        intent.putExtra("Name", name);
                        intent.putExtra("Email", email);
                        startActivity(intent);
                    }
                }
            }
        });
        //if reach this line mean that the email not found
        SaveGoogleUser (email,name,id);
    }

    public void SaveGoogleUser (String email, String name, final String id) {
        //First save the user in patient
        // Create a new user with a email and  name
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("name", name);
        // Add a new document with a sending ID
        db.collection("Patient")
                .document(id)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added with");
                        //add token
                        Token mToken = new Token("");
                        // Add a new document with a sending ID
                        db.collection("Tokens")
                                .document(mAuth.getCurrentUser().getUid())
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
                });
        updateToken(FirebaseInstanceId.getInstance().getToken(), mAuth.getCurrentUser().getUid());
        SharedPreferences sp = getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("CURRENT_USERID", mAuth.getCurrentUser().getUid());
        editor.apply();
        //so here go to Questionaries
        Toast.makeText(WelcomePage.this, "Sign in with Google Success", Toast.LENGTH_LONG).show();
        Intent i = new Intent(WelcomePage.this, Questionnairs.class);
        startActivity(i);
        finish();

    }

    public  void updateToken(String token, final String id) {

        DocumentReference userTokenDR = FirebaseFirestore.getInstance().collection("Tokens").document(id);
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

    private boolean checkUserLogin() {

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            return true;
        } else
            return false;

    }

}
