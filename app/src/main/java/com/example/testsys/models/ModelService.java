package com.example.testsys.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/*
* Base class for model services
* It contains instances for FirebaseDatabase, global DB reference and FirebaseAuth
 */
public class ModelService {
    static protected FirebaseDatabase db = FirebaseDatabase.getInstance();
    static protected DatabaseReference dbRef = db.getReference();
    static protected FirebaseAuth auth = FirebaseAuth.getInstance();
    static protected FirebaseStorage storage = FirebaseStorage.getInstance();
    static protected StorageReference storageRef = storage.getReference();
}
