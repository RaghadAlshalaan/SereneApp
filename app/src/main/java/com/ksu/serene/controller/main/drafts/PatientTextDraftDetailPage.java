package com.ksu.serene.controller.main.drafts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;

import www.sanju.motiontoast.MotionToast;

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

        if ( TDID == null ){
            TDID = "test_text_note142753";
        }

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
                        .setMessage(R.string.DeleteMessageTD)
                        .setPositiveButton(R.string.DeleteOKTD, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, final int i) {
                                db.collection("TextDraft")
                                        .document(TDID)
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                               // Toast.makeText(PatientTextDraftDetailPage.this, R.string.TDDeletedSuccess, Toast.LENGTH_LONG).show();

                                                Resources res = getResources();
                                                String text = String.format(res.getString(R.string.TDDeletedSuccess));

                                                MotionToast.Companion.darkToast(
                                                        PatientTextDraftDetailPage.this,
                                                        text,
                                                        MotionToast.Companion.getTOAST_SUCCESS(),
                                                        MotionToast.Companion.getGRAVITY_BOTTOM(),
                                                        MotionToast.Companion.getLONG_DURATION(),
                                                        ResourcesCompat.getFont( PatientTextDraftDetailPage.this, R.font.montserrat));


                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(PatientTextDraftDetailPage.this, R.string.TDDeletedFialed, Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton(R.string.DeleteCancleTD, null)
                        .show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (! CheckFields(title.getText().toString(),subj.getText().toString()) ){
                    Toast.makeText(PatientTextDraftDetailPage.this, R.string.EmptyFields, Toast.LENGTH_LONG).show();
                    return;
                }
                if ( CheckFields (title.getText().toString(),subj.getText().toString())) {
                    db.collection("TextDraft")
                            .document(TDID)
                            .update("title", title.getText().toString(),
                                    "text", subj.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(PatientTextDraftDetailPage.this, R.string.TDUpdatedSuccess, Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PatientTextDraftDetailPage.this, R.string.TDUpdatedFialed, Toast.LENGTH_LONG).show();
                                }
                            });
                }

            }
        });

    }
    public boolean CheckFields (String TitleDraft, String Message) {
        if (!TitleDraft.equals("") && TitleDraft!=null
                && !Message.equals("") && Message!=null) {
            return true;
        } else {
            return false;
        }
    }
}
