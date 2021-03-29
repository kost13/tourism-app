package com.kost13.tourismapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Database {

    private static final String DB_USERS = "users";
    private static final String DB_ROUTES = "routes";

    private static FirebaseDatabase firebaseDatabase;
    private static FirebaseFirestore firestore;

    private static DatabaseReference openDatabaseReference(String reference) {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
        }

        return firebaseDatabase.getReference().child(reference);
    }

    private static CollectionReference openFirestoreReference(String reference) {
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
        }
        return firestore.collection(reference);
    }

    public static DatabaseReference getUsersDb() {
        return openDatabaseReference(DB_USERS);
    }

    public static CollectionReference getRoutesDb() { return openFirestoreReference(DB_ROUTES); }

    public static DocumentReference getRoutesDocsReference() { return getRoutesDb().document("jRP5OOxRLrr51zQxcGen");  }

    public static Query findUserByName(String name) {
        name = name.trim();
        if (name == null || name.isEmpty()) {
            // all users
            return getUsersDb();
        }
        return getUsersDb().orderByChild("name").startAt(name).endAt(name + "\uf8ff");

    }
}
