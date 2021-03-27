package com.kost13.tourismapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Database {

    private static final String DB_USERS = "users";

    public static FirebaseDatabase firebaseDatabase;

    private static DatabaseReference openDatabaseReference(String reference) {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
        }

        return firebaseDatabase.getReference().child(reference);
    }

    public static DatabaseReference getUsersDb() {
        return openDatabaseReference(DB_USERS);
    }

    public static Query findUserByName(String name) {
        name = name.trim();
        if (name == null || name.isEmpty()) {
            // all users
            return getUsersDb();
        }
        return getUsersDb().orderByChild("name").startAt(name).endAt(name + "\uf8ff");

    }
}
