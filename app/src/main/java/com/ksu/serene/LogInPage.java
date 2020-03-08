package com.ksu.serene;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
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
import com.ksu.serene.Controller.Constants;
import com.ksu.serene.Controller.Signup.Signup;
import com.ksu.serene.Model.Token;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

//this is the controller
public class LogInPage extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private Button loginBtn;
    private TextView signup , Error;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String mToken;
    private String TAG = LogInPage.class.getSimpleName();
    private TextView forgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        getSupportActionBar().hide();

        // Change status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));

        init();

        email.addTextChangedListener(loginTextWatcher);
        password.addTextChangedListener(loginTextWatcher);


    }// end onCreate

    private void init() {

        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        loginBtn = findViewById(R.id.loginBtn);
        signup = findViewById(R.id.registerTV);
        forgotPassword = findViewById(R.id.forgetPassword);
        forgotPassword.setOnClickListener( this );
        Error = findViewById(R.id.Error);
        Error.setText("");
        signup.setOnClickListener ( this );
        loginBtn.setOnClickListener( this );

    }


    private TextWatcher loginTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Error.setText("");

            String emailInput = email.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();

            if( !emailInput.equals("") & !passwordInput.equals("") ){

                loginBtn.setBackground(getResources().getDrawable(R.drawable.main_button));

                loginBtn.setEnabled(true);

            }else{
                loginBtn.setBackground(getResources().getDrawable(R.drawable.off_button));

                loginBtn.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginBtn:

                login( email.getText().toString(), password.getText().toString() );

                break;

            case R.id.forgetPassword:

                forgotPassword();

                break;

            case R.id.registerTV:

                Intent i = new Intent(LogInPage.this, Signup.class);
                startActivity(i);

                break;

            default:
                break;
        }//end switch

    }

    private void forgotPassword() {

        final android.app.AlertDialog.Builder resetPasswordDialog = new android.app.AlertDialog.Builder(LogInPage.this);
        resetPasswordDialog.setTitle("Reset Password");

        final EditText forgetEmailET = new EditText(LogInPage.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        forgetEmailET.setLayoutParams(lp);

        resetPasswordDialog.setMessage("If you do not know your current password,\nyou may change it.\nPlease enter your email");

        resetPasswordDialog.setView(forgetEmailET);

        resetPasswordDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });//end setNegativeButton

        resetPasswordDialog.setPositiveButton("Request New Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if ( forgetEmailET.getText().toString().equals("") || !forgetEmailET.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    Toast.makeText(getApplication(), "Please, enter your email correctly.", Toast.LENGTH_SHORT).show();
                    forgotPassword();

                } else resetPassword(forgetEmailET.getText().toString());

            }
        });//end setPositiveButton

        resetPasswordDialog.show();

    }

    private void resetPassword(String email){

        mAuth.sendPasswordResetEmail(email)

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LogInPage.this, "Email has been sent to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LogInPage.this, "Failed to send reset email! Try again in moments.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void login(String emailIn, String passwordIn) {

        mAuth.signInWithEmailAndPassword(emailIn, passwordIn)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        checkIfEmailVerified();

                        Intent intent = new Intent(LogInPage.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "signInWithEmail:success");

                    } else {

                        if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            //there is'n user with this Email
                            Error.setText("* There is not user with this Email, Please try again!");

                        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            //the password is wrong
                            Error.setText("* Wrong Password, Please try again!");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInPage.this, R.string.auth,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
    }

    private void checkIfEmailVerified() {

        // TODO : MOVE IT TO MAIN PAGE
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
            SharedPreferences sp = getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("CURRENT_USERID",mAuth.getCurrentUser().getUid());
            editor.apply();

        }
        else
        {

            //TODO : MOVE TO MAIN PAGE & add to notification list (PLEASE VERIFY YOUR EMAIL)

        }

    }// end checkIfEmailVerified

    public  void updateToken(String token){

        DocumentReference userTokenDR = FirebaseFirestore.getInstance().collection("Tokens").document(mAuth.getUid());
        Token mToken = new Token(token);
        final Map<String, Object> tokenh = new HashMap<>();
        tokenh.put("token",mToken.getToken());

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

            Intent intent = new Intent(LogInPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        checkUserState();
    }

}
