package com.ksu.serene.Controller.Homepage.Patient;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.Model.MySharedPreference;
import com.ksu.serene.R;


public class MyDoctor extends AppCompatActivity {


    private TextView email, name;
    private Button delete;
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Doctor").document(mAuth.getUid()).delete();
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();

        if (db.collection("Doctor").document(mAuth.getUid()).get()!= null) {
            DocumentReference docRef = db.collection("Doctor").document(mAuth.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            name.setText(document.getString("name"));
                            email.setText(document.getString("email"));
                        } else {
                            Log.d("LOGGER", "No such document");
                        }
                    } else {
                        Log.d("LOGGER", "get failed with ", task.getException());
                    }
                }
            });
        }



    }
}
