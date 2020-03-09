package com.ksu.serene.controller.main.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ksu.serene.R;


public class MyDoctor extends AppCompatActivity {


    private TextView email, name, edit;
    private Button delete, save;
    private EditText nameET, emailET;
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private ImageView check;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_doctor);
        // Inflate the layout for this fragment
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        delete = findViewById(R.id.delete);
        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        edit = findViewById(R.id.edit);
        save = findViewById(R.id.save);
        check = findViewById(R.id.check);

        nameET.setVisibility(View.GONE);
        emailET.setVisibility(View.GONE);
        save.setVisibility(View.GONE);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MyDoctor.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("After deleting this doctor he or she will no longer be linked with your account " +
                        "and we will disable sending periodic reports to them.");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("Doctor")
                                .whereEqualTo("patientID"+mAuth.getUid().substring(0,5), mAuth.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if(!task.getResult().isEmpty()){
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    if(document.exists()) {

                                                        DocumentReference d= document.getReference();
                                                        d.update("patientID"+mAuth.getUid().substring(0,5), FieldValue.delete());


                                                    }
                                                }
                                            }
                                            Toast.makeText(MyDoctor.this, "Doctor deleted",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(MyDoctor.this, task.getException().getMessage(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }); //end dialog onComplete
                    }



                });// end dialog onclick
                dialog.setNegativeButton("Cancel",null);

                AlertDialog alertDialog =  dialog.create();
                alertDialog.show();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameET.setVisibility(View.VISIBLE);
                emailET.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);

                name.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);


                db.collection("Doctor")
                        .whereEqualTo("patientID"+mAuth.getUid().substring(0,5), mAuth.getUid())
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
        });
        //TODO add verification email link when the user changes the email
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailET.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                    Toast.makeText(MyDoctor.this, R.string.emailFormat,
                            Toast.LENGTH_SHORT).show();
                    emailET.setText("");
                    return;
            }
                else if (!nameET.getText().toString().matches("^[ A-Za-z]+$")) {
                    Toast.makeText(MyDoctor.this, R.string.nameFormat,
                            Toast.LENGTH_SHORT).show();
                    nameET.setText("");
                    return;}

                updateDoctor(nameET.getText().toString(),emailET.getText().toString());
                nameET.setVisibility(View.GONE);
                emailET.setVisibility(View.GONE);
                save.setVisibility(View.GONE);

                name.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        db.collection("Doctor")
                .whereEqualTo("patientID"+mAuth.getUid().substring(0,5), mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.exists()) {
                                        name.setText(document.getString("name"));
                                        email.setText(document.getString("email"));

                                    }
                                }
                            }

                        }
                        else {

                        }
                    }
                });

    }

    public void updateDoctor(final String name, final String email){

        db.collection("Doctor")
                .whereEqualTo("patientID"+mAuth.getUid().substring(0,5), mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.exists()) {
                                        DocumentReference d= document.getReference();
                                        d.update("name",name);
                                        d.update("email",email);
                                        Toast.makeText(MyDoctor.this, "Changes updated!",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }

                        }
                        else {
                             //do nothing
                        }
                    }
                });
    }
}
