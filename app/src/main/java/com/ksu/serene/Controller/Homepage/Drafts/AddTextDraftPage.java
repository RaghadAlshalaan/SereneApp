package com.ksu.serene.Controller.Homepage.Drafts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.MainActivity;
import com.ksu.serene.R;

import java.util.HashMap;
import java.util.Map;

public class AddTextDraftPage extends AppCompatActivity {

    private EditText Title;
    private EditText Subj;
    private Button Add;
    private String titleTxt;
    private boolean Added = false;
    private String draftId;
    private String patientID;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text_draft_page);
        Title = findViewById(R.id.TitleText);
        Subj = findViewById(R.id.draftMsg);
        Add = findViewById(R.id.ConfirmTextDraft);

        patientID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckFields(Title.getText().toString(), Subj.getText().toString())){
                    SaveTextDraft(Title.getText().toString(), Subj.getText().toString());

                }
            }
        });
    }

    private boolean CheckFields (String TitleDraft, String Message){
        if ( !(TextUtils.isEmpty(TitleDraft)) && !(TextUtils.isEmpty(Message))) {
            return true;
        }
        else {
            Toast.makeText(AddTextDraftPage.this, "All Fields Required", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void SaveTextDraft (String TitleDraft, String Message) {

        draftId = getDraftID();


        Map<String, Object> draft = new HashMap<>();
        draft.put("text", Message);
        draft.put("timestamp", FieldValue.serverTimestamp());
        draft.put("title", TitleDraft);
        draft.put("patinetID", patientID);

        db.collection("TextDraft").document(draftId).set(draft)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //todo: progress bar
                        Toast.makeText(AddTextDraftPage.this, "Saved Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddTextDraftPage.this, MainActivity.class);
                        startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }


    private String getDraftID() {
        titleTxt = Title.getText().toString();
        String id = titleTxt;
        id = id.toLowerCase();
        id = id.replace(" ", "_");
        int Min = 010101;
        int Max = 999999;
        int random = Min + (int) (Math.random() * ((Max - Min) + 1));
        return id.concat(random + "");
    }

}
