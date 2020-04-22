package com.ksu.serene.controller.main.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;


public class MyDoctor extends AppCompatActivity {

    private Button delete, save;
    private TextView nameET, emailET;
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private ImageView check,back;
    private boolean checkDoctor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_doctor);

        // Inflate the layout for this fragment
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        delete = findViewById(R.id.delete);
        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        save = findViewById(R.id.SaveChanges);
        check = findViewById(R.id.check);
        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MyDoctor.this);
                dialog.setTitle(R.string.DeleteDoc);
                dialog.setMessage(R.string.DeleteMessageDoc);
                dialog.setPositiveButton(R.string.DeleteOKDoc, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("Doctor")
                                .whereEqualTo("patientID", mAuth.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if(!task.getResult().isEmpty()){
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    if(document.exists()) {

                                                        DocumentReference d= document.getReference();
                                                        d.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(MyDoctor.this,R.string.DocDeletedSuccess,
                                                                        Toast.LENGTH_LONG).show();
                                                                Intent in = new Intent(MyDoctor.this, PatientProfile.class);
                                                                startActivity(in);
                                                                //finish();
                                                            }
                                                        })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(MyDoctor.this,R.string.DocDeletedFialed,
                                                                                Toast.LENGTH_LONG).show();
                                                                    }
                                                                });

                                                    }
                                                }
                                            }

                                        }
                                        else {
                                            Toast.makeText(MyDoctor.this, task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }); //end dialog onComplete
                    }



                });// end dialog onclick

                dialog.setNegativeButton(R.string.DeleteCancleDoc,null);

                AlertDialog alertDialog =  dialog.create();
                alertDialog.show();

            }
        });

        // Set name and email
        setData();
    }

    private void setData() {


        db.collection("Doctor")
                .whereEqualTo("patientID", mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.exists()) {
                                       nameET.setText(document.getString("name"));
                                       emailET.setText(document.getString("email"));
                                    }
                                }
                            }

                        }
                        else {

                        }
                    }
                }); //end db

    }

    @Override
    public void onResume() {
        super.onResume();

        db.collection("Doctor")
                .whereEqualTo("patientID", mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.exists()) {
                                        nameET.setText(document.getString("name"));
                                        emailET.setText(document.getString("email"));
                                        checkDoctor = document.getBoolean("isVerified");
                                        if(checkDoctor == true){
                                            check.setVisibility(View.VISIBLE);
                                        }

                                    }
                                }
                            }

                        }

                    }
                });

    }
}
