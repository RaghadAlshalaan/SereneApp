package com.ksu.serene.Controller.Homepage.Patient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.R;

import java.util.HashMap;
import java.util.Map;


public class AddDoctor extends AppCompatActivity {

    private EditText name, email;
    private Button confirm;
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private String TAG = AddDoctor.class.getSimpleName();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_doctor);
        // Inflate the layout for this fragment
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        confirm = findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 addDoctor(name.getText().toString(), email.getText().toString());

            }


        });

    }

    public void addDoctor(final String name, final String email){
        final Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        db.collection("Doctor")
                .document(mAuth.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added successfully");
                       sendEmail(name, email);
                    }
                });


    }
    public void sendEmail(String name,String email){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, "Serene system");
        i.putExtra(Intent.EXTRA_TEXT   , "Hello Doctor"+name+"you are invited to be my doctor..");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AddDoctor.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
