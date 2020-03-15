package com.ksu.serene.controller.main.drafts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ksu.serene.R;

public class TextDraftView extends AppCompatActivity {

    private Uri audioUri;
    private TextView title;
    private TextView delete;
    private TextView message;
    private String titleTxt = "";
    private String msgTxt = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_draft_view);

        title = findViewById(R.id.draftTitle);
        delete = findViewById(R.id.delete);
        message = findViewById(R.id.draftMsg);
        getExtras();


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }//onCreate



    private void getExtras() {

        Intent intent = getIntent();
            titleTxt = intent.getExtras().getString("title");
            msgTxt = intent.getExtras().getString("message");
            title.setText(titleTxt);
            message.setText(msgTxt);

    }// getExtras


}
