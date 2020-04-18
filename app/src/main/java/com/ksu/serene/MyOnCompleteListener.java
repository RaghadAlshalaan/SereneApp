package com.ksu.serene;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import androidx.annotation.NonNull;

public class MyOnCompleteListener<T> implements com.google.android.gms.tasks.OnCompleteListener<com.google.firebase.auth.AuthResult> {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){
            System.out.println(task);
        }
        else {
            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                System.out.println(R.string.ExistUser);

            } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                System.out.println(R.string.PasswordShort);

            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                System.out.println(R.string.NotValidEmail);

            } else {
                // If sign in fails, display a message to the user.
                System.out.println(R.string.SignUpFialed);
            }
        }
    }
}
