package com.ksu.serene.controller.main.drafts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.R;

public class EditPatientTextDraftPage extends AppCompatActivity {

    private EditText Title;
    private EditText Subj;
    private Button Confirm;
    private String ID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_text_draft_page);

        Title = findViewById(R.id.EditTitle);
        Subj = findViewById(R.id.editSubj);
        Confirm = findViewById(R.id.SaveChanges);

        ID = getIntent().getStringExtra("TextID");
        Title.setText(getIntent().getStringExtra("TextTitle"));
        Subj.setText(getIntent().getStringExtra("TextSubj"));

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("TextDraft")
                        .document(ID)
                        .update("title" , Title.getText().toString(),
                                "text",Subj.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(EditPatientTextDraftPage.this, "Updated Successfully", Toast.LENGTH_LONG);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditPatientTextDraftPage.this, "Did Not Update, Try Again", Toast.LENGTH_LONG);
                            }
                        });
            }
        });
    }
}
