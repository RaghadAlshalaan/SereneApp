package com.ksu.serene.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class dbSetUp {
    public FirebaseAuth mAuth;
    public static FirebaseFirestore db ;
    public FirebaseStorage storage ;
    public StorageReference storageRef ;

    public dbSetUp(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }


}