package com.ksu.serene.Controller.Homepage.Patient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ksu.serene.R;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class Editprofile extends Fragment {
    private EditText name, email;
    private ImageView image;
    private Button chooseImg, save;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private String ImageName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_editprofile, container, false);

        name = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        image = view.findViewById(R.id.imageView);
        chooseImg = view.findViewById(R.id.buttonImage);
        save = view.findViewById(R.id.save);


        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFile();
            }
        });

       //TODO get values from data base and store them

        return view;
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                image.setImageBitmap(bitmap);
                Toast.makeText(getActivity(), "Image choosen", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } }
}
