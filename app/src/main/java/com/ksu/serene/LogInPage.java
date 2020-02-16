package com.ksu.serene;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.ksu.serene.Controller.Signup.Signup;
import com.ksu.serene.Controller.Homepage.Home.HomeFragment;
import com.ksu.serene.Model.Token;

import java.util.HashMap;
import java.util.Map;

//this is the controller
public class LogInPage extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button logIn;
    private TextView signup;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String emailIn, passwordIn, mToken;
    private String TAG = LogInPage.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        logIn = findViewById(R.id.Log_In);
        signup = findViewById(R.id.sign_up);

        signup.setOnClickListener ( new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                Intent intent = new Intent(LogInPage.this, Signup.class);
                startActivity(intent);
              }
         }
        );

        logIn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                emailIn = email.getText().toString();
                passwordIn = password.getText().toString();
                //check all fields not empty
                if (checkAllFields(emailIn , passwordIn)) {

                    mAuth.signInWithEmailAndPassword(emailIn, passwordIn)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        checkIfEmailVerified();
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");

                                    } else {
                                        if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                            //there is'n user with this Email
                                            Toast.makeText(LogInPage.this, R.string.emailpassWrong,
                                                    Toast.LENGTH_SHORT).show();

                                        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                            //the password is wrong
                                            Toast.makeText(LogInPage.this, R.string.emailpassWrong,
                                                    Toast.LENGTH_SHORT).show();

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                            Toast.makeText(LogInPage.this, R.string.auth,
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    // ...
                                }
                            });
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.allFields, Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

      }

    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LogInPage.this,  new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    mToken = instanceIdResult.getToken();
                    Log.e("Token",mToken);
                }
            });
            updateToken(mToken);
            SharedPreferences sp = getSharedPreferences("user_details", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("CURRENT_USERID",mAuth.getCurrentUser().getUid());
            editor.apply();
            // user is verified
            Intent intent = new Intent(LogInPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            Toast.makeText(LogInPage.this, R.string.emailVerify, Toast.LENGTH_SHORT).show();

            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }
    public  void updateToken(String token){

        DocumentReference userTokenDR = FirebaseFirestore.getInstance().collection("Tokens").document(mAuth.getUid());
        Token mToken = new Token(token);
        final Map<String, Object> tokenh = new HashMap<>();
        tokenh.put("token",mToken.getToken());
        // Set the "isCapital" field of the city 'DC'
        //
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
    private void checkUserState() {

        if (mAuth.getCurrentUser() != null) {
            updateToken(FirebaseInstanceId.getInstance().getToken());
            SharedPreferences sp = getSharedPreferences("user_details", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("CURRENT_USERID",mAuth.getCurrentUser().getUid());
            editor.apply();

            //MySharedPreference.putString(LoginActivity.this,"user_id",mAuth.getCurrentUser().getUid());

            Intent intent = new Intent(LogInPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private boolean checkAllFields (String email , String password){
        if ( !(TextUtils.isEmpty(email)) && !(TextUtils.isEmpty(password)) ){
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserState();
    }
}
