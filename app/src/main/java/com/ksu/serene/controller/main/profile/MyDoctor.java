package com.ksu.serene.controller.main.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.ksu.serene.controller.Constants;
import com.ksu.serene.controller.liveChart.utils.Utils;
import androidx.core.content.res.ResourcesCompat;
import www.sanju.motiontoast.MotionToast;

import android.content.res.Resources;

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

        SharedPreferences sp = getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
        String preferred_lng = sp.getString("PREFERRED_LANGUAGE", "en");
        Utils.setLocale(preferred_lng, this);


        // Inflate the layout for this fragment
        getSupportActionBar().hide();

        // Change status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.darkAccent));


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
                                                                /*Resources res = getResources();
                                                                String text = String.format(res.getString(R.string.DocDeletedSuccess));
                                                                MotionToast.Companion.darkToast(
                                                                        MyDoctor.this,
                                                                        text,
                                                                        MotionToast.Companion.getTOAST_SUCCESS(),
                                                                        MotionToast.Companion.getGRAVITY_BOTTOM(),
                                                                        MotionToast.Companion.getLONG_DURATION(),
                                                                        ResourcesCompat.getFont( MyDoctor.this, R.font.montserrat));*/
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
