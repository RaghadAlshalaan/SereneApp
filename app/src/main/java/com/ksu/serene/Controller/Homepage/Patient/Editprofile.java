package com.ksu.serene.Controller.Homepage.Patient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ksu.serene.R;

import java.io.IOException;


public class Editprofile extends AppCompatActivity {
    private EditText name, email;
    private ImageView image;
    private Button chooseImg, save;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private String ImageName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);
        // Inflate the layout for this fragment

        name = findViewById(R.id.username);
        email = findViewById(R.id.email);
        image = findViewById(R.id.imageView);
        chooseImg = findViewById(R.id.buttonImage);
        save = findViewById(R.id.save);


        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFile();
            }
        });

       //TODO get values from data base and store them


    }

    private void showFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            ImageName = filePath.getLastPathSegment();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                image.setImageBitmap(bitmap);
                Toast.makeText(this, "Image choosen", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } }
}
