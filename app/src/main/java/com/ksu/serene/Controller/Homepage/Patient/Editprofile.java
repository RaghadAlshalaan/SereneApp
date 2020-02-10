package com.ksu.serene.Controller.Homepage.Patient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ksu.serene.Controller.Signup.GAD7;
import com.ksu.serene.Controller.Signup.Signup;
import com.ksu.serene.Model.MySharedPreference;
import com.ksu.serene.R;

import java.io.IOException;


public class Editprofile extends AppCompatActivity {
    private EditText name, oldPass, newPass, confirmPass;
    private ImageView image;
    private Button chooseImg, save;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private String ImageName, nameDb;
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private String TAG = Editprofile.class.getSimpleName();
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editprofile);
        // Inflate the layout for this fragment
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.username);
        image = findViewById(R.id.imageView);
        chooseImg = findViewById(R.id.buttonImage);
        save = findViewById(R.id.save);
        oldPass = findViewById(R.id.oldPassword);
        newPass = findViewById(R.id.newPassword);
        confirmPass = findViewById(R.id.reNewPassword);


        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFile();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName(name.getText().toString());
                uploadFile();
                if(!oldPass.getText().toString().equals("") &&! newPass.getText().toString().equals("")
                        && !confirmPass.getText().toString().equals("") && (oldPass.getText().toString().length()>=8
                        && newPass.getText().toString().length()>=8
                        && confirmPass.getText().toString().length()>=8))
                changePassword(oldPass.getText().toString(),newPass.getText().toString(),confirmPass.getText().toString());

                else if(!oldPass.getText().toString().equals("") &&! newPass.getText().toString().equals("")
                        && !confirmPass.getText().toString().equals("") &&(oldPass.getText().toString().length()<8
                        ||newPass.getText().toString().length()<8
                        || confirmPass.getText().toString().length()<8)){
                    Toast.makeText(Editprofile.this, "Password is less than 8 characters, please try again",
                            Toast.LENGTH_SHORT).show();
                    oldPass.setText("");
                    newPass.setText("");
                    confirmPass.setText("");
                    return;

                }





            }
        });



    }


    public void displayName(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            nameDb = user.getDisplayName();

            for (UserInfo userInfo : user.getProviderData()) {
                if (nameDb == null && userInfo.getDisplayName() != null
                ) {
                    nameDb = userInfo.getDisplayName();

                    MySharedPreference.putString(this, "name", nameDb);


                }
            }
            name.setText(nameDb);




        }//end if
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!MySharedPreference.getString(this, "name", "").equals("")) {
            name.setText(MySharedPreference.getString(this, "name", ""));
        }

        else {

            displayName();
        }
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

    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on

            final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageRef = storage.getReference(userId);
            final StorageReference ref = storageRef.child(ImageName);
               UploadTask uploadTask = ref.putFile(filePath);

           Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
               @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                   if (!task.isSuccessful()) {
                       throw task.getException();

                   }

                    // Continue with the task to get the download URL
                   return ref.getDownloadUrl();
               }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
               @Override
               public void onComplete(@NonNull Task<Uri> task) {
                   if (task.isSuccessful()) {


                        final Uri downloadUri = task.getResult();

                        DocumentReference userImage = db.collection("Patient").document(userId);
                        userImage
                                .update("Image",downloadUri.toString() )
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        final Uri download = downloadUri;

                                                            MySharedPreference.putString(Editprofile.this, "Image", download.toString());
                                                   }

                                                });
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                   }

                          }
                    });

                   }
                    else{
                        //
                    }
                }


    public void updateName(final String newName) {
        DocumentReference userName = db.collection("Patient").document(mAuth.getUid());

// Set the "isCapital" field of the city 'DC'
        userName
                .update("name", newName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //update FirebaseUser profile

                        FirebaseUser userf = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(newName).build();
                        userf.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");

                                            MySharedPreference.putString(Editprofile.this, "name", newName);

                                        }
                                    }
                                });
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);

                    }
                });



    }// updateName()

    public void changePassword(String oldPassword, final String newPassword, String confirmNewPassword) {
        //if the new password doesn't match show message otherwise change password
        if (!newPassword.equals(confirmNewPassword)) {
            Toast.makeText(Editprofile.this, "Password does not match, please try again",
                    Toast.LENGTH_SHORT).show();
            newPass.setText("");
            confirmPass.setText("");
            return;
        }//end if
//check if the the new password not the same the old password
        if(checkPassword(oldPassword,newPassword)){
            return;
        }
        // get user email from FireBase
       final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = "";
        if (user != null) {
            email = user.getEmail();
        }
        //change password

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, oldPassword);

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Editprofile.this, "Password updated", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(Editprofile.this, "the password is not updated, please try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Editprofile.this, "You have entered a wrong password", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }//end changePassword()
    public boolean checkPassword(String oldPassword, String newPassword) {

        if (oldPassword.equals(newPassword)) {
            Toast.makeText(Editprofile.this, "you can't use the same password", Toast.LENGTH_SHORT).show();
            oldPass.setText("");
            newPass.setText("");
            confirmPass.setText("");
            return true;
        } else {
            return false;
        }

    }
}
