package com.ksu.serene.controller.main.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;
import com.ksu.serene.WelcomePage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class AddDoctor extends AppCompatActivity {

    private EditText name, email;
    private Button confirm;
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private String TAG = AddDoctor.class.getSimpleName();
    private Random r = new Random();
    private ImageView back;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_doctor);

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.nameET);
        email = findViewById(R.id.emailET);
        confirm = findViewById(R.id.confirm);
        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ! EmptyFields(name.getText().toString(),email.getText().toString() )) {
                    Toast.makeText(AddDoctor.this, R.string.EmptyFields, Toast.LENGTH_LONG).show();
                    return;
                }
                else {


                    if (! checkEmailValidation (email.getText().toString())){
                        Toast.makeText(AddDoctor.this, R.string.emailFormat,Toast.LENGTH_LONG).show();
                        email.setText("");
                        return;
                    }

                    if (! checkNameValidation(name.getText().toString())){
                        Toast.makeText(AddDoctor.this, R.string.nameFormat,Toast.LENGTH_LONG).show();
                        name.setText("");
                        return;
                    }

                    addDoctor(name.getText().toString(), email.getText().toString());

                    //finish();
                }

            }


        });
    }

    public void addDoctor(final String name, final String email) {
        final DocumentReference userRev = FirebaseFirestore.getInstance().collection("Patient").document(mAuth.getUid());
        userRev.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("email").toString().equals(email)) {
                    Toast.makeText(AddDoctor.this, R.string.PatientEmail, Toast.LENGTH_LONG).show();
                } else {

                    String ID = String.valueOf(r.nextInt(999999 - 111 + 1) + 111);
                    final Map<String, Object> user = new HashMap<>();
                    user.put("name", name);
                    user.put("email", email);
                    user.put("patientID", mAuth.getUid());
                    user.put("isVerified", false);
                    user.put("id", ID);
                    db.collection("Doctor")
                            .document(ID)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot added successfully");
                                    Toast.makeText(AddDoctor.this, R.string.AddDocSuccess,
                                            Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(AddDoctor.this, PatientProfile.class);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddDoctor.this, R.string.AddDocFialed,
                                            Toast.LENGTH_LONG).show();
                                }
                            });


                }

            }
        });
    }

    public boolean EmptyFields (String name, String email) {
        if ( name.equals("") || email.equals("") ){// name == null || email == null
            return false;
        }
        return true;
    }

    public boolean checkEmailValidation (String email) {
        if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            return false;
        }
        return true;
    }

    public boolean checkNameValidation (String name) {
        if (!name.matches("^[ A-Za-z-.]+$")){
            return false;
        }
        return true;
    }


}
