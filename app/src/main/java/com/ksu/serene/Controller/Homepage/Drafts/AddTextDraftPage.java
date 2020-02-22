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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ksu.serene.MainActivity;
import com.ksu.serene.Model.TextDraft;
import com.ksu.serene.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddTextDraftPage extends AppCompatActivity {

    private EditText Title;
    private EditText Subj;
    private Button Add;
    private boolean Added = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text_draft_page);
        Title = findViewById(R.id.TitleText);
        Subj = findViewById(R.id.SubjText);
        Add = findViewById(R.id.ConfirmTextDraft);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckFields(Title.getText().toString(), Subj.getText().toString())){
                    if (SaveTextDraft(Title.getText().toString(), Subj.getText().toString())) {
                        Toast.makeText(AddTextDraftPage.this, "Saved Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddTextDraftPage.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(AddTextDraftPage.this, "Did not Saved Successfully, Something wrong try again", Toast.LENGTH_LONG).show();
                    }
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

    private boolean SaveTextDraft (String TitleDraft, String Message) {
        Date CurrentDate = new Date();
        String day = CurrentDate.toString();

        String patientID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String TextDraftID = db.collection("TextDraft").document().getId();

        TextDraft newDraft = new TextDraft(TextDraftID, TitleDraft, day, Message, "3");

        Map<String, Object> draft = new HashMap<>();
        draft.put("text", newDraft.getMessage());
        draft.put("timestamp", newDraft.getTimestap());
        draft.put("title", newDraft.getTitle());
        draft.put("patinetID", patientID);
        draft.put("day", newDraft.getDate());

        db.collection("TextDraft").document()
                .set(draft)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                            // Toast.makeText(Add_Medicine_Page.this, "The Med added successfully", Toast.LENGTH_LONG);
                            Added = true;
                        } else {
                            // Toast.makeText(Add_Medicine_Page.this, "The Med did not add", Toast.LENGTH_LONG);
                            Added = false;
                        }
                    }
                });

        return Added;

    }
}
