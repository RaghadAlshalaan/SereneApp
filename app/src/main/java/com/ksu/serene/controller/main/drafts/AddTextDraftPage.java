package com.ksu.serene.controller.main.drafts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;
import com.ksu.serene.controller.Constants;
import com.ksu.serene.controller.liveChart.utils.Utils;
import com.ksu.serene.model.TextDraft;

import java.util.HashMap;
import java.util.Map;

import www.sanju.motiontoast.MotionToast;

public class AddTextDraftPage extends AppCompatActivity {

    private EditText Title;
    private EditText Subj;
    private Button Add;
    private String titleTxt;
    private String draftId;
    private String patientID;
    private FirebaseFirestore db;
    ImageView back;
    private TextDraft textDraft;

    public void setDb(FirebaseFirestore mockDb) {
        this.db = mockDb;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text_draft_page);
        SharedPreferences sp = getSharedPreferences(Constants.Keys.USER_DETAILS, Context.MODE_PRIVATE);
        String preferred_lng = sp.getString("PREFERRED_LANGUAGE", "en");
        Utils.setLocale(preferred_lng, this);
        getSupportActionBar().hide();

        back = findViewById(R.id.backButton);
        Title = findViewById(R.id.TitleTextD);
        Subj = findViewById(R.id.SubjtextD);
        Add = findViewById(R.id.ConfirmTextDraft);

        patientID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (! CheckFields(Title.getText().toString(), Subj.getText().toString())){
                    Toast.makeText(AddTextDraftPage.this, R.string.EmptyFields, Toast.LENGTH_LONG).show();
                    return;
                }
                if (CheckFields(Title.getText().toString(), Subj.getText().toString())){
                    SaveTextDraft(Title.getText().toString(), Subj.getText().toString(),patientID);

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean CheckFields (String TitleDraft, String Message){
        if ( !TitleDraft.equals("") && !Message.equals("")) {
            return true;
        }
        else {
            return false;
        }
    }

    public TextDraft SaveTextDraft (final String TitleDraft,final String Message, String patientID) {

        draftId = getDraftID(TitleDraft);


        Map<String, Object> draft = new HashMap<>();
        draft.put("text", Message);
        draft.put("timestamp", FieldValue.serverTimestamp());
        draft.put("title", TitleDraft);
        draft.put("patinetID", patientID);

        db.collection("TextDraft").document(draftId).set(draft)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Resources res = getResources();
                        String text = String.format(res.getString(R.string.TDSavedSuccess));

                        MotionToast.Companion.darkToast(
                                AddTextDraftPage.this,
                                text,
                                MotionToast.Companion.getTOAST_SUCCESS(),
                                MotionToast.Companion.getGRAVITY_BOTTOM(),
                                MotionToast.Companion.getLONG_DURATION(),
                                ResourcesCompat.getFont( AddTextDraftPage.this, R.font.montserrat));
                        finish();
                        textDraft = new TextDraft(draftId,TitleDraft,FieldValue.serverTimestamp()+"",Message, FieldValue.serverTimestamp()+"");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Resources res = getResources();
                        String text = String.format(res.getString(R.string.TDSavedFialed));

                        MotionToast.Companion.createToast(
                                AddTextDraftPage.this,
                                text,
                                MotionToast.Companion.getTOAST_ERROR(),
                                MotionToast.Companion.getGRAVITY_BOTTOM(),
                                MotionToast.Companion.getLONG_DURATION(),
                                ResourcesCompat.getFont( AddTextDraftPage.this, R.font.montserrat));
                        textDraft = null;
                    }
                });
        return textDraft;
    }


    public String getDraftID(String title) {
        titleTxt = title;//Title.getText().toString();
        String id = titleTxt;
        id = id.toLowerCase();
        id = id.replace(" ", "_");
        int Min = 010101;
        int Max = 999999;
        int random = Min + (int) (Math.random() * ((Max - Min) + 1));
        return id.concat(random + "");
    }

}
