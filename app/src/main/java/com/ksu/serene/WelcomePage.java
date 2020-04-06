package com.ksu.serene;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.controller.signup.Signup;

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
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));

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


//        // For sign in with google need to create GoogleSigninOption object
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this , gso);
//
//        // When click to sign up with google will show sign up with google page
//        signInWithGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);//
//                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//                startActivityForResult(signInIntent, 9001);
//
//            }
//        });


        if (checkUserLogin()) {

            //user is logged in
            Intent intent = new Intent(WelcomePage.this, MainActivity.class);
            startActivity(intent);
            finish();

        }

    }// End onCreate

/*    //for sign up with google
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
    }*/

   /* private void firebaseAuthWithGoogle (GoogleSignInAccount account){

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
                            final String password = "google";

                            //if it first time go to Que either go to Home
                            //search in firebase for same emil
                            CollectionReference reference = FirebaseFirestore.getInstance().collection("Patient");
                            final Query query = reference.whereEqualTo("email",email);
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            //so here the email founded so the user sign in before go to Home page

                                            // TODO : LOGIN ??
                                            Intent intent = new Intent(WelcomePage.this, HomeFragment.class);
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
                                        // TODO : EDIT CODE HERE
                                        // here the email not founded so go to next step of register and then save the name and email in firebase
                                        mAuth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(WelcomePage.this, "success", Toast.LENGTH_LONG);

                                                            FirebaseUser userf = mAuth.getCurrentUser();

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


                                                            Intent i = new Intent( WelcomePage.this, Questionnairs.class );
                                                            startActivity(i);
                                                            finish();
                                                        }

                                                    }
                                                });

                                    }
                                }
                            });

                        } else {
                            Log.w("TAG", "signInWithCredential:failure", task.getException());

                            Toast.makeText(WelcomePage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/

/*    public  void updateToken(String token){

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
    }*/

    private boolean checkUserLogin() {

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            return true;
        } else
            return false;

    }

}
