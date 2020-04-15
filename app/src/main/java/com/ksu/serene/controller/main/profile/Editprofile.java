package com.ksu.serene.controller.main.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ksu.serene.MainActivity;
import com.ksu.serene.model.MySharedPreference;
import com.ksu.serene.R;
import com.ksu.serene.WelcomePage;
import com.ksu.serene.model.Token;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Editprofile extends AppCompatActivity {
    private EditText name, oldPass, newPass, confirmPass;
    private ImageView image,back;
    private Button chooseImg, delete,save;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private String ImageName, nameDb;
    private FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private String TAG = Editprofile.class.getSimpleName();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private String email;
    private TextView ForgetPassword;
    private String pastName;
    private boolean ChangePass = false;
    private TextView imgInStorage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
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
        delete = findViewById(R.id.delete);
        ForgetPassword = findViewById(R.id.forgetPassword);
        back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgInStorage = findViewById(R.id.ImageSavedStorage);

        //retrieve past name
        PastName ();

        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //retrieve email from firebase
                DocumentReference userName = db.collection("Patient").document(mAuth.getUid());
                userName.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        email = documentSnapshot.get("email").toString();
                        if (email != null) {
                            mAuth.sendPasswordResetEmail(email)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(Editprofile.this, R.string.ForgetPassSuccess, Toast.LENGTH_LONG).show();
                                            //log out user
                                            logout();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Editprofile.this, R.string.ForgetPassFialed, Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                });
            }
        });


        // TODO : REMOVE CHANGE PASSWORD IF GOOGLE SIGN IN

        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFile();
                //uploadFile();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadFile();
                //use method for check if name empty
                if (! CheckNameField( name.getText().toString() ) ) {
                    Toast.makeText(Editprofile.this, R.string.EmptyName, Toast.LENGTH_LONG).show();
                    if (pastName != null)
                        name.setText(pastName);
                    return;
                }
                //check if name not empty
                if (CheckNameField( name.getText().toString()) ) {
                    //check for valid name
                    if (! isValidName(name.getText().toString())) {
                        Toast.makeText(Editprofile.this, R.string.NotCorrectName, Toast.LENGTH_LONG).show();
                        if (pastName != null)
                            name.setText(pastName);
                        return;
                    }
                    //here the name not empty && valid check if name not equal old name
                    //update name if the edit not empty && new name not equal past name or when no past name
                    if (pastName != null && !(pastName.equals(name.getText().toString())))
                        updateName(name.getText().toString());

                    else if (pastName == null)
                        updateName(name.getText().toString());
                }

                /*if(!oldPass.getText().toString().equals("") &&! newPass.getText().toString().equals("")
                        && !confirmPass.getText().toString().equals("") && (oldPass.getText().toString().length()>=6
                        && newPass.getText().toString().length()>=6
                        && confirmPass.getText().toString().length()>=6)){*/
                //first check if pass/s not mepty
                if ( CheckPassField(oldPass.getText().toString(), newPass.getText().toString(),confirmPass.getText().toString() ) ){
                    //Second check for pass length
                    if (checkResetPassLength(oldPass.getText().toString(), newPass.getText().toString(),confirmPass.getText().toString())) {
                        //Third check when new pass == confirm new pass
                        /*if (!newPass.getText().toString().equals(confirmPass.getText().toString())) {*/
                        if ( ! isPasswordMatch(newPass.getText().toString(),confirmPass.getText().toString()) ){
                            Toast.makeText(Editprofile.this, R.string.passwordMatch, Toast.LENGTH_LONG).show();
                            newPass.setText("");
                            confirmPass.setText("");
                            return;
                        }//end if
                        //check if new pass == old pass
                        if (sameOldPassword(oldPass.getText().toString(), newPass.getText().toString())) {
                            Toast.makeText(Editprofile.this, R.string.passwordSame, Toast.LENGTH_SHORT).show();
                            oldPass.setText("");
                            newPass.setText("");
                            confirmPass.setText("");
                            return;
                        }
                        //call method when all if condition consulude then new pass == confirm && new pass not equal old pass
                        changePassword(oldPass.getText().toString(), newPass.getText().toString(), confirmPass.getText().toString());
                    }
                    //here the length for one or all of them <6
                    else if (! checkResetPassLength(oldPass.getText().toString(), newPass.getText().toString(),confirmPass.getText().toString())) {
                        Toast.makeText(Editprofile.this, R.string.passwordChar,Toast.LENGTH_LONG).show();
                        oldPass.setText("");
                        newPass.setText("");
                        confirmPass.setText("");
                        return;
                    }
                }

               /* else if(!oldPass.getText().toString().equals("") &&! newPass.getText().toString().equals("")
                        && !confirmPass.getText().toString().equals("") &&(oldPass.getText().toString().length()<6
                        ||newPass.getText().toString().length()<6
                        || confirmPass.getText().toString().length()<6)){
                }*/

                finish();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Editprofile.this);
                dialog.setTitle(R.string.DeleteAcc);
                dialog.setMessage(R.string.DeleteMessageAcc);
                dialog.setPositiveButton(R.string.DeleteOKAcc, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    //TODO REMOVE ALL THE REMINDERS AND DRAFTS FOR THE PATIENT ID
                                    //Remove Med Reminders
                                    final CollectionReference referenceMedicine = FirebaseFirestore.getInstance().collection("PatientMedicin");
                                    final Query queryPatientMedicine = referenceMedicine.whereEqualTo("patinetID",mAuth.getUid());
                                    queryPatientMedicine.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    //Delete all the document
                                                    referenceMedicine.document(document.getId()).delete();
                                                }
                                            }
                                        }
                                    });
                                    //Remove App Reminders
                                    final CollectionReference referenceApp = FirebaseFirestore.getInstance().collection("PatientSessions");
                                    final Query queryPatientApp = referenceApp.whereEqualTo("patinetID",mAuth.getUid());
                                    queryPatientApp.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    //Delete all the document
                                                    referenceApp.document(document.getId()).delete();
                                                }
                                            }
                                        }
                                    });
                                    //remove text draft
                                    final CollectionReference referenceTDraft = FirebaseFirestore.getInstance().collection("TextDraft");
                                    final Query queryPatientTDraft = referenceTDraft.whereEqualTo("patinetID",mAuth.getUid());
                                    queryPatientTDraft.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    //Delete all the document
                                                    referenceTDraft.document(document.getId()).delete();
                                                }
                                            }
                                        }
                                    });
                                    //remove audio draft
                                    final CollectionReference referenceADraft = FirebaseFirestore.getInstance().collection("AudioDraft");
                                    final Query queryPatientADraft = referenceADraft.whereEqualTo("patinetID",mAuth.getUid());
                                    queryPatientADraft.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    //Delete all the document
                                                    referenceADraft.document(document.getId()).delete();
                                                }
                                            }
                                        }
                                    });
                                    Toast.makeText(Editprofile.this,R.string.AccDeletedSuccess , Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Editprofile.this, WelcomePage.class);
                                    startActivity(intent);
                                    finish();
                                }

                                else{
                                    Log.d("errror Delete",task.getException().getMessage());
                                    Toast.makeText(Editprofile.this, R.string.AccDeletedFialed,
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

                dialog.setNegativeButton(R.string.DeleteCancleAcc,null);

                AlertDialog alertDialog =  dialog.create();
                alertDialog.show();
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
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.SelectPicture) ), PICK_IMAGE_REQUEST);

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
                Toast.makeText(this, R.string.image, Toast.LENGTH_LONG).show();
                uploadFile();
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
                                        //i need this toast please don't removed to ensure when test pass the image is teup in storage
                                        //Toast.makeText(Editprofile.this, "DocumentSnapshot successfully updated!", Toast.LENGTH_LONG).show();
                                        imgInStorage.setText("DocumentSnapshot successfully updated!");
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

        userName.update("name", newName)
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
                                            if ( TextUtils.isEmpty(oldPass.getText().toString()) && TextUtils.isEmpty(newPass.getText().toString())
                                                    && TextUtils.isEmpty(confirmPass.getText().toString())){
                                                Toast.makeText(Editprofile.this, R.string.UpdateNameSuccess, Toast.LENGTH_LONG).show();
                                                finish();
                                            }

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
                        Toast.makeText(Editprofile.this, R.string.UpdateNameFialed, Toast.LENGTH_LONG).show();
                    }
                });


    }// updateName()

    public boolean changePassword(String oldPassword, final String newPassword, String confirmNewPassword) {
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
                                        ChangePass = true;
                                        if (pastName != null && !TextUtils.isEmpty(name.getText().toString()) && pastName.equals(name.getText().toString())) {
                                            Toast.makeText(Editprofile.this, R.string.passwordUpdate, Toast.LENGTH_LONG).show();
                                            finish();
                                        } else {
                                            Toast.makeText(Editprofile.this, R.string.ProfileInfoUpdateSuccess, Toast.LENGTH_LONG).show();
                                            finish();
                                        }

                                    } else {
                                        ChangePass = false;
                                        if (pastName != null && !TextUtils.isEmpty(name.getText().toString()) && pastName.equals(name.getText().toString())) {
                                            Toast.makeText(Editprofile.this, R.string.passwordNotUpdated, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(Editprofile.this, R.string.ProfileInfoUpdateFialed, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                                        });
                                } else {
                                    ChangePass = false;
                                    Toast.makeText(Editprofile.this, R.string.wrongPassword, Toast.LENGTH_LONG).show();
                                    oldPass.setText("");
                                    newPass.setText("");
                                    confirmPass.setText("");
                                }
                            }
                        });

                        return ChangePass;
                    }//end changePassword()


     public boolean sameOldPassword(String oldPassword, String newPassword) {

        if (oldPassword.equals(newPassword)) {
            return true;
        } else {
            return false;
        }

    }

    public void logout() {
        // to delete the token after logout & avoid send him notification if he is logout
        DocumentReference userTokenDR = FirebaseFirestore.getInstance().collection("Tokens").document(mAuth.getUid());
        Token mToken = new Token("");

        final Map<String, Object> tokenh = new HashMap<>();
        tokenh.put("token",mToken);

        userTokenDR
                .update("token", mToken)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // [START auth_sign_out]
                        mAuth.signOut();
                        if (mAuth.getCurrentUser() == null) {
                            Intent intent = new Intent(Editprofile.this, WelcomePage.class);
                            startActivity(intent);
                            //finish();
                        }
                        // [END auth_sign_out]
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

    }

    public void PastName () {
        DocumentReference userName = db.collection("Patient").document(mAuth.getUid());
        userName.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //search if one of the socio quaestion founds
                if (documentSnapshot.get("name") != null) {
                    pastName = documentSnapshot.get("name").toString();
                }
            }
        });
    }

    public boolean CheckNameField (String newName){
        if ( !(TextUtils.isEmpty(newName))) {
            return true;
        }
        return false;
    }

    public boolean isValidName (String newName) {
        if (!newName.matches("^[ A-Za-z]+$")) {
            return false;
        }
        return true;
    }

    public boolean CheckPassField (String old, String newPass, String confirmNewPAss){
        if ( !(TextUtils.isEmpty(old)) &&!(TextUtils.isEmpty(newPass)) &&!(TextUtils.isEmpty(confirmNewPAss))) {
            return true;
        }
        return false;
    }

    public boolean checkResetPassLength (String oldPassword, String newPassword, String confirmNewPassword){
        if (oldPassword.length()<6 || newPassword.length()<6 || confirmNewPassword.length()<6 ) {
            return false;
        }
        return true;
    }

    public boolean isPasswordMatch (String password, String confrimPassword){
        if (!password.equals(confrimPassword)) {
            return false;
        }
        return true;
    }

}
