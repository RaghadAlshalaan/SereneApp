package com.ksu.serene.controller.main.drafts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.controller.liveChart.utils.Utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;

public class PatientTextDraftDetailPage extends AppCompatActivity {

    private EditText title;
    private EditText subj;
    private Button delete;
    private Button edit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String TDID;
    ImageView back;
    private String oldTitle, oldSubj;
    private TextView lastUpdated, DateCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_text_draft_detail_page);

        SharedPreferences sp = getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
        String preferred_lng = sp.getString("PREFERRED_LANGUAGE", "en");
        Utils.setLocale(preferred_lng, this);

        getSupportActionBar().hide();

        TDID = getIntent().getStringExtra("TextDraftID");
        title = findViewById(R.id.TitleTextD);
        subj = findViewById(R.id.SubjtextD);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.SaveChanges);
        back = findViewById(R.id.backButton);
        lastUpdated = findViewById(R.id.LastUpdated);
        DateCreated = findViewById(R.id.DateCreated);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        retrieveData ();

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
                                deleteDraft ();
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
                if ( CheckFields (title.getText().toString(), subj.getText().toString())) {
                    editDraft (title.getText().toString(), subj.getText().toString()) ;
                }

            }
        });

    }
    public boolean CheckFields (String TitleDraft, String Message) {
        if (!TitleDraft.equals("") && !Message.equals("") ) {
            return true;
        } else {
            return false;
        }
    }

    public void retrieveData () {
        db.collection("TextDraft")
                .document(TDID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                title.setText(documentSnapshot.get("title").toString());
                oldTitle = documentSnapshot.get("title").toString();
                subj.setText(documentSnapshot.get("text").toString());
                oldSubj = documentSnapshot.get("text").toString();
                DateCreated.setText(DateCreated.getText()+" "+getDateFormat((Timestamp) documentSnapshot.get("timestamp"))+" "+getTimeFormat((Timestamp) documentSnapshot.get("timestamp")));
                if (documentSnapshot.get("LastUpdated") != null)
                    lastUpdated.setText(lastUpdated.getText()+" "+getDateFormat((Timestamp) documentSnapshot.get("LastUpdated"))+" "+getTimeFormat((Timestamp) documentSnapshot.get("LastUpdated")));
                else
                    lastUpdated.setText(lastUpdated.getText()+" "+getDateFormat((Timestamp) documentSnapshot.get("timestamp"))+" "+getTimeFormat((Timestamp) documentSnapshot.get("timestamp")));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void deleteDraft () {
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
                        Resources res = getResources();
                        String text = String.format(res.getString(R.string.TDDeletedFialed));

                        MotionToast.Companion.createToast(
                                PatientTextDraftDetailPage.this,
                                text,
                                MotionToast.Companion.getTOAST_ERROR(),
                                MotionToast.Companion.getGRAVITY_BOTTOM(),
                                MotionToast.Companion.getLONG_DURATION(),
                                ResourcesCompat.getFont( PatientTextDraftDetailPage.this, R.font.montserrat));
                    }
                });
    }

    public void editDraft (String title, String subj) {
        db.collection("TextDraft")
                .document(TDID)
                .update("title", title,
                        "text", subj)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Resources res = getResources();
                        String text = String.format(res.getString(R.string.TDUpdatedSuccess));

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
                        Resources res = getResources();
                        String text = String.format(res.getString(R.string.TDUpdatedFialed));

                        MotionToast.Companion.darkToast(
                                PatientTextDraftDetailPage.this,
                                text,
                                MotionToast.Companion.getTOAST_ERROR(),
                                MotionToast.Companion.getGRAVITY_BOTTOM(),
                                MotionToast.Companion.getLONG_DURATION(),
                                ResourcesCompat.getFont( PatientTextDraftDetailPage.this, R.font.montserrat));

                    }
                });
        final Map<String, Object> newDate = new HashMap<>();
        newDate.put("LastUpdated", FieldValue.serverTimestamp());
        if (!oldSubj.equals(subj) || !oldTitle.equals(title)){
            db.collection("TextDraft")
                    .document(TDID).update(newDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("Date Updated","");
                }
            });
        }
    }

    private String getDateFormat(Timestamp timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp.getSeconds()*1000);
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        return mDay+"/"+(mMonth+1)+"/"+mYear;
    }// getDateFormat

    private String getTimeFormat(Timestamp timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp.getSeconds()*1000);
        int mHour = calendar.get(Calendar.HOUR);
        int mMinute = calendar.get(Calendar.MINUTE);

        return mHour+":"+mMinute;
    }
}
