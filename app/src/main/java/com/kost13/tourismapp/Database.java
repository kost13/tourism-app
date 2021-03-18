package com.kost13.tourismapp;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Database {

    private static final String DB_USERS = "users";

    public static FirebaseDatabase firebaseDatabase;

    private static DatabaseReference openDatabaseReference(String reference){
        if(firebaseDatabase == null){
            firebaseDatabase = FirebaseDatabase.getInstance();
        }

        return firebaseDatabase.getReference().child(reference);
    }

    public static DatabaseReference getUsersDb(){
        return openDatabaseReference(DB_USERS);
    }
}
