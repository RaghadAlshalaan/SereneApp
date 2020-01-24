package com.ksu.serene;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
//this is the model for the login page
public class Patient {

    private int id;
    private String name;
    private String email;
    private String password;
    //private Doctor doctor;
   //private Reminders [] reminders;
    //private Drafts [] drafts;

    public boolean login (String email , String password) {
        final boolean[] login = new boolean[1];
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            login[0] = true;
                            /*Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);*/
                        } else {
                            // If sign in fails, display a message to the user.
                            //display a specific message when  the sign in fails for invalid email/password or the email did not verified
                            login[0] = false;
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            //updateUI(null);
                        }

                        // ...
                    }
                });
        return login[0];
    }
}
