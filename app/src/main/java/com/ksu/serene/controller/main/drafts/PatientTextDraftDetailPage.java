package com.ksu.serene.controller.main.drafts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;

public class PatientTextDraftDetailPage extends AppCompatActivity {

    private EditText title;
    private EditText subj;
    private Button delete;
    private Button edit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TDID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_text_draft_detail_page);

        getSupportActionBar().hide();

        TDID = getIntent().getStringExtra("TextDraftID");
        title = findViewById(R.id.TitleTextD);
        subj = findViewById(R.id.SubjtextD);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.SaveChanges);

        db.collection("TextDraft")
                .document(TDID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                title.setText(documentSnapshot.get("title").toString());
                subj.setText(documentSnapshot.get("text").toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(PatientTextDraftDetailPage.this, "Fails to get data", Toast.LENGTH_LONG);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show window dialog with 2 button yes and no
                new AlertDialog.Builder(PatientTextDraftDetailPage.this)
                        .setTitle("Delete Draft")
                        .setMessage("Are you sure you want to delete this Draft ? ")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, final int i) {
                                db.collection("TextDraft")
                                        .document(TDID)
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(PatientTextDraftDetailPage.this, "Text Draft deleted successfully", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(PatientTextDraftDetailPage.this, "Text Draft did not delete", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("TextDraft")
                        .document(TDID)
                        .update("title" , title.getText().toString(),
                                "text",subj.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //Toast.makeText(PatientTextDraftDetailPage.this, "Updated Successfully", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Toast.makeText(PatientTextDraftDetailPage.this, "Did Not Update, Try Again", Toast.LENGTH_LONG).show();
                            }
                        });

            }
        });

    }
}
